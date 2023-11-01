import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.lang.reflect.*;
import java.util.IdentityHashMap;

public class Serializer{
    IdentityHashMap<Object, Integer> iMap;

    public org.jdom2.Document serialize(Object obj){
        iMap = new IdentityHashMap<>();
        Element root = new Element("serialized");
        Document doc = new Document(root);
        serializeObject(obj, doc);

        XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
        System.out.println(xmlOut.outputString(doc));
        
        return doc;
    }

    public void serializeObject(Object obj, Document doc){
        Class classObject = obj.getClass();
        Element object = new Element("object");
        object.setAttribute("class", classObject.getName());
        object.setAttribute("id", Integer.toString(iMap.size()));
        iMap.put(obj, iMap.size());
        doc.getRootElement().addContent(object);
    }
}