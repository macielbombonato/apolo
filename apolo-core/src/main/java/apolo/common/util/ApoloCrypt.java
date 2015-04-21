package apolo.common.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApoloCrypt {
	
	private static final Logger LOG = LoggerFactory.getLogger(ApoloCrypt.class);
	
	private String algorithm = "AES";
	private String transformation = "AES/CBC/NoPadding";
	private String encoding = "UTF-8";
	

	public String encrypt(String text, String secretKey, String iv) throws Exception {
		IvParameterSpec ivspec = new IvParameterSpec(buildKey(iv.trim()).getBytes(encoding));

		SecretKeySpec keyspec = new SecretKeySpec(buildKey(secretKey.trim()).getBytes(encoding), algorithm);

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(transformation);
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		if (text == null || text.length() == 0)
			throw new Exception("Empty string");

		byte[] encrypted = null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

			encrypted = cipher.doFinal(padString(text).getBytes(encoding));
		} catch (Exception e) {
			throw new Exception("[encrypt] " + e.getMessage());
		}

		return bytesToHex(encrypted);
	}

	public String decrypt(String code, String secretKey, String iv) throws Exception {
		IvParameterSpec ivspec = new IvParameterSpec(buildKey(iv.trim()).getBytes(encoding));

		SecretKeySpec keyspec = new SecretKeySpec(buildKey(secretKey.trim()).getBytes(encoding), algorithm);

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(transformation);
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		if (code == null || code.length() == 0)
			throw new Exception("Empty string");

		byte[] decrypted = null;

		try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			decrypted = cipher.doFinal(hexToBytes(code));
		} catch (Exception e) {
			throw new Exception("[decrypt] " + e.getMessage());
		}
		return new String(decrypted).trim();
	}

	public String bytesToHex(byte[] data) {
		if (data == null) {
			return null;
		}

		int len = data.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			if ((data[i] & 0xFF) < 16)
				str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
			else
				str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
		}
		return str;
	}

	public byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		} else if (str.length() < 2) {
			return null;
		} else {
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++) {
				buffer[i] = (byte) Integer.parseInt(
						str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}
	}

	private String padString(String source) {
		char paddingChar = ' ';
		int size = 16;
		int x = source.length() % size;
		int padLength = size - x;

		for (int i = 0; i < padLength; i++) {
			source += paddingChar;
		}

		return source;
	}

	private String buildKey(String code) {
		String key = null;

		String newCode = "";

		if (code.length() == 16) {
			key = code;
		} else {
			int diference = 0;
			if (code.length() < 16) {
				diference = 16 - code.length();
			}

			newCode = code;

			if (diference > 0) {
				for (int i = 0; i < diference; i++) {
					newCode += "0";
				}				
			} else {
				newCode = code.substring(0, 16);
			}
		}

		key = newCode;

		return key;
	}
}
