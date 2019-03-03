package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;

import nz.kiwi.loomans.canyoudigit.components.CameraTargetComponent;

public class CameraSystem extends IteratingSystem {
    public OrthographicCamera mapCamera = new OrthographicCamera();
    public OrthographicCamera hudCamera = new OrthographicCamera();

    public CameraSystem() {
        super(Aspect.all(CameraTargetComponent.class));
    }

    @Override
    protected void process(int entityId) {
        // TODO: track CameraTargetComponents
    }

    public void resize(int width, int height) {
        mapCamera.viewportWidth = width;
        mapCamera.viewportHeight = height;
        mapCamera.update();
    }
}
