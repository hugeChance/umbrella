package com.bohai.subAccount.swt.coreapp;

public class keyTest {
	public static void main(String[] args){
		byte[] priKeyText;
		byte[] pubKeyText;
		KeyGenerater aaa = new KeyGenerater();
		aaa.generater();
		System.out.println("KeyGenerater类生成公钥私钥对 ");
		System.out.println(aaa.getPriKey());
		System.out.println(aaa.getPubKey());
		priKeyText = aaa.getPriKey();
		pubKeyText = aaa.getPubKey();
		
		
		byte[] signret;
		String bbb = "caoxxtest12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
		Signaturer sign = new Signaturer();
		signret = sign.sign(aaa.getPriKey(), bbb);
		System.out.println("Signaturer类使用私钥进行签名");
		System.out.println(signret);
		
		SignProvider signPro = new SignProvider();
		if(signPro.verify(pubKeyText, bbb, priKeyText)){
			System.out.println("公钥验证正确");
		} else {
			System.out.println("公钥验证ERROR");
		}
		
	}
	
}
