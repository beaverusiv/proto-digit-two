package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class MapSystem extends BaseSystem {
    public TiledMap map;
    private IsometricTiledMapRenderer renderer;
    private CameraSystem cameraSystem;

    public MapSystem() {
        map = new TmxMapLoader().load("maps/first.tmx");
        renderer = new IsometricTiledMapRenderer(map);
    }

    @Override
    protected void initialize() {
        super.initialize();
        cameraSystem.mapCamera.translate(getMapCentre(), 0);
    }

    @Override
    protected void dispose() {
        super.dispose();
        map.dispose();
        renderer.dispose();
    }

    @Override
    protected void processSystem() {
        cameraSystem.mapCamera.update();
        renderer.setView(cameraSystem.mapCamera);
        renderer.render();
    }

    private float getMapCentre() {
        int mapWidth = map.getProperties().get("width", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);

        return mapWidth * tilePixelWidth / 2.0f;
    }
}
