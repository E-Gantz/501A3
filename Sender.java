
public class Sender {
    public static void main(String[] args) {
        Sender sender = new Sender();
        org.jdom2.Document doc = sender.createAndSerialize();
        sender.send(doc);
    }

    public org.jdom2.Document createAndSerialize(){
        ObjectCreator objC = new ObjectCreator();
        Object obj = objC.createObject();
        Serializer serializer = new Serializer();
        org.jdom2.Document doc = serializer.serialize(obj);
        System.out.println("Sending: ");
        serializer.printDoc(doc);
        return doc;
    }

    public void send(org.jdom2.Document doc){
        //socket stuff
        Receiver receiver = new Receiver();
        receiver.listen(doc);
    }
    
}
