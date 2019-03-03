package nz.kiwi.loomans.canyoudigit.screens;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import nz.kiwi.loomans.canyoudigit.systems.CameraSystem;
import nz.kiwi.loomans.canyoudigit.systems.InputSystem;
import nz.kiwi.loomans.canyoudigit.systems.MapSystem;
import nz.kiwi.loomans.canyoudigit.systems.MovingSystem;
import nz.kiwi.loomans.canyoudigit.systems.RenderingSystem;

public class PlayScreen implements Screen {
    private World world;
    private CameraSystem cameraSystem = new CameraSystem();

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.setDelta(delta);
        world.process();
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    @Override
    public void resize(int width, int height) {
        cameraSystem.resize(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void show() {
        MapSystem mapSystem = new MapSystem(cameraSystem.mapCamera);
        RenderingSystem renderingSystem = new RenderingSystem(cameraSystem.mapCamera);

        cameraSystem.mapCamera.translate(mapSystem.getMapCentre(), 0);

        int player = renderingSystem.getPlayer();

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(mapSystem, renderingSystem)
                .with(new InputSystem(player), new MovingSystem(mapSystem.map))
                .build();
        world = new World(config);
    }
}
