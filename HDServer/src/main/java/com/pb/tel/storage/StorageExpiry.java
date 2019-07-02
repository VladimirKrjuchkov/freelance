//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class StorageExpiry<K, V> implements Storage<K, V> {
    private static final Logger log = Logger.getLogger(StorageExpiry.class.getCanonicalName());
    private final Map<K, V> persist = new ConcurrentHashMap();
    private final DelayQueue<StorageExpiry.ExpiryObject<K>> expiryQueue = new DelayQueue();
    private static final int DEFAULT_FLUSH_INTERVAL = 500;
    private int flushInterval = 500;
    private AtomicInteger flushCounter = new AtomicInteger(0);
    private final Object syncObject = new Object();

    public StorageExpiry() {
    }

    public V putValue(K key, V value, Date expiry) {
        this.flush(false);
        if(expiry != null && !(new Date()).after(expiry)) {
            this.putInExpiryQueue(key, expiry);
            return this.persist.put(key, value);
        } else {
            return null;
        }
    }

    public boolean putValues(Map<K, V> data, Date expiry) {
        if(expiry != null && data != null && !(new Date()).after(expiry)) {
            int result = 0;

            for(Iterator var4 = data.entrySet().iterator(); var4.hasNext(); ++result) {
                Entry<K, V> entry = (Entry)var4.next();
                this.flush(false);
                this.putInExpiryQueue(entry.getKey(), expiry);
                this.persist.put(entry.getKey(), entry.getValue());
            }

            return result == data.size();
        } else {
            return false;
        }
    }

    public boolean putValues(Map<K, V> data, Map<K, Date> expiry) {
        if(expiry != null && data != null && data.size() == expiry.size()) {
            int result = 0;
            Iterator var4 = data.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<K, V> entry = (Entry)var4.next();
                this.flush(false);
                if(!(new Date()).after((Date)expiry.get(entry.getKey()))) {
                    this.putInExpiryQueue(entry.getKey(), (Date)expiry.get(entry.getKey()));
                    this.persist.put(entry.getKey(), entry.getValue());
                    ++result;
                }
            }

            return result == data.size();
        } else {
            return false;
        }
    }

    public V updateValue(K key, V value) {
        this.flush(false);
        return this.persist.containsKey(key)?this.persist.put(key, value):null;
    }

    public boolean prolong(K key, Date newExpiryDate) {
        if(this.contains(key)) {
            this.putInExpiryQueue(key, newExpiryDate);
            return true;
        } else {
            return false;
        }
    }

    private void flush(boolean withoutCounter) {
        if(withoutCounter || this.flushCounter.incrementAndGet() >= this.flushInterval) {
            Object var2 = this.syncObject;
            synchronized(this.syncObject) {
                if(withoutCounter || this.flushCounter.get() >= this.flushInterval) {
                    log.info("Start flush stored value");

                    for(StorageExpiry.ExpiryObject expiry = (StorageExpiry.ExpiryObject)this.expiryQueue.poll(); expiry != null; expiry = (StorageExpiry.ExpiryObject)this.expiryQueue.poll()) {
                        this.persist.remove(expiry.getKey());
                    }

                    log.info("Finish flush stored value");
                    if(!withoutCounter) {
                        this.flushCounter.set(0);
                    }
                }
            }
        }

    }

    private void putInExpiryQueue(K key, Date expiryDate) {
        StorageExpiry.ExpiryObject<K> expiry = new StorageExpiry.ExpiryObject(key, expiryDate);
        this.expiryQueue.put(expiry);
    }

    public V removeValue(K key) {
        StorageExpiry.ExpiryObject<K> expiry = new StorageExpiry.ExpiryObject(key, new Date());
        this.expiryQueue.remove(expiry);
        return this.persist.remove(key);
    }

    protected void showExpiryQueue() {
        log.info("######################");
        Iterator var1 = this.expiryQueue.iterator();

        while(var1.hasNext()) {
            StorageExpiry.ExpiryObject<K> expiry = (StorageExpiry.ExpiryObject)var1.next();
            log.info("expiry.key = " + expiry.key + "     expiry.expiry = " + expiry.expiry);
        }

        log.info("######################");
    }

    public V getValue(K key) {
        this.flush(false);
        return this.persist.get(key);
    }

    public V[] getValues(K[] keys) {
        this.flush(false);
        ArrayList<V> result = new ArrayList(keys.length);
        Object[] var3 = keys;
        int var4 = keys.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            K key = (K) var3[var5];
            result.add(this.persist.get(key));
        }

        return (V[]) result.toArray();
    }

    public void flushValue(K key) {
        this.flush(false);
    }

    public boolean contains(K key) {
        this.flush(true);
        return this.persist.get(key) != null;
    }

    public boolean containsKey(K key) {
        return this.persist.containsKey(key);
    }

    public boolean containsValue(V value) {
        return this.persist.containsValue(value);
    }

    public int getFlushInterval() {
        return this.flushInterval;
    }

    public void setFlushInterval(int flushInterval) {
        this.flushInterval = flushInterval;
    }

    private static class ExpiryObject<K> implements Delayed {
        private final K key;
        private final Date expiry;

        public ExpiryObject(K key, Date expiry) {
            this.key = key;
            this.expiry = expiry;
        }

        public long getDelay(TimeUnit unit) {
            return this.expiry.getTime() - System.currentTimeMillis();
        }

        public K getKey() {
            return this.key;
        }

        public int compareTo(Delayed other) {
            if(this == other) {
                return 0;
            } else if(this != null && other != null && this.key != null && this.key.equals(((StorageExpiry.ExpiryObject)other).key)) {
                return 0;
            } else {
                long diff = this.expiry.getTime() - ((StorageExpiry.ExpiryObject)other).expiry.getTime();
                return diff == 0L?0:(diff < 0L?-1:1);
            }
        }

        public int hashCode() {
            boolean prime = true;
            int result = 1;
            result = 31 * result + (this.key == null ? 0 : this.key.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if(this == obj) {
                return true;
            } else if(obj == null) {
                return false;
            } else if(this.getClass() != obj.getClass()) {
                return false;
            } else {
                StorageExpiry.ExpiryObject other = (StorageExpiry.ExpiryObject)obj;
                if(this.key == null) {
                    if(other.key != null) {
                        return false;
                    }
                } else if(!this.key.equals(other.key)) {
                    return false;
                }

                return true;
            }
        }
    }
}
