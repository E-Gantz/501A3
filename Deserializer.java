import java.lang.reflect.*;
import java.util.IdentityHashMap;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

public class Deserializer {
    IdentityHashMap<Integer,Object> iMap;
    
    public Object deserialize(org.jdom2.Document doc){
        //SAXBuilder sax = new SAXBuilder();
        iMap = new IdentityHashMap<>();
        Element root = doc.getRootElement();
        List<Element> objects = root.getChildren();
        
        for(Element object : objects){
            String className = object.getAttributeValue("class");
            System.out.println("attempting deserialization of " + className);
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
                    Field fieldObj = objClass.getDeclaredField(fieldName);
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
        
        
        return new Object();
    }

    public void primFieldSetter(Object instance, Class fType, Field fieldObj, String value){
        try {
            if(fType.getName().equals("Integer")){
                fieldObj.setInt(instance, Integer.parseInt(value));
            }
            else if(fType.getName().equals("Boolean")){
                fieldObj.setBoolean(instance, Boolean.parseBoolean(value));
            }
            else if(fType.getName().equals("Byte")){
                fieldObj.setByte(instance, Byte.parseByte(value));
            }
            else if(fType.getName().equals("Short")){
                fieldObj.setShort(instance, Short.parseShort(value));
            }
            else if(fType.getName().equals("Long")){
                fieldObj.setLong(instance, Long.parseLong(value));
            }
            else if(fType.getName().equals("Float")){
                fieldObj.setFloat(instance, Float.parseFloat(value));
            }
            else if(fType.getName().equals("Double")){
                fieldObj.setDouble(instance, Double.parseDouble(value));
            }
            else if(fType.getName().equals("Character")){
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