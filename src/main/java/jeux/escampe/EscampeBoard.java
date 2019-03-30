package jeux.escampe;

import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;

import jeux.modele.Partiel;

import jeux.escampe.exception.IllegalMove;

public class EscampeBoard implements Partiel {

    public static String jBlanc = "Blanc";
    public static String jNoir = "Noir";

    private int pions[];
    private final int width = 6;
    private final int height = 6;
    private final int nb_pions = 12;
    private int cases_pions[][] = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };

    private static final int licorne_blanche = 0;
    private static final int licorne_noire = 5;
    private static final int case_libre = 0;

    public static Lisere SIMPLE = Lisere.SIMPLE;
    public static Lisere DOUBLE = Lisere.DOUBLE;
    public static Lisere TRIPLE = Lisere.TRIPLE;
    private static Lisere cases_liseres[][] = { { SIMPLE, DOUBLE, DOUBLE, TRIPLE, SIMPLE, DOUBLE },
            { TRIPLE, SIMPLE, TRIPLE, SIMPLE, TRIPLE, DOUBLE }, { DOUBLE, TRIPLE, SIMPLE, DOUBLE, SIMPLE, TRIPLE },
            { DOUBLE, SIMPLE, TRIPLE, DOUBLE, TRIPLE, SIMPLE }, { SIMPLE, TRIPLE, SIMPLE, TRIPLE, SIMPLE, DOUBLE },
            { TRIPLE, DOUBLE, DOUBLE, SIMPLE, TRIPLE, DOUBLE } };

    public EscampeBoard() {
        pions = new int[24];
        Point2D pos;
        for (int i = 0; i < nb_pions; i++) {
            if (i < 6) {
                pions[i * 2] = 0;
                pions[i * 2 + 1] = i;
            } else {
                pions[i * 2] = 5;
                pions[i * 2 + 1] = i - 6;
            }
            pos = getPionPos(i);
            cases_pions[pos.y][pos.x] = i;
        }
    }

    public EscampeBoard(List<Point2D> positions) {
        if (positions == null) {
            throw new NullPointerException("La liste des positions ne doit pas être nulle");
        }
        if (positions.size() != nb_pions) {
            throw new IllegalArgumentException("La liste des positions doit contenir 12 positions.");
        }
        for (int i = 0; i < positions.size() - 1; i++) {
            Point2D current = positions.get(i);
            for (int j = i + 1; j < positions.size(); j++) {
                if (current.equals(positions.get(j))) {
                    throw new IllegalArgumentException(
                            "La liste des positions ne doit pas comporter de doublons. Deux pièces ne peuvent pas se chevaucher.");
                }
            }
        }

        pions = new int[nb_pions * 2];
        int pion = 0;
        for (Point2D pos : positions) {
            pions[pion * 2] = pos.y;
            pions[pion * 2 + 1] = pos.x;
            if (pos.x != -1 && pos.y != -1)
                cases_pions[pos.y][pos.x] = pion;
            pion++;
        }
    }

    @Override
    public boolean isValidMove(String move, String player) {
        switch(whatActionType(move)){
            case Move:
                Move mv = interpretMove(move);
                return isValidMove(mv, player);
            case Positioning:
                Positioning pos = interpretPositioning(move);
                return isValidMove(pos, player);
            case Skip:
            default:
                return false;
        }
    }

    public boolean isValidMove(Move move, String player) {
        Lisere lfrom = cases_liseres[move.from.x][move.from.x];
        boolean isjb = isJoueurBlanc(player);
        boolean isjn = isJoueurNoir(player);
        if (isjb) {
            if (move.pion > 6) {
                return false;
            }
        } else if (isjn) {
            if (move.pion < 7) {
                return false;
            }
        } else {
            throw new IllegalArgumentException("'player' (" + player + ") n'est pas une valeur valide.");
        }

        int dx = move.dx;
        int dy = move.dy;
        switch (lfrom) {
            case SIMPLE:
                return canMoveTo(move.pion, move.from, move.to);
            case DOUBLE:
                // test
                throw new UnsupportedOperationException("Cas du liseré double non implémenté");
            case TRIPLE:
            default:
                throw new UnsupportedOperationException("Cas du liseré triple non implémenté");
        }
    }

    public boolean isValidMove(Positioning pos, String player) {
        int i;
        boolean taken[] = new boolean[36];

        if (isJoueurBlanc(player)){
            for (Point2D p: pos.positions){
                i = p.y * width + p.x;
                if (p.y > -1 && p.y < 2 && !taken[i])
                    taken[i] = true;
                else
                    return false;
            }
            return true;
        } else {
            for (Point2D p: pos.positions){
                i = p.y * width + p.x;
                if (p.y > 3 && p.y < 6 && !taken[i])
                    taken[i] = true;
                else
                    return false;
            }
            return true;
        }
    }

    @Override
    public String[] possiblesMoves(String player) {
        throw new UnsupportedOperationException("Non implémenté");
    }

    @Override
    public void play(String move, String player) {
        try {
            switch(whatActionType(move)){
                case Move:
                    Move mv = interpretMove(move);
                    if (isValidMove(mv, player)){
                        move(mv);
                    } else {
                        throw new IllegalMove("'move' " + move + " is invalid.");
                    }
                    break;
                case Positioning:
                    Positioning pos = interpretPositioning(move);
                    if (isValidMove(pos, player)){
                        position(pos);
                    } else {
                        throw new IllegalMove("'move' " + move + " is invalid.");
                    }
                    break;
                case Skip:
                    // do nothing
                    break;
                default:
                    throw new IllegalArgumentException("'move' " + move + " doesn't match with any known move type.");
            }
        } catch (IllegalMove e){
            System.err.println("Got IllegalMove error : " + e);
        }
    }

    public void move(Move mv){
        cases_pions[mv.from.y][mv.from.x] = 0;
        place(mv.pion, mv.to.x, mv.to.y);
    }

    public void position(Positioning pos){
        Point2D p;
        // Vider les champs du joueur
        if (pos.positions.get(0).y == 0 || pos.positions.get(0).y == 1) {
             for (int j = 0; j < 2; j++)
                for (int i = 0; i < width; i++)
                    cases_pions[j][i] = 0;
        } else {
            for (int j = 4; j < 6; j++)
                for (int i = 0; i < width; i++)
                    cases_pions[j][i] = 0;
        }
        for (int i = 0; i < pos.positions.size(); i++){
            p = pos.positions.get(i);
            place(i, p.x, p.y);
        }
    }

    public void place(int pion, int x, int y){
        pions[pion*2]       = y;
        pions[pion*2+1]     = x;
        cases_pions[y][x]   = pion;
    }

    public TypeMove whatActionType(String action){
        if (Move.matchesMove(action)){
            return TypeMove.Move;
        }
        else if (Positioning.matchesPositioning(action)){
            return TypeMove.Positioning;
        } 
        else {
            return TypeMove.Skip;
        }
    }

    public Move interpretMove(String move){
        return new Move(move);
    }

    public Positioning interpretPositioning(String positioning){
        return new Positioning(positioning);
    }

    @Override
    public boolean gameOver() {
        return pions[0] == -1 && pions[1] == -1 || pions[12] == -1 && pions[13] == -1;
    }

    public EscampeBoard(EscampeBoard other) {
        pions = other.pions.clone();
        cases_pions = other.cases_pions.clone();
    }

    public PlateauJeu copy() {
        throw new UnsupportedOperationException();
        return new EscampeBoard();
    }

    public boolean canMoveTo(int pion, Point2D from, Point2D to){
        Point2D d = to.sub(from);
        int dx = d.x;
        int dy = d.y;
        boolean pion_blanc = pion < 6;
        boolean pion_noir = !pion_blanc;
        if ((dx == 0 && dy == -1) || (dx == 0 && dy == 1) || (dx == 1 && dy == 0) || (dx == -1 && dy == 0)) {
            // Verification de collision
            int case_cible = cases_pions[to.y][to.x];
            if (pion_blanc && case_cible != case_libre) {
                return (case_cible == licorne_noire);
            } else if (pion_noir && case_cible != case_libre) {
                return (case_cible == licorne_blanche);
            } else {
                // case libre
                return true;
            }
        } else {
            // Mouvement impossible
            return false;
        }
    }

    public Point2D getPionPos(int index) {
        return new Point2D(pions[index * 2 + 1], pions[index * 2]);
    }

    public boolean isJoueurBlanc(String joueur){
        return "blanc".equals(joueur);
    }

    public boolean isJoueurNoir(String joueur){
        return "noir".equals(joueur);
    }

    public String toString() {
        String[] p = new String[36];

        for (int i = 0; i < nb_pions; i++) {
            Point2D pos = getPionPos(i);

            if (i == 1) {
                p[pos.y * width + pos.x] = "LB";
            } else if (i == 6) {
                p[pos.y * width + pos.x] = "LN";
            } else if (i < 6) {
                p[pos.y * width + pos.x] = "PB";
            } else {
                p[pos.y * width + pos.x] = "PN";
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("+-----------------+\n");
        for (int j = 0; j < height; j++) {
            sb.append("|");
            for (int i = 0; i < width; i++) {
                String c = p[j * width + i];
                sb.append((c == null ? "  " : c) + "|");
            }
            sb.append("+-----------------+\n");
        }
        return sb.toString();
    }

    @Override
    public void setFromFile(String filename) {
        throw new UnsupportedOperationException("Non implémenté");
    }

    @Override
    public void saveToFile(String filename) {
        throw new UnsupportedOperationException("Non implémenté");
    }
}