package com.pb.tel.service;

import com.pb.tel.data.Mes;
import com.pb.tel.data.Request;
import com.pb.tel.data.Response;
import com.pb.tel.data.telegram.TelegramResponse;

/**
 * Created by vladimir on 11.04.18.
 */
public interface Connector {

    Mes webHook(String oper) throws Exception;

    Response doWebHookRequest(String webHookUrl) throws Exception;

    Response sendRequest(Request request) throws Exception;
}
