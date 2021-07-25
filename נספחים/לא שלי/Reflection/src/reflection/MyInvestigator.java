package reflection;

import reflection.api.Investigator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.reflect.Modifier.*;

public class MyInvestigator implements Investigator {
    private Object obj;
    private Class<?> objClass;

    @Override
    public void load(Object anInstanceOfSomething) {
        obj = anInstanceOfSomething;
        objClass = obj.getClass();
    }

    @Override
    public int getTotalNumberOfMethods() {
        return objClass.getDeclaredMethods().length;
    }

    @Override
    public int getTotalNumberOfConstructors() {
        return objClass.getDeclaredConstructors().length;
    }

    @Override
    public int getTotalNumberOfFields() {
        return objClass.getDeclaredFields().length;
    }

    @Override
    public Set<String> getAllImplementedInterfaces() {
        Set<String> interfacesNames = new HashSet<>();
        Class<?>[] interfaces = objClass.getInterfaces();

        for (Class<?> anInterface : interfaces) {
            interfacesNames.add(anInterface.getSimpleName());
        }

        return interfacesNames;
    }

    @Override
    public int getCountOfConstantFields() {
        int count = 0;
        Field[] fields = objClass.getDeclaredFields();

        for (Field field : fields) {
            if (isFinal(field.getModifiers())) {
                count++;
            }
        }

        return count;
    }

    @Override
    public int getCountOfStaticMethods() {
        int count = 0;
        Method[] methods = objClass.getDeclaredMethods();

        for (Method method : methods) {
            if (isStatic(method.getModifiers())) {
                count++;
            }
        }

        return count;
    }

    @Override
    public boolean isExtending() {
        Class<?> superClass = objClass.getSuperclass();
        boolean result = false;

        if (superClass != null && !superClass.equals(Object.class)) {
            result = true;
        }

        return result;
    }

    @Override
    public String getParentClassSimpleName() {
        Class<?> superClass = objClass.getSuperclass();
        String name = null;

        if (superClass != null && !superClass.equals(Object.class)) {
            name = superClass.getSimpleName();
        }

        return name;
    }

    @Override
    public boolean isParentClassAbstract() {
        Class<?> superClass = objClass.getSuperclass();
        boolean result = false;

        if (superClass != null && !superClass.equals(Object.class)) {
            if (isAbstract(superClass.getModifiers())) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {
        Set<String> names = new HashSet<>();
        Class<?> currentClass = objClass;
        Field[] fields;

        while (currentClass != null && !currentClass.equals(Object.class)) {
            fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                names.add(field.getName());
            }

            currentClass = currentClass.getSuperclass();
        }

        return names;
    }

    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        Method[] methods = objClass.getDeclaredMethods();
        Method methodToInvoke = null;
        int result = 0;

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                methodToInvoke = method;
                break;
            }
        }
        try {
            if (methodToInvoke != null) {
                result = (int) methodToInvoke.invoke(obj, args);
            }
        } catch (Exception ignored) {
        }

        return result;
    }

    @Override
    public Object createInstance(int numberOfArgs, Object... args) {
        Constructor<?>[] constructors = objClass.getDeclaredConstructors();
        Constructor<?> constructorToInvoke = null;
        Object instance = null;

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == numberOfArgs) {
                constructorToInvoke = constructor;
                break;
            }
        }

        try {
            if (constructorToInvoke != null) {
                instance = constructorToInvoke.newInstance(args);
            }
        } catch (Exception ignore) {
        }

        return instance;
    }

    @Override
    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        Method methodToInvoke;
        Object result = null;

        try {
            methodToInvoke = objClass.getDeclaredMethod(name, parametersTypes);
            methodToInvoke.setAccessible(true);
            result = methodToInvoke.invoke(obj, args);
        } catch (Exception ignored) {
        }

        return result;
    }

    @Override
    public String getInheritanceChain(String delimiter) {
        Class<?> currentClass = objClass;
        StringBuilder names = new StringBuilder();

        names.insert(0, currentClass.getSimpleName());
        currentClass = currentClass.getSuperclass();

        while (currentClass != null) {
            names.insert(0, currentClass.getSimpleName() + delimiter);
            currentClass = currentClass.getSuperclass();
        }

        return names.toString();
    }
}
