import java.awt.image.BufferedImage;

public class Sprite {
    private double x;
    private double y;
    private double width;
    private double height;

    public Sprite(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 0.2;
        this.height = 0.7;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
