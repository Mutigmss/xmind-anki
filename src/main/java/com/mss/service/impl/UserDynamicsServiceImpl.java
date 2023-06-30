package com.mss.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.constant.TokenConstant;
import com.mss.domain.dto.DynamicsCommentDto;
import com.mss.domain.dto.ReplyCommentDto;
import com.mss.domain.dto.UserDynamicsDto;
import com.mss.domain.dto.UserDynamicsDto1;
import com.mss.domain.entity.*;
import com.mss.domain.vo.*;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.mapper.UserDynamicsMapper;
import com.mss.service.*;
import com.mss.utils.AjaxResult;
import com.mss.utils.BeanCopyUtils;
import com.mss.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户动态表(UserDynamics)表服务实现类
 *
 * @author makejava
 * @since 2023-03-10 12:54:52
 */
@Service("userDynamicsService")
public class UserDynamicsServiceImpl extends
        ServiceImpl<UserDynamicsMapper, UserDynamics> implements
        UserDynamicsService {


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private LikesUserService likesUserService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OssService ossService;


    @Autowired
    private SysUserService sysUserService;



    /**
     * 用户发布动态
     * @param dynamics
     * @param request
     * @return
     */
    @Override
    public AjaxResult publishDynamics(UserDynamicsDto dynamics,
                                      List<MultipartFile> files,
                                      HttpServletRequest request) {

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        System.out.println(token);
        System.out.println();
//        String s = redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token).toString();
//        SysUser  user = JSONUtil.toBean(s, SysUser.class);

        if(Objects.isNull(files)){
            System.out.println("传递过来的文件未空！");
        }


        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);


//      bean拷贝
        UserDynamics userDynamics = BeanCopyUtils.copyBean(dynamics, UserDynamics.class);


        String images = ossService.updateImages(files);

        System.out.println(images);

        Long id = user.getId();

//      设置创建者
        userDynamics.setCreateBy(id);
        userDynamics.setCreaterName(user.getUsername());
        userDynamics.setCreaterAvatar(user.getAvatar());
        userDynamics.setImages(images);
        boolean issuccess = save(userDynamics);

        if(issuccess){
            return AjaxResult.success();
        }
        return AjaxResult.error();

    }


    /**
     * 用户发布动态
     * @param dynamics
     * @param number   照片数量
     * @param filerequest
     * @param request
     * @return
     */
    @Override
    public AjaxResult publishDynamicss(UserDynamicsDto dynamics,
                                       Integer number,
                                       MultipartRequest filerequest,
                                       HttpServletRequest request) {

//        创建图片二进制接收器数组
        List<MultipartFile> files = new ArrayList<>();

        System.out.println(number);
        System.out.println(filerequest);


        String images=null;

//        把接收到的二进制图片添加进入接收器数组
        if(number!=null)
        if (number >0) {

            if(Objects.isNull(filerequest)){
                return AjaxResult.error("File request is null");
            }

            for (int i = 0; i < number; i++) {

                MultipartFile file = filerequest.getFile("file" + i);

                if(!Objects.isNull(file)){
                    files.add(file);
                }else{
                    throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
                }

            }

            //上传图片，返回图片地址
            images = ossService.updateImages(files);
        }

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

//      bean拷贝
        UserDynamics userDynamics = BeanCopyUtils.copyBean(dynamics, UserDynamics.class);

        Long id = user.getId();

//      设置创建者
        userDynamics.setCreateBy(id);
        userDynamics.setCreaterName(user.getUsername());
        userDynamics.setCreaterAvatar(user.getAvatar());


        if(images!=null){
            userDynamics.setImages(images);
        }else{
            userDynamics.setImages(new String(""));
        }

        boolean issuccess = save(userDynamics);


        if(issuccess){

            return AjaxResult.success();
        }

        return AjaxResult.error();

    }


    /**
     * 获取社区动态列表
     * @return
     */
    @Override
    public AjaxResult getUserDynamicsList(HttpServletRequest request) {



        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user1 = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        List<UserDynamics> list =
                list(new LambdaQueryWrapper<UserDynamics>()
                        .eq(UserDynamics::getDelFlag,0));


        List<SysUser> userlist = sysUserService.list();


        //stream流排序,动态按照最新发布顺序来排序，最新发布，就排在最前面
        list =list.stream().sorted(new Comparator<UserDynamics>() {

            @Override
            public int compare(UserDynamics o1,UserDynamics o2) {

//               动态按照最新发布顺序来排序，最新发布，就排在最前面
                if(o1.getCreateTime().getTime()<o2.getCreateTime().getTime())
                {
                    return 1;
                }else if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime())
                {
                    return -1;
                }else{
                    return 0;
                }
            }
        }).collect(Collectors.toList());

        List<UserDynamicsVo> userDynamicsVos =
                BeanCopyUtils.copyBeanList(list, UserDynamicsVo.class);


        for (int i = 0; i < list.size(); i++) {

//          userDynamicsVos.get(i).setImagesArray(new ArrayList<String>());
            ArrayList<String> strings1 = new ArrayList<>();

            System.out.println(list.get(i).getImages());
            //查询出照片阿里云地址，并且分割，存入图片回显数组中
            if(list.get(i).getImages()!=""){

                String[] strings = list.get(i).getImages().split("<image>");

                for (String a: strings) {

                    strings1.add(a);

                }

                userDynamicsVos.get(i).setImagesArray(strings1);

            }

//            查询出该动态的作者
            for (SysUser user:userlist) {
                if(user.getId().equals(list.get(i).getCreateBy())){
                    userDynamicsVos
                            .get(i)
                            .setUserInfo(BeanCopyUtils.copyBean(user, SysUserVo2.class));
                }
            }

//            判断该动态是否可以删除,前端没有传token来，则所有的动态都无法删除，
//            传来token则可以根据token判断出登录用户，进而判断那篇动态可以删除
            if(!Objects.isNull(user1))
            if(list.get(i).getCreateBy().longValue()==user1.getId()){
                userDynamicsVos.get(i).setWhetherCanDelete(true);
            }


            //判断那些动态可以点赞，那些不可以点赞
            if(!Objects.isNull(user1))
            isDynamicsLiked(userDynamicsVos.get(i),user1);

        }

        return AjaxResult.success(userDynamicsVos);

    }


    /**
     * 用户点赞社区动态
     * @param dynamicsId
     * @return
     */
    @Override
    public AjaxResult likeDynamics(Long dynamicsId,HttpServletRequest request) {


        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");

        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        //判断该用户是否已经点赞
        LambdaQueryWrapper<LikesUser> qw = new LambdaQueryWrapper<>();

        qw.eq(LikesUser::getLikeType,0).eq(LikesUser::getUserId,user.getId())
                .eq(LikesUser::getBelikedId,dynamicsId);

        int count = likesUserService.count(qw);

        if(count==0){

            //3.如果未点赞，可以点赞
            //3.1 数据库点赞数+1
            boolean isSuccess = update().setSql("like_number = like_number + 1")
                    .eq("id", dynamicsId).update();
            //添加一条记录到点赞关联表当中
            likesUserService.save(new LikesUser()
                                  .setUserId(user.getId())
                                  .setLikeType(0)
                                  .setBelikedId(dynamicsId));

        }else{

            //4.如果已点赞，取消点赞
            //4.1 数据库点赞数-1
            boolean isSuccess = update().setSql("like_number = like_number - 1")
                    .eq("id", dynamicsId).update();
            //删除点赞关联表当中的一条记录
            likesUserService.remove(new LambdaQueryWrapper<LikesUser>()
                    .eq(LikesUser::getLikeType,0)
                    .eq(LikesUser::getUserId,user.getId())
                    .eq(LikesUser::getBelikedId,dynamicsId));

        }

        //调用异步方法，更新动态表中的点赞数量字段
        updateLikeNumber(user.getId(),dynamicsId,0);

        return AjaxResult.success();

    }


    /**
     * 用户点赞评论
     * @param id
     * @param request
     * @return
     */
    @Override
    public AjaxResult likeComment(Long id, HttpServletRequest request) {


        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        //判断该用户是否已经点赞
        LambdaQueryWrapper<LikesUser> qw = new LambdaQueryWrapper<>();

        qw.eq(LikesUser::getLikeType,1).eq(LikesUser::getUserId,user.getId())
                .eq(LikesUser::getBelikedId,id);

        int count = likesUserService.count(qw);

        if(count==0){

            //3.如果未点赞，可以点赞
            //3.1 数据库点赞数+1
            boolean isSuccess = update().setSql("like_number = like_number + 1")
                    .eq("id", id).update();
            //添加一条记录到点赞关联表当中
            likesUserService.save(new LikesUser()
                    .setUserId(user.getId())
                    .setLikeType(1)
                    .setBelikedId(id));

        }else{

            //4.如果已点赞，取消点赞
            //4.1 数据库点赞数-1
            boolean isSuccess = update().setSql("like_number = like_number - 1")
                    .eq("id", id).update();
            //删除点赞关联表当中的一条记录
            likesUserService.remove(new LambdaQueryWrapper<LikesUser>()
                    .eq(LikesUser::getLikeType,1)
                    .eq(LikesUser::getUserId,user.getId())
                    .eq(LikesUser::getBelikedId,id));

        }

        //调用异步方法，更新动态表中的点赞数量字段
        updateLikeNumber(user.getId(),id,1);
        return AjaxResult.success();

    }


    /**
     * 就某一个用户的某一条动态发布评论
     * @param dto
     * @return
     */
    @Override
    public AjaxResult publishComment(DynamicsCommentDto dto,
                                     Integer number,
                                     MultipartRequest filerequest,
                                     HttpServletRequest request   ) {


//        创建图片二进制接收器数组
        List<MultipartFile> files = new ArrayList<>();

//        把接收到的二进制图片添加进入接收器数组
        if (number!=null&&number > 0) {

            for (int i = 0; i < number; i++) {
                files.add(filerequest.getFile("file" + i));
            }

        }


        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser) redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        Comment comment = BeanCopyUtils.copyBean(dto, Comment.class);
        comment.setCreateBy(user.getId());
        comment.setToCommentId(dto.getToDynamicsId());
        comment.setToCommentUserId(dto.getToDynamicsUserId());

        comment.setRootId(dto.getToDynamicsId());
        comment.setType("0");

        comment.setCreaterName(user.getNickName());
        comment.setCreaterAvatar(user.getAvatar());


//        更新该被评论的动态的评论数量
        boolean  isture= update().setSql("comment_number = comment_number+1")
                .eq("id", dto.getToDynamicsId()).update();


        if(files.size()!=0){
//      上传图片，返回图片地址url
            String images = ossService.updateImages(files);
//        设置动态评论的图片url地址数组
//        comment.setImages(urls.toString());
            comment.setImages(images);
        }

        boolean issuccess = commentService.save(comment);

        if(!issuccess){
            return AjaxResult.error();
        }

        return AjaxResult.success();

    }


    /**
     * 对某一条评论回复
     * @param dto
     * @param request
     * @return
     */
    @Override
    public AjaxResult replyComment(ReplyCommentDto dto, HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        Comment comment = BeanCopyUtils.copyBean(dto, Comment.class);

        comment.setCreateBy(user.getId());
        comment.setRootId(dto.getToCommentId());
        comment.setType("1");

        comment.setCreaterName(user.getNickName());
        comment.setCreaterAvatar(user.getAvatar());

        //更新该被评论的动态一级评论的评论数量
        boolean  isture= update().setSql("comment_number = comment_number+1")
                .eq("id", dto.getToCommentId()).update();

        boolean issuccess = commentService.save(comment);




        if(!issuccess){
            return AjaxResult.error();
        }

        return AjaxResult.success();

    }


    /**
     * 根据动态id查看具体动态详情
     * @param id
     * @return
     */
    @Override
    public AjaxResult lookDynamicsDetailById(Long id,HttpServletRequest request) {


        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user1 = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);


        UserDynamics dynamics = getById(id);

        SysUser user = sysUserService.getById(dynamics.getCreateBy());

        String[] images = dynamics.getImages().split("<image>");

        UserDynamicsVo dynamicsVo = BeanCopyUtils.copyBean(dynamics, UserDynamicsVo.class);

//      返回用户动态的照片
        if(!Objects.isNull(images))
        {
            ArrayList<String> strings = new ArrayList<>();
            for (String image : images) {

                strings.add(image);

            }
            dynamicsVo.setImagesArray(strings);

        }

//      封装返回用户信息（头像，昵称，用户名）
        dynamicsVo.setUserInfo(BeanCopyUtils.copyBean(user,SysUserVo2.class));

//      判断该动态是否可以删除,前端传递token过来，即为目前有用户处于登录状态，可以删除，若没有token则不可删除
        if(!Objects.isNull(user1)){
            dynamicsVo.setWhetherCanDelete(true);
        }

//        判断是否已经点赞
        if(!Objects.isNull(user1))
        isDynamicsLiked(dynamicsVo,user1);

        return AjaxResult.success(dynamicsVo);


    }


    /**
     * 个人主页获取用户发布的动态列表
     * @param request
     * @return
     */
    @Override
    public AjaxResult getDynamicsListByUserId(HttpServletRequest request) {
        return null;
    }

    /**
     * 根据动态id删除用户动态
     * @param id
     * @return
     */
    @Override
    public AjaxResult deleteDynamicsById(Long id) {


//        删除动态表记录
        boolean issuccess = removeById(id);

//
        if(!issuccess){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
//        再删除与该表相关的所有评论记录


        return AjaxResult.success();

    }


    /**
     * 根据动态id查看动态评论列表,若评论有子评论则递归查询其子评论
     * @param id
     * @return
     */
    @Override
    public AjaxResult lookComment(Long id,HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

//        查询出所有评论
        List<Comment> allcomment = commentService.list(
                new LambdaQueryWrapper<Comment>().eq(Comment::getDelFlag,0));

//        查询出所有用户
        List<SysUser> userList = sysUserService.list();

//     查询出所有动态第一级评论
       List<Comment> commentLists = allcomment.stream()
                .filter(a -> a.getType().equals("0"))
                .filter(a -> a.getRootId().equals(id))
                .collect(Collectors.toList());

//       排序评论，后发布的评论排在前面，按照时间后先顺序
       commentLists=commentLists.stream().sorted(

               new Comparator<Comment>() {
                   @Override
                   public int compare(Comment o1, Comment o2) {

                       if(o1.getCreateTime().getTime()<o2.getCreateTime().getTime())
                       {
                           return 1;

                       }else if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime())
                       {
                           return -1;
                       }else{
                           return 0;
                       }
                   }
               }
       ).collect(Collectors.toList());


//     查询出所有动态的二级以及一下层的评论
       List<Comment> childcomment = allcomment.stream()
                .filter(b -> b.getType().equals("1"))
                .collect(Collectors.toList());

       List<DynamicsCommentVo> CommentLists =
                BeanCopyUtils.copyBeanList(commentLists, DynamicsCommentVo.class);

        for (DynamicsCommentVo a: CommentLists) {

//          判断一级评论是否存在照片，有照片则根据分隔符裁剪出相应的照片
            if(a.getImages()!=null){

                System.out.println(a.getImages());
                ArrayList<String> strings = new ArrayList<>();
                String[] images = a.getImages().split("<image>" );
                for (String b:images) {
                    strings.add(b);
                }
                a.setImageArray(strings);

            }

            List<DynamicsCommentVo2> abc = new ArrayList<>();

//          筛选出动态一级评论的子评论
            for (Comment b:childcomment) {

                if(b.getRootId().equals(a.getId()))
                {
                    /**
                     * TODO
                     */

                    DynamicsCommentVo2 bean =
                            BeanCopyUtils.copyBean(b, DynamicsCommentVo2.class);
//                    查询出该子评论的回复目标评论的用户昵称
                    for (SysUser user2: userList) {
                        if( b.getToCommentUserId().equals(user2.getId())){
                            bean.setToCommentUserNickName(user2.getNickName());
                        }
                    }

//                    判断该子评论是否为楼主发出
                    if(bean.getCreateBy().intValue()==a.getCreateBy().intValue()){
                        bean.setMaster(true);
                    }

                    abc.add(bean);

                }
            }

//          stream流筛选，动态一级评论的子评论按照时间后先排序，最新发布，就排在最前面
            List<DynamicsCommentVo2> sortedListVo = abc.stream()
                    .sorted(new Comparator<DynamicsCommentVo2>() {
                    @Override
                    public int compare(DynamicsCommentVo2 o1, DynamicsCommentVo2 o2) {

                        if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime())
                        {
                            return 1;

                        }else if(o1.getCreateTime().getTime()<o2.getCreateTime().getTime())
                        {
                            return -1;
                        }else{
                            return 0;
                        }
                    }

               }
               ).collect(Collectors.toList());

//            设置一级评论下的二级子评论
               a.setChildComment(sortedListVo);

//            判断该一级评论是否可以删除
            if(!Objects.isNull(user)){
                if(user.getId().longValue()==a.getCreateBy().longValue()){
                    a.setWhetherCanDelete(true);
                }
            }

//          判断一级评论是否已经被点赞
            isCommentLiked(a, user);

        }

        List<DynamicsCommentVo2> commentListVo2s =
                BeanCopyUtils.copyBeanList(CommentLists, DynamicsCommentVo2.class);

        return AjaxResult.success(commentListVo2s);

    }


    /**
     * 查看一级评论的子评论列表详情
     * @param id
     * @param request
     * @return
     */
    @Override
    public AjaxResult lookChildCommentDetails(Long id, HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

//
        List<Comment> list = commentService.list(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getType, "1")
                .eq(Comment::getRootId, id));


        List<ChildCommentVo> commentVos =
                BeanCopyUtils.copyBeanList(list, ChildCommentVo.class);


        for (ChildCommentVo vo:commentVos) {

//            判断是否可以点赞
            isCommentLiked(vo,user);

//            判断是否可以删除
            if(vo.getCreateBy().longValue()==user.getId().longValue()){
                vo.setWhetherCanDelete(true);
            }

            Comment comment = commentService.getOne(
                    new LambdaQueryWrapper<Comment>().eq(Comment::getId, id));

//            判断是否为楼主发出
            if(vo.getCreateBy().longValue()==comment.getToCommentUserId().longValue()){
                vo.setMaster(true);
            }

        }

        return AjaxResult.success(commentVos);
    }


    /**
     * 根据评论id查看该一级评论的评论详情
     * @param id
     * @param request
     * @return
     */
    @Override
    public AjaxResult lookCommentDetails(Long id, HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        Comment comment = commentService.getOne(
                new LambdaQueryWrapper<Comment>()
                .eq(Comment::getType, "0")
                .eq(Comment::getId, id));

        DynamicsCommentVo2 commentVo2 = BeanCopyUtils.copyBean(comment, DynamicsCommentVo2.class);

//        判断该一级动态是否有照片
        if(!comment.getImages().equals(null)){
            String[] strings = comment.getImages().split("<image>");
            ArrayList<String> strings1 = new ArrayList<>();
            for (String url : strings) {
                strings1.add(url);
            }
            commentVo2.setImageArray(strings1);
        }

//        判断该一级评论是否还可以点赞
        isCommentLiked(commentVo2,user);

//        设置该一级动态评论是否可以删除
        if(commentVo2.getCreateBy().longValue()==user.getId()){

            commentVo2.setWhetherCanDelete(true);

        }

        return AjaxResult.success(commentVo2);
    }


    /**
     * 发布新动态接口3，有照片
     * @param dynamics
     * @param files1
     * @param request
     * @return
     */
    @Override
    public AjaxResult publishNewDynamicss(UserDynamicsDto1 dynamics, MultipartRequest files1, HttpServletRequest request) {

        //        创建图片二进制接收器数组
        List<MultipartFile> files = new ArrayList<>();

        System.out.println(dynamics.getNumber());
        System.out.println(files1);

        String images=null;
        Integer number=dynamics.getNumber();

//        把接收到的二进制图片添加进入接收器数组
        if(number!=null)
            if (number >0) {

//                判断文件是否已经接收到
                if(Objects.isNull(files1)){
                    return AjaxResult.error("File request is null");
                }

                for (int i = 0; i < number; i++) {

                    MultipartFile file = files1.getFile("file" + i);

                    if(!Objects.isNull(file)){
                        files.add(file);
                    }else{
                        throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
                    }

                }
                //上传图片，返回图片地址
                images = ossService.updateImages(files);
            }

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);


//      bean拷贝
        UserDynamics userDynamics = BeanCopyUtils.copyBean(dynamics, UserDynamics.class);

        Long id = user.getId();

//      设置创建者
        userDynamics.setCreateBy(id);
        userDynamics.setCreaterName(user.getUsername());
        userDynamics.setCreaterAvatar(user.getAvatar());

        if(images!=null){
            userDynamics.setImages(images);
        }else{
            userDynamics.setImages("");
        }

        boolean issuccess = save(userDynamics);

        if(issuccess){
            return AjaxResult.success();
        }

        return AjaxResult.error();
    }


    /**
     * 发布动态，无照片
     * @param dynamics
     * @param request
     * @return
     */
    @Override
    public AjaxResult publishDynamicsNoPictrue(UserDynamicsDto dynamics, HttpServletRequest request) {

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

//      bean拷贝
        UserDynamics userDynamics = BeanCopyUtils.copyBean(dynamics, UserDynamics.class);

        Long id = user.getId();

//      设置创建者
        userDynamics.setCreateBy(id);
        userDynamics.setCreaterName(user.getUsername());
        userDynamics.setCreaterAvatar(user.getAvatar());

//        设置图片数组为空
        userDynamics.setImages("");

        boolean issuccess = save(userDynamics);

        if(issuccess){

            //获取用户动态列表，把最新的第一条动态返回给前端
            return AjaxResult.success();

        }

        return AjaxResult.error();
    }

    /**
     * 异步方法，更新点赞后的动态表/评论表的点赞数量
     * @param userId   点赞用户id
     * @param beLikedId  被点赞用户id
     * @param type  点赞类型：0动态  1评论
     */
    @Async
    public void updateLikeNumber(Long userId,Long beLikedId,Integer type){


        if(type==0){

            LambdaQueryWrapper<LikesUser> qw = new LambdaQueryWrapper<>();
            qw.eq(LikesUser::getLikeType,type)
              .eq(LikesUser::getBelikedId,beLikedId);
            int count = likesUserService.count(qw);

            boolean issuccess = update().setSql("like_number=" + count)
                    .eq("id", beLikedId).update();

        }

        if(type==1){

            LambdaQueryWrapper<LikesUser> qw = new LambdaQueryWrapper<>();
            qw.eq(LikesUser::getLikeType,type)
              .eq(LikesUser::getBelikedId,beLikedId);
            int count = likesUserService.count(qw);

            boolean issuccess = commentService.update().setSql("like_number=" + count)
                    .eq("id", beLikedId).update();

        }


    }


    /**
     * 判断a动态是否已经被b用户点赞
     * @param a
     * @param b
     */
    public void isDynamicsLiked(UserDynamicsVo a,SysUser b){


        int count = likesUserService.count(new LambdaQueryWrapper<LikesUser>()
                .eq(LikesUser::getLikeType, 0)
                .eq(LikesUser::getUserId, b.getId())
                .eq(LikesUser::getBelikedId, a.getId()));

        if(count == 1){

            a.setIsLike(true);

        }else if(count == 0){

            a.setIsLike(false);

        }

    }

    /**
     * 判断a评论是否已经被b用户点赞
     * @param a
     * @param b
     */
    public void isCommentLiked(DynamicsCommentVo a, SysUser b){

        int count = likesUserService.count(new LambdaQueryWrapper<LikesUser>()
                .eq(LikesUser::getLikeType, 0)
                .eq(LikesUser::getUserId, b.getId())
                .eq(LikesUser::getBelikedId, a.getId()));

        if(count == 1){
            a.setIsLike(true);
        }
    }

    /**
     * 判断a评论是否已经被b用户点赞
     * @param a
     * @param b
     */
    public void isCommentLiked(DynamicsCommentVo2 a, SysUser b){

        int count = likesUserService.count(new LambdaQueryWrapper<LikesUser>()
                .eq(LikesUser::getLikeType, 0)
                .eq(LikesUser::getUserId, b.getId())
                .eq(LikesUser::getBelikedId, a.getId()));

        if(count == 1){
            a.setIsLike(true);
        }
    }

    /**
     * 判断a评论是否已经被b用户点赞
     * @param a
     * @param b
     */
    public void isCommentLiked(ChildCommentVo a, SysUser b){

        int count = likesUserService.count(new LambdaQueryWrapper<LikesUser>()
                .eq(LikesUser::getLikeType, 0)
                .eq(LikesUser::getUserId, b.getId())
                .eq(LikesUser::getBelikedId, a.getId()));

        if(count == 1){
            a.setIsLike(true);
        }
    }

}

