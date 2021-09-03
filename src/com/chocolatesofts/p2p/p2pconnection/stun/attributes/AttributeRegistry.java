package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.*;

import java.io.DataOutputStream;
import java.util.HashMap;

public class AttributeRegistry {

    private static final HashMap<Class<?>, Serializer<?>> serializerMap = new HashMap<>();
    private static final HashMap<Character, Class<?>> typeMap = new HashMap<>();

    public interface Serializer<T> {
        char getUniqueType();
        T deserialize(TLV tlv, StunHeader header);
        void serialize(T attribute, DataOutputStream outputStream);
    }
    static {
        addSerializer(XORMappedAddress.class, XORMappedAddressParser.getInstance());
        addSerializer(MappedAddress.class, MappedAddressParser.getInstance());
        addSerializer(Reserved0.class, new Reserved0Parser());
        addSerializer(Reserved2.class, new Reserved2Parser());
        addSerializer(Reserved3.class, new Reserved3Parser());
        addSerializer(Reserved4.class, new Reserved4Parser());
        addSerializer(Reserved5.class, new Reserved5Parser());
        addSerializer(Reserved7.class, new Reserved7Parser());
        addSerializer(ReservedB.class, new ReservedBParser());
    }

    public static <T> void addSerializer(final Class<T> clazz, final Serializer<T> serializer) {
        typeMap.put(serializer.getUniqueType(), clazz);
        serializerMap.put(clazz, serializer);
    }

    public static <T> Serializer<T> getAttributeSerializer(final Class<T> clazz) throws IllegalArgumentException {
        if(serializerMap.containsKey(clazz))
            return (Serializer<T>) serializerMap.get(clazz);
        throw new IllegalArgumentException(String.format("Parser not registered for this class : %s", clazz.getName()));
    }

    public static Class<?> getAttributeClass(final TLV tlv) {
        if(typeMap.containsKey(tlv.getType()))
            return typeMap.get(tlv.getType());
        throw new IllegalArgumentException(String.format("Attribute class not registered for type : %s", tlv.getType()));
    }
}
