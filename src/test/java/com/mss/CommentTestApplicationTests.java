package com.mss;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;

import com.mss.domain.entity.MemoryBox;
import com.mss.utils.*;
import com.mss.utils.baidu.SpeechSynthesis.TtsUtil;
import com.mss.utils.baidu.SpeechSynthesis.core.ConnException;
import com.mss.utils.baidu.translate.TransApi;
import com.mss.utils.baidu.translate.UnicodeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class CommentTestApplicationTests {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisCache redisCache;


//    @Test
//    void contextLoads() {
//
//        System.out.println(TimeUnit.SECONDS);
//
//        final String test = redisCache.getCacheObject("test").toString();
//
//        System.out.println(test);
//
//      cat tom = new cat(2, "tom");
//
//        redisCache.setCacheObject("cat","DSBA", new Integer(1800), TimeUnit.SECONDS);
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        final String cat = redisCache.getCacheObject("cat").toString();
//        System.out.println(cat);
//    }


//    @Autowired
//    public  RedisCache redisCache;


//    @Test
//    public void TestCode(){
//
//        //获取手机号
//        String phone = "17807899343";				//输入手机号,要填绑定测试的手机号码
//        //生成验证码
//        String code = ValidateCodeUtils.generateValidateCode(4).toString();
//        //发送短信
//        /**
//         * 发送短信
//         * @param signName 签名
//         * @param templateCode 模板
//         * @param phoneNumbers 手机号
//         * @param param 参数
//         */
//        SMSUtils.sendMessage("蒙绍山的博客",
//                "SMS_271625812",phone,code,"LTAI5tBQfpsARyrifRvvAoLA",
//                "LmK5FgdkhPFSgngwVGNgK7DgBeHoK4");
//
//    }


//    @Test
//    public void test1(){
//
//
//        redisCache.setCacheObject("test",new cat(18,"tom"),30,1);
//
//        System.out.println(System.currentTimeMillis()+"    ");
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(System.currentTimeMillis()+"    ");
//
//
//         String test = redisCache.getCacheObject("test").toString();
//
//
//        System.out.println(test);
//
//    }


    @Test
    public void test2() {

        final String name = NameUtils.getRandomJianHan(6);

        String urls = "https://test-anki.oss-cn-guangzhou.aliyuncs.com/背景1.jpeg<<image>>https://test-anki.oss-cn-guangzhou.aliyuncs.com/背景2.jpeg<<image>>" +
                "https://test-anki.oss-cn-guangzhou.aliyuncs.com/背景3.jpeg" +
                "<<image>>https://test-anki.oss-cn-guangzhou.aliyuncs.com/背景4.jpeg<<image>>";
        System.out.println(name);

        System.out.println("用户" + NameUtils.getStringRandom(5));

        final boolean b = RegexUtils.checkEmail("3381829902@qq.com");
        System.out.println(b);

        final boolean admin = RegexUtils.checkPhone("17807899343");
        System.out.println(admin + passwordEncoder.encode("111111"));

        //生成随机数，指定长度
        final String numbers = RandomUtil.randomNumbers(8);
        System.out.println(numbers);
        final String[] strings = urls.split("https");
        for (String n : strings) {

            System.out.println(n);

        }

    }

//    @Autowired
//    private  AddressUtils address;


    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testredis() {


//        Date ss = new Date();
//        System.out.println("一般日期输出：" + ss);
//        System.out.println("时间戳：" + ss.getTime());
//        //Date aw = Calendar.getInstance().getTime();//获得时间的另一种方式，测试效果一样
//        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = format0.format(ss.getTime());//这个就是把时间戳经过处理得到期望格式的时间
//        System.out.println("格式化结果0：" + time);
//        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
//
//        final String time2 = format1.format(ss.getTime());
//        System.out.println(time2);
//        String a="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzMDZhYjVjYmQ0Y2Y0ZDk5OTRjNzZjZGNiZDA4NjJiYSIsIn" +
//                "N1YiI6IjE2MzI5NzM3NTcxODQyMzM0NzRlZTY4MzA0NC1lZDgxLTQyYjItYWM5ZC04MmFlMjdkZjM1OD" +
//                "AiLCJpc3MiOiJzZyIsImlhdCI6MTY3OTAzMjAwNSwiZXhwIjoxNzEwNTY4MDA1fQ.952BqSAlu0ovSfP" +
//                "J8-8xuN2djzIEvtx7GMdXCwB-ccU";
//
//        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY +a );
//
//        System.out.println(user);
//        System.out.println(user.getId());

//        String addresses = new AddressUtils().getRealAddressByIP(" 10.105.66.244");
//
//        System.out.println(addresses);
//

//        final boolean b = RegexUtils.checkAccountNumber("33333677");
//        System.out.println(b);
//        final boolean b1 = RegexUtils.checkPhone("17807899343");
//        System.out.println(b1);

    }



    @Test
    public void testCHATGPT(){

//        try {
//            String s = ChatGPTClient.generateResponse("你是谁？");
//            System.out.println(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }


        String result="{\n" +
                "  \"msg\": null,\n" +
                "  \"code\": 200\n" +
                "}";
        try {
            JSONObject responseJson = new JSONObject(result);
            System.out.println(responseJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试Unicode转汉字
     *
     * @throws UnsupportedEncodingException
     */
    @Test
    public void test05() throws UnsupportedEncodingException {


//        List<Object> list = new ArrayList<Object>();
//        list.add("CBDHCDH");
//        list.add(new Integer(757787));
//        list.add(new Date());
//        list.add(new SysUser());
//        for (Object a:list) {
//            System.out.println(a.getClass().getName());
//            String s = new String(a.getClass().getName());
//            System.out.println(s);
//            final String[] split = s.split(".");
//            System.out.println(split.length);
//        }

        String APP_ID = "20230322001610997";
        String SECURITY_KEY = "OkcK69wWAyHf01YqQ1Ue";
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String query = "english";
        String result = api.getTransResult(query, "auto", "zh");
        System.out.println(result);
        int begin = result.indexOf("dst");
        String en = result.substring(begin + 6, result.length() - 4);

        String s = UnicodeUtils.unicodeDecode(en);

        System.out.println(s);

        System.out.println("网上");


    }

    @Test
    public void testFANSHE(){

        MemoryBox memoryBox = new MemoryBox();

        String name = memoryBox.getClass().getName();

        System.out.println(name);

        String[] split = name.split("com.mss.domain.entity.");

        if(split.length==0){
            System.out.println("长度为0");
        }

        for (String s : split) {

            System.out.println(s);

        }


    }


    /**
     * 测试富文本工具类
     */
    @Test
    public void testRichText() {


        String text = "<div>\n" +
                "  <section style=\"text-align: center; margin: 0px auto;\">\n" +
                "    <section style=\"border-radius: 4px; border: 1px solid #757576; display: inline-block; padding: 5px 20px;\">\n" +
                "      <span style=\"font-size: 18px; color: #595959;\">表格</span>\n" +
                "    </section>\n" +
                "  </section>\n" +
                "  <section style=\"margin-top: 1.5em;\">\n" +
                "    <table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\" style=\"border-collapse: collapse;\">\n" +
                "      <thead>\n" +
                "        <tr>\n" +
                "          <th>标题 1</th>\n" +
                "          <th>标题 2</th>\n" +
                "        </tr>\n" +
                "      </thead>\n" +
                "      <tbody>\n" +
                "        <tr>\n" +
                "          <td align=\"center\"></td>\n" +
                "          <td align=\"center\">内容 2</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td align=\"center\">内容 3</td>\n" +
                "          <td align=\"center\">内容 4</td>    \n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td align=\"center\">内容 5</td>\n" +
                "          <td align=\"center\">内容 6</td>\n" +
                "        </tr>\n" +
                "      </tbody>\n" +
                "    </table>\n" +
                "  </section>\n" +
                "  <section style=\"text-align: center; margin: 0px auto; margin-top: 2em\">\n" +
                "    <section style=\"border-radius: 4px; border: 1px solid #757576; display: inline-block; padding: 5px 20px;\">\n" +
                "      <span style=\"font-size: 18px; color: #595959;\">文本</span>\n" +
                "    </section>\n" +
                "  </section>\n" +
                "  <section style=\"margin-top: 1.5em;\">\n" +
                "    &ldquo;富文本编辑器&rdquo;不同于文本编辑器，程序员可到网上下载免费的富文本编辑器内嵌于自己的网站或程序里（当然付费的功能会更强大些），方便用户编辑文章或信息。这是一条<a href=\"https://github.com/jin-yufeng/mp-html\">链接</a>\n" +
                "  </section>\n" +
                "  <section style=\"text-align: center; margin: 0px auto; margin-top: 2em\">\n" +
                "    <section style=\"border-radius: 4px; border: 1px solid #757576; display: inline-block; padding: 5px 20px;\">\n" +
                "      <span style=\"font-size: 18px; color: #595959;\">图片</span>\n" +
                "    </section>\n" +
                "  </section>\n" +
                "  <section style=\"margin-top: 1.5em; text-align: center;\">\n" +
                "    <img src=\"demo.jpg\">\n" +
                "  </section>\n" +
                "</div>\n" +
                "\n" +
                "<div><section style=\"text-align:center;margin:0px auto\"></section><section style=\"margin-top:1.5em\"><table border=\"1\" cellspacing=\"0\" cellpadding=\"5\" style=\"width:100%;border-collapse:collapse;" +
                "\"><thead><tr><th>标题 1</th><th>标题 2</th></tr></thead><tbody><tr><td style=\"text-align:center\"></td><td style=\"text-align:center\">内容 2</td></tr><tr><td style=\"text-align:center\">内容 3</td><td " +
                "style=\"text-align:center\">内容 4</td></tr><tr><td style=\"text-align:center\">内容 5</td><td style=\"text-align:center\">内容 6</td></tr></tbody></table></section><section style=\"text-align:center;margin:0px auto;" +
                "margin-top:2em\"><section style=\"border-radius:4px;border:1px solid #757576;display:inline-block;padding:5px 20px\"><span style=\"font-size:18px;color:#595959\">文本</span></section></section><section style=\"margin-top:1.5em\"> " +
                "“富文本编辑器”不同于文本编辑器，程序员可到网上下载免费的富文本编辑器内嵌于自己的网站或程序里（当然付费的功能会更强大些），方便用户编辑文章或信息。这是一条<a href=\"https://github.com/jin-yufeng/mp-html\">链接</a></section><section style=\"text-align:center;margin:0px auto;" +
                "margin-top:2em\"><section style=\"border-radius:4px;border:1px solid #757576;display:inline-block;padding:5px 20px\"><span style=\"font-size:18px;color:#595959\">图片</span></section></section><section style=\"margin-top:1.5em;text-align:center\">" +
                "<img src=\"https://mp-html.oss-cn-hangzhou.aliyuncs.com/demo.jpg\" width=\"480\"></section></div><img src=\"http://tmp/viaJjp85EzRw67e5d1d15995fb06e3dc7c11cc88767b.jpg\" width=\"400\">\n";


        String s = RichTextUtils.getText(text);
        System.out.println(s);


    }


    /**
     * 测试
     */
    @Test
    public void testBaiduYuYanHeCheng() {

        String text = "      <tr>\n" +
                "          <td align=\"center\"></td>\n" +
                "          <td align=\"center\">内容 2</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td align=\"center\">内容 3</td>\n" +
                "          <td align=\"center\">内容 4</td>    \n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td align=\"center\">内容 5</td>\n" +
                "          <td align=\"center\">内容 6</td>\n" +
                "        </tr>\n";
        String s = RichTextUtils.getText(text);

        String s2 = s.replace(" ", "");

        try {

            String s1 = TtsUtil.textSound(s2, 0, 5, 5, 5, 3);
            String s3 = TtsUtil.textSound(s, 0, 5, 5, 5, 3);

            System.out.println(s1);
            System.out.println(s3);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConnException e) {
            e.printStackTrace();
        }

    }

//
//        判断用户是否上传图片
//        if(!Objects.isNull(files))
//            for(MultipartFile file : files){
//                try {
//                    inputStream= file.getInputStream();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //获取文件名称
//                String fileName = file.getOriginalFilename();
//
//                if(inputStream==null){
//                    System.out.println("文件为空！");
//                    throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
//                }
//                ObjectMetadata meta = new ObjectMetadata();
//
//                meta.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
//                meta.setObjectAcl(CannedAccessControlList.PublicRead);
//
//                ossClient.putObject(bucketName,fileName,inputStream,meta);
//
//                String url = "https://"+bucketName+"."+endpoint+"/"+fileName+"<image>";
//                urls.append(url);
//            }


    @Test
    public void test8() {


        // 1.使用Calendar日期函数(声明)
        Calendar calendar = new GregorianCalendar();
        // 或者用Calendar calendar = Calendar.getInstance();


        Date date = new Date();
        System.out.println(date.toString());
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM");

        String s = st.format(new Date());


        System.out.println(s);

        // 格式化日期--设置date
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.ENGLISH);
        sdf.applyPattern("yyyyMM");  // 年月格式


        System.out.println("请输入年月，格式为：yyyyMM");



//        try {
//
//            System.out.println(sdf.parse(date.toString()));
//            calendar.setTime(sdf.parse(date.toString()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        int num2 = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(num2);
        // 日期减一,取得上月最后一天时间对象
         calendar.add(Calendar.DAY_OF_MONTH, -1);
        // 输出上月最后一天日期
         System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

        Date date1 = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String format = dateFormat.format(date1);

        String substring = format.substring(5, 7);
        System.out.println(substring);

        DateTime dateTime = new DateTime("2023-03" + "-01 00:00:00");
        System.out.println(dateTime.toString());
        final String format1 = dateFormat.format(dateTime);
        System.out.println(format1);

    }


    @Test
    public void testLXdayofMonth(){
        try {
            /**
             * 造一些测试数据，这里就不去数据库里查了，一般正常是数据库有个表记录签到记录
             */
            List<String> signInDateStrs = new ArrayList<>();
            signInDateStrs.add("2019-12-31");
            signInDateStrs.add("2020-01-01");
            signInDateStrs.add("2020-01-02");
            signInDateStrs.add("2020-01-03");
//            signInDateStrs.add("2020-01-05");
            List<Date> signInDates = new ArrayList<>();
            for (String dateStr : signInDateStrs) {
                Calendar calendarTo = Calendar.getInstance();
                calendarTo.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(0);
                c.set(calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH),
                        calendarTo.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                signInDates.add(c.getTime());
            }

            int count = persistentDay(signInDates);
            System.out.println("连续签到了" + count + "天");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 自动签到的情况，只要一进入系统就会签到，所以连续签到肯定至少为1天
     * @param signInDates
     * @return
     */
    private static int persistentDay(List<Date> signInDates){
        //定义一个变量表示连续签到天数，从1开始
        int continuousDays = 1;

        //1. 注意先对时间进行从小到大排序（可以在数据库里查的时候就根据时间排序，也可以查询出来之后再排序）

        /**
         * 2. 从最大的时间开始往前比较，因为我们是要拿连续签到的时间，这样才有意义，减少无谓的比较
         */
        Calendar later = Calendar.getInstance();
        Calendar before = Calendar.getInstance();
        for (int i = signInDates.size() - 1; i > 0; i--){
            later.setTime(signInDates.get(i));
            before.setTime(signInDates.get(i - 1));
            //前一天 + 1天 = 后一天，则视为连续签到
            before.add(Calendar.DAY_OF_MONTH,1);
            if (later.get(Calendar.YEAR) == before.get(Calendar.YEAR)
                    && later.get(Calendar.MONTH) == before.get(Calendar.MONTH)
                    && later.get(Calendar.DAY_OF_YEAR) == before.get(Calendar.DAY_OF_YEAR)){
                continuousDays++;
            }else {
                //只要遇到不连续的就不用再往前比较了
                break;
            }
        }
        return continuousDays;
    }



    @Test
    public void testClockInDay(){

        Date date = new Date();

//        yyyy-MM-dd HH:mm:ss
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        String format = dateFormat.format(date);
        String substring = format.substring(5, 7);
        int i = Integer.parseInt(substring);
        String s = String.valueOf(i);


//        获取当月天数
        Calendar calendar = new GregorianCalendar();
        int alldays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<String> lists = new ArrayList<>();
        lists.add("2023-03-02 00:00:00");
        lists.add("2023-03-05 00:00:00");
        lists.add("2023-03-06 00:00:00");
        lists.add("2023-03-07 00:00:00");
        lists.add("2023-03-17 00:00:00");
        lists.add("2023-02-17 00:00:00");
        Date date1 = new DateTime(format + "-01 00:00:00");
        Date date2 = new DateTime(format +"-"+alldays+" 23:59:59");

//      筛选出该月的打卡记录
//        lists=lists.stream()
//                .filter(a->.before(date2))
//                .filter(b->b.getClockTime().after(date1))
//                .collect(Collectors.toList());

        lists=lists.stream()
                .filter(a->new DateTime(a).before(date2))
                .filter(b->new DateTime(b).after(date1))
                .collect(Collectors.toList());

        System.out.println(lists.size());
        for (String sb:lists) {
            System.out.println(sb);
        }

    }


    @Test
    public void testeverydaySentence(){


        long time = new Date().getTime();
        System.out.println(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String date = dateFormat.format(time);
        System.out.println(date);
        int intValue = new Integer(date).intValue();

        System.out.println(intValue);

    }
}

