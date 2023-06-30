package com.mss.answer;


import com.mss.api.OpenAiApi;
import com.mss.api.model.CreateCompletionRequest;
import com.mss.api.model.CreateCompletionResponse;
import com.mss.config.OpenAiConfig;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * OpenAi 回答者
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Slf4j
public class OpenAiAnswerer implements Answerer {


    private final OpenAiApi openAiApi = SpringContextUtils.getBean(OpenAiApi.class);


    private final OpenAiConfig openAiConfig = SpringContextUtils.getBean(OpenAiConfig.class);


    @Override
    public String doAnswer(String prompt) {

        System.out.println(openAiConfig.getApiKey());
        System.out.println(openAiConfig);

        CreateCompletionRequest request = new CreateCompletionRequest();
        request.setPrompt(prompt);

        request.setModel(openAiConfig.getModel());
        request.setTemperature(0);
        request.setMax_tokens(2048);

        CreateCompletionResponse response =
                openAiApi.createCompletion(request, openAiConfig.getApiKey());

        if(Objects.isNull(response)){
            throw new SystemException(AppHttpCodeEnum.NULL_Pointer_Exception);
        }

        System.out.println(response);

        List<CreateCompletionResponse.ChoicesItem> choicesItemList = response.getChoices();

//        String answer=null;
//        try{
//           answer = choicesItemList.stream()
//                    .map(CreateCompletionResponse.ChoicesItem::getText)
//                    .collect(Collectors.joining());
//        }catch (Exception e){
//            return e.getMessage();
//        }

        String  answer = choicesItemList.stream()
                .map(CreateCompletionResponse.ChoicesItem::getText)
                .collect(Collectors.joining());

        log.info("OpenAiAnswerer 回答成功 \n 答案：{}", answer);
        return answer;
    }


}
