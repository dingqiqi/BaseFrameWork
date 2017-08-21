package com.dingqiqi.baseframework.util.cer;

import java.security.PrivateKey;
import java.security.PublicKey;

final class CertInfoModual {
	
	/**
	 * 证书公钥
	 */
	private PublicKey publicKey;
	
	/**
	 * 证书私钥
	 */
	private PrivateKey privateKey;
	
	/**
	 * 用户信息
	 */
	private String commonName;

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

}
