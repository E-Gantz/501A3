import java.lang.reflect.*;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import org.jdom2.*;

public class Deserializer {
    IdentityHashMap<Integer,Object> iMap;
    Object rootObject = null;
    List<Element> objects;
    
    public Object deserialize(org.jdom2.Document doc){
        //SAXBuilder sax = new SAXBuilder();
        iMap = new IdentityHashMap<>();
        Element root = doc.getRootElement();
        objects = root.getChildren();
        
        objectSetter(objects.get(0));
        return rootObject;
    }

    public void objectSetter(Element object){
        String className = object.getAttributeValue("class");
        try {
            Class objClass = Class.forName(className);
            Constructor c = objClass.getDeclaredConstructor(null);
            c.setAccessible(true);
            Object instance = c.newInstance();
            iMap.put(Integer.parseInt(object.getAttributeValue("id")), instance);

            List<Element> fields = object.getChildren();
            for(Element field : fields){
                fieldSetter(field, instance);
            }
            rootObject = instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fieldSetter(Element field, Object instance){
        String fieldName = field.getAttributeValue("name");
        String declaringClass = field.getAttributeValue("declaringclass");
        Field fieldObj;
        try {
            fieldObj = instance.getClass().getDeclaredField(fieldName);
            Class fType = fieldObj.getType();
            if(fType.isPrimitive()){
                List<Element> values = field.getChildren();
                if(values.size() == 1){
                    String value = values.get(0).getText();
                    primFieldSetter(instance, fType, fieldObj, value);
                }
                else {  //should never be run unless I've overlooked something.
                    System.out.println("This primitive has multiple values for some reason");
                }
            }
            else{
                int id = Integer.parseInt(field.getChildren().get(0).getText());
                if(iMap.get(id) == null){
                    if(fType.isArray()){
                        Class compType = fType.getComponentType();
                        if(compType.isPrimitive()){
                            Object arrayOb = setPrimArray(compType, id);
                            iMap.put(id, arrayOb);
                            fieldObj.set(instance, arrayOb);
                        }
                        else{
                            Object arrayOb = setRefArray(compType, id);
                            iMap.put(id, arrayOb);
                            fieldObj.set(instance, arrayOb);
                        }
                    } 
                    else if(fType.getDeclaredConstructor(null).newInstance() instanceof Collection){
                        Collection collectionOb = createCollection(fType, id);
                        iMap.put(id, collectionOb);
                        fieldObj.set(instance, collectionOb);
                    }
                    else{
                        Object refObject = refObBuilder(id);
                        iMap.put(id, refObject);
                        fieldObj.set(instance, refObject);
                    }
                }
                else{
                    fieldObj.set(instance, iMap.get(id));
                }
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public Object refObBuilder(int id){
        if(iMap.get(id) == null){
            Element object = findObject(id);
            String className = object.getAttributeValue("class");
            try {
                Class objClass = Class.forName(className);
                Constructor c = objClass.getDeclaredConstructor(null);
                c.setAccessible(true);
                Object instance = c.newInstance();
                iMap.put(Integer.parseInt(object.getAttributeValue("id")), instance);

                List<Element> fields = object.getChildren();
                for(Element field : fields){
                    fieldSetter(field, instance);
                }
                return instance;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        else{
            return iMap.get(id);
        }
    }

    public void primFieldSetter(Object instance, Class fType, Field fieldObj, String value){
        try {
            if(fType.getName().equals("int")){
                fieldObj.setInt(instance, Integer.parseInt(value));
            }
            else if(fType.getName().equals("boolean")){
                fieldObj.setBoolean(instance, Boolean.parseBoolean(value));
            }
            else if(fType.getName().equals("byte")){
                fieldObj.setByte(instance, Byte.parseByte(value));
            }
            else if(fType.getName().equals("short")){
                fieldObj.setShort(instance, Short.parseShort(value));
            }
            else if(fType.getName().equals("long")){
                fieldObj.setLong(instance, Long.parseLong(value));
            }
            else if(fType.getName().equals("float")){
                fieldObj.setFloat(instance, Float.parseFloat(value));
            }
            else if(fType.getName().equals("double")){
                fieldObj.setDouble(instance, Double.parseDouble(value));
            }
            else if(fType.getName().equals("char")){
                fieldObj.setChar(instance, value.charAt(0));
            }
            else if(fType.getName().equals("Void")){
                //fieldObj.set(instance, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object setPrimArray(Class fType, int id){
        Element object = findObject(id);
        List<Element> values = object.getChildren();
        int length = Integer.parseInt(object.getAttributeValue("length"));
        Object instance = Array.newInstance(fType, length);
        for(int i=0; i<length; i++){
            if(fType.getName().equals("int")){
                Array.setInt(instance, i, Integer.parseInt(values.get(i).getText()));
            }
            else if(fType.getName().equals("boolean")){
                Array.setBoolean(instance, i, Boolean.parseBoolean(values.get(i).getText()));
            }
            else if(fType.getName().equals("byte")){
                Array.setByte(instance, i, Byte.parseByte(values.get(i).getText()));
            }
            else if(fType.getName().equals("short")){
                Array.setShort(instance, i, Short.parseShort(values.get(i).getText()));
            }
            else if(fType.getName().equals("long")){
                Array.setLong(instance, i, Long.parseLong(values.get(i).getText()));
            }
            else if(fType.getName().equals("float")){
                Array.setFloat(instance, i, Float.parseFloat(values.get(i).getText()));
            }
            else if(fType.getName().equals("double")){
                Array.setDouble(instance, i, Double.parseDouble(values.get(i).getText()));
            }
            else if(fType.getName().equals("char")){
                Array.setChar(instance, i, values.get(i).getText().charAt(0));
            }
            else if(fType.getName().equals("Void")){
                //fieldObj.set(instance, null);
            }
        }
        return instance;
    }

    public Object setRefArray(Class fType, int id){
        Element object = findObject(id);
        List<Element> refIds = object.getChildren();
        int length = Integer.parseInt(object.getAttributeValue("length"));
        Object instance = Array.newInstance(fType, length);
        for(int i=0; i<length; i++){
            Object elem = refObBuilder(Integer.parseInt(refIds.get(i).getText()));
            Array.set(instance, i, elem);
        }
        return instance;
    }

    public Collection createCollection(Class fType, int id){
        try {
            Element object = findObject(id);
            List<Element> refIds = object.getChildren();
            int length = Integer.parseInt(object.getAttributeValue("length"));
            Collection collection = (Collection) fType.getDeclaredConstructor(null).newInstance();
            for(int i=0; i<length; i++){
                Object elem = refObBuilder(Integer.parseInt(refIds.get(i).getText()));
                collection.add(elem);
            }
            return collection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Element findObject(int id){
        Element object = null;
        for(Element obj : objects){
            if(Integer.parseInt(obj.getAttributeValue("id")) == id){
                object = obj;
                break;
            }
        }
        return object;
    }
}