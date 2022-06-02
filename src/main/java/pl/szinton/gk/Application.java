package pl.szinton.gk;

import pl.szinton.gk.math.Vector2i;
import pl.szinton.gk.math.Vector3f;
import pl.szinton.gk.utils.ModelUtils;
import pl.szinton.gk.view.*;

import java.util.List;

import static pl.szinton.gk.view.ApplicationWindow.DEFAULT_HEIGHT;
import static pl.szinton.gk.view.ApplicationWindow.DEFAULT_WIDTH;

public class Application {

    public static void main(String[] args) {
        Vector3f initialPosition = new Vector3f(-4f, 2f, 5f);
        Vector2i frameSize = new Vector2i(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Camera3D camera = new Camera3D(frameSize, initialPosition);
        camera.rotate(new Vector3f(0f, (float) (45f * Math.PI / 180f), 0f));

        List<Model3D> objects = List.of(
                ModelUtils.createCuboidModel(new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f))
        );

        ApplicationWindow app = new ApplicationWindow(camera, objects);
        app.run();
    }
}
