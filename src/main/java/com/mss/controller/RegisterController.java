package com.mss.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mss.domain.dto.RegisterDto;
import com.mss.domain.entity.MemoryBox;
import com.mss.domain.entity.PublicCard;
import com.mss.domain.entity.SysUser;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.service.MemoryBoxService;
import com.mss.service.PublicCardService;
import com.mss.service.SysUserService;
import com.mss.utils.AjaxResult;
import com.mss.utils.RegexUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 注册控制器
 */
@Api(tags = "用户注册")
@RestController
@RequestMapping("/system/user")
public class RegisterController {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PublicCardService publicCardService;

    @Autowired
    private MemoryBoxService memoryBoxService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation("新用户注册(账号密码注册)")
    @PostMapping("/register")
    public AjaxResult userRegister(@RequestBody RegisterDto dto){

        SysUser sysUser = new SysUser();

        SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getAccount()));


        if(!Objects.isNull(one)){
            throw new SystemException(AppHttpCodeEnum.ACCOUNT_EXIST);
        }

        System.out.println(sysUser.getId());
        // 使用springsecurity默认加密器给密码加密
        String password = passwordEncoder.encode(dto.getPassword());
        sysUser.setPassword(password);
        sysUser.setUsername(dto.getAccount());
        sysUser.setNickName("新用户"+ RandomUtil.randomNumbers(9));

        boolean issucess = sysUserService.save(sysUser);

        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getAccount())
                .eq(SysUser::getPassword, password);

//         $2a$10$aFe1Odfr5AeyNsoSrto21eG3OVv2aRbRKPMn5mXALGireoaybci1C
//        获取到注册用户信息
        SysUser user = sysUserService.getOne(qw);


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

//       给用户手册盒子推荐15张卡牌闪击
        return AjaxResult.success("注册成功！");


    }


}
