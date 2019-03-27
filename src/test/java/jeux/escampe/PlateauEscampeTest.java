package jeux.escampe;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jeux.escampe.PlateauEscampe;

public class PlateauEscampeTest {

    public PlateauEscampe plateau;
    public PlateauEscampe licorneBlanchePrise;
    public PlateauEscampe licorneNoirePrise;

    @Before
    public void init() {
        plateau = new PlateauEscampe();

        LinkedList<Point2D> positions = new LinkedList<Point2D>();
        positions.add(new Point2D(-1, -1)); // licorne blanche
        positions.add(new Point2D(2, 1)); // paladin blanc
        positions.add(new Point2D(3, 1)); // ..
        positions.add(new Point2D(4, 1)); // ..
        positions.add(new Point2D(5, 1)); // ..
        positions.add(new Point2D(6, 1)); // ..
        positions.add(new Point2D(1, 6)); // licorne noire
        positions.add(new Point2D(2, 6)); // paladin noir
        positions.add(new Point2D(3, 6)); // ..
        positions.add(new Point2D(4, 6)); // ..
        positions.add(new Point2D(5, 6)); // ..
        positions.add(new Point2D(6, 6)); // ..
        licorneBlanchePrise = new PlateauEscampe(positions);

        positions.set(0, new Point2D(1, 1));
        positions.set(6, new Point2D(-1, -1));
        licorneNoirePrise = new PlateauEscampe(positions);
    }

    @Test
    public void testPlateauDominos() {
        Assert.assertTrue(true);
    }

    @Test
    public void testToString(){
        plateau.toString().equals(
            "+-----------------+\n"+
            "|LB|PB|PB|PB|PB|PB|\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|LN|PN|PN|PN|PN|PN|\n"+
            "+-----------------+\n"
        );
    }

    @Test
    public void testFinDePartie(){
        Assert.assertFalse(plateau.finDePartie());
        Assert.assertTrue(licorneBlanchePrise.finDePartie());
        Assert.assertTrue(licorneNoirePrise.finDePartie());
    }
}
