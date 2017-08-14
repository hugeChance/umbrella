package com.bohai.subAccount.swt.coreapp;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class SignProvider {
	SignProvider() {
	}

	public static boolean verify(byte[] pubKeyText, String plainText, byte[] signText) {
		try {
			// 解密由base64编码的公钥,并构造X509EncodedKeySpec对象
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.decode(pubKeyText));
			// RSA对称加密算法
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// 取公钥匙对象
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			// 解密由base64编码的数字签名
			byte[] signed = Base64.decode(signText);
			Signature signatureChecker = Signature.getInstance("MD5withRSA");
//			signatureChecker.initVerify(pubKey);
			signatureChecker.initVerify(pubKey);
			signatureChecker.update(plainText.getBytes());
			// 验证签名是否正常
			if (signatureChecker.verify(signed))
			{
				return true;
			}
			else{
				return false;
			}
		} catch (NoSuchAlgorithmException e) {
            System.out.println("无此算法");
            return false;
        } catch (InvalidKeySpecException e) {
        	System.out.println("公钥非法");
        	return false;
        } catch (NullPointerException e) {
        	System.out.println("公钥数据为空");
        	return false;
        } catch (Exception e) {
        	System.out.println(e + "初始化密钥失败");
        	return false;
        }
		
//		try {
//			// 解密由base64编码的公钥,并构造X509EncodedKeySpec对象
//			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.decode(signText));
//			// RSA对称加密算法
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			// 取公钥匙对象
//			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
//			// 解密由base64编码的数字签名
//			byte[] signed = Base64.decode(pubKeyText);
//			Signature signatureChecker = Signature.getInstance("MD5withRSA");
////			signatureChecker.initVerify(pubKey);
//			signatureChecker.initVerify(pubKey);
//			signatureChecker.update(plainText.getBytes());
//			// 验证签名是否正常
//			if (signatureChecker.verify(signed))
//			{
//				return true;
//			}
//			else{
//				return false;
//			}
//		} catch (NoSuchAlgorithmException e) {
//            System.out.println("无此算法");
//            return false;
//        } catch (InvalidKeySpecException e) {
//        	System.out.println("公钥非法");
//        	return false;
//        } catch (NullPointerException e) {
//        	System.out.println("公钥数据为空");
//        	return false;
//        } catch (Exception e) {
//        	System.out.println(e + "初始化密钥失败");
//        	return false;
//        }
	}
}