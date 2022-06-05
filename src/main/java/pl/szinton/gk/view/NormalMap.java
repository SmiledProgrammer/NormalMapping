package pl.szinton.gk.view;

import pl.szinton.gk.math.Vector2f;
import pl.szinton.gk.math.Vector2i;
import pl.szinton.gk.math.Vector3f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.IllegalFormatException;

public class NormalMap {

    private final static int MIN_LIGHT_VALUE = 50;
    private final static int MAX_LIGHT_VALUE = 255;

    private static Vector3f negativeLightDirection = new Vector3f(1.5f, 1.2f, 1f).negative().normalize();
    private static int[][] normalMapPixels;

    public static void loadNormalMap(File file) throws Exception {
        BufferedImage bimg = ImageIO.read(file);
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        normalMapPixels = new int[height][width];

        if (width != height)
            throw new IllegalArgumentException("Normal map's width and height must be the same!");
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                normalMapPixels[row][col] = bimg.getRGB(col, row);
    }

    private static Vector2i convertFloatCoordinatesToIntCoordinates(Vector2f floatCoordinates) {
        // f:[0,1] -> [a,b]; a=0; b=height - 1;
        // f(x) = (1-x)a + xb = xb
        float x = floatCoordinates.x();
        float y = floatCoordinates.y();
        if(x < 0 || x > 1 || y < 0 || y > 1)
            throw new IllegalArgumentException("Bad range of float coordinates when reffering to pixel!");
        int b = normalMapPixels.length - 1;
        return new Vector2i((int) x*b, (int) y*b);

    }

    private static Color getPixelColorFromMapByCoordinates(Vector2f coordinates) {
        Vector2i mapCoordinates = convertFloatCoordinatesToIntCoordinates(coordinates);
        int x = mapCoordinates.getX();
        int y = mapCoordinates.getY();
        return new Color(normalMapPixels[x][y]);
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
