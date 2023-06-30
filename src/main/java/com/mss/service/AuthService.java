package com.mss.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mss.constant.TokenConstant;
import com.mss.domain.Result;
import com.mss.domain.dto.QqAppLoginDto;
import com.mss.domain.dto.ThirdPartyLoginDto;
import com.mss.domain.dto.WxAppLoginDto;
import com.mss.domain.entity.LoginUser;
import com.mss.domain.entity.MemoryBox;
import com.mss.domain.entity.PublicCard;
import com.mss.domain.entity.SysUser;
import com.mss.domain.vo.SysUserVo;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.PasswordException;
import com.mss.exception.SystemException;
import com.mss.utils.*;
//import com.mss.utils.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Component
//@Transactional
public class AuthService {

    @Value("${wx.online_people_url}")
    private String ONLINE_PEOPLE_URL;

    //小程序的AppID
    @Value("${wx.appId}")
    private String APP_ID;

    //小程序的AppSecret
    @Value("${wx.appSecret}")
    private String SECRET;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PublicCardService publicCardService;

    @Autowired
    private MemoryBoxService memoryBoxService;


    public Result doWxLogin(String code){

        //使用登录凭证code获取openid
        JSONObject json = JSONUtil.parseObj(getSessionKeyByWX(code));
        String session_key = (String)json.get("session_key");

        // 通过 json.getStr("openid") 获取openid
        // 根据openid判断新用户还是老用户
        //   老用户，登录操作，返回token
        //   新用户，则随机注册一个账号（用户名、账号、用户ID），登录操作，返回token
        //...
        // return xxx（登陆成功）

        if(json.getBool("isSuccess")) {

            System.out.println( json.getStr("openid") );
            String openid = json.getStr("openid");
            LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
            qw.eq(SysUser::getOpendId,openid);
            int count = sysUserService.count(qw);
            if(count!=0){

               //老用户，登录操作，返回token
                System.out.println("老用户，登录操作，返回token");

//               存储用户token登录配置
                 SysUser sysUser = new SysUser();
                 sysUser.setNickName("zhang");

//              SysUser user = redisCache.getCacheObject(TokenConstant.TOKEN_KEY + session_key);

                Map<String,Object> map = new HashMap<String,Object>();
                map.put("token","我是token");
                map.put("userInfo",sysUser);

                System.out.println("登录成功！  "+sysUser.toString());
                return Result.ok(map);

            }else{

               //新用户，则随机注册一个账号（用户名、账号、用户ID），登录操作，返回token
                System.out.println("新用户，则随机注册一个账号（用户名、账号、用户ID），登录操作，返回token");
                SysUser user = new SysUser();
                user.setAvatar(json.getStr("avatarUrl"));
                user.setNickName(json.getStr("nickName"));
                user.setOpendId(json.getStr("openId"));
                sysUserService.save(user);

                String key = json.getStr("session_key");
                String token = JwtUtil.createJWT(key);

                //把用户信息存入redis
//                redisCache.setCacheObject(TokenConstant.TOKEN_KEY +key,user);

//              封装token和用户信息(默认头像与昵称)，并且返回
                Map<String,String> map = new HashMap<String,String>();
                map.put("token",token);
                map.put("userInfo",user.toString());

                return Result.ok(map);


            }

        }else {
            // 记录日志
//            log.error("获取sessionkey失败! code = "+code);
            return Result.fail("登录失败! 无法获取openid！");

        }


    }

    public String getSessionKeyByWX(String code) {
        String baseUrl = ONLINE_PEOPLE_URL + "?appid=" + APP_ID + "&secret=" + SECRET + "&js_code=" + code + "&grant_type=authorization_code";
//        log.info("---微信登陆，通过code获取sessionkey");
//        log.info("---baseUrl ="+baseUrl);
        //使用 hutool 发起get请求
        //基于JDK的HttpUrlConnection封装
        String result = HttpUtil.get(baseUrl);
//        log.info("---拿到GET结果 ="+result);
        //使用 hutool 解析json
        JSONObject jsonObject = JSONUtil.parseObj(result);
        System.out.println(jsonObject);
        if(!jsonObject.isNull("session_key")) {
            //成功获取到session_key等数据
            return result.substring(0,result.length()-1)+",\"isSuccess\":\"true\"}";
        }else {
            //失败，没获取到session_key等数据
            return "{\"session_key\":\"\",\"openid\":\"\",\"isSuccess\":\"false\"}";
        }
    }


    /**
     * 微信登陆，通过code获取session_key
     * @param code 通过wx.login获取到的用户的code（登录凭证）
     * @return 返回session_key，用于解密 前端wx.getUserProfile接口 返回的加密的用户数据（2022-11，微信已弃用该接口，已无法通过其获取用户信息）。
     */
    public Result getSessionKey(String code) {
        //使用 hutool 解析json
        JSONObject json = JSONUtil.parseObj(getSessionKeyByWX(code));
        if(json.getBool("isSuccess")) {
//            log.info("isSuccess = true，成功获取到session_key。session_key = "+json.getStr("session_key"));
            return Result.ok(json.getStr("session_key"));
        }else {
            // 记录日志
//            log.error("获取sessionkey失败! code = "+code);
            return Result.fail("获取sessionkey失败! code = "+code);
        }
    }


    /**
     * 用户名/邮箱/手机号+密码统一登录方法
     * @param param
     * @param password
     * @return
     */
    public AjaxResult login( String param, String password)
    {

        if(RegexUtils.checkEmail(param)){

           return emailLogin(param, password);

        } else if(RegexUtils.checkPhone(param)){

           return phoneLogin(param,password);

        }

        //        用户验证
        Authentication authentication=null;

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(param, password);

//            存储当前用户信息到自定义身份验证类AuthenticationContextHolder中
//        AuthenticationContextHolder.setContext(authenticationToken);

        // authenticate该方法会去调用UserDetailsServiceImpl.loadUserByUsername方法
//             在该方法中进行用户名的校验与密码的校验
        authentication = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authentication)){

            throw new RuntimeException("用户名或者密码错误！");

        }

//        生成token返回前端
//        SecurityContextHolder.getContext().getAuthentication().getPrincipal()等价于
//        authenticationManager.authenticate(authenticationToken).getPrincipal()
//        都是获取security存储登录用户的信息

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        System.out.println(loginUser.getUsername());
        String userId = loginUser.getUser().getId().toString();
        // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
        String uuidKey = UUID.randomUUID().toString();
        String token = JwtUtil.createJWT(userId+uuidKey);

        System.out.println(loginUser.getUser());

        SysUser user = loginUser.getUser();
        //把用户信息存入redis,设置存储时间为一天
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(), TimeUnit.SECONDS);

        //封装token信息，并且返回
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("token",token);
        ajaxResult.put("userInfo",user);

        return ajaxResult;

    }


    /**
     * 邮箱密码登录子方法
     * @param email
     * @param password
     * @return
     */
    public AjaxResult emailLogin(String email, String password){

        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUser::getEmail,email);

        int count = sysUserService.count(qw);

        if(count == 0){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_EXIST);
//            return AjaxResult.error("邮箱不存在！请先前往注册！");
        }

       SysUser user = sysUserService.getOne(qw);

        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new PasswordException(AppHttpCodeEnum.PASSWORD_ERROR);
//            return AjaxResult.error("密码错误！");
        }

        // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
        String uuidKey = UUID.randomUUID().toString();
        String token = JwtUtil.createJWT(user.getId()+uuidKey);

        //把用户信息存入redis,设置存储时间为一天
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(),TimeUnit.SECONDS);

        AjaxResult ajaxResult = AjaxResult.success();
        return ajaxResult.put("token",token);
    }


    /**
     * 手机号+密码登录子方法
     * @param phone
     * @param password
     * @return
     */
    public  AjaxResult phoneLogin(String phone,String password){

        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();

        qw.eq(SysUser::getPhone,phone);

        int count = sysUserService.count(qw);

        if(count==0){

            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_NOT_EXIST);
//            return AjaxResult.error("手机号不存在！请先前往注册！");

        }

        SysUser user = sysUserService.getOne(qw);
//        校验密码
        if(!passwordEncoder.matches(password,user.getPassword())){

            throw new SystemException(AppHttpCodeEnum.PASSWORD_ERROR);

        }

        // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
        String uuidKey = UUID.randomUUID().toString();
        String token = JwtUtil.createJWT(user.getId()+uuidKey);

        //把用户信息存入redis,设置存储时间为一天
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(),TimeUnit.SECONDS);

        AjaxResult ajaxResult = AjaxResult.success();
        return ajaxResult.put("token", token);

    }


    /**
     * 账号密码登录
     * @param account
     * @param password
     * @return
     */
    public AjaxResult accountNumberLogin(String account, String password) {


        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUser::getUsername,account);

        int count = sysUserService.count(qw);

        if(count==0){

            throw new SystemException(AppHttpCodeEnum.ACCOUNT_NOT_EXITS);
//            return AjaxResult.error("手机号不存在！请先前往注册！");

        }

        SysUser user = sysUserService.getOne(qw);
//        校验密码
        if(!passwordEncoder.matches(password,user.getPassword())){

            throw new SystemException(AppHttpCodeEnum.PASSWORD_ERROR);

        }

        // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
        String uuidKey = UUID.randomUUID().toString();
        String token = JwtUtil.createJWT(user.getId()+uuidKey);

        //把用户信息存入redis,设置存储时间为一天
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(),TimeUnit.SECONDS);

        AjaxResult ajaxResult = AjaxResult.success();
        return ajaxResult.put("token", token);


    }


    /**
     * 自动登录接口
     * @param request
     * @return
     */
    public AjaxResult autoLogin(HttpServletRequest request) {

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        if(!Objects.isNull(user)){
            return AjaxResult.error(524,"该用户登录凭证已经失效");
        }

//        刷新登录凭证最大时间时间
        redisCache.expire(TokenConstant.TOKEN_KEY,redisCache.getmaxOnineTime());

        AjaxResult ajaxResult = AjaxResult.success();
        return ajaxResult.put("token", token);

    }

    /**
     * 手机app端微信登录
     * @param dto
     * @return
     */
    public AjaxResult doWxAppLogin(WxAppLoginDto dto) {


        SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpendId, dto.getOpenId()));

        if(!Objects.isNull(user)){
            // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
            String uuidKey = UUID.randomUUID().toString();
            String token = JwtUtil.createJWT(user.getId()+uuidKey);

            //把用户信息存入redis,设置存储时间为一天
            redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                    redisCache.getmaxOnineTime(),TimeUnit.SECONDS);

            return new AjaxResult().put("token", token);
        }

        SysUser sysUser = new SysUser();
        sysUser.setOpendId(dto.getOpenId());
        sysUser.setNickName(dto.getNickName());
        sysUser.setAvatar(dto.getAvatarUrl());
        sysUserService.save(sysUser);

        SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpendId, dto.getOpenId()));

        initMsg(one);
        // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
        String uuidKey = UUID.randomUUID().toString();
        String token = JwtUtil.createJWT(one.getId()+uuidKey);

        //把用户信息存入redis,设置存储时间为一天
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(),TimeUnit.SECONDS);

        return new AjaxResult().put("token", token);

    }


    /**
     * 第三方qq登录方法
     * @param dto
     * @return
     */
    public AjaxResult doQqAppLogin(QqAppLoginDto dto) {

        SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpendId, dto.getOpenId()));

        if(!Objects.isNull(user)){
            // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
            String uuidKey = UUID.randomUUID().toString();
            String token = JwtUtil.createJWT(user.getId()+uuidKey);

            //把用户信息存入redis,设置存储时间为一天
            redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                    redisCache.getmaxOnineTime(),TimeUnit.SECONDS);

            return new AjaxResult().put("token", token);
        }

        SysUser sysUser = new SysUser();
        sysUser.setOpendId(dto.getOpenId());
        sysUser.setNickName(dto.getNickName());
        sysUser.setAvatar(dto.getAvatarUrl());
        sysUser.setGender(dto.getGender());
        sysUserService.save(sysUser);

        SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpendId, dto.getOpenId()));

        initMsg(one);

        // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
        String uuidKey = UUID.randomUUID().toString();
        String token = JwtUtil.createJWT(one.getId()+uuidKey);

        //把用户信息存入redis,设置存储时间为一天
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(),TimeUnit.SECONDS);

        return new AjaxResult().put("token", token);

    }


    /**
     * 第三方新浪微博登录方法
     * @param dto
     * @return
     */
    public AjaxResult doXlAppLogin(WxAppLoginDto dto) {

        return null;

    }


    /**
     * 第三方登录整合方法，包括微信登录，qq登录，新浪微博登录
     * @param dto
     * @return
     */
    public AjaxResult ThirdPartyLogin(ThirdPartyLoginDto dto) {





        return null;

    }


    /**
     * 初始化用户数据，如果第一次登录时使用第三方登录
     * @param user
     */
    public void initMsg(SysUser user){

//      创建闪击盒与用户手册盒
        MemoryBox box = new MemoryBox();
        MemoryBox box1 = new MemoryBox();

        box.setUserId(user.getId());
        box.setRootId(0l);
        box.setCoverPhoto("https://test-anki.oss-cn-guangzhou.aliyuncs.com/bd8c46379f9d0c0655a9598a6582c91a.jpg");//设置默认卡牌盒闪记盒封面照片
        box.setBoxName("闪记盒");

        box1.setUserId(user.getId());
        box1.setRootId(0l);
        box1.setCoverPhoto("https://test-anki.oss-cn-guangzhou.aliyuncs.com/bb744afc70fe223c7b895446ca50cd6f.jpeg");//设置默认卡牌盒产品小手册盒封面照片
        box1.setBoxName("产品小手册");

        memoryBoxService.save(box);
        memoryBoxService.save(box1);


        MemoryBox chanpin = memoryBoxService.getOne(
                new LambdaQueryWrapper<MemoryBox>()
                        .eq(MemoryBox::getUserId, user.getId())
                        .eq(MemoryBox::getRootId, 0)
                        .eq(MemoryBox::getBoxName, "产品小手册"));



        MemoryBox box2 = new MemoryBox();
        MemoryBox box3 = new MemoryBox();
        MemoryBox box4 = new MemoryBox();



//      执行sql新建一张卡牌

        box2.setUserId(user.getId());
        box2.setRootId(chanpin.getId());
        box2.setBoxName("基础介绍");

        memoryBoxService.save(box2);

        MemoryBox jcjs = memoryBoxService.getOne(
                new LambdaQueryWrapper<MemoryBox>()
                        .eq(MemoryBox::getUserId, user.getId())
                        .eq(MemoryBox::getRootId, chanpin.getId())
                        .eq(MemoryBox::getBoxName, "基础介绍"));

        PublicCard card9 =  new PublicCard(user.getId(),jcjs.getBoxName(),jcjs.getId());
        PublicCard card10 = new PublicCard(user.getId(),jcjs.getBoxName(),jcjs.getId());
        PublicCard card11 = new PublicCard(user.getId(),jcjs.getBoxName(),jcjs.getId());
        PublicCard card12 = new PublicCard(user.getId(),jcjs.getBoxName(),jcjs.getId());

        card9.setQuestion("<p>复习是如何安排的？</p>");
        card9.setAnswer("<p><br></p><p>&nbsp;速记卡采用了间隔记忆曲线算法，会根据你的回答情况和学习记录，个性化合理地为你安排每天的学习任务，难的知识点会想要增加复习次数、简单的减少复习次数，实现千人千面的复习记忆，为你精准分配学习精力，实现高效学习。</p>");
        card9.setType("记忆卡");

        card10.setQuestion("<p>速记卡为什么能帮你提高考试分数？</p");
        card10.setAnswer("<p>anki中的记忆卡非常适用于为考试做准备，也是考取高分的秘密武器，应试教育考验每个人对课本知识点的熟悉程度，而这恰恰是惊叹的长处，闪卡间隔复习帮你实现有效的检索记忆，记忆算法为你合理安排每天应该复习什么，高效掌握知识点，持久记忆。</p><p>&nbsp;</p>");
        card10.setType("记忆卡");

        card11.setQuestion("<p>速记卡有什么用？</p>");
        card11.setAnswer("<p>速记卡融合闪卡和笔记产品的核心之处，让它变得更加强大，从记笔记、复习记忆到知识管理，它都能良好完成，轻松建立个人的终身知识库</p><p>&nbsp;</p><p>你可以用速记卡</p><p>记读书笔记</p><p>快速记录灵感、新知</p><p>背单词、背短文</p><p>考研刷题提分</p><p>学习编程</p><p>学中医西医</p><p>学习法律</p><p>……</p><p>&nbsp;</p>");
        card11.setType("记忆卡");

        card12.setQuestion("<p>速记卡是什么？</p>");
        card12.setAnswer("<p>速记卡=Anki闪卡+大纲笔记</p><p>&nbsp;</p><p>Anki是一款间隔重复记忆的闪卡软件，他可以帮助学生们记录知识点、通过间隔重复再记忆知识点。但闪卡产品又存在严重的知识孤岛效应问题，即知识点是零散的，学习后无法形成知识结构和产生知识间的链接，速记卡通过大纲+闪卡的产品创新，解决了孤岛效应问题，现在你既能专注闪卡学习，又能有结构地回顾知识，两全其美</p>");
        card12.setType("记忆卡");


        publicCardService.save(card12);
        publicCardService.save(card11);
        publicCardService.save(card10);
        publicCardService.save(card9);


        box3.setUserId(user.getId());
        box3.setRootId(chanpin.getId());
        box3.setBoxName("常用操作");

        memoryBoxService.save(box3);

        MemoryBox cycz = memoryBoxService.getOne(
                new LambdaQueryWrapper<MemoryBox>()
                        .eq(MemoryBox::getUserId, user.getId())
                        .eq(MemoryBox::getRootId, chanpin.getId())
                        .eq(MemoryBox::getBoxName, "常用操作"));


//      创建10张卡牌盒
        PublicCard card1 = new PublicCard(user.getId(),cycz.getBoxName(),cycz.getId());
        PublicCard card2 = new PublicCard(user.getId(),cycz.getBoxName(),cycz.getId());
        PublicCard card3 = new PublicCard(user.getId(),cycz.getBoxName(),cycz.getId());
        PublicCard card4 = new PublicCard(user.getId(),cycz.getBoxName(),cycz.getId());
        PublicCard card5 = new PublicCard(user.getId(),cycz.getBoxName(),cycz.getId());
        PublicCard card6 = new PublicCard(user.getId(),cycz.getBoxName(),cycz.getId());
        PublicCard card7 = new PublicCard(user.getId(),cycz.getBoxName(),cycz.getId());
        PublicCard card8 = new PublicCard(user.getId(),cycz.getBoxName(),cycz.getId());


        card1.setQuestion("<p>如何筛选记忆程度不同的卡片？</p>");
        card1.setAnswer("<p>打开卡片盒后，在我的卡牌下方，可以根据未学习、学习中、已掌握、疑难去筛选不同掌握程度卡片进行浏览和编辑</p>");
        card1.setType("记忆卡");

        card2.setQuestion("<p>如何设定学习计划？</p>");
        card2.setAnswer("<p>新卡片盒点开始学习后，会弹窗选择记忆强度，记忆强度有强、高、中、低四档，这里根据对该知识点重要性分级选择。</p><p><br></p><p><br></p><p><br></p><p>选择完记忆强度后，进入学习计划设置弹窗，这里可以根据你希望完成的新卡学习时间选择，也可以按每天学习量选择，选完后自动计算预计完成时间和每天耗时。 新卡学完后，还会进入复习阶段，复习阶段是帮你掌握的阶段。</p>");
        card2.setType("记忆卡");

        card3.setQuestion("<p>如何重置学习计划？</p>");
        card3.setAnswer("<p>打开卡片盒-&gt;右上角更多图标-&gt;重置学习进度，重置进度后，学习记录无法恢复</p>");
        card3.setType("记忆卡");

        card4.setQuestion("<p>如何切换卡片模板？</p>");
        card4.setAnswer("<p>打开卡片编辑器后，点击顶部模板切换</p>");
        card4.setType("记忆卡");

        card5.setQuestion("<p>如何添加卡牌盒？</p>");
        card5.setAnswer("<p>打开首页后，滑动到卡牌盒的最后一个，点击+创建卡牌盒进入卡牌盒创建流程，输入卡牌盒名称和上传卡牌盒封面完成创建。</p>");
        card5.setType("记忆卡");

        card6.setQuestion("<p>如何选择回答按钮？</p>");
        card6.setAnswer("<p>速记卡有三个回答按钮，忘记、一般、简单，他们分别对应着不同的复习间隔，跟背单词软件很像，学习时根据对该知识点掌握程度如实选择即可。</p>");
        card6.setType("记忆卡");

        card7.setQuestion("<p>闪卡正反面有什么用？</p>");
        card7.setAnswer("<p>闪卡正面写提问、反面写答案，这样的设计是为了强行让你进行提问式学习，当你看正面提问时，你需要开始动脑筋思考答案，触发这个思考检索的过程才会真是真正的学习，正反面的设计，减少了似懂非懂的情况</p>");
        card7.setType("记忆卡");

        card8.setQuestion("<p>如何开始学习？</p>");
        card8.setAnswer("<p>创建卡片后（卡片可以是知识点、单词等等）可以通过首页的总开学按钮和打开卡片盒里的开学按钮</p>");
        card8.setType("记忆卡");


        publicCardService.save(card8);
        publicCardService.save(card7);
        publicCardService.save(card6);
        publicCardService.save(card5);
        publicCardService.save(card4);
        publicCardService.save(card3);
        publicCardService.save(card2);
        publicCardService.save(card1);


        box4.setUserId(user.getId());
        box4.setRootId(chanpin.getId());
        box4.setBoxName("读书笔记示例《科学学习》");
        memoryBoxService.save(box4);

        MemoryBox dushushili = memoryBoxService.getOne(
                new LambdaQueryWrapper<MemoryBox>()
                        .eq(MemoryBox::getUserId, user.getId())
                        .eq(MemoryBox::getRootId, chanpin.getId())
                        .eq(MemoryBox::getBoxName, "读书笔记示例《科学学习》"));

        PublicCard DSSLCard = new PublicCard();

        DSSLCard.setUserId(user.getId());
        DSSLCard.setBelongToBoxId(dushushili.getId());
        DSSLCard.setBelongToBoxName(dushushili.getBoxName());
        DSSLCard.setType("记忆卡");
        DSSLCard.setQuestion("<p>作者介绍</p>");
        DSSLCard.setAnswer("<p>丹尼尔 L. 施瓦茨（Daniel L. Schwartz）</p><p>学习科学权专家。斯坦福大学教育学院院长，“Nomellini &amp; Olivier”教育科技讲席教授。哥伦比亚大学人类认知与学习博士。</p>" +
                "<p>施瓦茨教授在学习科学基础理论与创新教学的研究中取得了很多重大成就，至今发表重要学术论文60余篇。他先后指导的20余位博士及博士后，大多进入美国高校就任教授，或成功创办有广泛社会影响力的教育企业。</p>" +
                "<p>在斯坦福大学指导研究生的同时，施瓦茨教授先后开设了18门学习理论和学习科学相关课程。" +
                "其中受欢迎的是“核心学习机制”课程，囊括各类适用范围广泛且对学习具有明确指导意义的学习方法与技巧，吸引了不同背景的学生，如律师、物理学家、工程师、商业管理者、教师、教育科技工作者等。由于该课程供不应求，" +
                "施瓦茨教授及其团队基于课程内容撰写了本书，满足更广大读者对学习的强烈兴趣。</p><p>&nbsp;</p>");

        publicCardService.save(DSSLCard);
    }

}
