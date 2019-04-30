package esiapp.esi.db.dao;

import java.io.IOException;

import esiapp.esi.db.Serializer;

/**
 * Created on 17-06-2015.
 *
 * @author anil
 */
public abstract class BaseDao {

    /**
     * Convert the string to byte[] array.
     *
     * @param value Input String to convert into byte array.
     * @return Byte array if value is not null else return null.
     */
    protected byte[] getPersistByteValue(String value) {
        if (value != null) {
            return value.getBytes();
        }

        return null;
    }

    /**
     * Convert object to byte[] array.
     *
     * @param obj Object to convert into byte array.
     * @return Byte array if object is not null else return null.
     */
    protected byte[] getPersistSerializeData(Object obj) {
        byte[] bytes = null;

        try {
            if (obj != null) {
                bytes = Serializer.serialize(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }
}
