import org.jdom2.*;

public class Serializer{

    public org.jdom2.Document serialize(Object obj){
        Element root = new Element("serialized");
        Document doc = new Document(root);
        
        return doc;
    }
}