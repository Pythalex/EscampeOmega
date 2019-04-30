package jeux.escampe;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        plateau.play("A1/B1/C1/D1/E1/F1", "blanc");
        plateau.play("A6/B6/C6/D6/E6/F6", "noir");

        licorneBlanchePrise = new EscampeBoard();
        licorneBlanchePrise.play("A1/B1/C1/D1/E1/F1", "blanc");
        licorneBlanchePrise.play("A6/B6/C6/D6/E6/F6", "noir");
        licorneBlanchePrise.arbitraryplace(0, -1, -1);

        licorneNoirePrise = new EscampeBoard();
        licorneNoirePrise.play("A1/B1/C1/D1/E1/F1", "blanc");
        licorneNoirePrise.play("A6/B6/C6/D6/E6/F6", "noir");
        licorneNoirePrise.arbitraryplace(6, -1, -1);
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
    public void testCanMoveTo(){
        // mouvements simple
        Point2D to = new Point2D(0, 1);
        Assert.assertTrue(plateau.canMoveTo(0, to));

        // mouvements double
        to = new Point2D(1, 3);
        plateau.arbitraryplace(2, 0, 2);
        Assert.assertTrue(plateau.canMoveTo(2, to));
        to = new Point2D(1, 4);
        Assert.assertFalse(plateau.canMoveTo(6, to));
        to = new Point2D(2, 3);
        Assert.assertFalse(plateau.canMoveTo(6, to));

        // mouvements triple
        to = new Point2D(1, 3);
        plateau.arbitraryplace(2, 1, 2);
        Assert.assertTrue(plateau.canMoveTo(6, to));
        to = new Point2D(1, 2);
        Assert.assertFalse(plateau.canMoveTo(6, to));
        to = new Point2D(0, 3);
        Assert.assertFalse(plateau.canMoveTo(6, to));

        // collision avec paladins
        to = new Point2D(1, 5); 
        Assert.assertFalse(plateau.canMoveTo(6, to));
        to = new Point2D(1, 0);
        Assert.assertFalse(plateau.canMoveTo(0, to));

        // collision avec licornes alliées
        to = new Point2D(0, 0);
        Assert.assertFalse(plateau.canMoveTo(1, to));
        to = new Point2D(0, 5);
        Assert.assertFalse(plateau.canMoveTo(7, to));
    
        // collision avec licorne ennemie
        to = new Point2D(2, 2);
        plateau.arbitraryplace(1, 2, 2);
        plateau.arbitraryplace(7, 0, 2);
        Assert.assertTrue(plateau.canMoveTo(7, to));

        plateau.arbitraryplace(1, 0, 0);
        plateau.arbitraryplace(7, 0, 5);
        plateau.arbitraryplace(6, 2, 2);
        plateau.arbitraryplace(2, 0, 2);
        Assert.assertTrue(plateau.canMoveTo(2, to));
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

        plateau.play("A6-A3", "noir");
        Assert.assertEquals(plateau.toString(),
            "+-----------------+\n"+
            "|  |PB|PB|PB|PB|PB|\n"+
            "+-----------------+\n"+
            "|LB|  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|LN|  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |PN|PN|PN|PN|PN|\n"+
            "+-----------------+\n"
        );

        plateau.play("B1-C2", "blanc");
        Assert.assertEquals(plateau.toString(),
            "+-----------------+\n"+
            "|  |  |PB|PB|PB|PB|\n"+
            "+-----------------+\n"+
            "|LB|  |PB|  |  |  |\n"+
            "+-----------------+\n"+
            "|LN|  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |PN|PN|PN|PN|PN|\n"+
            "+-----------------+\n"
        );

        plateau.play("E6-C5", "noir");
        Assert.assertEquals(plateau.toString(),
            "+-----------------+\n"+
            "|  |  |PB|PB|PB|PB|\n"+
            "+-----------------+\n"+
            "|LB|  |PB|  |  |  |\n"+
            "+-----------------+\n"+
            "|LN|  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |PN|  |  |  |\n"+
            "+-----------------+\n"+
            "|  |PN|PN|PN|  |PN|\n"+
            "+-----------------+\n"
        );

        plateau.play("E1-E2", "blanc");
        Assert.assertEquals(plateau.toString(),
            "+-----------------+\n"+
            "|  |  |PB|PB|  |PB|\n"+
            "+-----------------+\n"+
            "|LB|  |PB|  |PB|  |\n"+
            "+-----------------+\n"+
            "|LN|  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |  |  |  |  |\n"+
            "+-----------------+\n"+
            "|  |  |PN|  |  |  |\n"+
            "+-----------------+\n"+
            "|  |PN|PN|PN|  |PN|\n"+
            "+-----------------+\n"
        );

        EscampeBoard copy = plateau.copy();
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

        EscampeBoard copy = plateau.copy();
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

    @Test
    public void testCoupsPossibles(){
        String[] rien = {"E"};
        Assert.assertArrayEquals(licorneBlanchePrise.possiblesMoves("blanc"), rien);
        Assert.assertArrayEquals(licorneBlanchePrise.possiblesMoves("noir"), rien);
        Assert.assertArrayEquals(licorneNoirePrise.possiblesMoves("blanc"), rien);
        Assert.assertArrayEquals(licorneNoirePrise.possiblesMoves("blanc"), rien);

        String[] moves = {"A1-A2", "B1-B3", "B1-A2", "B1-C2", "C1-C3", "C1-B2", "C1-D2", "D1-D4", "D1-B2", "D1-F2", "D1-C3", "D1-E3", "E1-E2", "F1-F3", "F1-E2"};
        Assert.assertTrue(sameContent(plateau.possiblesMoves("blanc"), moves));

        EscampeBoard p = new EscampeBoard();
        p.play("A5/B5/C5/D5/E5/F5", "noir");
        p.play("A1/B1/C1/D1/E1/F1", "blanc");
        EscampeBoard p1 = p.copy();
        EscampeBoard p2 = p.copy();
        EscampeBoard p3 = p.copy();

        p1.play("A1-A2", "blanc");
        String[] coups1 = {"B5-A3", "B5-D4", "B5-B2", "B5-C3", "B5-D6", "D5-B4", "D5-C3", "D5-D2", "D5-B6", "D5-F6", "D5-E3", "D5-F4"};
        List<String> coupsp1 = Arrays.stream(coups1).collect(Collectors.toList());;
        List<String> possiblesMoves = Arrays.stream(p1.possiblesMoves("noir")).collect(Collectors.toList());
        Assert.assertTrue(coupsp1.containsAll(possiblesMoves) && possiblesMoves.containsAll(coupsp1));

        p2.play("D1-C3", "blanc");
        String[] coups2 = {"A5-A6", "A5-A4", "C5-C6", "C5-C4", "E5-E6", "E5-E4"};
        List<String> coupsp2 = Arrays.stream(coups2).collect(Collectors.toList());;
        possiblesMoves = Arrays.stream(p2.possiblesMoves("noir")).collect(Collectors.toList());
        Assert.assertTrue(coupsp2.containsAll(possiblesMoves) && possiblesMoves.containsAll(coupsp2));

        p3.play("D1-F2", "blanc");
        String[] coups3 = {"F5-F3", "F5-E4", "F5-E6"};
        List<String> coupsp3 = Arrays.stream(coups3).collect(Collectors.toList());;
        possiblesMoves = Arrays.stream(p3.possiblesMoves("noir")).collect(Collectors.toList());
        Assert.assertTrue(coupsp3.containsAll(possiblesMoves) && possiblesMoves.containsAll(coupsp3));
    }

    public static boolean sameContent(String[] a1, String[] a2){
        List<String> l1 = Arrays.asList(a1);
        List<String> l2 = Arrays.asList(a2);

        for (String s: l1){
            if (!l2.contains(s)){
                return false;
            }
        }
        for (String s: l2){
            if (!l1.contains(s)){
                return false;
            }
        }

        return true;
    }

    @Test
    public void testFile(){
        licorneBlanchePrise.saveToFile("save/blancheprise.game");
        EscampeBoard p = new EscampeBoard();
        p.setFromFile("save/blancheprise.game");

        Assert.assertEquals(licorneBlanchePrise, p);

        licorneNoirePrise.saveToFile("save/noireprise.game");
        p.setFromFile("save/noireprise.game");

        Assert.assertEquals(licorneNoirePrise, p);

        plateau.saveToFile("save/default.game");
        p.setFromFile("save/default.game");

        Assert.assertEquals(plateau, p);
    }

    @Test
    public void testPlacements(){
        String[] placements = plateau.placements("blanc");
        Assert.assertEquals(placements.length, 5544);
        Assert.assertEquals(placements[0], "A1/B1/C1/D1/E1/F1");
    }

    @Test
    public void testPlacementEncode(){
        Assert.assertEquals(plateau.encodePlacement(0, 1, 2, 3, 4, 5, "blanc"), "A1/B1/C1/D1/E1/F1");
        Assert.assertEquals(plateau.encodePlacement(0, 1, 2, 3, 4, 5, "noir"), "A5/B5/C5/D5/E5/F5");
        Assert.assertEquals(plateau.encodePlacement(6, 7, 8, 9, 10, 11, "blanc"), "A2/B2/C2/D2/E2/F2");
        Assert.assertEquals(plateau.encodePlacement(6, 7, 8, 9, 10, 11, "noir"), "A6/B6/C6/D6/E6/F6");
    }

    @Test
    public void testCopy(){
        EscampeBoard pcopy = plateau.copy();
        pcopy.play("A5/B5/C5/D5/E5/F5", "noir");
        Assert.assertFalse(plateau.equals(pcopy));
        Assert.assertTrue(plateau.equals(plateau));
    }
}
