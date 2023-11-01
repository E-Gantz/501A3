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

        
        printDoc(doc);
        return doc;
    }

    public void serializeObject(Object obj, Document doc, ArrayList<Object> recurseObjects){
        Class classObject = obj.getClass();
        Element object = new Element("object");
        object.setAttribute("class", classObject.getName());
        object.setAttribute("id", Integer.toString(iMap.size()));
        iMap.put(obj, iMap.size());
        doc.getRootElement().addContent(object);

        serializeFields(classObject, obj, recurseObjects, object);
    }

    public void serializeFields(Class classObject, Object obj, ArrayList<Object> recurseObjects, Element element){
        Field[] fields = classObject.getDeclaredFields();
        for(Field field : fields){
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

    public void serializeFieldValue(Element fieldElement, Field field, Object obj, ArrayList<Object> recurseObjects){
        Class fType = field.getType();
        if(fType.isArray()){
            //array stuff
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
                recurseObjects.add(field);
                //object stuff
            }
        }
    }

    public void printDoc(org.jdom2.Document doc){
        XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
        System.out.println(xmlOut.outputString(doc));
    }
}