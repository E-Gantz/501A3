import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
        //Taken from Kimiya's tutorial session 13 example
        String address = "localhost";
        int port = 2023;
        try {
            Socket socket = new Socket(address, port);
            OutputStream outputStream = socket.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            xmlOutputter.output(doc, byteArrayOutputStream);
            byte[] documentBytes = byteArrayOutputStream.toByteArray();

            bufferedOutputStream.write(documentBytes);
            
            bufferedOutputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
