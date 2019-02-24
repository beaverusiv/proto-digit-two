package nz.kiwi.loomans.canyoudigit.screens;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;

import nz.kiwi.loomans.canyoudigit.systems.MapSystem;

public class PlayScreen implements Screen {
    private OrthographicCamera camera;
    private World world;

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
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        MapSystem mapSystem = new MapSystem(camera);

        MapProperties prop = mapSystem.getMapProperties();

        int mapWidth = prop.get("width", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);

        float mapCentreWidth = mapWidth * tilePixelWidth / 2.0f;
        camera.translate(mapCentreWidth, 0);

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(mapSystem)
                .build();
        world = new World(config);
    }
}
