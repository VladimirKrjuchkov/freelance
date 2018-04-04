package com.pb.tel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.tel.data.Mes;
import com.pb.tel.data.Request;
import com.pb.tel.data.telegram.SimpleTelegramResponse;
import com.pb.tel.data.telegram.TelegramRequest;
import com.pb.tel.data.telegram.TelegramResponse;
import com.pb.tel.data.telegram.User;
import com.pb.uniwin.atm.host.DetailedAnswer;
import com.pb.uniwin.atm.host.RequestHTTPS;
import com.pb.util.zvv.PropertiesUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.apache.http.entity.mime.content.FileBody;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 27.02.18.
 */
@Service("telegramConnector")
public class TelegramConnector {

    private final Logger log = Logger.getLogger(TelegramConnector.class.getCanonicalName());

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public Mes webHook(String oper) throws Exception {
        String webHookUrl = "";
        if("set".equals(oper)){
                webHookUrl = PropertiesUtil.getProperty("webhook_url") + "?token=" + PropertiesUtil.getProperty("telegram_bot_token");
        }
        TelegramResponse telegramResponse = doWebHookRequest(webHookUrl);
        Mes mes = new Mes();
        if(telegramResponse.getOk()){
            mes.setState(Mes.MesState.ok);
        }else{
            mes.setState(Mes.MesState.err);
        }
        mes.setDescription(telegramResponse.getDescription());
        return mes;
    }

    private TelegramResponse doWebHookRequest(String webHookUrl) throws Exception {
            RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
            String url = PropertiesUtil.getProperty("telegram_bot_url") + PropertiesUtil.getProperty("telegram_bot_token") + "/setWebhook" + "?url=" + webHookUrl;
            log.log(Level.INFO, "URL OF REQUEST : " + url);
            URL urlAddr = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlAddr.openConnection();//
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")));
            connection.setReadTimeout(Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
            File file = new File(PropertiesUtil.getProperty("path_to_webhook_cert"));
            log.log(Level.INFO, "FILE LENGTH: " + file.length());
            FileBody fileBody = new FileBody(file);
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            multipartEntity.setMode(HttpMultipartMode.STRICT);
            multipartEntity.setCharset(Charset.forName("UTF-8"));
            multipartEntity.addPart("file", fileBody);
            HttpEntity entity = multipartEntity.build();
            connection.setRequestProperty("Content-Type", entity.getContentType().getValue());
            connection.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
            OutputStream out = connection.getOutputStream();
            entity.writeTo(out);
            out.close();
            String answer = requestHTTP.getResponse(connection, "");
            TelegramResponse telegramResponse = jacksonObjectMapper.readValue(answer, SimpleTelegramResponse.class);
            return telegramResponse;
    }

    public TelegramResponse sendRequest(Request request) throws Exception {
            RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
            String url = PropertiesUtil.getProperty("telegram_bot_url") + PropertiesUtil.getProperty("telegram_bot_token") + "/sendMessage";
            Map<String, String> requestProperties = new HashMap<String, String>();
            requestProperties.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            requestProperties.put("Accept", MediaType.APPLICATION_JSON_VALUE);
            requestHTTP.setRequestProperties(requestProperties);
            DetailedAnswer answer = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(request), url);
            return jacksonObjectMapper.readValue(answer.getBody().getBytes(), TelegramResponse.class);
    }
}
