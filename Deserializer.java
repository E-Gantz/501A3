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
            try {
                Class objClass = Class.forName(className);
                Constructor c = objClass.getDeclaredConstructor(null);
                c.setAccessible(true);
                Object instance = c.newInstance();
                iMap.put(Integer.parseInt(object.getAttributeValue("id")), instance);
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
            }
            
            List<Element> fields = object.getChildren();
                for(Element field : fields){
                    List<Element> values = field.getChildren();
                }
        }
        
        
        return new Object();
    }
}