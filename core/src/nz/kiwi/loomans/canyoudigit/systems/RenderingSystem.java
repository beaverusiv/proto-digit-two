package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.components.TextureComponent;

public class RenderingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<TextureComponent> texMap;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private int player;

    private static final int PLAYER_DIM = 30;
    private static final float TILE_WIDTH = 128;
    private static final float TILE_HEIGHT = 64;

    public RenderingSystem(OrthographicCamera cam) {
        super(Aspect.all(PositionComponent.class, TextureComponent.class));
        camera = cam;
    }

    @Override
    protected void initialize() {
        player = world.create();
        PositionComponent pos = posMap.create(player);
        TextureComponent tex = texMap.create(player);

        pos.position = new Vector2(0, 0);
        setPlayerTileCoords(4, 2);
        tex.dimensions = new Vector2(PLAYER_DIM, PLAYER_DIM);
    }

    @Override
    protected void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }

    @Override
    protected void process(int entityId) {
        final PositionComponent pos = posMap.get(entityId);
        final TextureComponent tex = texMap.get(entityId);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 0, 1);
        shapeRenderer.rect(pos.position.x, pos.position.y, tex.dimensions.x, tex.dimensions.y);
        shapeRenderer.end();
    }

    public int getPlayer() {
        return player;
    }

    private void setPlayerTileCoords(int x, int y) {
        PositionComponent pos = posMap.get(player);
        pos.position.x = ((x + y) * TILE_WIDTH / 2f) + ((TILE_WIDTH - PLAYER_DIM) / 2f);
        pos.position.y = ((x - y) * TILE_HEIGHT / 2f) + ((TILE_HEIGHT - PLAYER_DIM) / 2f);
    }

    private Vector2 getTileCoords(int x, int y) {
        return new Vector2(
        ((x / TILE_WIDTH / 2f) - (y / TILE_HEIGHT / 2f)) / 2,
        ((y / TILE_HEIGHT / 2f) + (x / TILE_WIDTH / 2f)) / 2
        );
    }
}
