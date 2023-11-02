import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import org.jdom2.*;

public class Deserializer {
    IdentityHashMap<Integer,Object> iMap;
    
    public Object deserialize(org.jdom2.Document doc){
        //SAXBuilder sax = new SAXBuilder();
        ArrayList<Object> instances = new ArrayList<Object>();
        iMap = new IdentityHashMap<>();
        Element root = doc.getRootElement();
        List<Element> objects = root.getChildren();
        
        for(Element object : objects){
            String className = object.getAttributeValue("class");
            try {
                Class objClass = Class.forName(className);
                Constructor c = objClass.getDeclaredConstructor(null);
                c.setAccessible(true);
                Object instance = c.newInstance();
                iMap.put(Integer.parseInt(object.getAttributeValue("id")), instance);

                List<Element> fields = object.getChildren();
                for(Element field : fields){
                    String fieldName = field.getAttributeValue("name");
                    String declaringClass = field.getAttributeValue("declaringclass");
                    Field fieldObj = instance.getClass().getDeclaredField(fieldName);
                    Class fType = fieldObj.getType();
                    if(fType.isArray()){
                        //array stuff
                    }
                    else{
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
                            //object stuff
                        }
                    }
                }
                instances.add(instance);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return instances.get(0);
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
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}