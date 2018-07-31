package com.pb.tel.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.facebook.*;
import com.pb.tel.data.telegram.*;
import com.pb.tel.data.telegram.Message;
import com.pb.uniwin.atm.host.DetailedAnswer;
import com.pb.uniwin.atm.host.RequestHTTPS;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.*;

/**
 * Created by vladimir on 21.05.18.
 */
public class FunctionalTest {

    /************************ ПОЛЕГОН **************************/
    private static String urlForTelegram = "https://bankid.privatbank.ua/TelegramBot/update?token=578758997:AAEzBN2LK3O1sHyaFFRdekBXh3tgUN3EYMQ&mode=test";
    private static String urlForFaceBook = "https://bankid.privatbank.ua/TelegramBot/facebook/update?testMode=true";
    private static String urlForFlushTelegram = "https://bankid.privatbank.ua/TelegramBot/telegram/flush/000000000";
    private static String urlForFlushFaceBook = "https://bankid.privatbank.ua/TelegramBot/telegram/flush/0000000000000000";

    /************************ БОЕВОЙ **************************/
//    private static String urlForTelegram = "https://bankid.org.ua/TelegramBot/update?token=471826174:AAEAcBIp-yqoMcHMu4Yg-0TrVKWWAkjmOEY&mode=test";
//    private static String urlForFaceBook = "https://bankid.org.ua/TelegramBot/facebook/update?testMode=true";
//    private static String urlForFlushTelegram = "https://bankid.org.ua/TelegramBot/telegram/flush/000000000";
//    private static String urlForFlushFaceBook = "https://bankid.org.ua/TelegramBot/telegram/flush/0000000000000000";

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    @Test
    public void logicTest(){
        try {
            RequestHTTPS requestHTTP = new RequestHTTPS(7000, 9000);
            Map<String, String> requestProperties = new HashMap<String, String>();
            requestProperties.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            requestProperties.put("Accept", MediaType.APPLICATION_JSON_VALUE);
            requestHTTP.setRequestProperties(requestProperties);

            requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(null), urlForFlushTelegram);

            DetailedAnswer answer1 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage1")), urlForTelegram);
            TelegramRequest telegramRequest1 = jacksonObjectMapper.readValue(answer1.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage1"), telegramRequest1);
            System.out.println("****************************** TELEGRAM STAGE 1 SUCCESS ******************************");

            DetailedAnswer answer2 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage1")), urlForTelegram);
            TelegramRequest telegramRequest2 = jacksonObjectMapper.readValue(answer2.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage2"), telegramRequest2);
            System.out.println("****************************** TELEGRAM STAGE 2 SUCCESS ******************************");

            DetailedAnswer answer3 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage1")), urlForTelegram);
            TelegramRequest telegramRequest3 = jacksonObjectMapper.readValue(answer3.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage1"), telegramRequest3);
            System.out.println("****************************** TELEGRAM STAGE 3 SUCCESS ******************************");

            DetailedAnswer answer4 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage2")), urlForTelegram);
            TelegramRequest telegramRequest4 = jacksonObjectMapper.readValue(answer4.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage3"), telegramRequest4);
            System.out.println("****************************** TELEGRAM STAGE 4 SUCCESS ******************************");

            DetailedAnswer answer5 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage1")), urlForTelegram);
            TelegramRequest telegramRequest5 = jacksonObjectMapper.readValue(answer5.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage4"), telegramRequest5);
            System.out.println("****************************** TELEGRAM STAGE 5 SUCCESS ******************************");

            DetailedAnswer answer6 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage2")), urlForTelegram);
            TelegramRequest telegramRequest6 = jacksonObjectMapper.readValue(answer6.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage3"), telegramRequest6);
            System.out.println("****************************** TELEGRAM STAGE 6 SUCCESS ******************************");

            DetailedAnswer answer7 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage3")), urlForTelegram);
            TelegramRequest telegramRequest7 = jacksonObjectMapper.readValue(answer7.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage5"), telegramRequest7);
            System.out.println("****************************** TELEGRAM STAGE 7 SUCCESS ******************************");

            DetailedAnswer answer8 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage4")), urlForTelegram);
            TelegramRequest telegramRequest8 = jacksonObjectMapper.readValue(answer8.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage6"), telegramRequest8);
            System.out.println("****************************** TELEGRAM STAGE 8 SUCCESS ******************************");

            DetailedAnswer answer9 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage5")), urlForTelegram);
            TelegramRequest telegramRequest9 = jacksonObjectMapper.readValue(answer9.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage7"), telegramRequest9);
            System.out.println("****************************** TELEGRAM STAGE 9 SUCCESS ******************************");

            DetailedAnswer answer10 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage6")), urlForTelegram);
            TelegramRequest telegramRequest10 = jacksonObjectMapper.readValue(answer10.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage8"), telegramRequest10);
            System.out.println("****************************** TELEGRAM STAGE 10 SUCCESS ******************************");

            DetailedAnswer answer11 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage1")), urlForTelegram);
            TelegramRequest telegramRequest11 = jacksonObjectMapper.readValue(answer11.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage6"), telegramRequest11);
            System.out.println("****************************** TELEGRAM STAGE 11 SUCCESS ******************************");

            DetailedAnswer answer20 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getTelegramRequest("stage7")), urlForTelegram);
            TelegramRequest telegramRequest20 = jacksonObjectMapper.readValue(answer20.getBody().getBytes(), TelegramRequest.class);
            Assert.assertEquals(getTelegramResponse("stage2"), telegramRequest20);
            System.out.println("****************************** TELEGRAM STAGE 12 SUCCESS ******************************");

            System.out.println("****************************** TELEGRAM OK! ******************************");

            requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(null), urlForFlushFaceBook);

            DetailedAnswer answer12 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage1")), urlForFaceBook);
            Messaging faceBookRequest12 = jacksonObjectMapper.readValue(answer12.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage1"), faceBookRequest12);
            System.out.println("****************************** FACEBOOK STAGE 1 SUCCESS ******************************");

            DetailedAnswer answer13 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage5")), urlForFaceBook);
            Messaging faceBookRequest13 = jacksonObjectMapper.readValue(answer13.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage5"), faceBookRequest13);
            System.out.println("****************************** FACEBOOK STAGE 2 SUCCESS ******************************");

            DetailedAnswer answer14 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage1")), urlForFaceBook);
            Messaging faceBookRequest14 = jacksonObjectMapper.readValue(answer14.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage1"), faceBookRequest14);
            System.out.println("****************************** FACEBOOK STAGE 3 SUCCESS ******************************");

            DetailedAnswer answer15 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage2")), urlForFaceBook);
            Messaging faceBookRequest15 = jacksonObjectMapper.readValue(answer15.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage2"), faceBookRequest15);
            System.out.println("****************************** FACEBOOK STAGE 4 SUCCESS ******************************");

            DetailedAnswer answer16 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage6")), urlForFaceBook);
            Messaging faceBookRequest16 = jacksonObjectMapper.readValue(answer16.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage6"), faceBookRequest16);
            System.out.println("****************************** FACEBOOK STAGE 5 SUCCESS ******************************");

            DetailedAnswer answer17 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage2")), urlForFaceBook);
            Messaging faceBookRequest17 = jacksonObjectMapper.readValue(answer17.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage2"), faceBookRequest17);
            System.out.println("****************************** FACEBOOK STAGE 6 SUCCESS ******************************");

            DetailedAnswer answer18 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage3")), urlForFaceBook);
            Messaging faceBookRequest18 = jacksonObjectMapper.readValue(answer18.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage3"), faceBookRequest18);
            System.out.println("****************************** FACEBOOK STAGE 7 SUCCESS ******************************");

            DetailedAnswer answer19 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage4")), urlForFaceBook);
            Messaging faceBookRequest19 = jacksonObjectMapper.readValue(answer19.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage4"), faceBookRequest19);
            System.out.println("****************************** FACEBOOK STAGE 8 SUCCESS ******************************");

            DetailedAnswer answer21 = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(getFaceBookRequest("stage7")), urlForFaceBook);
            Messaging faceBookRequest21 = jacksonObjectMapper.readValue(answer21.getBody().getBytes(), Messaging.class);
            Assert.assertEquals(getFaceBookResponse("stage2"), faceBookRequest21);
            System.out.println("****************************** FACEBOOK STAGE 9 SUCCESS ******************************");

            System.out.println("****************************** FACEBOOK OK! ******************************");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Messaging getFaceBookResponse(String stage){
        Messaging faceBookResponse = new Messaging();
        Participant recipient = new Participant();
        recipient.setId("0000000000000000");
        faceBookResponse.setRecipient(recipient);
        com.pb.tel.data.facebook.Message message = new com.pb.tel.data.facebook.Message();

        if("stage1".equals(stage)) {
            Attachment attachment = new Attachment();
            attachment.setType("template");
            Payload payload = new Payload();
            payload.setTemplate_type("button");
            payload.setText("Доброго дня, Шановний користувач! \uD83D\uDE00 Я допоможу знайти вiдповiдi на твої зaпитання! Якою мовою тобi зручно спiлкуватись?");
            List<Buttons> buttons = new ArrayList<Buttons>();

            Buttons ua = new Buttons();
            ua.setType("postback");
            ua.setTitle("Українська \uD83C\uDDFA\uD83C\uDDE6");
            ua.setPayload("ua");
            buttons.add(ua);

            Buttons ru = new Buttons();
            ru.setType("postback");
            ru.setTitle("Російська \uD83C\uDDF7\uD83C\uDDFA");
            ru.setPayload("ru");
            buttons.add(ru);
            payload.setButtons(buttons);
            attachment.setPayload(payload);
            message.setAttachment(attachment);

        }else if("stage2".equals(stage)){
            Attachment attachment = new Attachment();
            attachment.setType("template");
            Payload payload = new Payload();
            payload.setTemplate_type("button");
            payload.setText("Шановний користувач, уточніть, будь ласка, Ваше питання. \uD83D\uDE0F");
            List<Buttons> buttons = new ArrayList<Buttons>();

            Buttons tracking = new Buttons();
            tracking.setType("postback");
            tracking.setTitle("\uD83D\uDE9A Знайти посилку");
            tracking.setPayload("tracking");
            buttons.add(tracking);

            Buttons callOper = new Buttons();
            callOper.setType("postback");
            callOper.setTitle("\uD83D\uDE1C Підключити оператора");
            callOper.setPayload("callOper");
            buttons.add(callOper);

            payload.setButtons(buttons);

            attachment.setPayload(payload);
            message.setAttachment(attachment);

        }else if("stage3".equals(stage)){
            message.setText("Вкажіть, будь ласка, номер вашого відправлення ✏");

        }else if("stage4".equals(stage)){
            message.setText("Шановний користувач, статус Вашого відправлення 000000000: Номер не знайдено");

        }else if("stage5".equals(stage)){
            message.setText("Шановний користувач, на жаль я не зрозумів вашу відповідь \uD83D\uDE14");

        }else if("stage6".equals(stage)){
            message.setText("Шановний користувач, на жаль я не зрозумів вашу відповідь \uD83D\uDE14");
        }
        faceBookResponse.setMessage(message);
        return faceBookResponse;
    }

    private FaceBookRequest getFaceBookRequest(String stage){
        FaceBookRequest faceBookRequest = new FaceBookRequest();
        faceBookRequest.setObject("page");
        List<Entry> entries = new ArrayList<Entry>();
        Entry entry = new Entry();
        entry.setId("000000000000000");
        entry.setTime(new Date());
        List<Messaging> messagings = new ArrayList<Messaging>();
        Messaging messaging = new Messaging();

        Participant sender = new Participant();
        sender.setId("0000000000000000");
        messaging.setSender(sender);

        Participant recipient = new Participant();
        recipient.setId("000000000000000");
        messaging.setRecipient(recipient);

        messaging.setTimestamp(new Date());
        if("stage1".equals(stage)) {
            com.pb.tel.data.facebook.Message message = new com.pb.tel.data.facebook.Message();
            message.setMid("mid.$cAAC6KSZcpUxpwlt-bFjkRfyYodHy");
            message.setSeq("0000");
            message.setText("Yo!");
            messaging.setMessage(message);
            messagings.add(messaging);
        }else if("stage2".equals(stage)){
            Postback postback = new Postback();
            postback.setPayload("ua");
            postback.setTitle("\u0423\u043a\u0440\u0430\u0457\u043d\u0441\u044c\u043a\u0430 \ud83c\uddfa\ud83c\udde6");
            messaging.setPostback(postback);
            messagings.add(messaging);
        }else if("stage3".equals(stage)){
            Postback postback = new Postback();
            postback.setPayload("tracking");
            postback.setTitle("\ud83d\ude9a \u0417\u043d\u0430\u0439\u0442\u0438 \u043f\u043e\u0441\u0438\u043b\u043a\u0443");
            messaging.setPostback(postback);
            messagings.add(messaging);
        }else if("stage4".equals(stage)){
            com.pb.tel.data.facebook.Message message = new com.pb.tel.data.facebook.Message();
            message.setMid("mid.$cAAC6KSZcpUxpwlt-bFjkRfyYodHy");
            message.setSeq("0000");
            message.setText("000000000");
            messaging.setMessage(message);
            messagings.add(messaging);
        }else if("stage5".equals(stage)){
            com.pb.tel.data.facebook.Message message = new com.pb.tel.data.facebook.Message();
            message.setMid("mid.$cAAC6KSZcpUxpwlt-bFjkRfyYodHy");
            message.setSeq("0000");
            message.setText("R");
            messaging.setMessage(message);
            messagings.add(messaging);
        }else if("stage6".equals(stage)){
            com.pb.tel.data.facebook.Message message = new com.pb.tel.data.facebook.Message();
            message.setMid("mid.$cAAC6KSZcpUxpwlt-bFjkRfyYodHy");
            message.setSeq("0000");
            message.setText("Y");
            messaging.setMessage(message);
            messagings.add(messaging);
        }else if("stage7".equals(stage)){
            com.pb.tel.data.facebook.Message message = new com.pb.tel.data.facebook.Message();
            message.setMid("mid.$cAAC6KSZcpUxpwlt-bFjkRfyYodHy");
            message.setSeq("0000");
            message.setText("\\u0417\\u0434\\u0440\\u0430\\u0432\\u0441\\u0442\\u0432\\u0443\\u0439\\u0442\\u0435! \\u041c\\u0435\\u043d\\u044f \\u0437\\u043e\\u0432\\u0443\\u0442 \\u041c\\u0430\\u043a\\u0441, \\u044f \\u0430\\u0434\\u043c\\u0438\\u043d\\u0438\\u0441\\u0442\\u0440\\u0430\\u0442\\u043e\\u0440 \\u0440\\u0435\\u043a\\u043b\\u0430\\u043c\\u043d\\u044b\\u0445 \\u0441\\u0442\\u0440\\u0430\\u043d\\u0438\\u0446:\\n\\u2022 fb.com\\/besplatnoua (28 000 \\u043f\\u043e\\u0434\\u043f\\u0438\\u0441\\u0447\\u0438\\u043a\\u043e\\u0432);\\n\\u2022 fb.com\\/freemobua \\n\\u0425\\u043e\\u0447\\u0443 \\u043f\\u0440\\u0435\\u0434\\u043b\\u043e\\u0436\\u0438\\u0442\\u044c \\u0440\\u0435\\u043a\\u043b\\u0430\\u043c\\u0443 \\u0434\\u043b\\u044f \\u0432\\u0430\\u0448\\u0435\\u0439 \\u0441\\u0442\\u0440\\u0430\\u043d\\u0438\\u0446\\u044b \\u0432 \\u0432\\u0438\\u0434\\u0435 \\u0440\\u043e\\u0437\\u044b\\u0433\\u0440\\u044b\\u0448\\u0430 (\\u043a\\u043e\\u043d\\u043a\\u0443\\u0440\\u0441\\u0430) \\u0432 \\u043d\\u0430\\u0448\\u0438\\u0445 \\u0433\\u0440\\u0443\\u043f\\u043f\\u0430\\u0445. \\u0415\\u0441\\u043b\\u0438 \\u0443 \\u0432\\u0430\\u0441 \\u0435\\u0441\\u0442\\u044c \\u0443\\u0436\\u0435 \\u0433\\u043e\\u0442\\u043e\\u0432\\u044b\\u0439 \\u043a\\u043e\\u043d\\u043a\\u0443\\u0440\\u0441, \\u043c\\u044b \\u043c\\u043e\\u0436\\u0435\\u043c \\u0441\\u0434\\u0435\\u043b\\u0430\\u0442\\u044c \\u0435\\u0433\\u043e \\u0440\\u0435\\u043f\\u043e\\u0441\\u0442. \\n\\u042f \\u0434\\u0443\\u043c\\u0430\\u044e, \\u0447\\u0442\\u043e \\u0412\\u0430\\u043c \\u043c\\u043e\\u0436\\u0435\\u0442 \\u0431\\u044b\\u0442\\u044c \\u0438\\u043d\\u0442\\u0435\\u0440\\u0435\\u0441\\u043d\\u043e \\u0434\\u0430\\u043d\\u043d\\u043e\\u0435 \\u043f\\u0440\\u0435\\u0434\\u043b\\u043e\\u0436\\u0435\\u043d\\u0438\\u0435. \\u0411\\u043b\\u0430\\u0433\\u043e\\u0434\\u0430\\u0440\\u044f \\u0441\\u043e\\u0442\\u0440\\u0443\\u0434\\u043d\\u0438\\u0447\\u0435\\u0441\\u0442\\u0432\\u0443 \\u0441 \\u043d\\u0430\\u043c\\u0438 \\u0412\\u044b \\u043f\\u043e\\u0432\\u044b\\u0441\\u0438\\u0442\\u0435 \\u0430\\u043a\\u0442\\u0438\\u0432\\u043d\\u043e\\u0441\\u0442\\u044c \\u0441\\u0442\\u0440\\u0430\\u043d\\u0438\\u0446\\u044b \\u0438 \\u043e\\u0431\\u044a\\u0435\\u043c\\u044b \\u043f\\u0440\\u043e\\u0434\\u0430\\u0436.\\u0427\\u0442\\u043e \\u0434\\u0443\\u043c\\u0430\\u0435\\u0442\\u0435 \\u043f\\u043e \\u044d\\u0442\\u043e\\u043c \\u043f\\u043e\\u0432\\u043e\\u0434\\u0443?");
            List<Attachment> attachments = new ArrayList<>();
            Attachment attachment = new Attachment();
            attachment.setUrl("http:\\/\\/l.facebook.com\\/l.php?u=http\\u00253A\\u00252F\\u00252Ffb.com\\u00252Fbesplatnoua&h=AT12scbJJtkYOBFTcyQva8iT1_Lw20rwN7zQ7eBQuB64dT8i9LIKsS7sA5Mo8Zrw6ztNkOFA-swAlnitxffM8Inh9Y9JKX79D-vUF-HNPb9JehSsa5XtfWFOlQJqgftavdkzKB04cAZD&s=1");
            attachment.setType("fallback");
            attachment.setPayload(null);
            attachment.setTitle("fb.com");
            attachments.add(attachment);
            message.setAttachments(attachments);
            messaging.setMessage(message);
            messagings.add(messaging);
        }

        entry.setMessaging(messagings);
        entries.add(entry);
        faceBookRequest.setEntry(entries);
        return faceBookRequest;
    }

    private TelegramRequest getTelegramResponse(String stage){
        TelegramRequest response = new TelegramRequest();
        response.setChat_id("000000000");
        if("stage1".equals(stage)) {
            response.setText("Доброго дня, Test! \uD83D\uDE00 Я допоможу знайти вiдповiдi на твої зaпитання! Якою мовою тобi зручно спiлкуватись?");
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            ArrayList<ArrayList<KeyboardButton>> keyboardButtons = new ArrayList<ArrayList<KeyboardButton>>();

            KeyboardButton ua = new KeyboardButton("Українська \uD83C\uDDFA\uD83C\uDDE6");
            KeyboardButton ru = new KeyboardButton("Російська \uD83C\uDDF7\uD83C\uDDFA");

            ArrayList<KeyboardButton> uas = new ArrayList<KeyboardButton>();
            uas.add(ua);
            ArrayList<KeyboardButton> rus = new ArrayList<KeyboardButton>();
            rus.add(ru);

            keyboardButtons.add(uas);
            keyboardButtons.add(rus);
            ;
            reply_markup.setKeyboard(keyboardButtons);
            response.setReply_markup(reply_markup);

        }else if("stage2".equals(stage)) {
            response.setText("Test, на жаль я не зрозумів вашу відповідь \uD83D\uDE14");
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            response.setReply_markup(reply_markup);
        }else if("stage3".equals(stage)) {
            response.setText("І ще одна дрібничка! \uD83D\uDE00 Тисни на кнопку '✅ Зареєструватись' під полем для вводу і почнемо!");
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            ArrayList<ArrayList<KeyboardButton>> keyboardButtons = new ArrayList<ArrayList<KeyboardButton>>();

            KeyboardButton register = new KeyboardButton("✅ Зареєструватись");
            register.setRequest_contact(true);

            ArrayList<KeyboardButton> registers = new ArrayList<KeyboardButton>();
            registers.add(register);

            keyboardButtons.add(registers);

            reply_markup.setKeyboard(keyboardButtons);
            response.setReply_markup(reply_markup);

        }else if("stage4".equals(stage)) {
            response.setText("Test, на жаль я не можу продовжити бесіду, якщо не буду знати з ким розмовляю \uD83D\uDE22");
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            response.setReply_markup(reply_markup);
        }else if("stage5".equals(stage)) {
            response.setText("Test, мені не потрібен чужий контакт");
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            ArrayList<ArrayList<KeyboardButton>> keyboardButtons = new ArrayList<ArrayList<KeyboardButton>>();

            KeyboardButton register = new KeyboardButton("✅ Зареєструватись");
            register.setRequest_contact(true);

            ArrayList<KeyboardButton> registers = new ArrayList<KeyboardButton>();
            registers.add(register);

            keyboardButtons.add(registers);

            reply_markup.setKeyboard(keyboardButtons);
            response.setReply_markup(reply_markup);

        }else if("stage6".equals(stage)) {
            response.setText("Test, уточніть, будь ласка, Ваше питання. \uD83D\uDE0F");
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            ArrayList<ArrayList<KeyboardButton>> keyboardButtons = new ArrayList<ArrayList<KeyboardButton>>();

            KeyboardButton tracking = new KeyboardButton("\uD83D\uDE9A Знайти посилку");
            ArrayList<KeyboardButton> trackings = new ArrayList<KeyboardButton>();
            trackings.add(tracking);

            KeyboardButton callOper = new KeyboardButton("\uD83D\uDE1C Підключити оператора");
            ArrayList<KeyboardButton> callOpers = new ArrayList<KeyboardButton>();
            callOpers.add(callOper);

            keyboardButtons.add(trackings);
            keyboardButtons.add(callOpers);

            reply_markup.setKeyboard(keyboardButtons);
            response.setReply_markup(reply_markup);

        }else if("stage7".equals(stage)) {
            response.setText("Вкажіть, будь ласка, номер вашого відправлення ✏");

        }else if("stage8".equals(stage)) {
            response.setText("Test, статус Вашого відправлення 000000000: Номер не знайдено");
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            response.setReply_markup(reply_markup);
        }

        return response;
    }

    private Update getTelegramRequest(String stage){
        Update update = new Update();
        update.setUpdate_id(000000);
        Message message = new Message();
        message.setMessage_id(0000);
        User user = new User();
        user.setId("000000000");
        user.setIs_bot(false);
        user.setFirst_name("Test");
        user.setLast_name("Test");
        user.setLanguage_code("en");
        message.setFrom(user);
        Chat chat = new Chat();
        chat.setId("000000000");
        chat.setFirst_name("Test");
        chat.setLast_name("Test");
        chat.setType("private");
        message.setChat(chat);
        message.setDate(new Date().getSeconds());
        InlineQuery inline_query = new InlineQuery();
        inline_query.setFrom(user);
        if("stage1".equals(stage)){
            message.setText("Yo!");

        }else if("stage2".equals(stage)){
            message.setText("Українська \uD83C\uDDFA\uD83C\uDDE6");

        }else if("stage3".equals(stage)){
            Contact contact = new Contact();
            contact.setPhone_number("380000000000");
            contact.setFirst_name("Test");
            contact.setLast_name("Test");
            contact.setUser_id("000000001");
            message.setContact(contact);

        }else if("stage4".equals(stage)){
            Contact contact = new Contact();
            contact.setPhone_number("380000000000");
            contact.setFirst_name("Test");
            contact.setLast_name("Test");
            contact.setUser_id("000000000");
            message.setContact(contact);
        }else if("stage5".equals(stage)){
            message.setText("\ud83d\ude9a \u0417\u043d\u0430\u0439\u0442\u0438 \u043f\u043e\u0441\u0438\u043b\u043a\u0443");

        }else if("stage6".equals(stage)){
            message.setText("000000000");
        }else if("stage7".equals(stage)){
            inline_query.setId("1650003269082000005");
            inline_query.setQuery("");
            inline_query.setOffset("");
        }
        if(!"stage7".equals(stage)) {
            update.setMessage(message);
        }else{
            update.setInline_query(inline_query);
        }
        return update;
    }

}
