package com.mss.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 阿里云短信发送工具类
 */
public class SMSUtils {

	/**
	 * 发送短信
	 * @param signName 签名
	 * @param templateCode 模板
	 * @param phoneNumbers 手机号
	 * @param param 参数
	 */
	public static void sendMessage(String signName, String templateCode,
								   String phoneNumbers,
								   String param,
								   String accessKeyId,
								   String accessKeySecret){

		//填入刚刚创建用户时记录的AccessKey ID和AccessKey Secret
		DefaultProfile profile =
		DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
//		"LmK5FgdkhPFSgngwVGNgK7DgBeHoK4"

		IAcsClient client = new DefaultAcsClient(profile);
		SendSmsRequest request = new SendSmsRequest();
		request.setSysRegionId("cn-hangzhou");
		request.setPhoneNumbers(phoneNumbers);
		request.setSignName(signName);
		request.setTemplateCode(templateCode);
		request.setTemplateParam("{\"code\":\""+param+"\"}");
		try {
			SendSmsResponse response = client.getAcsResponse(request);

			System.out.println("短信发送成功  " +response.getMessage());
		}catch (ClientException e) {
			e.printStackTrace();
		}

	}



	public static AjaxResult sendMessage( String phoneNumbers, String code){


		SMSUtils.sendMessage("蒙绍山的博客",
				"SMS_271625812",phoneNumbers,code,
				"LTAI5tBQfpsARyrifRvvAoLA",
				"LmK5FgdkhPFSgngwVGNgK7DgBeHoK4");

		return AjaxResult.success();
	}


}
