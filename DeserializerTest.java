import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.jdom2.Document;
import org.junit.Test;

public class DeserializerTest {

    @Test
    public void reconstructedPrimitiveVariables(){
        primOb pob = new primOb(3, 4.5, false);
        Serializer ser = new Serializer();
        Deserializer der = new Deserializer();
        Document doc = ser.serialize(pob);
        primOb rob = (primOb) der.deserialize(doc);
        assertTrue(pob.getNum() == rob.getNum());
        assertEquals(pob.getDub(), rob.getDub(), 0.00001);
        assertTrue(!pob.getBool() && !rob.getBool());
    }
}
