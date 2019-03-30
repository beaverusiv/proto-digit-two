package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

import java.util.HashMap;
import java.util.Map;

import nz.kiwi.loomans.canyoudigit.MapCellMetadata;
import nz.kiwi.loomans.canyoudigit.components.MapComponent;

public class MapSystem extends IteratingSystem {
    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private MovingSystem movingSystem;
    private PlayerSystem playerSystem;
    private SaveGameSystem saveGameSystem;

    private ComponentMapper<MapComponent> mapMap;

    int map;

    private HashMap<String, TiledMap> maps = new HashMap<>();
    private HashMap<String, IsometricTiledMapRenderer> renderers = new HashMap<>();
    private SpriteBatch spriteBatch = new SpriteBatch();
    private float stateTime = 0f;
    private TiledMapTileLayer baseLayer;
    private TiledMapTileLayer dugLayer;

    public MapSystem() {
        super(Aspect.all(MapComponent.class));
    }

    @Override
    protected void process(int entityId) {
        MapComponent mapComponent = mapMap.get(entityId);
        IsometricTiledMapRenderer renderer = renderers.get(mapComponent.mapId);
        int BG_TILE_DIM = 32;

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = assetSystem.animations.get("ocean").getKeyFrame(stateTime, true);
        spriteBatch.begin();
        for (int y = (int)cameraSystem.mapCamera.viewportHeight; y >= -BG_TILE_DIM; y -= BG_TILE_DIM) {
            for (int x = 0; x < (int)cameraSystem.mapCamera.viewportWidth; x += BG_TILE_DIM) {
                spriteBatch.draw(currentFrame,
                        x, y,
                        0, 0,
                        BG_TILE_DIM, BG_TILE_DIM,
                        1.01f, 1.01f,
                        0);
            }
        }
        spriteBatch.end();
        cameraSystem.mapCamera.update();
        renderer.setView(cameraSystem.mapCamera);
        int[] layers = {0};
        renderer.render(layers);
    }

    @Override
    protected void initialize() {
        map = world.create();
        mapMap.create(map);
        if (saveGameSystem.save.mapData != null) {
            loadMap(map, saveGameSystem.save.mapData.mapId, saveGameSystem.save.mapData.metadata);
        } else {
            loadMap(map, "maps/tutorial/first.tmx", null);
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
        for (Map.Entry<String, TiledMap> item : maps.entrySet()) {
            item.getValue().dispose();
        }
        for (Map.Entry<String, IsometricTiledMapRenderer> item : renderers.entrySet()) {
            item.getValue().dispose();
        }
    }

    private void loadMap(int entityId, String mapPath, MapCellMetadata[][] mapMetadata) {
        MapComponent mapComponent = mapMap.get(entityId);
        mapComponent.mapId = mapPath;

        TiledMap map = new TmxMapLoader().load(mapPath);
        maps.put(mapPath, map);
        renderers.put(mapPath, new IsometricTiledMapRenderer(map));

        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
        cameraSystem.mapCamera.translate(mapWidth * tilePixelWidth / 2.0f, 0);

        baseLayer = (TiledMapTileLayer) map.getLayers().get(0);
        dugLayer = (TiledMapTileLayer) map.getLayers().get(1);
        TiledMapTileLayer metaLayer = (TiledMapTileLayer) map.getLayers().get(2);

        if (mapMetadata == null) {
            mapComponent.metadata = new MapCellMetadata[mapWidth][mapHeight];
            for (int i = 0; i < mapWidth; i++) {
                for (int j = 0; j < mapHeight; j++) {
                    mapComponent.metadata[i][j] = new MapCellMetadata();
                    mapComponent.metadata[i][j].dug = metaLayer.getCell(i, j).getTile().getProperties().get("dug", Integer.class);
                    mapComponent.metadata[i][j].walkable = metaLayer.getCell(i, j).getTile().getProperties().get("walkable", Boolean.class);
                }
            }
        } else {
            mapComponent.metadata = mapMetadata;
            for (int i = 0; i < mapWidth; i++) {
                for (int j = 0; j < mapHeight; j++) {
                    if (mapComponent.metadata[i][j].dug == 100) {
                        digMapTile(i, j, true);
                    }
                }
            }
        }

        int x = map.getProperties().get("entry_x", Integer.class);
        int y = map.getProperties().get("entry_y", Integer.class);
        movingSystem.setTileCoords(x, y, playerSystem.player);
    }

    /**
     * Replace a map tile with the corresponding tile in the layer above to simulate digging
     * @param across x coord
     * @param down y coord
     * @return whether the tile was replaced or not (useful for energy calc)
     */
    boolean digMapTile(int across, int down) {
        return digMapTile(across, down, false);
    }

    private boolean digMapTile(int across, int down, boolean ignoreEnergy) {
        MapComponent mapComponent = mapMap.get(map);
        Cell baseCell = baseLayer.getCell(across, down);
        Cell dugCell = dugLayer.getCell(across, down);

        if (baseCell == null || dugCell == null) {
            // TODO: when collision detection and pathing is done this should never happen (walk off map)
            return false;
        }

        if (mapComponent.metadata[across][down].dug < 100 || ignoreEnergy) {
            baseCell.setTile(dugCell.getTile());
            mapComponent.metadata[across][down].dug = 100;
            return true;
        }

        return false;
    }
}
