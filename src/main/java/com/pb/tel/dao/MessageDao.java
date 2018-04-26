package com.pb.tel.dao;

import com.pb.tel.data.privatmarket.BotMessage;
import com.pb.tel.data.privatmarket.Customer;

import java.util.List;

/**
 * Created by vladimir on 25.04.18.
 */
public interface MessageDao {

    public List<BotMessage> getByLang(String lang);

}
