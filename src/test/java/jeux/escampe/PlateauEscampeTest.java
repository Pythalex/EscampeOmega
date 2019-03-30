package jeux.escampe;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jeux.escampe.EscampeBoard;

public class PlateauEscampeTest {

    public EscampeBoard plateau;
    public EscampeBoard licorneBlanchePrise;
    public EscampeBoard licorneNoirePrise;

    @Before
    public void init() {
        plateau = new EscampeBoard();

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
        licorneBlanchePrise = new EscampeBoard(positions);

        positions.set(0, new Point2D(0, 0));
        positions.set(6, new Point2D(-1, -1));
        licorneNoirePrise = new EscampeBoard(positions);
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
        Assert.assertFalse(plateau.gameOver());
        Assert.assertTrue(licorneBlanchePrise.gameOver());
        Assert.assertTrue(licorneNoirePrise.gameOver());
    }

    @Test
    public void testCoupValide() {
        Assert.assertTrue(plateau.isValidMove("A1-A2", "blanc"));
        Assert.assertFalse(plateau.isValidMove("A1-A2", "noir"));
    }
}
