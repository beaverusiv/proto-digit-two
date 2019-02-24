package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class MapSystem extends BaseSystem {
    private TiledMap map;
    private IsometricTiledMapRenderer renderer;
    private OrthographicCamera camera;

    public MapSystem(OrthographicCamera cam) {
        camera = cam;
        map = new TmxMapLoader().load("maps/first.tmx");
        renderer = new IsometricTiledMapRenderer(map);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void dispose() {
        super.dispose();
        map.dispose();
        renderer.dispose();
    }

    @Override
    protected void processSystem() {
        renderer.setView(camera);
        renderer.render();
    }

    public MapProperties getMapProperties() {
        return map.getProperties();
    }
}
