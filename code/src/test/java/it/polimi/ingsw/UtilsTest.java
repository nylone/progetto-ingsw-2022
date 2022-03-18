package it.polimi.ingsw;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import org.junit.Test;

import it.polimi.ingsw.misc.Utils;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class UtilsTest{

    @Test
    public void randomTest(){

        // initialized arrayList to apply the "random" method on it
        ArrayList<String> list = new ArrayList<String>(3);
        list.addAll(Arrays.asList("mago", "strega", "re"));

        // execution of the random method
        String randomValue = Utils.random(list);

        System.out.println(randomValue);

        // verify correctness of random implementation
        assertTrue(list.contains(randomValue));

    }


}