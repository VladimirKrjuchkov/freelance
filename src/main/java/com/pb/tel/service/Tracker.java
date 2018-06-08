package com.pb.tel.service;

import com.pb.tel.data.UserAccount;

/**
 * Created by vladimir on 05.06.18.
 */
public interface Tracker {

    String getTrackingByTTN(UserAccount userAccount) throws Exception;
}
