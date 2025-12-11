package com.myvalut.envalut_backend.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String ALGO = "AES";

    private static final String MASTER_KEY = "A9c!2xF8d@G7pL0mQw3R#tY9bV4nH6k0";


    public SecretKey generateFileKey() throws Exception {
        KeyGenerator generator=KeyGenerator.getInstance(ALGO);
        generator.init(256);
        return generator.generateKey();
    }

    public byte[] encryptFile(byte[] fileBytes, SecretKey aesKey) throws Exception {
        Cipher cipher=Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE,aesKey);
        return cipher.doFinal(fileBytes);
    }

    public String encrypAesKey(SecretKey aesKey) throws Exception {
        Cipher cipher=Cipher.getInstance(ALGO);
        SecretKeySpec masterKey=new SecretKeySpec(MASTER_KEY.getBytes(),ALGO);
        cipher.init(Cipher.ENCRYPT_MODE,masterKey);
        byte[]  encrypted=cipher.doFinal(aesKey.getEncoded());
        return Base64.getEncoder().encodeToString(encrypted);

    }

    public SecretKey decryptAesKey(String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec masterKey = new SecretKeySpec(MASTER_KEY.getBytes(), ALGO);
        cipher.init(Cipher.DECRYPT_MODE, masterKey);
        byte[] decoded = Base64.getDecoder().decode(encryptionKey);
        byte[] aesKeyBytes = cipher.doFinal(decoded);
        return new SecretKeySpec(aesKeyBytes, ALGO);
    }

    public byte[] decryptFile(byte[] encryptedBytes, SecretKey aesKey) throws Exception {
        Cipher cipher=Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE,aesKey);
        return cipher.doFinal(encryptedBytes);
    }
}
