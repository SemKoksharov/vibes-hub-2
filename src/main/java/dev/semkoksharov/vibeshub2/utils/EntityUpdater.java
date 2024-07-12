package dev.semkoksharov.vibeshub2.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityUpdater {

    public void update(Object source, Object target) throws InvocationTargetException, IllegalAccessException {
        Method[] sourceMethods = source.getClass().getMethods();
        Method[] targetMethods = target.getClass().getMethods();

        for (Method sourceMethod : sourceMethods) {
            if (isGetter(sourceMethod)) {
                Object value = sourceMethod.invoke(source);
                if (value == null || (value instanceof String && ((String) value).isBlank())) {
                    continue;
                }

                String setterName = "set" + sourceMethod.getName().substring(3);
                Method targetSetter = findMethod(targetMethods, setterName, sourceMethod.getReturnType());

                if (targetSetter != null) {
                    targetSetter.invoke(target, value);
                }
            }
        }
    }

    private boolean isGetter(Method method) {
        return method.getName().startsWith("get") &&
                method.getParameterCount() == 0 &&
                !method.getReturnType().equals(void.class);
    }

    private Method findMethod(Method[] methods, String name, Class<?> returnType) {
        for (Method method : methods) {
            if (method.getName().equals(name) && method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(returnType)) {
                return method;
            }
        }
        return null;
    }

}
