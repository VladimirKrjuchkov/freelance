package com.pb.tel.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pb.tel.controller.TelegramRequestController;
import com.pb.tel.data.UserAccount;
import com.pb.util.zvv.PropertiesUtil;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by vladimir on 06.03.18.
 */
public class Utils {

    private static final Logger log = Logger.getLogger(Utils.class.getCanonicalName());

    public static final String encode = "UTF-8";

    public static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");

    public static int getSecondsToDate(Date date){
        int ttlValue = (int)(date.getTime() - System.currentTimeMillis())/1000;
        if(ttlValue<0)
            ttlValue = 0;
        return ttlValue;
    }

    public static Date getDateAfterSeconds(int afterSeconds){
        return new Date(System.currentTimeMillis() + afterSeconds*1000);
    }

    public static String createJWT(UserAccount userAccount) throws UnsupportedEncodingException {
        String key = PropertiesUtil.getProperty("channels_company_id");
        String secret = PropertiesUtil.getProperty("channels_api_secret");
        Algorithm algorithm = Algorithm.HMAC256(secret);
        HashMap<String, Object> headers = new HashMap<String, Object>();
        headers.put("typ", "JWT");
        String token = JWT.create()
                .withIssuer(key)
                .withHeader(headers)
                .withExpiresAt(getDateAfterSeconds(180))
                .withClaim("login", userAccount.getId())
                .withClaim("name", userAccount.getFirstName()!=null ? userAccount.getFirstName() : (userAccount.getLastName()!=null ? userAccount.getLastName() : (userAccount.getUserName() != null ? userAccount.getUserName() : "unknown_user")))
                .sign(algorithm);
        return token;


//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
//        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//        JwtBuilder builder = Jwts.builder().setIssuer(key)
//                .setHeaderParam("typ", "JWT")
//                .setExpiration(getDateAfterSeconds(180))
//                .claim("login", userAccount.getId())
//                .signWith(signatureAlgorithm, signingKey);
//
//        return builder.compact();
    }

    public static String makeEkbPhone(String phone){
        String prefix = phone.substring(0, 1);
        if("+".equals(prefix)){
            return phone;
        }else{
            return "+" + phone;
        }
    }
}
