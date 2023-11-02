public class Receiver {

    public void listen(org.jdom2.Document doc){
        Deserializer deserializer = new Deserializer();
        //setup and listen to socket stuff here
        //get a doc from the socket
        Object reconstructed = deserializer.deserialize(doc);
        Inspector inspecter = new Inspector();
        inspecter.inspect(reconstructed, false);
    }
}
