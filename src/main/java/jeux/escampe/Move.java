package jeux.escampe;

import java.util.HashMap;

public class Move{

    public int pion;
    public int dx;
    public int dy;
    public Point2D from;
    public Point2D to;

    public static final String moveRegex = CaseCoder.moveRegex;

	public Move(int pion, Point2D from, Point2D to) {
        this.pion = pion;
        this.from = from;
        this.to = to;
        Point2D d = to.sub(from);
        dx = d.x;
        dy = d.y;
    }

    public Move(String move, int[][] cases_pions){
        String fromto[] = move.split("[-]");
        if (fromto.length == 2){
            from = CaseCoder.decode(fromto[0]);
            to = CaseCoder.decode(fromto[1]);
            Point2D d = to.sub(from);
            dx = d.x;
            dy = d.y;

            // determine le pion
            pion = cases_pions[from.y][from.x];
        } else {
            throw new IllegalArgumentException("'move' argument is not of the format [A-F][1-6][-][A-F][1-6]");
        }
    }

    public static boolean matchesMove(String action){
        return action.matches(moveRegex);
    }
    
    // accessors

    public int getPion() { return pion; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }
	     
}
