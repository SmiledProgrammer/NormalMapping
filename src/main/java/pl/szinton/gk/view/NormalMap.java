package pl.szinton.gk.view;

import pl.szinton.gk.math.Vector2f;
import pl.szinton.gk.math.Vector3f;

import java.awt.*;
import java.io.File;

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

    public static Color getPlanePixelColor(Plane plane, int x, int scanLineY) {
        Vector2f tangentPoint = findTangentPlaneProjectionPoint(plane, x, scanLineY);
        Vector3f normalMapVector = getNormalMapVector(tangentPoint).normalize();
        Vector3f planeNormal = plane.normalVector3D().normalize();
        Vector3f combinedNormal = planeNormal.add(normalMapVector).normalize();
        float dotProduct = Vector3f.dotProduct(negativeLightDirection, combinedNormal);
        int lightValue = (int) (MIN_LIGHT_VALUE + ((dotProduct + 2f) / 4f * (MAX_LIGHT_VALUE - MIN_LIGHT_VALUE)));
        return new Color(lightValue, lightValue, lightValue);
    }

    private static Vector2f findTangentPlaneProjectionPoint(Plane plane, int x, int y) {
        // TODO: convert point on plane projection (planes 2D vertices) to <0,1> x/y range
        throw new UnsupportedOperationException();
    }

    private static Vector3f getNormalMapVector(Vector2f tangentPoint) {
        // TODO: calculate vector created based on pixel's RGB values from tangent point of normal map
        throw new UnsupportedOperationException();
    }
}
