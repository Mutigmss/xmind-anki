package com.mss.utils.baidu.SpeechSynthesis;

import com.mss.utils.baidu.SpeechSynthesis.core.ConnException;
import com.mss.utils.baidu.SpeechSynthesis.core.TtsCore;

import java.io.IOException;

/**
 * @Author Zilin Hsu
 * @Date 2019/10/26
 *
 */
public class TtsUtil {
    /**
     * 使用默认配置，调用文本传语音接口
     * 如无特殊需求，直接调用即可
     * @param text  转换文本
     * @return
     * @throws IOException
     * @throws ConnException
     */
    public static String text2Sound(String text) throws IOException, ConnException {
        return TtsCore.query(text, 0, 5, 5, 5, 6);
    }

    /**
     * 可选择发音人
     * @param text 转换文本
     * @param per 发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
     *                      精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
     * @return
     * @throws IOException
     * @throws ConnException
     */
    public static String text2Sound(String text,int per) throws IOException, ConnException {
        return TtsCore.query(text, per, 5, 5, 5, 6);
    }

    /**
     * 带有完整配置参数的接口
     * @param text   转换的文本
     * @param per    发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
     *               精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
     * @param spd    语速，取值0-15，默认为5中语速
     * @param pit    音调，取值0-15，默认为5中语调
     * @param vol    音量，取值0-9，默认为5中音量
     * @param aue    下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
     * @return
     * @throws IOException
     * @throws ConnException
     */
    public static String text2Sound(String text,int per,int spd,int pit,int vol ,int aue) throws IOException, ConnException {
        return TtsCore.query(text, per, spd, pit, vol, aue);
    }



    /**
     * 带有完整配置参数的接口,返回在线合成语音播放地址
     * @param text   转换的文本
     * @param per    发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
     *               精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
     * @param spd    语速，取值0-15，默认为5中语速
     * @param pit    音调，取值0-15，默认为5中语调
     * @param vol    音量，取值0-9，默认为5中音量
     * @param aue    下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
     * @return
     * @throws IOException
     * @throws ConnException
     */
    public static String textSound(String text,int per,int spd,int pit,int vol ,int aue) throws IOException, ConnException {
        return TtsCore.simpleQuery(text, per, spd, pit, vol, aue);
    }


}
