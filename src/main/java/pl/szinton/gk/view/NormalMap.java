package pl.szinton.gk.view;

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

    public static Color getPlanePixelColor(Plane2D plane, int x, int scanLineY) {
        // TODO: implement normal map calculations
        Vector3f planeNormal = plane.normalVector3D().normalize();
        float dotProduct = Vector3f.dotProduct(negativeLightDirection, planeNormal);
        int lightValue = (int) (MIN_LIGHT_VALUE + ((dotProduct + 2f) / 4f * (MAX_LIGHT_VALUE - MIN_LIGHT_VALUE)));
        return new Color(lightValue, lightValue, lightValue);
    }
}
