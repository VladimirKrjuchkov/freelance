package com.pb.tel.data.custom.type;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vladimir on 26.07.19.
 */
public class TypeArrayToSet implements UserType {

    public static final String NAME = "TypeArrayToSet";

    @Override
    public int[] sqlTypes() {
        return new int[] {Types.ARRAY};
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class returnedClass() {
        return Set.class;
    }

    @Override
    public boolean equals(final Object x, final Object y) throws HibernateException {
        return  ObjectUtils.nullSafeEquals(x, y);
    }

    @Override
    public int hashCode(final Object x) throws HibernateException {
        if(x==null)
            return 0;
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names, final SharedSessionContractImplementor session, final Object owner) throws HibernateException, SQLException {
        Array friendsSqlArray = rs.getArray(names[0]);
        if(friendsSqlArray==null)
            return null;
        String[] friends = (String[]) friendsSqlArray.getArray();
        Set<String> result = new HashSet<>();
        for(String friend :friends)
            result.add(friend);
        return result;
    }

    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index, final SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if(value==null){
            st.setArray(index, null);
            return;
        }
        @SuppressWarnings("unchecked")
        Set<String> okpos = (Set<String>)value;
        Array ar = st.getConnection().createArrayOf("VARCHAR", okpos.toArray());
        st.setArray(index, ar);
    }

    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(final Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return original;
    }
}

