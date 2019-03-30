package jeux.escampe.exception;

public class IllegalMove extends Exception {

    private static final long serialVersionUID = 432175073303765139L;

    public IllegalMove(String msg) {
        super(msg);
    }

}