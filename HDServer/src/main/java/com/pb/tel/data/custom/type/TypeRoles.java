package com.pb.tel.data.custom.type;

import com.pb.tel.data.Roles;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class TypeRoles implements UserType {

    public static final String NAME = "TypeRoles";

    private static final Logger log = Logger.getLogger(TypeRoles.class.getCanonicalName());

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
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
        final String stringRoles = rs.getString(names[0]);
        //log.info("stringRoles: "+stringRoles);
        Set<Roles> result = new HashSet<Roles>();
        if(!StringUtils.isEmpty(stringRoles)) {
            String [] rolesString = stringRoles.split(",");
            //log.info("rolesString.length: "+rolesString.length);
            for(String roleString : rolesString){
                //log.info("rolesString1 : "+roleString);
                Roles role = Roles.getByCode(roleString);
                //log.info("role: "+role);
                if(role != null)
                    result.add(role);
            }
        }
        //log.info("stringRoles result: "+result);
        return result;
    }

    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index, final SharedSessionContractImplementor session) throws HibernateException, SQLException {
        @SuppressWarnings("unchecked")
        Set<Roles> roles = (Set<Roles>)value;
        String result = "";
        for(Roles role : roles)
            result = result +","+ role.getCode();
        result = result.replaceFirst(",", "");
        st.setString(index, result);
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
