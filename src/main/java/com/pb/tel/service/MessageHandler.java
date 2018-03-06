package com.pb.tel.service;

import com.pb.tel.data.enums.UserState;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by vladimir on 05.03.18.
 */
@Service("messageHandler")
public class MessageHandler {

    private final Logger log = Logger.getLogger(MessageHandler.class.getCanonicalName());

    public String getRowMessageByUserState(UserState userState){
        return "{user_first_name}, чим можу бути корисним сьогодні?\uD83D\uDE0F";//пока только с костылем. Тут также будет поход на АПИ Новой Почты
    }
}