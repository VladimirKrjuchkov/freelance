package com.pb.tel.encoder;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by vladimir on 26.04.18.
 */
public class MessageEncoder {

    @Test
    public void encodeMessage(){
        String message = "И еще одна мелочь! \uD83D\uDE00 Жми на кнопку '✅ Регистрация \"под полем для ввода и начнем!";
        String encodedUrl =null;
        try {
            encodedUrl = URLEncoder.encode(message, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR!");
        }
        System.out.println("encoded message = " + encodedUrl);
    }

//    @Test
    public void decodeMessage(){
        String message = "%D0%94%D0%BE%D0%B1%D1%80%D0%BE%D0%B3%D0%BE+%D0%B4%D0%BD%D1%8F%2C+%7Buser_first_name%7D%21+%F0%9F%98%80+%D0%AF+%D0%B4%D0%BE%D0%BF%D0%BE%D0%BC%D0%BE%D0%B6%D1%83+%D0%B7%D0%BD%D0%B0%D0%B9%D1%82%D0%B8+%D0%B2%D1%96%D0%B4%D0%BF%D0%BE%D0%B2%D1%96%D0%B4%D1%96+%D0%BD%D0%B0+%D1%82%D0%B2%D0%BE%D1%97+%D0%B7%D0%B0%D0%BF%D0%B8%D1%82%D0%B0%D0%BD%D0%BD%D1%8F%21+%D0%A2%D0%B8%D1%81%D0%BD%D0%B8+%D0%BD%D0%B0+%D0%BA%D0%BD%D0%BE%D0%BF%D0%BA%D1%83+%27%E2%9C%85+%D0%97%D0%B0%D1%80%D0%B5%D1%94%D1%81%D1%82%D1%80%D1%83%D0%B2%D0%B0%D1%82%D0%B8%D1%81%D1%8C%27+%D0%BF%D1%96%D0%B4+%D0%BF%D0%BE%D0%BB%D0%B5%D0%BC+%D0%B4%D0%BB%D1%8F+%D0%B2%D0%B2%D0%BE%D0%B4%D1%83+%D1%96+%D0%BF%D0%BE%D1%87%D0%BD%D0%B5%D0%BC%D0%BE%21";
        String decodedUrl =null;
        try {
            decodedUrl = URLDecoder.decode(message, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR!");
        }
        System.out.println("decoded message = " + decodedUrl);
    }
}
