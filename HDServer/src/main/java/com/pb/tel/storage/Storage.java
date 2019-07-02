package com.pb.tel.storage;

import java.util.Date;
import java.util.Map;

/**
 * Created by vladimir on 02.07.19.
 */
public interface Storage<K, V> {
    V putValue(K var1, V var2, Date var3);

    boolean putValues(Map<K, V> var1, Date var2);

    boolean putValues(Map<K, V> var1, Map<K, Date> var2);

    V updateValue(K var1, V var2);

    V removeValue(K var1);

    V getValue(K var1);

    V[] getValues(K[] var1);

    void flushValue(K var1);

    boolean contains(K var1);
}
