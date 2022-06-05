package pl.szinton.gk;

import org.ejml.simple.SimpleMatrix;
import pl.szinton.gk.math.Matrix;
import pl.szinton.gk.math.Vector2i;
import pl.szinton.gk.math.Vector3f;
import pl.szinton.gk.utils.MatrixUtils;
import pl.szinton.gk.utils.ModelUtils;
import pl.szinton.gk.utils.ResourceUtils;
import pl.szinton.gk.view.*;

import java.io.File;
import java.util.List;

import static pl.szinton.gk.view.ApplicationWindow.DEFAULT_HEIGHT;
import static pl.szinton.gk.view.ApplicationWindow.DEFAULT_WIDTH;

public class Application extends Thread {

    private final ApplicationWindow applicationWindow;

    public Application(Camera3D camera, List<Model3D> objects) {
        applicationWindow = new ApplicationWindow(camera, objects);
        applicationWindow.run();
    }

    public static void main(String[] args) {
        Vector3f initialPosition = new Vector3f(1.3f, 0.75f, 0.05f);
        Vector2i frameSize = new Vector2i(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Camera3D camera = new Camera3D(frameSize, initialPosition);
        camera.rotate(new Vector3f(0f, 0.79f, 0f));

        List<Model3D> objects = List.of(
                ModelUtils.createCuboidModel(new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f))
        );

        File normalMapFile = ResourceUtils.getFileFromResources("normal2.jpg");
        NormalMap.loadNormalMap(normalMapFile);

        Thread appThread = new Application(camera, objects);
        appThread.start();
    }

    @Override
    public void run() {
        while (true) {
            Vector3f lightVector = NormalMap.getLightDirection();
            SimpleMatrix rotationMatrix = Matrix.rotationZ((float) (2f * Math.PI / 180f));
            Vector3f newLightVector = MatrixUtils.getVectorFromMatrix(
                    MatrixUtils.multiplyExtendedVectorByMatrix(lightVector, rotationMatrix));
//            NormalMap.setLightDirection(newLightVector);
            applicationWindow.repaint();
            try {
                Thread.sleep(1000 / 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
