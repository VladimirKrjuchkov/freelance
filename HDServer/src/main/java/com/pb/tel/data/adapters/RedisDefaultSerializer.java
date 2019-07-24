package com.pb.tel.data.adapters;

import com.pb.tel.utils.Utils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;

/**
 * Created by vladimir on 18.07.19.
 */
public class RedisDefaultSerializer<T> implements RedisSerializer<T> {

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if(t==null)
            return null;
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject((Serializable)t);
            oos.flush();
            oos.close();
            bos.close();
            return bos.toByteArray();
        }
        catch (Throwable ex) {
            throw new SerializationException("Failed to serialize object "+
                    t.getClass().getSimpleName() +" using serialazer " +this.getClass().getSimpleName(), ex);
        }
    }


    @Override
    public T deserialize(byte[] bytes) throws SerializationException{
        if(Utils.isEmpty(bytes))
            return null;
        try{
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            T object = (T)objectInputStream.readObject();
            bais.close();
            objectInputStream.close();
            return object;
        }
        catch (Throwable ex) {
            throw new SerializationException("Failed to deserialize using " +this.getClass().getSimpleName(), ex);
        }
    }


    public T deserialize(byte[] bytes, Class<T> clas) throws SerializationException{
        return clas.cast(deserialize(bytes));
    }

}
