package jeux.escampe;

public class Point2D {

    public int x;
    public int y;

    public Point2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object other){
        if (other == null){
            return false;
        }
        else if (other == this){
            return true;
        }
        else if (other instanceof Point2D){
            Point2D o = (Point2D) other;
            return this.x == o.x && this.y == o.y;
        }
        return false;
    }

}