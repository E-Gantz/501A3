import java.util.ArrayList;

public class Sender {
    public static void main(String[] args) {
        Sender sender = new Sender();
        sender.createAndSerialize();
    }

    public void createAndSerialize(){
        ObjectCreator objC = new ObjectCreator();
        objC.createObjects();
        Serializer serializer = new Serializer();
        ArrayList<org.jdom2.Document> docs = new ArrayList<org.jdom2.Document>();
        for(Object obj : objC.objects){
            docs.add(serializer.serialize(obj));
        }
    }
    
}
