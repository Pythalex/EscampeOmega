package jeux.escampe;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testRegex {

    public static final String caseRegex = "[A-F][1-6]";
    public static final String moveRegex = caseRegex + "-" + caseRegex;
    public static final String positioningRegex = caseRegex + "/" + caseRegex + "/" + caseRegex + "/" + caseRegex + "/" + caseRegex + "/" + caseRegex;

    @Before
    public void init() {

    }

    @Test
    public void tests(){
        Assert.assertTrue(" ".matches("\\s"));
        Assert.assertTrue("Abdede".matches("\\w+"));
        Assert.assertTrue(" ".matches("\\s"));

        Assert.assertTrue("B1-D1".matches(moveRegex));
        Assert.assertTrue("C6/A6/B5/D5/E6/F5".matches(positioningRegex));
    }

}