package com.mss.utils.baidu.SpeechSynthesis;

/**
 * @Author Zilin Hsu
 * @Date 2019/10/26
 */

import com.mss.utils.baidu.SpeechSynthesis.core.ConnException;

import java.io.IOException;

public class TtsTestMain {

    public static void main(String[] args) throws IOException, ConnException {
//        com.mss.utils.baidu.SpeechSynthesis.TtsUtil.text2Sound("文字转换语音测试");
//        com.mss.utils.baidu.SpeechSynthesis.TtsUtil.text2Sound("使用度丫丫",4);
//        com.mss.utils.baidu.SpeechSynthesis.TtsUtil.text2Sound("使用度丫丫,语速5，音调10，音量3",4,5,10,3,3);

        com.mss.utils.baidu.SpeechSynthesis.TtsUtil.textSound("使用度小美,语速5，音调10，音量3",0,5,5,5,3);
    }

}
