package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class MapSystem extends BaseSystem {
    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;

    private SpriteBatch spriteBatch = new SpriteBatch();
    private float stateTime = 0f;
    private TiledMap map;
    private IsometricTiledMapRenderer renderer;
    private TiledMapTileLayer baseLayer;
    private TiledMapTileLayer dugLayer;

    public MapSystem() {
        map = new TmxMapLoader().load("maps/first.tmx");
        renderer = new IsometricTiledMapRenderer(map);
        baseLayer = (TiledMapTileLayer)map.getLayers().get(0);
        dugLayer = (TiledMapTileLayer)map.getLayers().get(1);
    }

    @Override
    protected void initialize() {
        super.initialize();
        cameraSystem.mapCamera.translate(getMapCentre(), 0);
        int width = map.getProperties().get("width", Integer.class);
        int height = map.getProperties().get("height", Integer.class);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = baseLayer.getCell(i, j);
                if (cell != null) {
                    baseLayer.getCell(i, j).getTile().getProperties().put("dug", 0);
                }
            }
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
        map.dispose();
        renderer.dispose();
    }

    @Override
    protected void processSystem() {
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

    private float getMapCentre() {
        int mapWidth = map.getProperties().get("width", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);

        return mapWidth * tilePixelWidth / 2.0f;
    }

    /**
     * Replace a map tile with the corresponding tile in the layer above to simulate digging
     * @param across x coord
     * @param down y coord
     * @return whether the tile was replaced or not (useful for energy calc)
     */
    boolean digMapTile(int across, int down) {
        Cell baseCell = baseLayer.getCell(across, down);
        Cell dugCell = dugLayer.getCell(across, down);

        if (baseCell == null || dugCell == null) {
            // TODO: when collision detection and pathing is done this should never happen (walk off map)
            return false;
        }

        if (baseCell.getTile().getProperties().get("dug", Integer.class) < 100) {
            baseCell.setTile(dugCell.getTile());
            baseCell.getTile().getProperties().put("dug", 100);
            return true;
        }

        return false;
    }
}
