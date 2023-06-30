package com.mss.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mss.constant.CaptchaConstant;
import com.mss.constant.TokenConstant;
import com.mss.domain.Result;
import com.mss.domain.dto.*;
import com.mss.domain.entity.SysUser;

import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.CaptchaException;
import com.mss.exception.SystemException;
import com.mss.mapper.SysUserMapper;
import com.mss.service.AuthService;
import com.mss.service.SysUserService;
import com.mss.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Security;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 登录控制器
 */
@Api(tags = "用户登录")
@RestController
@RequestMapping("/system")
@Slf4j
public class LoginController {


    @Value("${aliyun.AccessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.AccessKeySecret}")
    private String accessKeySecret;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserService userService;

//    @Autowired
//    private AuthService authService;

    /**
     * 微信小程序用户登录
     * @param code
     * @return
     */
    @ApiOperation("微信小程序自动登录")
    @GetMapping("/wxlogin/{code}")
    public Result getSessionkey(@PathVariable("code") String code)
    {

        return authService.doWxLogin(code);

    }



    /**
     * 微信用户登录
     * @param
     * @return
     */
    @ApiOperation("第三方微信登录")
    @PostMapping("/wxlogin")
    public AjaxResult wxLoginInApp(@RequestBody WxAppLoginDto dto)
    {

        return authService.doWxAppLogin(dto);

    }


    /**
     * qq用户登录
     * @param
     * @return
     */
    @ApiOperation("第三方qq登录")
    @PostMapping("/qqlogin")
    public AjaxResult qqLoginInApp(@RequestBody QqAppLoginDto dto)
    {

        return authService.doQqAppLogin(dto);

    }


    /**
     * 新浪微博用户登录
     * @param
     * @return
     */
//    @ApiOperation("第三方新浪微博登录")
//    @PostMapping("/xllogin")
//    public AjaxResult xlLoginInApp(@RequestBody WxAppLoginDto dto)
//    {
//
//        return authService.doXlAppLogin(dto);
//
//    }


    /**
     * app端第三方用户登录，包括微信，qq,新浪微博
     * @param
     * @return
     */
//    @ApiOperation("app端第三方登录")
//    @PostMapping("/xllogin")
//    public AjaxResult xlLoginInApp(@RequestBody ThirdPartyLoginDto dto)
//    {
//
//        return authService.ThirdPartyLogin(dto);
//
//    }



//    jdk8无法解密PKCS7Padding，引入
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 微信小程序登录后端解密用户数据卷
     * @param signature
     * @param encryptedData
     * @param iv
     * @return
     */
    @ApiOperation("微信小程序解密用户数据")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam("signature") String signature,
                              @RequestParam("encryptedData") String encryptedData,
                              @RequestParam("iv") String iv){

        // 被加密的数据
        byte[] dataByte = Base64.getDecoder().decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.getDecoder().decode(signature);
        // 偏移量
        byte[] ivByte = Base64.getDecoder().decode(iv);

//        log.error("被加密的数据 encryptedData= "+encryptedData);
//        log.error("加密秘钥 sessionKey= "+signature);
//        log.error("偏移量 iv= "+iv);

        System.out.println("-----");
        //hutool AES解密
        AES aes = new AES("CBC","PKCS7Padding",keyByte,ivByte);
//        AES aes = new AES("AES-128-CBC","PKCS#7",keyByte,ivByte);

        String res = aes.decryptStr(dataByte);

//        log.info("---解密后 ="+res);

        System.out.println(res);
        JSONObject obj = JSONUtil.parseObj(res);
        SysUser user = new SysUser();

//        "openId":"obKX75J8rSNiLFyQqbPTaLkmKg9Y","nickName":"微信用户","gender":0,
//        "language":"","city":"","province":"","country":"",
//        "avatarUrl":"https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132",
//        "watermark":{"timestamp":1677306107,"appid":"wxe1d4882a9a94b849"}}
        user.setAvatar(obj.getStr("avatarUrl"));
        user.setNickName(obj.getStr("nickName"));
        user.setOpendId(obj.getStr("openId"));

        boolean save = userService.save(user);

        if(save) {
            System.out.println("保存成功！");
        }

        // 解密并返回
        return Result.ok(res);
    }


    /**
     * 发送手机号验证码
     * @param phoneNumber
     * @return
     */
    @ApiOperation(value="发送手机号验证码")
    @GetMapping("/phoneCaptcha/{phoneNumber}")
    public Result getPhoneCaptcha(@PathVariable String phoneNumber){

        //生成验证码
        String code = ValidateCodeUtils.generateValidateCode(6).toString();

        //把验证码存入redis,存入时间为3分钟
        redisCache.setCacheObject(phoneNumber,code, new Integer(300),
                     TimeUnit.SECONDS);

        //发送短信给指定手机号码
        /**
         * 发送短信
         * @param signName 签名
         * @param templateCode 模板
         * @param phoneNumbers 手机号
         * @param param 参数
         */
        System.out.println(accessKeyId+"  "+accessKeySecret);

        try {

            SMSUtils.sendMessage("蒙绍山的博客",
                    "SMS_271625812",phoneNumber,code,
                    accessKeyId,accessKeySecret);
        }catch (Exception e){

            return Result.fail("系统异常，请稍后再试！");

        }
        return Result.ok("短信发送成功");
    }


    /**
     * 短信验证码登录
     * @param dto
     * @return
     */
    @ApiOperation(value="手机号验证码登录,code为短信验证码")
    @PostMapping("/smsLogin")
    public AjaxResult smsLogin(@RequestBody SmsLoginDto dto) {

        //获取手机号码
        String phone = dto.getPhone();
        //获取验证码
        String code = dto.getCode();
        //校验验证码是否正确
        String captcha = redisCache.getCacheObject(phone);

        if(captcha==null){
            //验证码过期
            throw new CaptchaException(AppHttpCodeEnum.CAPTCHA_TIME_OUT);

        }

        if(!captcha.equals(code)){
            //验证码错误
           throw new CaptchaException(AppHttpCodeEnum.CAPTCHA_ERROR);

        }

        //查询用户表中的号码，查看是否是新用户，新用户则自动注册在返回token，老用户则直接返回token
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUser::getPhone,phone);

        int count = (int)userService.count(qw);

        //是新用户，新用户则自动注册在返回token
        if(count==0){
            SysUser sysUser = new SysUser();
            sysUser.setPhone(phone);
            sysUser.setNickName("新用户"+ RandomUtil.randomNumbers(9));
            userService.save(sysUser);
        }

        //是老用户，直接查询用户id,根据用户id生成toke并且返回
        SysUser user = userService.getOne(qw);
        // 随机字符串（用于区分同一账号多次登录时，缓存的key不重复）
        String uuidKey = UUID.randomUUID().toString();
        //根据用户id生成token
        String token = JwtUtil.createJWT(user.getId()+uuidKey);

        //把用户信息存入redis,设置存储时间为365天
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(),TimeUnit.SECONDS);

        return  AjaxResult.success().put("token", token);

    }


    /**
     * 账号（用户名,邮箱,用户名，账号）与密码登录
     * @param dto
     * @return
     */
    @ApiOperation(value="账号（用户名,邮箱,用户名）与密码登录统一接口,手机号码邮箱设置绑定后即可用来登录")
    @PostMapping("/login")
    public AjaxResult baselogin(@RequestBody PublicLoginDto dto){

        return authService.login(dto.getAccount(),dto.getPassword());

    }

    @ApiOperation("账号密码登录")
    @PostMapping("/usernameLogin")
    public AjaxResult login(@RequestBody LoginDto dto){

        return authService.accountNumberLogin(dto.getAccount(),dto.getPassword());

    }

    @ApiOperation("自动登录接口,需要传递token")
    @PostMapping("/autoLogin")
    public AjaxResult autoLogin(HttpServletRequest request){

        return authService.autoLogin(request);

    }



//    @ApiOperation("短信验证码登录验证")
//    @PostMapping("/smsLogin")
//    public AjaxResult smsLogin(@RequestBody @Validated SmsLoginDto dto) {
//        // 手机号码
//        String phoneNumber = dto.getPhoneNumber();
//        // 校验验证码
//        String verifyKey = SysConst.REDIS_KEY_SMSLOGIN_SMSCODE + dto.getUuid();
//        String captcha = redisCache.getCacheObject(verifyKey);
//        if(captcha == null) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
//            // 抛出一个验证码过期异常
//            throw new CaptchaExpireException();
//        }
//        if(!captcha.equals(dto.getSmsCode().trim())){
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
//            // 抛出一个验证码错误的异常
//            throw new CaptchaException();
//        }
//        redisCache.deleteObject(verifyKey);
//
//        // 用户验证
//        Authentication authentication = null;
//        try {
//            // 该方法会去调用UserDetailsByPhonenumberServiceImpl.loadUserByUsername
//            authentication = authenticationManager.authenticate(new SmsCodeAuthenticationToken(phoneNumber));
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber,
//                        Constants.LOGIN_FAIL, MessageUtils.message("account.not.incorrect")));
//                throw new UserPasswordNotMatchException();
//            } else {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber,
//                        Constants.LOGIN_FAIL, e.getMessage()));
//                throw new ServiceException(e.getMessage());
//            }
//        }
//        // 执行异步任务，记录登录信息
//        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber,
//                Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
//        // 获取登录人信息
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        // 修改最近登录IP和登录时间
//        recordLoginInfo(loginUser.getUserId());
//        // 生成token
//        String token = tokenService.createToken(loginUser);
//
//        // 返回token给前端
//        AjaxResult ajax = AjaxResult.success();
//        ajax.put(Constants.TOKEN, token);
//        return ajax;
//    }



}
