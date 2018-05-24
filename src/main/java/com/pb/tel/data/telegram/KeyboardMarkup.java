package com.pb.tel.data.telegram;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by vladimir on 28.03.18.
 */
@JsonDeserialize(as = InlineKeyboardMarkup.class)
public abstract class KeyboardMarkup {}
