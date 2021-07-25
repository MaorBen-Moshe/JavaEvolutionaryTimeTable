package Test;
import main.InvestClass;
import reflection.api.Investigator;

public class Program {
    public static void main(String[] args) {
        Polygon polygon = new Polygon();
        Investigator investigator = new InvestClass();
        investigator.load(polygon);

        System.out.println("getTotalNumberOfMethods: " + investigator.getTotalNumberOfMethods());
        System.out.println("getTotalNumberOfConstructors: " + investigator.getTotalNumberOfConstructors());
        System.out.println("getTotalNumberOfFields: " + investigator.getTotalNumberOfFields());
        System.out.println("getAllImplementedInterfaces: " + investigator.getAllImplementedInterfaces().toString());
        System.out.println("getCountOfConstantFields: " + investigator.getCountOfConstantFields());
        System.out.println("getCountOfStaticMethods: " + investigator.getCountOfStaticMethods());
        System.out.println("isExtending: " + investigator.isExtending());
        System.out.println("getParentClassSimpleName: " + investigator.getParentClassSimpleName());
        System.out.println("isParentClassAbstract: " + investigator.isParentClassAbstract());
        System.out.println("getNamesOfAllFieldsIncludingInheritanceChain: " + investigator.getNamesOfAllFieldsIncludingInheritanceChain().toString());
        System.out.println("invokeMethodThatReturnsInt: " + investigator.invokeMethodThatReturnsInt("getTotalPoints"));
        System.out.println("createInstance: " + investigator.createInstance(0).toString());
        System.out.println("elevateMethodAndInvoke: " + investigator.elevateMethodAndInvoke("addPoint", new Class[]{int.class, int.class}, 2,2));
        System.out.println("getInheritanceChain: " + investigator.getInheritanceChain("->"));

    }
}
