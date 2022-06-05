package pl.szinton.gk.view;

import org.ejml.simple.SimpleMatrix;
import pl.szinton.gk.math.Vector2f;
import pl.szinton.gk.math.Vector2i;
import pl.szinton.gk.math.Vector3f;
import pl.szinton.gk.utils.MatrixUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NormalMap {

    private final static int MIN_LIGHT_VALUE = 40;
    private final static int MAX_LIGHT_VALUE = 200;

    private static Vector3f negativeLightDirection = new Vector3f(-3f, 1.2f, 1f).negative().normalize();
    private static Color[][] normalMapPixels;

    public static void setLightDirection(Vector3f direction) {
        negativeLightDirection = direction.negative().normalize();
    }

    public static Vector3f getLightDirection() {
        return negativeLightDirection.negative();
    }

    public static void loadNormalMap(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            int width = img.getWidth();
            int height = img.getHeight();
            normalMapPixels = new Color[height][width];
            if (width != height)
                throw new IllegalArgumentException("Normal map's width and height must be the same!");
            for (int row = 0; row < height; row++)
                for (int col = 0; col < width; col++)
                    normalMapPixels[row][col] = new Color(img.getRGB(col, row));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Vector2i convertFloatCoordinatesToIntCoordinates(Vector2f floatCoordinates) {
        float x = floatCoordinates.getX();
        float y = floatCoordinates.getY();
        if (x < 0f || x > 1f || y < 0f || y > 1f)
            throw new IllegalArgumentException("Bad range of float coordinates when referring to pixel!");
        int b = normalMapPixels.length - 1;
        return new Vector2i((int) (x * b), (int) (y * b));
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
        if (xDiff == 0f) {
            return new Vector2f(v1.getX(), null); // vertical line: storing information about line's x coordinate
        }
        float a = yDiff / xDiff;
        float b = v1.getY() - a * v1.getX();
        return new Vector2f(a, b);
    }

    private static Color getPlanePixelColor(Plane plane, int x, int y, List<Vector2f> planeEdgeLines) {
        Vector2f tangentPoint = findTangentPlaneProjectionPoint(x, y, planeEdgeLines);

        List<Vector3f> vertices = plane.getVertices3D();
        Vector3f pos1 = vertices.get(0);
        Vector3f pos2 = vertices.get(1);
        Vector3f pos3 = vertices.get(2);
        Vector2f uv1 = new Vector2f(0f, 1f);
        Vector2f uv2 = new Vector2f(0f, 0f);
        Vector2f uv3 = new Vector2f(1f, 0f);
        Vector3f edge1 = pos2.subtract(pos1);
        Vector3f edge2 = pos3.subtract(pos1);
        Vector2f deltaUV1 = uv2.subtract(uv1);
        Vector2f deltaUV2 = uv3.subtract(uv1);
        float f = 1f / (deltaUV1.getX() * deltaUV2.getY() - deltaUV2.getX() * deltaUV1.getY());
        Vector3f normal = plane.normalVector3D().normalize();
        Vector3f tangent = new Vector3f(
                f * (deltaUV2.getY() * edge1.getX() - deltaUV1.getY() * edge2.getX()),
                f * (deltaUV2.getY() * edge1.getY() - deltaUV1.getY() * edge2.getY()),
                f * (deltaUV2.getY() * edge1.getZ() - deltaUV1.getY() * edge2.getZ())
        ).normalize();
        Vector3f bitangent = new Vector3f(
                f * (-deltaUV2.getX() * edge1.getX() + deltaUV1.getX() * edge2.getX()),
                f * (-deltaUV2.getX() * edge1.getY() + deltaUV1.getX() * edge2.getY()),
                f * (-deltaUV2.getX() * edge1.getZ() + deltaUV1.getX() * edge2.getZ())
        ).normalize();
        SimpleMatrix tbnMatrix = new SimpleMatrix(3, 3, true, new float[]{
                tangent.getX(), bitangent.getX(), normal.getX(),
                tangent.getY(), bitangent.getY(), normal.getY(),
                tangent.getZ(), bitangent.getZ(), normal.getZ()
        });

        Vector3f normalMapVector = getNormalMapVector(tangentPoint);
        Vector3f combinedNormal = MatrixUtils.getVectorFromMatrix(
                MatrixUtils.multiplyVectorByMatrix(normalMapVector, tbnMatrix)).normalize();
        float dotProduct = Vector3f.dotProduct(negativeLightDirection, combinedNormal);
        int lightValue = (int) (MIN_LIGHT_VALUE + ((dotProduct + 2f) / 4f * (MAX_LIGHT_VALUE - MIN_LIGHT_VALUE)));
        return new Color(lightValue, lightValue, lightValue);
//        return new Color(20 + lightValue, 7 + lightValue, 2 + lightValue);
    }

    private static Vector2f findTangentPlaneProjectionPoint(int x, int y, List<Vector2f> planeEdgeLines) {
        Vector2f point = new Vector2f((float) x, (float) y);
        float d0 = findDistanceFromLine(point, planeEdgeLines.get(0));
        float d1 = findDistanceFromLine(point, planeEdgeLines.get(1));
        float d2 = findDistanceFromLine(point, planeEdgeLines.get(2));
        float d3 = findDistanceFromLine(point, planeEdgeLines.get(3));
        float u = d0 / (d0 + d2);
        float v = d1 / (d1 + d3);
        return new Vector2f(u, v);
    }

    private static float findDistanceFromLine(Vector2f point, Vector2f lineCoefficients) {
        float x = point.getX();
        float y = point.getY();
        if (lineCoefficients.getY() == null) {
            float lineX = lineCoefficients.getX();
            return Math.abs(x - lineX);
        }
        float a = lineCoefficients.getX();
        float b = -1f;
        float c = lineCoefficients.getY();
        double distance = Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
        return (float) distance;
    }

    private static Vector3f getNormalMapVector(Vector2f tangentPoint) {
        Vector2i normalMapCoordinates = convertFloatCoordinatesToIntCoordinates(tangentPoint);
        int x = normalMapCoordinates.getX();
        int y = normalMapCoordinates.getY();
        Color c = normalMapPixels[x][y];
        float xValue = (float) (c.getRed() - 128);
        float yValue = (float) (c.getGreen() - 128);
        float zValue = (float) (c.getBlue() - 128);
        return new Vector3f(xValue, yValue, zValue).normalize();
    }
}
