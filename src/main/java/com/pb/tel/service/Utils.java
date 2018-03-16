package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.util.zvv.PropertiesUtil;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;

/**
 * Created by vladimir on 06.03.18.
 */
public class Utils {

    public static final String encode = "UTF-8";

    public static int getSecondsToDate(Date date){
        int ttlValue = (int)(date.getTime() - System.currentTimeMillis())/1000;
        if(ttlValue<0)
            ttlValue = 0;
        return ttlValue;
    }

    public static Date getDateAfterSeconds(int afterSeconds){
        return new Date(System.currentTimeMillis() + afterSeconds*1000);
    }

    public static String createJWT(UserAccount userAccount) {
        String key = PropertiesUtil.getProperty("channels_api_key");
        String secret = PropertiesUtil.getProperty("channels_api_secret");
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setIssuer(key)
                .setExpiration(getDateAfterSeconds(180))
                .claim("login", userAccount.getUserName())
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }
}
