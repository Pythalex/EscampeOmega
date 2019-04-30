package jeux.escampe;

import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.StringBuilder;

import jeux.modele.PlateauClonable;
import jeux.escampe.exception.IllegalMove;

import java.util.ArrayList;
import java.util.Arrays;

public class EscampeBoard implements PlateauClonable {

    // joueurs
    public static String jBlanc = "blanc";
    public static String jNoir = "noir";

    // plateau
    public int pions[];
    public static final int width = 6;
    public static final int height = 6;
    public static final int nb_pions = 12;
    public int cases_pions[][] = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };

    // constantes de pions
    public static final int licorne_blanche = 0;
    public static final int licorne_noire = 6;
    public static final int case_libre = -1;

    // liserés
    public static Lisere SIMPLE = Lisere.SIMPLE;
    public static Lisere DOUBLE = Lisere.DOUBLE;
    public static Lisere TRIPLE = Lisere.TRIPLE;
    public static Lisere cases_liseres[][] = { { SIMPLE, DOUBLE, DOUBLE, TRIPLE, SIMPLE, DOUBLE },
            { TRIPLE, SIMPLE, TRIPLE, SIMPLE, TRIPLE, DOUBLE }, { DOUBLE, TRIPLE, SIMPLE, DOUBLE, SIMPLE, TRIPLE },
            { DOUBLE, SIMPLE, TRIPLE, DOUBLE, TRIPLE, SIMPLE }, { SIMPLE, TRIPLE, SIMPLE, TRIPLE, SIMPLE, DOUBLE },
            { TRIPLE, DOUBLE, DOUBLE, SIMPLE, TRIPLE, DOUBLE } };

    // souvenir du dernier coup
    public Lisere derniereaction = null;

    // est-ce que le placement a déjà été fait
    public boolean placementfait = false;
    public boolean placementBlanc = false;
    public boolean placementNoir = false;

    public EscampeBoard() {

        pions = new int[24];

        for (int[] line: cases_pions)
            Arrays.fill(line, case_libre);

        Arrays.fill(pions, -1);
    }

    public EscampeBoard(EscampeBoard other) {
        this.pions = other.pions.clone();

        for (int i = 0; i < cases_pions.length; i++){
            this.cases_pions[i] = other.cases_pions[i].clone();
        }
        
        this.placementBlanc = other.placementBlanc;
        this.placementNoir = other.placementNoir;
        this.placementfait = other.placementfait;
        this.derniereaction = other.derniereaction;
    }

    // public EscampeBoard(List<Point2D> positions) {
    //     if (positions == null) {
    //         throw new NullPointerException("La liste des positions ne doit pas être nulle");
    //     }
    //     if (positions.size() != nb_pions) {
    //         throw new IllegalArgumentException("La liste des positions doit contenir 12 positions.");
    //     }
    //     for (int i = 0; i < positions.size() - 1; i++) {
    //         Point2D current = positions.get(i);
    //         for (int j = i + 1; j < positions.size(); j++) {
    //             if (current.equals(positions.get(j))) {
    //                 throw new IllegalArgumentException(
    //                         "La liste des positions ne doit pas comporter de doublons. Deux pièces ne peuvent pas se chevaucher.");
    //             }
    //         }
    //     }

    //     for (int[] line: cases_pions)
    //         Arrays.fill(line, -1);

    //     pions = new int[nb_pions * 2];
    //     int pion = 0;
    //     for (Point2D pos : positions) {
    //         pions[pion * 2] = pos.y;
    //         pions[pion * 2 + 1] = pos.x;
    //         if (pos.x != -1 && pos.y != -1)
    //             cases_pions[pos.y][pos.x] = pion;
    //         pion++;
    //     }
    // }

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

        // Si le joueur tente de bouger un pion ennemi
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
                int distance = Math.abs(d.x) + Math.abs(d.y);
                if (distance != 3 && distance != 1){
                    return false;
                }
                break;
        }

        return canMoveTo(move.pion, move.to);
    }

    // TODO y'a un bug ici
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

    // TODO : possiblesmoves ne renvoie pas tous les coups possibles
    @Override
    public String[] possiblesMoves(String player) {

        if (gameOver()) {
            String[] res = {"E"};
            System.out.println("Je suis gameover " + player);
            return res;
        }

        if (!placementfait){
            return placements(player);
        }

        boolean isjb = isJoueurBlanc(player);
        ArrayList<String> res = new ArrayList<>();
        String move;
        for (int i = licorne_blanche; i < 12; i++){
            int y = pions[i*2];
            int x = pions[i*2+1];
            Lisere from = cases_liseres[y][x];
            if ((i < licorne_noire && isjb || i >= licorne_noire) && (from == derniereaction || derniereaction == null)){
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

    public String[] placements(String joueur){
        String res[] = new String[5544]; // (6 parmi 12) * 6

        int c = 0;
        // pour chaque choix de 6 parmi 12 cases
        for (int l = 0; l < 12; l++){ // licorne
            for (int p1 = 0; p1 < 12; p1++){ // paladin 1
                if (p1 != l){
                    for (int p2 = p1 + 1; p2 < 12; p2++){ // paladin 2
                        if (p2 != l && p2 != p1){
                            for (int p3 = p2 + 1; p3 < 12; p3++){ // paladin 3
                                if (p3 != l && p3 != p1 && p3 != p2){
                                    for (int p4 = p3 + 1; p4 < 12; p4++){ // paladin 4
                                        if (p4 != l && p4 != p1 && p4 != p2 && p4 != p3){
                                            for (int p5 = p4 + 1; p5 < 12; p5++){ // paladin 5
                                                if (p5 != l && p5 != p1 && p5 != p2 && p5 != p3 && p5 != p4){
                                                    res[c] = encodePlacement(l, p1, p2, p3, p4, p5, joueur);
                                                    c++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return res;
    }

    public String encodePlacement(int l, int p1, int p2, int p3, int p4, int p5, String joueur){
        int base = isJoueurBlanc(joueur) ? 0 : 4;
        return  CaseCoder.encode( l % width, base +  l / width) + "/" +
                CaseCoder.encode(p1 % width, base + p1 / width) + "/" +
                CaseCoder.encode(p2 % width, base + p2 / width) + "/" +
                CaseCoder.encode(p3 % width, base + p3 / width) + "/" +
                CaseCoder.encode(p4 % width, base + p4 / width) + "/" +
                CaseCoder.encode(p5 % width, base + p5 / width);
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
                        System.out.println("Dernière action : " + derniereaction);
                    } else {
                        throw new IllegalMove("'move' " + move + " is invalid.");
                    }
                    break;
                case Positioning:
                    Positioning pos = interpretPositioning(move);
                    if (isValidMove(pos, player)){
                        if (isJoueurBlanc(player))
                            placementBlanc = true;
                        else
                            placementNoir = true;
                        if (placementBlanc && placementNoir)
                            placementfait = true;

                        position(pos, player);
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
            // System.err.println("Got IllegalMove error : " + e);
            // System.err.println(this);
            // System.err.println(player);
            // Scanner sc = new Scanner(System.in);
            // sc.nextLine();
            // sc.close();
            // System.err.println("ok");
        }
    }

    public void move(Move mv){
        // Si une licorne est prise il faut la retirer du terrain
        int case_cible = cases_pions[mv.to.y][mv.to.x];
        if (case_cible != case_libre){
            pions[case_cible*2] = -1;
            pions[case_cible*2+1] = -1;
        }

        cases_pions[mv.from.y][mv.from.x] = case_libre;
        place(mv.pion, mv.to.x, mv.to.y);
    }

    public void position(Positioning pos, String player){
        int base = isJoueurBlanc(player) ? licorne_blanche : licorne_noire;
        for (int i = 0; i < pos.positions.size(); i++){
            place(i + base, pos.positions.get(i).x, pos.positions.get(i).y);
        }
    }

    public void place(int pion, int x, int y){
        pions[pion*2]       = y;
        pions[pion*2+1]     = x;
        cases_pions[y][x]   = pion;
    }

    public void arbitraryplace(int pion, int x, int y){
        cases_pions[pions[pion*2]][pions[pion*2+1]] = case_libre;
        pions[pion*2]   = y;
        pions[pion*2+1] = x;
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
        return placementfait && (pions[0] == -1 && pions[1] == -1 || pions[12] == -1 && pions[13] == -1);
    }

    @Override
    public EscampeBoard copy() {
        return new EscampeBoard(this);
    }

    public boolean canMoveTo(int pion, Point2D to){

        Point2D from = new Point2D(pions[pion*2+1], pions[pion*2]);

        if (from.x < 0 || from.x >= width || from.y < 0 || from.y >= height ||
            to.x < 0 || to.x >= width || to.y < 0 || to.y >= height)
            return false;

        ArrayList<Point2D> walkedon = new ArrayList<>(3);

        boolean jb = pion < licorne_noire;

        Lisere lfrom = cases_liseres[from.y][from.x];
        switch (lfrom){
            case SIMPLE:
                return find(pion, from, to, walkedon, 1, jb);
            case DOUBLE:
                return find(pion, from, to, walkedon, 2, jb);
            case TRIPLE:
            default:
                return find(pion, from, to, walkedon, 3, jb);
        }

        // Point2D d = to.sub(from);

        // boolean found = false;
        // if (d.x > 0 && !found){
        //     found = canMoveTo(pion, new Point2D(from.x + 1, from.y), to) && canMoveTo(pion, from.x, from.y, 1, 0);
        // }
        // else if (d.x < 0 && !found){
        //     found = canMoveTo(pion, new Point2D(from.x - 1, from.y), to) && canMoveTo(pion, from.x, from.y, -1, 0);
        // }
        // if (d.y > 0 && !found){
        //     found = canMoveTo(pion, new Point2D(from.x, from.y + 1), to) && canMoveTo(pion, from.x, from.y, 0, 1);
        // }
        // else if (d.y < 0 && !found){
        //     found = canMoveTo(pion, new Point2D(from.x, from.y - 1), to) && canMoveTo(pion, from.x, from.y, 0, -1);
        // }
        // return found;
    }

    public boolean find(int pion, Point2D from, Point2D to, List<Point2D> walkedon, int count, boolean jb){

        walkedon.add(from);
        if (count == 0 && from.equals(to)){
            if (cases_pions[from.y][from.x] != case_libre){
                if (cases_pions[from.y][from.x] == licorne_noire && jb) {
                    return true;
                }
                else if (cases_pions[from.y][from.x] == licorne_blanche && !jb) {
                    return true;
                }
                else {
                    return false;
                }
            } else {
                return true;
            }
        }
        else if (from.manhattan_distance(to) > count){
            return false;
        }
        else {

            // cas == pion -> on a pas encore bougé
            if (cases_pions[from.y][from.x] != case_libre && cases_pions[from.y][from.x] != pion){
                return false;
            }

            Point2D nexthop = new Point2D(from.x + 1, from.y);
            if (from.x + 1 < width && !walkedon.contains(nexthop)){
                if (find(pion, nexthop, to, walkedon, count - 1, jb))
                    return true;
            }
            nexthop = new Point2D(from.x - 1, from.y);
            if (from.x - 1 >= 0 && !walkedon.contains(nexthop)){
                if (find(pion, nexthop, to, walkedon, count - 1, jb))
                    return true;
            }
            nexthop = new Point2D(from.x, from.y + 1);
            if (from.y + 1 < height && !walkedon.contains(nexthop)){
                if (find(pion, nexthop, to, walkedon, count - 1, jb))
                    return true;
            }
            nexthop = new Point2D(from.x, from.y - 1);
            if (from.y - 1 >= 0 && !walkedon.contains(nexthop)){
                if (find(pion, nexthop, to, walkedon, count - 1, jb))
                    return true;
            }
            return false;
        }
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
        return jBlanc.equals(joueur);
    }

    public boolean isJoueurNoir(String joueur){
        return jNoir.equals(joueur);
    }

    public String toString() {
        String[] p = new String[36];

        for (int i = 0; i < nb_pions; i++) {
            Point2D pos = getPionPos(i);
            
            // Ne pas tenter d'afficher un pion hors du terrain
            if (pos.y != -1 && pos.x != -1) {
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