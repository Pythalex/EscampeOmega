package jeux.escampe;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jeux.escampe.CaseCoder;

public class testCaseCoder {

    @Before
    public void init() {

    }

    @Test
    public void testDecode(){
        Assert.assertEquals(CaseCoder.decode("A2"), new Point2D(0, 1));
        Assert.assertEquals(CaseCoder.decode("D5"), new Point2D(3, 4));
        try {
            CaseCoder.decode("A");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e){
        }
        try {
            CaseCoder.decode("5");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e){
        }
        try {
            CaseCoder.decode("H5");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e){
        }
        try {
            CaseCoder.decode("AA");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e){
        }
        try {
            CaseCoder.decode("33");
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e){
        }
    }

    @Test
    public void testEncode(){
        Assert.assertEquals(CaseCoder.encode(0, 1), "A2");
        Assert.assertEquals(CaseCoder.encode(3, 4), "D5");
        try {
            CaseCoder.encode(-1, 2);
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e){
        }
        try {
            CaseCoder.encode(1, 6);
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e){
        }
    }

}