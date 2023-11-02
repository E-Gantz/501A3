import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SerializerTest {
    final PrintStream sOut = System.out;
    final ByteArrayOutputStream captcha = new ByteArrayOutputStream();

    @Before
    public void setup(){
        System.setOut(new PrintStream(captcha));
    }

    @After
    public void teardown(){
        System.setOut(sOut);
    }

    @Test
    public void ObjectNameID(){
        primOb pob = new primOb();
        Serializer ser = new Serializer();
        ser.printDoc(ser.serialize(pob));
        assertTrue(captcha.toString().contains("<object class=\"primOb\" id=\"0\">"));
    }

    @Test
    public void FieldNameClass(){
        primOb pob = new primOb();
        Serializer ser = new Serializer();
        ser.printDoc(ser.serialize(pob));
        assertTrue(captcha.toString().contains("<field name=\"num\" declaringclass=\"primOb\">"));
    }

    @Test
    public void FieldValue(){
        primOb pob = new primOb();
        Serializer ser = new Serializer();
        ser.printDoc(ser.serialize(pob));
        assertTrue(captcha.toString().contains("<value>1</value>"));
        assertTrue(captcha.toString().contains("<value>1.0</value>"));
        assertTrue(captcha.toString().contains("<value>true</value>"));
    }
}
