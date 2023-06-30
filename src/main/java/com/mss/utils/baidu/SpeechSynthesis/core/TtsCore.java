package com.mss.utils.baidu.SpeechSynthesis.core;

import com.mss.service.OssService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * @Author Zilin Hsu
 * @Date 2019/10/26
 * 发送请求的核心类，部分参考官方实现
 */
public class TtsCore {
    // 请求Url
    private static final String url = "http://tsn.baidu.com/text2audio";

    // 用户唯一标识cuid
    private static final String cuid = "Starry316";

    private static OssService ossService;
    /**
     *
     * @param text   转换的文本
     * @param per    发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
     *               精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
     * @param spd    语速，取值0-15，默认为5中语速
     * @param pit    音调，取值0-15，默认为5中语调
     * @param vol    音量，取值0-9，默认为5中音量
     * @param aue    下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
     * @return 音频文件绝对路径
     * @throws IOException
     * @throws ConnException
     */
    public static String query(String text, int per, int spd, int pit, int vol, int aue) throws IOException,
            com.mss.utils.baidu.SpeechSynthesis.core.ConnException {

        String token = TokenHolder.getToken();
        // 此处2次urlencode， 确保特殊字符被正确编码
        StringBuffer params = new StringBuffer("tex=" +
                com.mss.utils.baidu.SpeechSynthesis.core.ConnUtil
                .urlEncode(com.mss.utils.baidu.SpeechSynthesis.core.ConnUtil.urlEncode(text)));
        params.append("&per=" + per);
        params.append("&spd=" + spd);
        params.append("&pit=" + pit);
        params.append("&vol=" + vol);
        params.append("&cuid=" + cuid);
        params.append("&tok=" + token);
        params.append("&aue=" + aue);
        params.append("&lan=zh&ctp=1");
        System.out.println(url + "?" + params); // 反馈请带上此url，浏览器上可以测试
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);

        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());

        printWriter.write(params.toString());
        System.out.println(params.toString());

        printWriter.close();
        String contentType = conn.getContentType();
        if (contentType.contains("audio/")) {
            byte[] bytes =com.mss.utils.baidu.SpeechSynthesis.core.ConnUtil
                    .getResponseBytes(conn);
//          输出语音文件格式
            String format = getFormat(aue);

            System.out.println(format);

            // 使用UUID生成文件名,把该语音文件写入磁盘
            File file = new File(UUID.randomUUID() + "." + format);
            FileOutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
            System.out.println("audio file write to " + file.getAbsolutePath());
            return file.getAbsolutePath();


        } else {
            System.err.println("ERROR: content-type= " + contentType);
            String res = com.mss.utils.baidu.SpeechSynthesis.core.ConnUtil
                    .getResponseString(conn);
            System.err.println(res);
            return null;
        }
    }

    public static String simpleQuery(String text, int per, int spd, int pit, int vol, int aue) throws IOException,
            com.mss.utils.baidu.SpeechSynthesis.core.ConnException {

        String token = TokenHolder.getToken();
        // 此处2次urlencode， 确保特殊字符被正确编码
        StringBuffer params = new StringBuffer("tex=" +
                com.mss.utils.baidu.SpeechSynthesis.core.ConnUtil
                        .urlEncode(com.mss.utils.baidu.SpeechSynthesis.core.ConnUtil.urlEncode(text)));
        params.append("&per=" + per);
        params.append("&spd=" + spd);
        params.append("&pit=" + pit);
        params.append("&vol=" + vol);
        params.append("&cuid=" + cuid);
        params.append("&tok=" + token);
        params.append("&aue=" + aue);
        params.append("&lan=zh&ctp=1");
        System.out.println(url + "?" + params); // 反馈请带上此url，浏览器上可以测试
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);

        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());

//        printWriter.write(params.toString()+"\n");
//        System.out.println("http://tsn.baidu.com/text2audio?"+params.toString());

        printWriter.close();

        return "http://tsn.baidu.com/text2audio?"+params.toString();

    }

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private static String getFormat(int aue) {
        String[] formats = {"mp3", "pcm", "pcm", "wav"};
        return formats[aue - 3];
    }
}
