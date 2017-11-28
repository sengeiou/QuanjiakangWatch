package com.quanjiakan.util.encrypt;

//import org.apache.commons.codec.binary.Base64;
import android.util.Base64;

import com.quanjiakan.util.common.MessageDigestUtil;
import com.quanjiakan.util.encode.RSACoder;
import com.quanjiakan.util.encode.RSAHttp;

import org.json.JSONObject;

public class SMSValidateUtil {
	
	//公钥: 
	private static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+igQGePuuSKbw6gb0lyQk4VAnz6434hfaU5dX87Hu9UXk2V6JwnTa6E2Ea+EFLz7MZ6OxaQCFI/1cho4TTgz+EOpFexrlTumg38toFHpvQBg3/B0q5pBK8zfLxlF/5aUNTBfdFl5QwIP0ORsJEA23HS7dp27GGosXgMoljY03jQIDAQAB";
	
	private static String SMS_APP_ID = "12345678901234567890c115";
	
	/**
	 * 加密签名数据
	 * @param mobile
	 * @param validateType
	 * @return
	 * @throws Exception
	 */
	public static String getCiphertext(String mobile, int validateType) throws Exception {
		JSONObject json = new JSONObject();
		json.put("mobile", mobile);
		json.put("validateType", validateType);
		json.put("sign", getSign(mobile, validateType));
		String inputStr = json.toString();
		byte[] data = inputStr.getBytes();
		byte[] encodedData = RSACoder.encryptByPublicKey(data, PUBLIC_KEY);
		String strEncodedData =
				Base64.encodeToString(encodedData, Base64.DEFAULT);
//				Base64.encodeBase64String(encodedData);
		strEncodedData = RSAHttp.httpTransferCharacter(strEncodedData);
		return strEncodedData;
	}

	public static String getSign(String mobile, int validateType){
		StringBuilder sb = new StringBuilder();
		sb.append("mobile=").append(mobile);
		sb.append(",");
		sb.append("validateType=").append(validateType);
		sb.append(",");
		sb.append("key=");
		sb.append(SMS_APP_ID);
		return MessageDigestUtil.getMD5String(sb.toString()).toUpperCase();
	}
}
