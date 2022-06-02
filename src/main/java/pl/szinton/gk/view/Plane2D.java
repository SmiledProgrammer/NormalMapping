package pl.szinton.gk.view;

import pl.szinton.gk.math.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Plane2D {

    private final List<Vector3f> vertices;
    private final List<Integer> verticesOrder;

    public Plane2D(List<Vector3f> vertices, List<Integer> verticesOrder) {
        this.vertices = new ArrayList<>(vertices);
        this.verticesOrder = new ArrayList<>(verticesOrder);
    }

    public Vector3f normalVector() {
        Vector3f p1 = vertices.get(0);
        Vector3f p2 = vertices.get(1);
        Vector3f p3 = vertices.get(2);
        Vector3f u = new Vector3f(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
        Vector3f v = new Vector3f(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
        return Vector3f.crossProduct(u, v);
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Integer> getVerticesOrder() {
        return verticesOrder;
    }
}
