package jeux.escampe;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jeux.escampe.EscampeBoard;

public class PlateauEscampeTest {

    public static EscampeBoard plateau;
    public static EscampeBoard licorneBlanchePrise;
    public static EscampeBoard licorneNoirePrise;

    public static String placementsblancs = "C1/A1/B1/C2/D1/E1";
    public static String placementsnoirs = "C6/A6/B6/C5/D6/E6";

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
        Assert.assertEquals(plateau.toString(),
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
        // mouvement valide
        Assert.assertTrue(plateau.isValidMove("A1-A2", "blanc"));

        // Pas le bon joueur
        Assert.assertFalse(plateau.isValidMove("A1-A2", "noir"));
        // Pas un mouvement d'une case depuis un liseré simple
        Assert.assertFalse(plateau.isValidMove("A1-A3", "blanc"));
        // Il y a déjà un pion sur la case B1
        Assert.assertFalse(plateau.isValidMove("A1-B1", "blanc"));
    }

    @Test
    public void testMove(){
        plateau.play("A1-A2", "blanc");
        Assert.assertEquals(plateau.toString(),
            "+-----------------+\n"+
            "|  |PB|PB|PB|PB|PB|\n"+
            "+-----------------+\n"+
            "|LB|  |  |  |  |  |\n"+
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

        EscampeBoard copy = plateau.clone();
        plateau.play("A1-A1", "blanc");
        // nothing should happen (invalid move same case)
        Assert.assertEquals(plateau, copy);

        plateau.play("A1-A3", "blanc");
        // nothing should happen (invalid move too long)
        Assert.assertEquals(plateau, copy);

        plateau.play("A1-A2", "noir");
        // nothing should happen (invalid player)
        Assert.assertEquals(plateau, copy);

        plateau.play("A1-B1", "blanc");
        // nothing should happen (invalid move pawn on case B1)
        Assert.assertEquals(plateau, copy);
    }

    @Test
    public void testPositioning(){
        plateau.play(placementsblancs, "blanc");
        plateau.play(placementsnoirs, "noir");
        plateau.toString().equals(
            "+-----------------+\n"+
            "|PB|PB|LB|PB|PB|  |\n"+
            "+-----------------+\n"+
            "|  |  |PB|  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |PN|  |  |  |\n"+
            "+-----------------+\n"+
            "|PN|PN|LN|PN|PN|  |\n"+
            "+-----------------+\n"
        );

        EscampeBoard copy = plateau.clone();
        plateau.play("C1/A5/T5/F5/D4/B2", "noir");
        // nothing should happen (invalid case T5)
        Assert.assertEquals(plateau, copy);

        plateau.play("A1/A1/B1/C1/D1/E1", "noir");
        // nothing should happen (two A1)
        Assert.assertEquals(plateau, copy);

        plateau.play("C1/A5/F5/D4/B2/F4/B3", "noir");
        // nothing should happen (not good format, too long)
        Assert.assertEquals(plateau, copy);

        plateau.play("C1/A5/F5/D4/B2", "noir");
        // nothing should happen (not good format, too short)
        Assert.assertEquals(plateau, copy);
    }
}
