import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;

public class Serializer{
    IdentityHashMap<Object, Integer> iMap;

    public org.jdom2.Document serialize(Object obj){
        iMap = new IdentityHashMap<>();
        ArrayList<Object> recurseObjects = new ArrayList<Object>();
        Element root = new Element("serialized");
        Document doc = new Document(root);
        serializeObject(obj, doc, recurseObjects);
        for(Object fieldObj : recurseObjects){
            serialize(fieldObj, doc);
        }
        return doc;
    }

    public void serialize(Object obj, org.jdom2.Document doc){
        ArrayList<Object> recurseObjects = new ArrayList<Object>();
        serializeObject(obj, doc, recurseObjects);
        for(Object fieldObj : recurseObjects){
            serialize(fieldObj, doc);
        }
    }

    public void serializeObject(Object obj, Document doc, ArrayList<Object> recurseObjects){
        Class classObject = obj.getClass();
        Element object = new Element("object");
        object.setAttribute("class", classObject.getName());
        if(iMap.get(obj) == null){
            object.setAttribute("id", Integer.toString(iMap.size()));
            iMap.put(obj, iMap.size());
        }
        else {
            object.setAttribute("id", Integer.toString(iMap.get(obj)));
        }
        if(classObject.isArray()){
            int length = Array.getLength(obj);
            object.setAttribute("length", Integer.toString(length));
            serializeElements(classObject, obj, recurseObjects, object, length);
        }
        else if(obj instanceof Collection){
            int length = ((Collection) obj).size();
            object.setAttribute("length", Integer.toString(length));
            serializeCollection(classObject, obj, recurseObjects, object, length);
        }
        else{
            serializeFields(classObject, obj, recurseObjects, object);
        }
        doc.getRootElement().addContent(object);
    }

    public void serializeFields(Class classObject, Object obj, ArrayList<Object> recurseObjects, Element element){
        Field[] fields = classObject.getDeclaredFields();
        for(Field field : fields){
            int mod = field.getModifiers();
            if(!Modifier.isStatic(mod)){
                Element fieldElement = new Element("field");
                fieldElement.setAttribute("name",field.getName());
                fieldElement.setAttribute("declaringclass", classObject.getName());
                element.addContent(fieldElement);
                try {
                    field.setAccessible(true);
                    serializeFieldValue(fieldElement, field, obj, recurseObjects);
                } catch (InaccessibleObjectException e) {
                    fieldElement.setText("Field Inaccessible");
                }
            }
        }
    }

    public void serializeFieldValue(Element fieldElement, Field field, Object obj, ArrayList<Object> recurseObjects){
        Class fType = field.getType();
        if(fType.isPrimitive()){
            String text = null;
            try {
                text = field.get(obj).toString();
            } catch (IllegalArgumentException | IllegalAccessException e) {
                text = "Unreachable";
            }
            addRefValElement("value", text, fieldElement);
        }
        else{
            try {
                Object fieldValue = field.get(obj);
                checkAndAdd(fieldValue, recurseObjects);
                addRefValElement("reference", Integer.toString(iMap.get(fieldValue)), fieldElement);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void serializeElements(Class classObject, Object obj, ArrayList<Object> recurseObjects, Element element, int length){
        Class compType = classObject.getComponentType();
        if(compType.isPrimitive()){
            for(int i=0; i<length; i++){
                addRefValElement("value", Array.get(obj, i).toString(), element);
            }
        }
        else{
            for(int i=0; i<length; i++){
                Object fieldValue = Array.get(obj, i);
                checkAndAdd(fieldValue, recurseObjects);
                addRefValElement("reference", Integer.toString(iMap.get(fieldValue)), element);
            }
        }
    }

    public void serializeCollection(Class classObject, Object obj, ArrayList<Object> recurseObjects, Element element, int length){
       for(Object fieldValue : (Collection)obj){
            checkAndAdd(fieldValue, recurseObjects);
            addRefValElement("reference", Integer.toString(iMap.get(fieldValue)), element);
        }
    }

    public void printDoc(org.jdom2.Document doc){
        XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
        System.out.println(xmlOut.outputString(doc));
    }

    public void addRefValElement(String elemName, String elemText, Element parent){
        Element refVal = new Element(elemName);
        refVal.setText(elemText);
        parent.addContent(refVal);
    }

    public void checkAndAdd(Object fieldValue, ArrayList<Object> recurseObjects){
        if(iMap.get(fieldValue) == null){
            iMap.put(fieldValue, iMap.size());
            recurseObjects.add(fieldValue);
        }
    }
}