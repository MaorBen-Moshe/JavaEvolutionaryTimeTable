package main;

import reflection.api.Investigator;
import java.lang.reflect.*;
import java.util.*;

public class InvestClass implements Investigator
{
    private Class<?> instanceClass;
    private Object instanceObject;

    public InvestClass(){
    }

    @Override
    public void load(Object anInstanceOfSomething) {
        if(anInstanceOfSomething != null){
            instanceObject = anInstanceOfSomething;
            instanceClass = anInstanceOfSomething.getClass();
        }
    }

    @Override
    public int getTotalNumberOfMethods() {
        try{
            return instanceClass.getDeclaredMethods().length;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getTotalNumberOfConstructors() {
        try{
            return instanceClass.getDeclaredConstructors().length;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getTotalNumberOfFields() {
        try{
            return instanceClass.getDeclaredFields().length;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public Set<String> getAllImplementedInterfaces() {
        try{
            Set<String> interfacesNames = new HashSet<>();
            Class<?>[] interfacesClasses = instanceClass.getInterfaces();
            for(Class<?> current : interfacesClasses){
                interfacesNames.add(current.getSimpleName());
            }

            return interfacesNames;
        } catch (Exception e){
            return new HashSet<>(); // returning an empty name data
        }
    }

    @Override
    public int getCountOfConstantFields() {
        try{
            byte counter = 0;
            Field[] fields = instanceClass.getDeclaredFields();
            for (Field field: fields) {
                if(Modifier.isFinal(field.getModifiers())){
                    counter++;
                }
            }

            return counter;
        } catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getCountOfStaticMethods() {
        try{
            byte counter = 0;
            Method[] methods = instanceClass.getDeclaredMethods();
            for (Method method: methods) {
                if(Modifier.isStatic(method.getModifiers())){
                    counter++;
                }
            }

            return counter;
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public boolean isExtending() {
        try{
            Class<?> superClass = instanceClass.getSuperclass();
            return superClass != null && !superClass.getName().equals(Object.class.getName());
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public String getParentClassSimpleName() {
        try{
            String name = null;
            if(isExtending()){
                name = instanceClass.getSuperclass().getSimpleName();
            }

            return name;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean isParentClassAbstract() {
        try{
            return isExtending() && Modifier.isAbstract(instanceClass.getSuperclass().getModifiers());
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {
        try{
            Set<String> fieldsNames = new HashSet<>();
            Field[] fields;
            Class<?> currentClass = instanceClass;

            while(currentClass != null){
                fields = currentClass.getDeclaredFields();
                for(Field field : fields){
                    fieldsNames.add(field.getName());
                }

                currentClass = currentClass.getSuperclass();
            }

            return fieldsNames;
        } catch(Exception e){
            return new HashSet<>();
        }
    }

    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        try{
            Method toInvoke = instanceClass.getDeclaredMethod(methodName);
            return (int)toInvoke.invoke(instanceObject, args);
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    public Object createInstance(int numberOfArgs, Object... args) {
        try{

            Object retVal = null;
            Constructor<?> wantedCtor = null;
            Constructor<?>[] constructors = instanceClass.getDeclaredConstructors();
            for(Constructor<?> constructor : constructors){
                if(constructor.getParameterCount() == numberOfArgs){
                    wantedCtor = constructor;
                    break;
                }
            }

            if(wantedCtor != null){
                retVal = wantedCtor.newInstance(args);
            }

            return retVal;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        try{
            Method toInvoke = instanceClass.getDeclaredMethod(name, parametersTypes);
            toInvoke.setAccessible(true);
            return toInvoke.invoke(instanceObject, args);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public String getInheritanceChain(String delimiter) {
        try{
            StringBuilder chainString = new StringBuilder();
            Class<?> parent = instanceClass;

            while(parent != null){
                chainString.insert(0, parent.getSimpleName() + delimiter);
                parent = parent.getSuperclass();
            }

            // delete the last delimiter
            chainString.delete(chainString.length() - delimiter.length(), chainString.length());
            return chainString.toString();
        }catch(Exception e) {
            return null;
        }
    }
}