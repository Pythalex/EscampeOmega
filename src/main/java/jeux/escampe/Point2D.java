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

    public Point2D translate(int dx, int dy){
        return new Point2D(x + dx, y + dy);
    }

    public Point2D add(Point2D other){
        return new Point2D(this.x + other.x, this.y + other.y);
    }

    public Point2D sub(Point2D other){
        return new Point2D(this.x - other.x, this.y - other.y);
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

    public int manhattan_distance(Point2D other){
        int dx = 0;
        int dy = 0;
        while (other.x > this.x + dx)
            dx++;
        while (other.x < this.x + dx)
            dx--;
        while (other.y > this.y + dy)
            dy++;
        while (other.y < this.y + dy)
            dy--;
        return dx + dy;
    }

    @Override
    public Point2D clone(){
        return new Point2D(x, y);
    }

}