package com.pb.tel.encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by vladimir on 26.04.18.
 */
public class MessageEncoder {

//    @Test
    public void encodeMessage(){
        String message = "";
        String encodedUrl =null;
        try {
            encodedUrl = URLEncoder.encode(message, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR!");
        }
        System.out.println("encoded message = " + encodedUrl);
    }
}
