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
        positions.add(new Point2D(1, 0)); // paladin blanc
        positions.add(new Point2D(2, 0)); // ..
        positions.add(new Point2D(3, 0)); // ..
        positions.add(new Point2D(4, 0)); // ..
        positions.add(new Point2D(5, 0)); // ..
        positions.add(new Point2D(0, 5)); // licorne noire
        positions.add(new Point2D(1, 5)); // paladin noir
        positions.add(new Point2D(2, 5)); // ..
        positions.add(new Point2D(3, 5)); // ..
        positions.add(new Point2D(4, 5)); // ..
        positions.add(new Point2D(5, 5)); // ..
        licorneBlanchePrise = new PlateauEscampe(positions);

        positions.set(0, new Point2D(0, 0));
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

    @Test
    public void testCoupValide() {
        CoupEscampe avancelicorne = new CoupEscampe(0, 0, 1);
        Assert.assertTrue(plateau.coupValide(plateau.jBlanc, avancelicorne));
    }
}
