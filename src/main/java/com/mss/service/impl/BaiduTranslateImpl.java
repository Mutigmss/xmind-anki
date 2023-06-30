package com.mss.service.impl;

import com.mss.service.BaiduTranslate;
import com.mss.utils.baidu.translate.TransApi;
import com.mss.utils.baidu.translate.UnicodeUtils;
import org.springframework.stereotype.Service;


@Service
public class BaiduTranslateImpl implements BaiduTranslate {





    @Override
    public String translate(String text) {

        String APP_ID = "20230322001610997";
        String SECURITY_KEY = "OkcK69wWAyHf01YqQ1Ue";

        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String query = text;
        String result = api.getTransResult(query, "auto", "zh");
        int begin = result.indexOf("dst");
        String en = result.substring(begin + 6, result.length() - 4);

        String s = UnicodeUtils.unicodeDecode(en);

        return s;
    }
}
