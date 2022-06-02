
package pl.szinton.gk.view;

import pl.szinton.gk.math.Vector2i;

import javax.swing.*;
import java.awt.*;

import static pl.szinton.gk.utils.Constants.DEFAULT_BACKGROUND_COLOR;

public class ScenePanel extends JPanel {

    protected final Camera3D camera;
    private Scene scene;

    public ScenePanel(Camera3D camera, Scene scene) {
        this.camera = camera;
        this.scene = scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Vector2i windowSize = camera.getFrameSize();
        this.setSize(windowSize.getX(), windowSize.getY());

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(DEFAULT_BACKGROUND_COLOR);
        g2d.setClip(0, 0, this.getWidth(), this.getHeight());
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        scene.render(g2d, camera);
    }
}
