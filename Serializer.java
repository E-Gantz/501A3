import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.lang.reflect.*;
import java.util.ArrayList;
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
        if(fType.isArray()){
            try {
                Object fieldValue = field.get(obj);
                if(iMap.get(fieldValue) == null){
                    iMap.put(fieldValue, iMap.size());
                    recurseObjects.add(fieldValue);
                }
                Element refElement = new Element("reference");
                refElement.setText(Integer.toString(iMap.get(fieldValue)));
                fieldElement.addContent(refElement);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else{
            if(fType.isPrimitive()){
                Element valueElement = new Element("value");
                fieldElement.addContent(valueElement);
                try {
                    valueElement.setText(field.get(obj).toString());
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    valueElement.setText("Unreachable");
                }
            }
            else{
                try {
                    Object fieldValue = field.get(obj);
                    if(iMap.get(fieldValue) == null){
                        iMap.put(fieldValue, iMap.size());
                        recurseObjects.add(fieldValue);
                    }
                    Element refElement = new Element("reference");
                    refElement.setText(Integer.toString(iMap.get(fieldValue)));
                    fieldElement.addContent(refElement);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void serializeElements(Class classObject, Object obj, ArrayList<Object> recurseObjects, Element element, int length){
        Class compType = classObject.getComponentType();
        if(compType.isPrimitive()){
            for(int i=0; i<length; i++){
                Element valueElement = new Element("value");
                valueElement.setText(Array.get(obj, i).toString());
                element.addContent(valueElement);
            }
        }
        else{
            for(int i=0; i<length; i++){
                Object fieldValue = Array.get(obj, i);
                if(iMap.get(fieldValue) == null){
                    iMap.put(fieldValue, iMap.size());
                    recurseObjects.add(fieldValue);
                }
                Element refElement = new Element("reference");
                refElement.setText(Integer.toString(iMap.get(fieldValue)));
                element.addContent(refElement);
            }
        }
    }

    public void printDoc(org.jdom2.Document doc){
        XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
        System.out.println(xmlOut.outputString(doc));
    }
}