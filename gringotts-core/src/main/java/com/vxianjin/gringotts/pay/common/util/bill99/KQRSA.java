package com.vxianjin.gringotts.pay.common.util.bill99;

import com.vxianjin.gringotts.pay.common.util.chanpay.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by fxj on 2017/11/27.
 */
public class KQRSA {
    private static final Logger logger = LoggerFactory.getLogger(KQRSA.class);
    private static String keyFlag = "AES";
    public static String symmetricAlgorithm = "AES/CBC/PKCS5Padding";
    public static String asymmetricAlgorithm = "RSA/ecb/pkcs1Padding";
    public static String signAlgorithm = "SHA1withRSA";


    public static PublicKey getPubKey(String cerPath) {
        InputStream in = null;
        try {
            Resource resource = new ClassPathResource(cerPath);
            in = new FileInputStream(resource.getFile());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
            //获得公钥
            return cert.getPublicKey();
        } catch (Exception e) {
            logger.info("获取公钥异常:", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) {
        try {
            Signature s = Signature.getInstance(signAlgorithm, "BC");
            s.initSign(privateKey);
            s.update(data);
            return s.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verifySign(byte[] sign, byte[] signData, PublicKey publicKey) {
        try {
            Signature s = Signature.getInstance(signAlgorithm, "BC");
            s.initVerify(publicKey);
            s.update(signData);
            return s.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] encrypt(String alg, byte[] originData, byte[] secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(alg, "BC");
            cipher.init(1, transform(secretKey), new IvParameterSpec(getIv(cipher)));

            return cipher.doFinal(originData);
        } catch (Exception e) {
            logger.info("数据加密异常" + e.getMessage());
        }
        return null;
    }

    public static byte[] decrypt(String alg, byte[] encryptedData, byte[] secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(alg, "BC");
            cipher.init(2, transform(secretKey), new IvParameterSpec(getIv(cipher)));

            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            logger.info("数据解密异常" + e.getMessage());
        }
        return null;
    }

    protected static byte[] getIv(Cipher cipher) {
        return new byte[cipher.getBlockSize()];
    }

    private static SecretKey transform(byte[] encodedKey) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey, keyFlag);
        return secretKeySpec;
    }

    public static byte[] generateSecretKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(keyFlag, "BC");
            kg.init(128);
            return kg.generateKey().getEncoded();
        } catch (Exception e) {
            logger.info("SecretKey生成异常" + e.getMessage());
        }
        return null;
    }

    static {
        try {
            Security.addProvider((Provider) Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider")
                    .newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encryptSecretKey(String alg, byte[] originData, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(alg, "BC");
            cipher.init(1, key);
            return cipher.doFinal(originData);
        } catch (Exception e) {
            logger.info("SecretKey加密异常" + e.getMessage());
        }
        return null;
    }

    public static byte[] decryptSecretKey(String alg, byte[] originData, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(alg, "BC");
            cipher.init(2, key);
            return cipher.doFinal(originData);
        } catch (Exception e) {
            logger.info("SecretKey解密异常" + e.getMessage());
        }
        return null;
    }

    public static PrivateKey getPriKey(String pfxPath, String keyStorePass, String keyPass) {
        InputStream in = null;
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
            Resource resource = new ClassPathResource(pfxPath);
            in = new FileInputStream(resource.getFile());
            ks.load(in, keyStorePass.toCharArray());

            // 从密钥仓库得到私钥
            return (PrivateKey) ks.getKey(RSA.initAlias(null, ks), keyPass.toCharArray());
        } catch (Exception e) {
            logger.info("获取私钥异常:", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
