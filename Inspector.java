//Structure inspired by Jordan Kidney
import java.util.*;
import java.lang.reflect.*;

public class Inspector {
    IdentityHashMap<Object, Integer> iMap = new IdentityHashMap<>();

    public void inspect(Object obj, boolean recursive){
        if(iMap.get(obj) == null){
            iMap.put(obj, iMap.size());
            ArrayList<Object> recurseObjects = new ArrayList<Object>();
            ArrayList<Object> superObjects = new ArrayList<Object>();
            Class classObject = obj.getClass();

            inspectClass(classObject, superObjects);

            fieldInspection(classObject, obj, recurseObjects, recursive);

            if (recursive){
                for(Object recurseObj : recurseObjects){
                    inspect(recurseObj, recursive);
                }
            }
        }
        else {
            //
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
                try {
                    Object fieldValue = field.get(obj);
                    recurseObjects.add(fieldValue);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
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
                Object[] a = Arrays.asList(field.get(obj)).toArray();
                System.out.println(Arrays.deepToString(a));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.out.println("unable to access");
        }
    }
}