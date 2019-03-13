package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.HashMap;

import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class MovingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<MovingComponent> moveMap;
    private ComponentMapper<AnimationComponent> aniMap;
    private HashMap<String, Actor> actors = new HashMap<String, Actor>();

    private MapSystem mapSystem;
    private TiledMapTileLayer layer;

    private static final int PLAYER_DIM = 30;
    private static final float TILE_WIDTH = 128;
    private static final float TILE_HEIGHT = 64;
    private static final float STEP_DURATION = 0.8f;

    @Override
    protected void initialize() {
        super.initialize();

        this.layer = (TiledMapTileLayer)mapSystem.map.getLayers().get(0);
    }

    public MovingSystem() {
        super(Aspect.all(PositionComponent.class, MovingComponent.class));
    }

    @Override
    protected void process(int entityId) {
        float deltaTime = Gdx.graphics.getDeltaTime();

        final MovingComponent m = moveMap.get(entityId);
        final AnimationComponent a = aniMap.get(entityId);
        PositionComponent p = posMap.get(entityId);
        if (m.target == null) {
            return;
        }
        String key = String.valueOf(entityId);
        if (!actors.containsKey(key)) {
            actors.put(key, new Actor());
            actors.get(key).setPosition(p.position.x, p.position.y);
        }
        Actor actor = actors.get(key);
        if (a.name == null) {
            Vector2 v = getTileCoords((int) p.position.x, ((int) p.position.y) * -1);
            if (v.x < m.target.x) {
                a.name = "walk_right";
                actor.addAction(
                        sequence(
                                Actions.moveTo(p.position.x + TILE_WIDTH / 2f, p.position.y + TILE_HEIGHT / 2f, STEP_DURATION),
                                run(new Runnable() {
                                    public void run() {
                                        a.name = null;
                                    }
                                })
                        )
                );
            } else if (v.x > m.target.x) {
                a.name = "walk_left";
                actor.addAction(
                        sequence(
                                Actions.moveTo(p.position.x - TILE_WIDTH / 2f, p.position.y - TILE_HEIGHT / 2f, STEP_DURATION),
                                run(new Runnable() {
                                    public void run() {
                                        a.name = null;
                                    }
                                })
                        )
                );
            } else if (v.y < m.target.y) {
                a.name = "walk_down";
                actor.addAction(
                        sequence(
                                Actions.moveTo(p.position.x + TILE_WIDTH / 2f, p.position.y - TILE_HEIGHT / 2f, STEP_DURATION),
                                run(new Runnable() {
                                    public void run() {
                                        a.name = null;
                                    }
                                })
                        )
                );
            } else if (v.y > m.target.y) {
                a.name = "walk_up";
                actor.addAction(
                        sequence(
                                Actions.moveTo(p.position.x - TILE_WIDTH / 2f, p.position.y + TILE_HEIGHT / 2f, STEP_DURATION),
                                run(new Runnable() {
                                    public void run() {
                                        a.name = null;
                                    }
                                })
                        )
                );
            } else if (v.x == m.target.x && v.y == m.target.y) {
                TiledMapTileLayer.Cell cell = layer.getCell((int)m.target.y, (int)m.target.x);
                if (cell != null) {
                    cell.setRotation(1);
                }
                m.target = null;
                a.name = null;
            }
        }

        actor.act(deltaTime);
        p.position.x = actor.getX();
        p.position.y = actor.getY();
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
