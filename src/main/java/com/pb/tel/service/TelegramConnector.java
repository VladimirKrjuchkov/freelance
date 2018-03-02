package com.pb.tel.service;

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
import org.springframework.stereotype.Service;
import org.apache.http.entity.mime.content.FileBody;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 27.02.18.
 */
@Service("telegramConnector")
public class TelegramConnector {

    private final Logger log = Logger.getLogger(TelegramConnector.class.getCanonicalName());

    public void setWebHook() throws Exception {
            RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
            String url = PropertiesUtil.getProperty("telegram_bot_url") + PropertiesUtil.getProperty("telegram_bot_token") + "/setWebhook" + "?url=" + PropertiesUtil.getProperty("webhook_url") + "?token=" + PropertiesUtil.getProperty("telegram_bot_token");
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
            log.log(Level.INFO, "FILE BODY : " + fileBody);
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            multipartEntity.setMode(HttpMultipartMode.STRICT);
            multipartEntity.setCharset(Charset.forName("UTF-8"));
            multipartEntity.addPart("file", fileBody);
            HttpEntity entity = multipartEntity.build();
            connection.setRequestProperty("Content-Type", entity.getContentType().getValue());
            connection.setRequestProperty("Accept", "application/json");
            OutputStream out = connection.getOutputStream();
            entity.writeTo(out);
            out.close();
            String answer = requestHTTP.getResponse(connection, "");
            log.log(Level.INFO, "RESPONSE : " + answer);
    }
}
