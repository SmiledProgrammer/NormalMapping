package pl.szinton.gk.view;

import pl.szinton.gk.math.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Plane {

    private final List<Vector3f> vertices2D;
    private final List<Vector3f> vertices3D;
    private final List<Integer> verticesOrder;

    public Plane(List<Vector3f> vertices2D, List<Vector3f> vertices3D, List<Integer> verticesOrder) {
        this.vertices2D = new ArrayList<>(vertices2D);
        this.vertices3D = new ArrayList<>(vertices3D);
        this.verticesOrder = new ArrayList<>(verticesOrder);
    }

    public Vector3f normalVector2D() {
        return normalVector(vertices2D);
    }

    public Vector3f normalVector3D() {
        return normalVector(vertices3D);
    }

    private Vector3f normalVector(List<Vector3f> vertices) {
        Vector3f p1 = vertices.get(0);
        Vector3f p2 = vertices.get(1);
        Vector3f p3 = vertices.get(2);
        Vector3f u = new Vector3f(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
        Vector3f v = new Vector3f(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
        return Vector3f.crossProduct(u, v);
    }

    public List<Vector3f> getVertices2D() {
        return vertices2D;
    }

    public List<Vector3f> getVertices3D() {
        return vertices3D;
    }

    public List<Integer> getVerticesOrder() {
        return verticesOrder;
    }
}
