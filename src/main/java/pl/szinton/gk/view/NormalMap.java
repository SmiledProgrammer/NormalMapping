package pl.szinton.gk.view;

import pl.szinton.gk.math.Vector2f;
import pl.szinton.gk.math.Vector3f;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NormalMap {

    private final static int MIN_LIGHT_VALUE = 50;
    private final static int MAX_LIGHT_VALUE = 255;

    private static Vector3f negativeLightDirection = new Vector3f(1.5f, 1.2f, 1f).negative().normalize();
    private static int[][] normalMapPixels;

    public static void loadNormalMap(File file) {
        throw new UnsupportedOperationException(); // TODO
    }

    public static void setLightDirection(Vector3f direction) {
        negativeLightDirection = direction.negative().normalize();
    }

    public static void fillHorizontalLine(Graphics2D g, int startX, int endX, int y, Plane plane) {
        List<Vector2f> planeEdgeLines = findPlaneEdgeLines(plane);
        for (int x = startX; x <= endX; x++) {
            Color color = getPlanePixelColor(plane, x, y, planeEdgeLines);
            g.setColor(color);
            g.drawLine(x, y, x, y);
        }
    }

    private static List<Vector2f> findPlaneEdgeLines(Plane plane) {
        List<Vector2f> edgeLines = new ArrayList<>(4);
        List<Vector3f> vertices = plane.getVertices2D();
        Vector3f v0 = vertices.get(0);
        Vector3f v1 = vertices.get(1);
        Vector3f v2 = vertices.get(2);
        Vector3f v3 = vertices.get(3);
        edgeLines.add(findEdgeLineCoefficients(v0, v1));
        edgeLines.add(findEdgeLineCoefficients(v1, v2));
        edgeLines.add(findEdgeLineCoefficients(v2, v3));
        edgeLines.add(findEdgeLineCoefficients(v3, v0));
        return edgeLines;
    }

    private static Vector2f findEdgeLineCoefficients(Vector3f v1, Vector3f v2) {
        float xDiff = v1.getX() - v2.getX();
        float yDiff = v1.getY() - v2.getY();
        float a = (xDiff != 0f) ? (yDiff / xDiff) : Float.MAX_VALUE;
        float b = v1.getY() - a * v1.getX();
        return new Vector2f(a, b);
    }

    private static Color getPlanePixelColor(Plane plane, int x, int y, List<Vector2f> planeEdgeLines) {
        Vector2f tangentPoint = findTangentPlaneProjectionPoint(x, y, planeEdgeLines);
        Vector3f normalMapVector = getNormalMapVector(tangentPoint).normalize();
        Vector3f planeNormal = plane.normalVector3D().normalize();
        Vector3f combinedNormal = planeNormal.add(normalMapVector).normalize();
        float dotProduct = Vector3f.dotProduct(negativeLightDirection, combinedNormal);
        int lightValue = (int) (MIN_LIGHT_VALUE + ((dotProduct + 2f) / 4f * (MAX_LIGHT_VALUE - MIN_LIGHT_VALUE)));
        return new Color(lightValue, lightValue, lightValue);
    }

    private static Vector2f findTangentPlaneProjectionPoint(int x, int y, List<Vector2f> planeEdgeLines) {
        Vector2f point = new Vector2f(x, y);
        float d0 = findDistanceFromLine(point, planeEdgeLines.get(0));
        float d1 = findDistanceFromLine(point, planeEdgeLines.get(1));
        float d2 = findDistanceFromLine(point, planeEdgeLines.get(2));
        float d3 = findDistanceFromLine(point, planeEdgeLines.get(3));
        float u = d0 / (d0 + d2);
        float v = d1 / (d1 + d3);
        return new Vector2f(u, v);
    }

    private static float findDistanceFromLine(Vector2f point, Vector2f lineCoefficients) {
        float x = point.x();
        float y = point.y();
        float a = lineCoefficients.x();
        float b = -1f;
        float c = lineCoefficients.y();
        double distance = Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
        return (float) distance;
    }

    private static Vector3f getNormalMapVector(Vector2f tangentPoint) {
        // TODO: calculate vector created based on pixel's RGB values from tangent point of normal map
        throw new UnsupportedOperationException();
    }
}
