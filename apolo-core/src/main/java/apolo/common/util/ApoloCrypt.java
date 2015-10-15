package apolo.common.util;

import apolo.common.config.model.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

@Component("apoloCrypt")
public class ApoloCrypt implements PasswordEncoder {

	private static final Logger LOG = LoggerFactory.getLogger(ApoloCrypt.class);

	private String algorithm = "AES";
	private String transformation = "AES/CBC/NoPadding";
	private String encoding = "UTF-8";

	@Autowired
	private ApplicationProperties applicationProperties;

	private ApplicationProperties getApplicationProperties() {
		if (this.applicationProperties == null) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

			applicationProperties = (ApplicationProperties) ctx.getBean("applicationProperties");
		}

		return this.applicationProperties;
	}

	public String encode(CharSequence rawPassword) {
		String result = "";

		try {
			result = this.encode(
					rawPassword.toString(),
					this.getApplicationProperties().getSecretKey(),
					this.getApplicationProperties().getIvKey()
			);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return result;
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String encoded = this.encode(rawPassword);
		return matches(encoded.getBytes(), encodedPassword.getBytes());
	}

	public String encode(String text, String secretKey, String iv) throws Exception {
		byte[] ivBytes = new byte[16];
		byte[] generatedIV = buildKey(iv.trim()).getBytes(encoding);

		for (int i = 0; i < ivBytes.length; i++) {
			ivBytes[i] = generatedIV[i];
		}

		IvParameterSpec ivspec = new IvParameterSpec(
				ivBytes
		);

		byte[] secretKeyBytes = new byte[16];
		byte[] generatedKey = buildKey(secretKey.trim()).getBytes(encoding);

		for (int i = 0; i < secretKeyBytes.length; i++) {
			secretKeyBytes[i] = generatedKey[i];
		}

		SecretKeySpec keyspec = new SecretKeySpec(
				secretKeyBytes,
				algorithm
		);

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

	public String decode(String code) {
		String result = "";

		try {
			result = this.decode(
					code,
					this.getApplicationProperties().getSecretKey(),
					this.getApplicationProperties().getIvKey()
			);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return result;
	}

	public String decode(String code, String secretKey, String iv) throws Exception {
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
				str = str + "0" + Integer.toHexString(data[i] & 0xFF);
			else
				str = str + Integer.toHexString(data[i] & 0xFF);
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

			key = newCode;
		}

		return key;
	}

	private boolean matches(byte[] expected, byte[] actual) {
		if (expected.length != actual.length) {
			return false;
		}

		int result = 0;
		for (int i = 0; i < expected.length; i++) {
			result |= expected[i] ^ actual[i];
		}
		return result == 0;
	}
}
