package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;

public class MovingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<MovingComponent> moveMap;
    private TiledMapTileLayer layer;

    private static final int PLAYER_DIM = 30;
    private static final float TILE_WIDTH = 128;
    private static final float TILE_HEIGHT = 64;

    public MovingSystem(TiledMap map) {
        super(Aspect.all(PositionComponent.class, MovingComponent.class));
        this.layer = (TiledMapTileLayer)map.getLayers().get(0);
    }

    @Override
    protected void process(int entityId) {
        MovingComponent m = moveMap.get(entityId);
        if (m.target == null) {
            return;
        }
        PositionComponent p = posMap.get(entityId);
        Vector2 v = getTileCoords((int)p.position.x, ((int)p.position.y) * -1);
        if (v.x < m.target.x) {
            setTileCoords(v.x + 1, v.y, entityId);
            return;
        } else if (v.x > m.target.x) {
            setTileCoords(v.x - 1, v.y, entityId);
            return;
        }
        if (v.y < m.target.y) {
            setTileCoords(v.x, v.y + 1, entityId);
            return;
        } else if (v.y > m.target.y) {
            setTileCoords(v.x, v.y - 1, entityId);
            return;
        }
        if (v.x == m.target.x && v.y == m.target.y) {
            layer.getCell((int)m.target.y, (int)m.target.x).setRotation(1);
            m.target = null;
        }
    }

    private void setTileCoords(float x, float y, int entity) {
        setTileCoords((int)x, (int)y, entity);
    }

    private void setTileCoords(int x, int y, int entity) {
        PositionComponent pos = posMap.get(entity);
        pos.position.x = ((x + y) * TILE_WIDTH / 2f) + ((TILE_WIDTH - PLAYER_DIM) / 2f);
        pos.position.y = ((x - y) * TILE_HEIGHT / 2f) + ((TILE_HEIGHT - PLAYER_DIM) / 2f);
    }

    private Vector2 getTileCoords(int x, int y) {
        float x2 = x / TILE_WIDTH - y / TILE_HEIGHT;
        float y2 = y / TILE_HEIGHT + x / TILE_WIDTH;

        return new Vector2((float)Math.floor(x2), (float)Math.floor(y2));
    }
}
