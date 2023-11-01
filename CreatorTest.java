import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
public class CreatorTest {
    @Test
    public void simpleObjectDefault(){
        primOb pob = new primOb();
        assertEquals(pob.getNum(), 1);
        assertEquals(pob.getDub(), 1.0, 0.001);
        assertTrue(pob.getBool());
    }

    @Test
    public void simpleObjectCustom(){
        primOb pob = new primOb(3, 60.78, false);
        assertEquals(pob.getNum(), 3);
        assertEquals(pob.getDub(), 60.78, 0.001);
        assertTrue(!pob.getBool());
    }
}