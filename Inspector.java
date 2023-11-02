//Structure inspired by Jordan Kidney
import java.util.*;
import java.lang.reflect.*;

public class Inspector {
    ArrayList<Integer> seenObjects = new ArrayList<Integer>();

    public void inspect(Object obj, boolean recursive){
        if(!seenObjects.contains(obj.hashCode())){  //According to TA we want to avoid inspecting the same object twice.
            seenObjects.add(obj.hashCode());
            ArrayList<Object> recurseObjects = new ArrayList<Object>();
            ArrayList<Object> superObjects = new ArrayList<Object>();
            Class classObject = obj.getClass();

            inspectClass(classObject, superObjects);

            fieldInspection(classObject, obj, recurseObjects, recursive);

            if (recursive){
                System.out.println("");
                System.out.println("Recursively Inspecting Objects in " + obj + "\n");
                for(Object recurseObj : recurseObjects){
                    inspect(recurseObj, recursive);
                }
            }
        }
        else {
            System.out.println(obj + " Has already been inspected.\n");
        }
    }


    public void inspectClass(Class classObject, ArrayList<Object> superObjects){
        try {
            System.out.println("Name of declaring class: " + classObject.getName());

            Class superClass = classObject.getSuperclass();
            if(superClass != null){
                System.out.println("Name of immediate superclass: " + superClass.getName());
                superObjects.add(superClass);
            }

            Class[] interfaces = classObject.getInterfaces();
            System.out.print("Name of the interfaces the class implements: ");
            for (Class i : interfaces){
                System.out.print(i.getName() + ", ");
                superObjects.add(i);
            }
            System.out.println("");
        } catch (Exception e) {
            //
        }
    }

    public void fieldInspection(Class classObject, Object obj, ArrayList<Object> recurseObjects, boolean recursive){
        System.out.println("Fields this class declares:");
        Field[] fields = classObject.getDeclaredFields();
        for(Field field : fields){
            try {
                field.setAccessible(true);
                inspectField(field, obj, recurseObjects, recursive);
            } catch (InaccessibleObjectException e) {
                System.out.println("Field Inaccessible: " + field.getName());
            }
        }
    }

    public void inspectField(Field field, Object obj, ArrayList<Object> recurseObjects, boolean recursive){
        System.out.println("    Field name: " + field.getName());

        Class fType = field.getType();
        System.out.println("        Type: " + fType.getTypeName());
        if(fType.isArray()){
            inspectFieldArray(field, obj, fType);
        }
        else{
            System.out.println("        Modifiers: " + Modifier.toString(field.getModifiers()));

            if(fType.isPrimitive()){
                try {
                    System.out.println("        Value: " + field.get(obj));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    System.out.println("        Value: Unreachable");
                }
            }
            else{
                recurseObjects.add(field);
                System.out.println("        Identity Hash Code: " + field.hashCode());
                if(recursive){System.out.println("            See below for introspection");}
            }
        }
    }

    public void inspectFieldArray(Field field, Object obj, Class fType){
        try {
            int len = Array.getLength(field.get(obj));
            System.out.println("        Length: " + len);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.out.println("        Value: Unreachable");
        }
        System.out.println("        Modifiers: " + Modifier.toString(field.getModifiers()));
        System.out.print("        Contents: ");
        try {
            if(fType.getComponentType().isArray()){
            System.out.println(Arrays.deepToString((Object[]) field.get(obj)));
            }
            else{
                System.out.println(Arrays.toString((Object[]) field.get(obj)));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.out.println("unable to access");
        }
    }
}