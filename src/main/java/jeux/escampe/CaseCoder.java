package jeux.escampe;

import java.util.HashMap;

/**
 * Contient les tables de chiffrement et déchiffrement des cases.
 */
public class CaseCoder {

    public static final String caseRegex = "[A-F][1-6]";
    public static final String moveRegex = caseRegex + "-" + caseRegex;
    public static final String positioningRegex = caseRegex + "/" + caseRegex + "/" + caseRegex + "/" + caseRegex + "/" + caseRegex + "/" + caseRegex;

    public static HashMap<Character, Integer> decodeTable;
    static {
        decodeTable = new HashMap<Character,Integer>();
        decodeTable.put('A', 0);
        decodeTable.put('B', 1);
        decodeTable.put('C', 2);
        decodeTable.put('D', 3);
        decodeTable.put('E', 4);
        decodeTable.put('F', 5);
        decodeTable.put('1', 0);
        decodeTable.put('2', 1);
        decodeTable.put('3', 2);
        decodeTable.put('4', 3);
        decodeTable.put('5', 4);
        decodeTable.put('6', 5);
    }

    public static HashMap<Integer, Character> encodeColTable;
    static {
        encodeColTable = new HashMap<Integer, Character>();
        encodeColTable.put(0, 'A');
        encodeColTable.put(1, 'B');
        encodeColTable.put(2, 'C');
        encodeColTable.put(3, 'D');
        encodeColTable.put(4, 'E');
        encodeColTable.put(5, 'F');
    }

    public static HashMap<Integer, Character> encodeRowTable;
    static {
        encodeRowTable = new HashMap<Integer, Character>();
        encodeRowTable.put(0, '1');
        encodeRowTable.put(1, '2');
        encodeRowTable.put(2, '3');
        encodeRowTable.put(3, '4');
        encodeRowTable.put(4, '5');
        encodeRowTable.put(5, '6');
    }

    /**
     * Retourne la case correspondante au code envoyé.
     * Exemple: A5 renvoie (0, 4).
     * @param codedCase     La case à décoder
     * @return Point2D      contenant les (x, y) décodés
     */
    public static Point2D decode(String codedCase){
        if (!codedCase.matches(caseRegex)){
            throw new IllegalArgumentException("'codedCase' must be of the format " + caseRegex);
        }
        return new Point2D(decodeTable.get(codedCase.charAt(0)), decodeTable.get(codedCase.charAt(1)));
    }

    public static String encode(int x, int y){
        if (x < 0 || x > 5 || y < 0 || y > 5){
            throw new IllegalArgumentException("x, y must be in [0:5]");
        } else {
            return "" + encodeColTable.get(x) + encodeRowTable.get(y);
        }
    }
}