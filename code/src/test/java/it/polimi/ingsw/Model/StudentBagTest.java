package it.polimi.ingsw.Model;

import junit.framework.TestCase;

public class StudentBagTest extends TestCase {

    public void testExtract() {
    }

    public void testExtract_multiple() {
    }

    public void testAppendAndShuffle() {
        StudentBag bag = new StudentBag(24);
        System.out.println(bag.extract_multiple(10));
    }
}