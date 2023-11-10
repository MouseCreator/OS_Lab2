package univ.lab.lib.fill;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class FillerImpl implements Filler {

    @Override
    public void fill(Object toInitialize, String attribute, Object value) {
        if (attribute.isEmpty()) {
            return;
        }
        Field[] declaredFields = toInitialize.getClass().getDeclaredFields();
        boolean fill = false;
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Fill.class)) {
                Fill annotation = field.getAnnotation(Fill.class);
                if (annotation.attribute().equals(attribute)) {
                    setPrimitiveOrObject(field, toInitialize, value);
                    fill = true;
                }
            }
            else if (field.isAnnotationPresent(FillList.class)) {
                FillList annotation = field.getAnnotation(FillList.class);
                if (annotation.attribute().equals(attribute)) {
                    appendList(field, toInitialize, value);
                    fill = true;
                }
            }
        }
        if (!fill) {
            throw new IllegalArgumentException(
                    String.format("Object of %s class does not have attribute '%s'", toInitialize.getClass(), attribute)
                    );
        }
    }

    private String isListOf(Field field) {
        Type genericFieldType = field.getGenericType();

        if (genericFieldType instanceof ParameterizedType parameterizedType) {
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            for (Type typeArgument : typeArguments) {
                if (typeArgument.equals(Integer.class)) {
                    return "integer";
                }
                else if (typeArgument.equals(Boolean.class)) {
                    return "boolean";
                }
                else if (typeArgument.equals(Long.class)) {
                    return "long";
                }
                else if (typeArgument.equals(String.class)) {
                    return "string";
                }
            }
        }
        throw new UnsupportedOperationException("Unsupported type to cast from String for Field:" + field.getName());
    }
    private void setPrimitiveOrObject(Field field, Object toInitialize, Object value) {
        if (value instanceof String) {
            setPrimitive(field, toInitialize, value);
        } else {
            setField(field, toInitialize, value);
        }
    }
    private void setField(Field field, Object obj, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
           throw new RuntimeException("Field is not accessible", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot set field value. Expected: "
                    + field.getType().getName() + ", but got " + value.getClass().getName(), e);
        }
    }
    private void appendList(Field field, Object toInit, Object toAdd) {
        field.setAccessible(true);
        if (toAdd instanceof String str) {
            String listOf = isListOf(field);
            switch (listOf) {
                case "integer" -> toAdd = Integer.parseInt(str);
                case "boolean" -> toAdd = Boolean.parseBoolean(str);
                case "long" -> toAdd = Long.parseLong(str);
            }
        }
        try {
            if (field.get(toInit) == null) {
                initList(field, toInit, toAdd);
            } else {
                addToList(field, toInit, toAdd);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void initList(Field field, Object toInit, Object t) throws InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object list;
        if (field.getType().isInterface()) {
            list = new ArrayList<>();
        } else {
            list = field.getType().getConstructor().newInstance();
        }
        boolean valid = testGeneric(field, list, t);
        if (!valid) {
            throw new IllegalArgumentException("Argument is not of Generic type!");
        }
        Method add = List.class.getDeclaredMethod("add",Object.class);
        add.invoke(list, t);
        field.set(toInit, list);
    }

    private boolean testGeneric(Field field, Object list, Object toAdd)  {
        if (list instanceof List) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType parameterizedType) {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length == 1) {
                    Type listType = typeArguments[0];
                    return listType.equals(toAdd.getClass());
                }
            }
        }
        return false;
    }

    private void addToList(Field field, Object toInitialize, Object toAdd) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object list = field.get(toInitialize);
        boolean valid = testGeneric(field, list, toAdd);
        if (!valid) {
            throw new IllegalArgumentException("Argument is not of Generic type!");
        }
        Method add = List.class.getDeclaredMethod("add",Object.class);
        if (!(list instanceof List)) {
            throw new IllegalArgumentException("Field is not of type List");
        }
        add.invoke(list, toAdd);
        field.set(toInitialize, list);
    }

    private void setPrimitive(Field field, Object toInitialize, Object value) {
        String stringValue = (String) value;
        Class<?> fieldType = field.getType();
        if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
            setField(field, toInitialize, Boolean.parseBoolean(stringValue));
        } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
            setField(field, toInitialize, Integer.parseInt(stringValue));
        } else if (fieldType.equals(String.class)) {
            setField(field, toInitialize, stringValue);
        } else if (fieldType.equals(Long.class)) {
            setField(field, toInitialize, Long.parseLong(stringValue));
        } else {
            throw new UnsupportedOperationException("Unsupported type to cast from String:" + value.getClass().getName());
        }
    }
}
