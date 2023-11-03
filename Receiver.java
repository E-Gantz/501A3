import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

public class Receiver {

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        receiver.listen();
    }

    public void listen(){
        //Taken from Kimiya's tutorial session 13 example
        int port = 2023;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            SAXBuilder saxBuilder = new SAXBuilder();
            Document doc = saxBuilder.build(bufferedInputStream);

            bufferedInputStream.close();
            socket.close();
            serverSocket.close();

            reconstruct(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reconstruct(org.jdom2.Document doc){
        Deserializer deserializer = new Deserializer();
        Object reconstructed = deserializer.deserialize(doc);
        //Inspector inspectre = new Inspector();
        //inspectre.inspect(reconstructed, true);
        Serializer serializer = new Serializer();
        org.jdom2.Document doc2 = serializer.serialize(reconstructed);
        System.out.println("Recieved: ");
        serializer.printDoc(doc2);
    }
}
