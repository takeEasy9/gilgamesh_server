package com.gilgamesh.config.security;

import com.gilgamesh.common.entity.property.JwtProperty;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description web 安全配置
 * @createDate 2025/11/22 13:04
 * @since 1.0.0
 */
@Configuration
public class WebSecurityConfigurationSupport {
    /**
     * JWT配置属性
     */
    private final JwtProperty jwtProperty;

    public WebSecurityConfigurationSupport(JwtProperty jwtProperty) {
        this.jwtProperty = jwtProperty;
    }

    /**
     * 构建 RSA KeyPair（包含公钥和私钥）
     */
    @Bean("jwtSignRsaKeyPair")
    public KeyPair jwtSignRsaKeyPair() throws Exception {
        // 1. 解析私钥（PKCS#8格式）
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] privateKeyBytes = Base64.getDecoder().decode(jwtProperty.getSignRsaPrivateKey());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // 2. 解析公钥（X.509格式）
        byte[] publicKeyBytes = Base64.getDecoder().decode(jwtProperty.getSignRsaPublicKey());
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // 3. 构建KeyPair
        return new KeyPair(publicKey, privateKey);
    }

    /**
     * 构建 ECDSA KeyPair（包含公钥和私钥）
     */
    @Bean("jwtSignEcdsaKeyPair")
    public KeyPair jwtSignEcdsaKeyPair() throws Exception {
        // 1. 解析私钥（PKCS#8格式）
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        byte[] privateKeyBytes = Base64.getDecoder().decode(jwtProperty.getSignEcdsaPrivateKey());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // 2. 解析公钥（X.509格式）
        byte[] publicKeyBytes = Base64.getDecoder().decode(jwtProperty.getSignEcdsaPublicKey());
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // 3. 构建KeyPair
        return new KeyPair(publicKey, privateKey);
    }

    /**
     * 构建JWT解析器
     *
     * @return jwtParser
     */
    @Bean("jwtParser")
    public JwtParser jwtParser(@Qualifier("jwtSignRsaKeyPair") KeyPair jwtSignRsaKeyPair) {
        // 初始化解析器
        return Jwts.parser() // 注意：0.11+ 推荐用 parserBuilder() 构建解析器
                .verifyWith(jwtSignRsaKeyPair.getPublic()) // 设置公钥（验证签名）
                .build(); // 构建解析器实例
    }

    public static void main(String[] args) {
        try {
            // 1. 生成 EC 密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 2. 直接对 DER 二进制字节进行 Base64（URL 安全）编码
            String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            System.out.println("privateKeyBase64: " + privateKeyBase64);
            String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            System.out.println("publicKeyBase64: " + publicKeyBase64);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
