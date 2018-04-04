package com.pb.tel.service;

import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.exception.UnresponsibleException;

/**
 * Created by vladimir on 03.04.18.
 */
public interface UpdateHandler {

    public Request deligateMessage(UserAccount userAccount) throws UnresponsibleException;
}
