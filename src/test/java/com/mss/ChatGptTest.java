package com.mss;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.util.Proxys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class ChatGptTest {

    @Test
    public void testChatGpt() {

//      https://agent-openai.ccrui.dev/v1/chat/completions

        //国内需要代理
        Proxy proxy = Proxys.http("https://agent-openai.ccrui.dev/v1/chat/completions",1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();

        try{
            String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
            System.out.println(res);
        }catch (Exception e) {

            System.out.println(e.getMessage());

        }


    }


    @Test
    public void testChatGpt2() {
        String res = "\n\nHello World";

        String[] split = res.split("\n\n");


        for (String a: split) {

            System.out.println(a);

        }

//
//        String dest = "";
//        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//        Matcher m = p.matcher(res);
//        dest = m.replaceAll("");
//
//        System.out.println(dest);

        String substring = res.substring(2);
        System.out.println(substring);
    }

}
