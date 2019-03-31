package jeux.escampe;

import java.util.HashMap;
import java.util.ArrayList;

public class Positioning {

    public ArrayList<Point2D> positions = new ArrayList<Point2D>(6);

    public static final String positioningRegex = CaseCoder.positioningRegex;

    public static HashMap<Character, Integer> decodeTable = CaseCoder.decodeTable;

    public Positioning(Point2D licorne, Point2D p1, Point2D p2, Point2D p3, Point2D p4, Point2D p5){
        positions = new ArrayList<Point2D>(6);
        positions.add(licorne);
        positions.add(p1);
        positions.add(p2);
        positions.add(p3);
        positions.add(p4);
        positions.add(p5);
    }

    public Positioning(String positioning){
        String pos[] = positioning.split("/");
        if (pos.length == 6){
            for(String p: pos){
                positions.add(CaseCoder.decode(p));
            }
        } else {
            throw new IllegalArgumentException("'positioning' must be of the format [A-F][1-6]/[A-F][1-6]/[A-F][1-6]/[A-F][1-6]/[A-F][1-6]/[A-F][1-6]");
        }
    }

    public static boolean matchesPositioning(String action){
        return action.matches(positioningRegex);
    }

}