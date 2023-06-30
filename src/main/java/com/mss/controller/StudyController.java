package com.mss.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mss.answer.OpenAiAnswerer;
import com.mss.chatgptdemo.CustomChatGpt;
import com.mss.constant.TokenConstant;
import com.mss.domain.dto.*;
import com.mss.domain.entity.MemoryBox;
import com.mss.domain.entity.PublicCard;
import com.mss.domain.entity.SysUser;
import com.mss.mapper.PublicCardMapper;
import com.mss.service.*;
import com.mss.utils.*;
import com.mss.utils.baidu.SpeechSynthesis.TtsUtil;
import com.mss.utils.baidu.SpeechSynthesis.core.ConnException;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.util.Proxys;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "学习")
@RestController
@RequestMapping("/study")
public class StudyController {


    @Autowired
    public MemoryCardService memoryCardService;

    @Autowired
    public MemoryBoxService memoryBoxService;

    @Autowired
    public NoteCardService noteCardService;

    @Autowired
    public OptionCardService optionCardService;

    @Autowired
    public WordCardService wordCardService;

    @Autowired
    public PublicCardService publicCardService;

    @Autowired
    public PublicCardMapper publicCardMapper;

    @Autowired
    public FrontBackCardService frontBackCardService;


    @Autowired
    public FillInBackCardService fillInBackCardService;

//    @ApiOperation("获取学习内容")
//    @GetMapping("/getStudyContent")
//    public AjaxResult getStudyContent() {
//
//        return AjaxResult.success();
//
//    }

//    @ApiOperation("今日待学")
//    @GetMapping("toStudyContent")
//    public AjaxResult getToStudyContent() {
//
//        return AjaxResult.success();
//
//    }


    @ApiOperation("获取登录用户所有的卡片,带上token")
    @GetMapping("/getAllCards")
    public AjaxResult getAllCard(HttpServletRequest request) {

        return publicCardService.getAllCardByUserId(request);

    }

    @ApiOperation("获取登录用户的20张最新的卡片,带上token")
    @GetMapping("/previewCards")
    public AjaxResult previewCard(HttpServletRequest request){

        return publicCardService.previewCardByUserId(request);

    }


    @ApiOperation("获取用户卡片盒列表")
    @GetMapping("/getUserBoxList")
    public AjaxResult getUserMemoryBoxList(HttpServletRequest request) {

        return memoryBoxService.getUserMemoryBoxList(request);

    }

    @ApiOperation("根据具体卡片盒子id查询卡片盒详情")
    @GetMapping("/getBoxById/{id}")
    public AjaxResult getBoxDetailsById(@PathVariable("id") Long id){

        return memoryBoxService.getBoxDetailsByBoxId(id);

        //        递归构造卡片盒子树
//        MemoryBox memoryBox = MemoryBoxUtils.findChildren(box, list, cardList);

    }

    @ApiOperation("根据根盒子id和用户id，获取该用户所创建的该盒子下的所有卡牌")
    @GetMapping("/getAllCardInBox")
    public AjaxResult getAllCardInBox(Long boxId,HttpServletRequest request) {

        return publicCardService.getAllCardInBox(boxId,request);

    }


    @ApiOperation("根据卡片id获取卡片详情")
    @GetMapping("/getCardById/{id}")
    public AjaxResult getCardById(@PathVariable("id") Long id){

      PublicCard card = publicCardMapper.selectCardById(id);
//      PublicCard card = publicCardMapper.selectById(id);
        System.out.println(card);
        Object object = MemoryBoxUtils.toTargetObject(card);
        return AjaxResult.success(object);

    }


    @ApiOperation("创建用户记忆卡")
    @PostMapping("/createMemoryCard")
    public AjaxResult createMemoryCard(@RequestBody MemoryCardDto dto,
                                       HttpServletRequest request) {

        return memoryCardService.createMemoryCard(dto, request);

    }


    @ApiOperation("修改用户记忆卡")
    @PutMapping("/updateMemoryCard")
    public AjaxResult updateMemoryCard(@RequestBody UpdateMemoryCardDto dto) {

//        修改
        boolean update = publicCardService.update().setSql("question=" + dto.getQuestion())
                .setSql("answer=" + dto.getAnswer())
//              .setSql("belong_to_box_name=" + dto.getBelongToBoxName())
                .setSql("belong_to_box_id=" + dto.getBelongToBoxId())
                .eq("id=", dto.getId()).update();

//        查询最新卡片数据，返回最新的修改数据给前端
        PublicCard memorycard = publicCardService.getById(dto.getId());

        return AjaxResult.success(memorycard);


    }


    @ApiOperation("用户创建笔记卡")
    @PostMapping("/createNoteCard")
    public AjaxResult createNoteCard(@RequestBody NoteCardDto dto,
                                     HttpServletRequest request) {

        return noteCardService.createNoteCard(dto, request);

    }


    @ApiOperation("修改用户笔记卡")
    @PostMapping("/updateNoteCard")
    public AjaxResult updateNoteCard(@RequestBody UpdateNoteCardDto dto) {

        //修改
        boolean update = publicCardService.update().setSql("title=" + dto.getTitle())
                .setSql("note=" + dto.getNote())
                .setSql("belong_to_box_id=" + dto.getBelongToBoxId())
                .eq("id=", dto.getId()).update();

//      查询最新卡片数据，返回最新的修改数据给前端
        PublicCard memorycard = publicCardService.getById(dto.getId());

        return AjaxResult.success(memorycard);

    }


    @ApiOperation("用户创建选项卡(选项区不建议放照片）")
    @PostMapping("/createOptionCard")
    public AjaxResult createOptionCard(@RequestBody OptionCardDto dto,
                                       HttpServletRequest request) {

        return optionCardService.createOptionCard(dto, request);

    }


    @ApiOperation("修改用户选项卡(选项区不建议放卡片）")
    @PostMapping("/updateOptionCard")
    public AjaxResult updateOptionCard(@RequestBody UpdateOptionCardDto dto) {

        //修改
        boolean update1 = publicCardService.update(
                BeanCopyUtils.copyBean(dto, PublicCard.class),
                new QueryWrapper<PublicCard>().eq("id", dto.getId()));


//      查询最新卡片数据，返回最新的修改数据给前端
        PublicCard memorycard = publicCardService.getById(dto.getId());

        return AjaxResult.success(memorycard);

    }




    @ApiOperation("用户创建智能单词卡")
    @PostMapping("/createWordCard")
    public AjaxResult createWordCard(@RequestBody WordCardDto dto,
                                     HttpServletRequest request) {

        return wordCardService.createWordCard(dto, request);

    }


    @ApiOperation("修改用户智能单词卡")
    @PostMapping("/updateWordCard")
    public AjaxResult updateWordCard(@RequestBody UpdateWordCardDto dto) {

        //修改
        boolean update = publicCardService.update(BeanCopyUtils.copyBean(dto, PublicCard.class),
                new QueryWrapper<PublicCard>().eq("id", dto.getId()));

//        查询最新卡片数据，返回最新的修改数据给前端
        PublicCard memorycard = publicCardService.getById(dto.getId());

        return AjaxResult.success(memorycard);

    }


    @ApiOperation("单词卡片单词实时翻译，翻译为中文")
    @GetMapping("/translateWord")
    public AjaxResult translateWord(String text) {

        return wordCardService.translateWord(text);

    }


    @ApiOperation("用户创建正反卡")
    @PostMapping("/createFrontBackCard")
    public AjaxResult createFrontBackCard(@RequestBody FrontBackCardDto dto,
                                          HttpServletRequest request) {

        return frontBackCardService.createFrontBackCard(dto, request);

    }


//    @ApiOperation("修改用户正反卡")
//    @PutMapping("/updateFrontBackCard")
//    public AjaxResult updateFrontBackCard(@RequestBody UpdateFrontBackCardDto dto){
//
//        //修改
//        boolean update = publicCardService.update(BeanCopyUtils.copyBean(dto, PublicCard.class),
//                new QueryWrapper<PublicCard>().eq("id", dto.getId()));
//        查询最新卡片数据，返回最新的修改数据给前端
//        PublicCard memorycard = publicCardService.getById(dto.getId());
//        return AjaxResult.success(memorycard);
//
//    }


    @ApiOperation("用户创建填空卡")
    @PostMapping("/createFillInBackCard")
    public AjaxResult createFillInBackCard(@RequestBody FillInBackCardDto dto, HttpServletRequest request) {

        return fillInBackCardService.createFillInBackCard(dto, request);

    }

    @ApiOperation("修改用户填空卡")
    @PostMapping("/updateFillInBackCard")
    public AjaxResult updateFillInBackCard(@RequestBody UpdateFillInBackCardDto dto) {

        //修改
        boolean update = publicCardService.update(BeanCopyUtils.copyBean(dto, PublicCard.class),
                new QueryWrapper<PublicCard>().eq("id", dto.getId()));

        //查询最新卡片数据，返回最新的修改数据给前端
        PublicCard memorycard = publicCardService.getById(dto.getId());

        return AjaxResult.success(memorycard);

    }



    @ApiOperation("创建卡牌盒")
    @PostMapping("/createBox")
    public AjaxResult createMemoryBox(@RequestBody MemoryBoxDto dto,
                                      HttpServletRequest request) {

        return memoryBoxService.createCardBox(dto,request);

    }



    @ApiOperation("根据id修改编辑用户卡片盒")
    @PutMapping("/updateBox")
    public AjaxResult updateMemoryBox(UpdateMemoryBoxDto dto, MultipartFile file) {

        return memoryBoxService.updateBox(dto, file);

    }


    @ApiOperation("根据id删除用户卡片盒")
    @DeleteMapping("/deleteBox/{id}")
    public AjaxResult deleteMemoryBox(@PathVariable("id") Long id) {

        boolean issuccess = memoryBoxService.removeById(id);

        if (!issuccess) {
            return AjaxResult.error();
        }

        return AjaxResult.success();

    }


    @ApiOperation("根据id停学用户卡片盒")
    @PutMapping("stopLearnBox/{id}")
    public AjaxResult stopLearnMemoryBox(@PathVariable("id") Long id) {

        return memoryBoxService.stopLearnBoxById(id);

    }


    @ApiOperation("根据id重置用户卡片盒")
    @PutMapping("/resetBox/{id}")
    public AjaxResult resetMemoryBox(@PathVariable("id") Long id,HttpServletRequest request) {

        return memoryBoxService.resetBoxById(id,request);

    }




    @ApiOperation("卡片未隐藏内容实时语音朗读")
    @GetMapping("/readAloud")
    public AjaxResult readAloud(String text, @RequestParam(required = false) Integer speed) {

//      解析富文本，获取其中的文字
        String s = RichTextUtils.getText(text);
        System.out.println(s);
        String url = null;
        try {

            if (!Objects.isNull(speed)) {
                url = TtsUtil.textSound(s, 0, speed, 5, 5, 3);
            } else {
                url = TtsUtil.textSound(s, 0, 5, 5, 5, 3);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConnException e) {
            e.printStackTrace();
        }

//        返回在线播放语音地址
        return AjaxResult.success().put("playUrl", url);

    }




    @ApiOperation("获取用户卡片盒树")
    @GetMapping("/getUserBoxTree")
    public AjaxResult getUserMemoryBoxTree(HttpServletRequest request) {

        return memoryBoxService.getUserMemoryBoxTree(request);

    }


    @ApiOperation("卡片盒里创建文件夹(即创建子盒子)")
    @PostMapping("/createChildBox")
    public AjaxResult createChildBox(@RequestBody ChildMemoryBoxDto dto,
                                     HttpServletRequest request) {

        return memoryBoxService.createChildBox(dto, request);

    }


    @ApiOperation("根据id删除卡片")
    @DeleteMapping("/deleteCard/{id}")
    public AjaxResult deleteCardById(@PathVariable("id") Long id) {

        return publicCardService.deleteCardById(id);

    }


    @ApiOperation("根据id停学卡片")
    @PutMapping("/stopLearnCard/{id}")
    public AjaxResult stopLearnCardById(@PathVariable("id") Long id) {

        return publicCardService.stopLearnCardById(id);

    }


    @ApiOperation("根据id重置卡片")
    @PutMapping("/resetCard/{id}")
    public AjaxResult resetCardById(@PathVariable("id") Long id) {

        return publicCardService.resetCardById(id);

    }


    @ApiOperation("根据卡牌盒子id,开始学习")
    @GetMapping("/learnBoxById/{id}")
    public AjaxResult learnBoxById(@RequestBody BoxLearnSetDto dto,
                                   @PathVariable(value = "id", required = true) Long id,
                                   HttpServletRequest request) {

        return publicCardService.getLearnCardListBySetting(dto, id, request);

    }


    @ApiOperation("根据关键词搜索卡片,返回包含关键字的卡片数组，elascsearch接口")
    @GetMapping("/searchByKeyWord/{keyword}")
    public AjaxResult searchCardByKeyWord(@PathVariable("keyword") String keyword,
                                          HttpServletRequest request){

//        return AjaxResult.success("环境暂未搭建好");
        return  publicCardService.searchCardListByKeyWord(keyword,request);

    }


    @ApiOperation("根据关键词搜索卡片,返回包含关键字的卡片数组,mysql版本接口")
    @GetMapping("/searchByKeyWordByMysql/{keyword}")
    public AjaxResult searchCardByKeyWordByMySql(@PathVariable("keyword") String keyword,
                                          HttpServletRequest request){

        return  publicCardService.searchCardListByKeyWordByMysql(keyword,request);

    }


    @ApiOperation("去学习接口,把所有未学习的卡片都学习完")
    @GetMapping("/toLearn")
    public AjaxResult toLearnByUserId(HttpServletRequest request)
    {

        return publicCardService.toLearn(request);

    }


    @ApiOperation("每一张卡片学习复习后调用接口")
    @PostMapping("learnCard")
    public AjaxResult learnCard(
            @RequestParam("id") Long id,
            @RequestParam("status") String learnStatus)
    {

        return publicCardService.learnCard(id,learnStatus);

    }


//    @ApiOperation("chatgpt智能问答接口(一问一答，不是聊天)")
//    @PostMapping("/askChatGPT")
//    public AjaxResult testChatgpt(@RequestBody String question){
//
//        OpenAiAnswerer openAiAnswerer = new OpenAiAnswerer();
//        String answer = openAiAnswerer.doAnswer(question);
//
////        去除掉前面两个换行符号
//        String substring = answer.substring(2);
//
//        return AjaxResult.success().put("answer",substring);
//
//    }




    @ApiOperation("chatgpt智能问答接口")
    @PostMapping("/askQuestionToChatGPT")
    public AjaxResult AskQuestionToChatgpt(@RequestBody String question){


        CloseableHttpClient httpClient = HttpClients.createDefault();
        String apiKey = "your apiKey";
        CustomChatGpt customChatGpt = new CustomChatGpt(apiKey);
        // 根据自己的网络设置吧
        customChatGpt.setResponseTimeout(25000);

        long start = System.currentTimeMillis();
        String answer = customChatGpt.getAnswer(httpClient, question);
        long end = System.currentTimeMillis();
        System.out.println("该回答花费时间为：" + (end - start) / 1000.0 + "秒");
        System.out.println(answer);

        return AjaxResult.success().put("answer", answer);


    }



}


