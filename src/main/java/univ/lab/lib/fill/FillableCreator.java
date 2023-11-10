package univ.lab.lib.fill;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class FillableCreator {
    private final HashMap<String, Class<?>> map = new HashMap<>();
    public boolean isElementDeclaration(String qName) {
        return map.containsKey(qName);
    }
    public void add(Class<?> fillableClass) {
        if (fillableClass.isAnnotationPresent(Fillable.class)) {
            Fillable fillableAnnotation = fillableClass.getAnnotation(Fillable.class);
            String name = fillableAnnotation.name();
            map.put(name, fillableClass);
        }
    }
    public Object createNew(String qName) {
        Class<?> myClass = map.get(qName);
        try {
            Constructor<?> constructor = myClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
