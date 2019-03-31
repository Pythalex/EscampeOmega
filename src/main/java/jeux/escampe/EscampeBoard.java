package jeux.escampe;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.StringBuilder;

import jeux.modele.Partiel;

import jeux.escampe.exception.IllegalMove;

import java.util.ArrayList;
import java.util.Arrays;

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
    private static final int licorne_noire = 6;
    private static final int case_libre = -1;

    public static Lisere SIMPLE = Lisere.SIMPLE;
    public static Lisere DOUBLE = Lisere.DOUBLE;
    public static Lisere TRIPLE = Lisere.TRIPLE;
    private static Lisere cases_liseres[][] = { { SIMPLE, DOUBLE, DOUBLE, TRIPLE, SIMPLE, DOUBLE },
            { TRIPLE, SIMPLE, TRIPLE, SIMPLE, TRIPLE, DOUBLE }, { DOUBLE, TRIPLE, SIMPLE, DOUBLE, SIMPLE, TRIPLE },
            { DOUBLE, SIMPLE, TRIPLE, DOUBLE, TRIPLE, SIMPLE }, { SIMPLE, TRIPLE, SIMPLE, TRIPLE, SIMPLE, DOUBLE },
            { TRIPLE, DOUBLE, DOUBLE, SIMPLE, TRIPLE, DOUBLE } };

    private Lisere derniereaction = null;

    public EscampeBoard() {

        for (int[] line: cases_pions)
            Arrays.fill(line, -1);

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

        for (int[] line: cases_pions)
            Arrays.fill(line, -1);

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

        if (move.pion == -1){
            return false;
        }

        boolean isjb = isJoueurBlanc(player);
        boolean isjn = isJoueurNoir(player);

        if (isjb) {
            if (move.pion > 5) {
                return false;
            }
        } else if (isjn) {
            if (move.pion < 6) {
                return false;
            }
        } else {
            throw new IllegalArgumentException("'player' (" + player + ") n'est pas une valeur valide.");
        }

        // test liseré
        Lisere lfrom = cases_liseres[move.from.y][move.from.x];
        Point2D d = move.to.sub(move.from);

        switch (lfrom){
            case SIMPLE:
                if (Math.abs(d.x) + Math.abs(d.y) != 1){
                    return false;
                }
                break;
            case DOUBLE:
                if (Math.abs(d.x) + Math.abs(d.y) != 2){
                    return false;
                }
                break;
            case TRIPLE:
            default:
                if (Math.abs(d.x) + Math.abs(d.y) != 3){
                    return false;
                }
                break;
        }

        return canMoveTo(move.pion, move.from, move.to);
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

        if (gameOver()) {
            String[] res = {"E"};
            return res;
        }

        boolean isjb = isJoueurBlanc(player);
        ArrayList<String> res = new ArrayList<>();
        String move;
        for (int i = licorne_blanche; i < licorne_noire; i++){
            int y = pions[i*2];
            int x = pions[i*2+1];
            if ((i < licorne_noire && isjb || i >= licorne_noire) && cases_liseres[y][x] == derniereaction || derniereaction == null){
                Lisere from = cases_liseres[y][x];
                String base = CaseCoder.encode(x, y) + "-";
                switch (from){
                    case SIMPLE:
                        try {
                            move = base + CaseCoder.encode(x + 1, y);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 1, y);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x, y + 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x, y - 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        break;
                    case DOUBLE:
                        try {
                            move = base + CaseCoder.encode(x + 2, y);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 2, y);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x, y + 2);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x, y - 2);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x + 1, y + 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 1, y - 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 1, y + 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x + 1, y - 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        break;
                    case TRIPLE:
                        try {
                            move = base + CaseCoder.encode(x, y - 3);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x + 1, y - 2);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x + 2, y - 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x + 3, y);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x + 2, y + 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x + 1, y + 2);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x, y + 3);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 1, y + 2);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 2, y + 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 3, y);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 2, y - 1);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        try {
                            move = base + CaseCoder.encode(x - 1, y - 2);
                            if (isValidMove(move, player)) res.add(move);
                        } catch (IllegalArgumentException e){}
                        break;
                }
            }   
        }

        if (res.size() == 0)
            res.add("E");

        String [] output = new String[res.size()];
        for (int i = 0; i < output.length; i++){
            output[i] = res.get(i);
        }

        return output;
    }

    @Override
    public void play(String move, String player) {
        try {
            switch(whatActionType(move)){
                case Move:
                    Move mv = interpretMove(move);
                    if (isValidMove(mv, player)){
                        move(mv);
                        derniereaction = cases_liseres[mv.to.y][mv.to.x];
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
                    derniereaction = null;
                    break;
                default:
                    throw new IllegalArgumentException("'move' " + move + " doesn't match with any known move type.");
            }
        } catch (IllegalMove e){
            System.err.println("Got IllegalMove error : " + e);
        }
    }

    public void move(Move mv){
        cases_pions[mv.from.y][mv.from.x] = case_libre;
        place(mv.pion, mv.to.x, mv.to.y);
    }

    public void position(Positioning pos){
        Point2D p;
        // Vider les champs du joueur
        if (pos.positions.get(0).y == 0 || pos.positions.get(0).y == 1) {
             for (int j = 0; j < 2; j++)
                for (int i = 0; i < width; i++)
                    cases_pions[j][i] = case_libre;
        } else {
            for (int j = 4; j < 6; j++)
                for (int i = 0; i < width; i++)
                    cases_pions[j][i] = case_libre;
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
        return new Move(move, cases_pions);
    }

    public Positioning interpretPositioning(String positioning){
        return new Positioning(positioning);
    }

    @Override
    public boolean gameOver() {
        return pions[0] == -1 && pions[1] == -1 || pions[12] == -1 && pions[13] == -1;
    }

    public EscampeBoard(EscampeBoard other) {
        this.pions = other.pions.clone();
        this.cases_pions = other.cases_pions.clone();
    }

    @Override
    public EscampeBoard clone() {
        return new EscampeBoard(this);
    }

    public boolean canMoveTo(int pion, Point2D from, Point2D to){

        if (from.equals(to)){
            return true;
        }

        Point2D d = to.sub(from);

        boolean found = false;
        if (d.x > 0 && !found){
            found = canMoveTo(pion, new Point2D(from.x + 1, from.y), to) && canMoveTo(pion, from.x, from.y, 1, 0);
        }
        else if (d.x < 0 && !found){
            found = canMoveTo(pion, new Point2D(from.x - 1, from.y), to) && canMoveTo(pion, from.x, from.y, -1, 0);
        }
        if (d.y > 0 && !found){
            found = canMoveTo(pion, new Point2D(from.x, from.y + 1), to) && canMoveTo(pion, from.x, from.y, 0, 1);
        }
        else if (d.y < 0 && !found){
            found = canMoveTo(pion, new Point2D(from.x, from.y - 1), to) && canMoveTo(pion, from.x, from.y, 0, -1);
        }
        return found;
    }

    public boolean canMoveTo(int pion, int x, int y, int dx, int dy){
        int tox = x+dx;
        int toy = y+dy;

        boolean pion_blanc = pion < 6;
        boolean pion_noir = !pion_blanc;
        if ((dx == 0 && dy == -1) || (dx == 0 && dy == 1) || (dx == 1 && dy == 0) || (dx == -1 && dy == 0)) {
            // Verification de collision
            int case_cible = cases_pions[toy][tox];
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

            if (i == 0) {
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
            sb.append("\n+-----------------+\n");
        }
        return sb.toString();
    }

    @Override
    public void setFromFile(String filename) {
        File f = new File(filename);
        String lines[] = new String[6];
        int i = 0;
        
        String formaterror = "Le fichier de sauvegarde " + filename + " n'est pas bien formaté ou est corrompu. ";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = reader.readLine()) != null){
                if (line.matches("[%].*")){
                    // skip comment
                } else {
                    line = line.replaceAll("\\s?[0-6][0-6]\\s?", "");
                    if (line.matches("[bBnN-][bBnN-][bBnN-][bBnN-][bBnN-][bBnN-]")){
                        try {
                            lines[i] = line;
                        } catch (IndexOutOfBoundsException e){
                            reader.close();
                            System.err.println(formaterror + "(Ligne trop longue)");
                            return;
                        }
                    } else {
                        reader.close();
                        System.err.println(formaterror);
                        return;
                    }
                    i++;
                }
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        int pb = 1;
        int pn = 7;
        boolean lb = false;
        boolean ln = false;

        // On met les licornes hors du terrain par defaut car elles ne sont pas représentées dans le fichier
        pions[licorne_blanche * 2] = -1;
        pions[licorne_blanche * 2 + 1] = -1;
        pions[licorne_noire * 2] = -1;
        pions[licorne_noire * 2 + 1] = -1;

        for (int j = 0; j < height; j++){
            for (i = 0; i < width; i++){
                switch (lines[j].charAt(i)){
                    case 'B':
                        if (!lb) {
                            cases_pions[j][i] = licorne_blanche;
                            pions[licorne_blanche * 2] = j;
                            pions[licorne_blanche * 2 + 1] = i;
                            lb = true;
                        } else {
                            System.err.println(formaterror + "(Plusieurs licornes blanches)");
                            return;
                        }
                        break;
                    case 'b':
                        if (pb < 6){
                            cases_pions[j][i] = pb;
                            pions[pb * 2] = j;
                            pions[pb * 2 + 1] = i;
                            pb++;
                        } else {
                            System.err.println(formaterror + "(Trop de paladins blancs)");
                            return;
                        }
                        break;
                    case 'N':
                        if (!ln) {
                            cases_pions[j][i] = licorne_noire;
                            pions[licorne_noire * 2] = j;
                            pions[licorne_noire * 2 + 1] = i;
                            ln = true;
                        } else {
                            System.err.println(formaterror + "(Plusieurs licornes noires)");
                            return;
                        }
                        break;
                    case 'n':
                        if (pn < 12){
                            cases_pions[j][i] = pn;
                            pions[pn * 2] = j;
                            pions[pn * 2 + 1] = i;
                            pn++;
                        } else {
                            System.err.println(formaterror + "(Trop de paladins noirs)");
                            return;
                        }
                        break;
                    case '-':
                    default:
                        cases_pions[j][i] = case_libre;
                }
            }
        }

        // on vérifie qu'il y a assez de paladins
        if (pb < 6){
            System.err.println(formaterror + "(Pas assez de paladins blancs)");
            return;
        } else if (pn < 12){
            System.err.println(formaterror + "(Pas assez de paladins noirs)");
            return;
        }
        System.err.println("Plateau chargé depuis " + filename);
    }

    @Override
    public void saveToFile(String filename) {
        File of = new File(filename);
        int pion;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(of));
            
            // write file
            writer.write("%  ABCDEF   \n");
            for (int j = 0; j < height; j++){
                writer.write("0" + (j+1) + " ");
                for (int i = 0; i < width; i++){
                    pion = cases_pions[j][i];
                    if (pion == -1){
                        writer.write("-");
                    } else if (pion == licorne_blanche){
                        writer.write("B");
                    } else if (pion < licorne_noire){
                        writer.write("b");
                    } else if (pion == licorne_noire){
                        writer.write("N");
                    } else {
                        writer.write("n");
                    }
                }
                writer.write(" 0" + (j+1) + "\n");
            }
            writer.write("%  ABCDEF   ");

            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof EscampeBoard){
            EscampeBoard o = (EscampeBoard) other;
            if (Arrays.equals(pions, o.pions)){
                for (int i = 0; i < cases_pions.length; i++){
                    if (!Arrays.equals(cases_pions[i], o.cases_pions[i])){
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }   
        } else {
            return false;
        }
    }

    public static void main(String[] args){
        // voir PlateauEscampeTest.java
        System.err.println("voir PlateauEscampeTest.java");
    }
}