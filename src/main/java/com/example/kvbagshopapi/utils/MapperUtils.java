package com.example.kvbagshopapi.utils;
import java.lang.reflect.Field;

public class MapperUtils<T, D> {
    public static <T, D> void toDto(T src, D des ) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = src.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(src) != null && !field.getName().contains("id")) {
                Field sd = des.getClass().getDeclaredField(field.getName());
                sd.setAccessible(true);
                sd.set(des, field.get(src));
            }
        }
    }
}
