package com.mss.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

import com.mss.api.model.CreateCompletionRequest;
import com.mss.api.model.CreateCompletionResponse;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.plexpt.chatgpt.util.Proxys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Objects;


/**
 * OpenAi 接口
 * <a href="https://platform.openai.com/docs/api-reference">参考文档</a>
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 **/
@Service
public class OpenAiApi {

    /**
     * 补全
     *
     * @param request
     * @param openAiApiKey
     * @return
     */
    public CreateCompletionResponse createCompletion(CreateCompletionRequest request,
                                                     String openAiApiKey) {

        if (StringUtils.isBlank(openAiApiKey)) {

            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);

        }

        String url = "https://api.openai.com/v1/completions";
        String json = JSONUtil.toJsonStr(request);
        System.out.println(json);



        String result = HttpRequest.post(url)
                .header("Authorization", "Bearer " + openAiApiKey)
                .body(json)
//                .setProxy()
                .setConnectionTimeout(20000)
                .execute()
                .body();

        if(Objects.isNull(result)){
            throw new SystemException(AppHttpCodeEnum.NULL_Pointer_Exception);
        }

        System.out.println(result);

        return JSONUtil.toBean(result, CreateCompletionResponse.class);
    }
}
