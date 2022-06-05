package pl.szinton.gk.math;

public class Vector2f {

    private final Float x;
    private final Float y;

    public Vector2f(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f other) {
        return new Vector2f(this.x + other.x, this.y + other.y);
    }

    public Vector2f subtract(Vector2f other) {
        return new Vector2f(this.x - other.x, this.y - other.y);
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }
}
