package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.HashMap;

import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.states.PlayerState;

public class MovingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<MovingComponent> moveMap;
    private ComponentMapper<AnimationComponent> aniMap;
    private ComponentMapper<EnergyComponent> energyComponentMap;
    private HashMap<String, Actor> actors = new HashMap<>();

    private MapSystem mapSystem;
    private PlayerSystem playerSystem;

    private static final int PLAYER_DIM = 30;
    private static final float TILE_WIDTH = 128;
    private static final float TILE_HEIGHT = 64;
    public static final float STEP_DURATION = 0.8f;

    public MovingSystem() {
        super(Aspect.all(PositionComponent.class, MovingComponent.class));
    }

    @Override
    protected void process(int entityId) {
        // TODO: these different scenarios should be handled by the entity fsm (player system or npc system)
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
        Vector2 v = getTileCoords((int) p.position.x, ((int) p.position.y) * -1);
        if (playerSystem.fsm.isInState(PlayerState.IDLE)) {
            if (v.x < m.target.x) {
                a.name = "walk_right";
                actor.addAction(Actions.moveTo(p.position.x + TILE_WIDTH / 2f, p.position.y + TILE_HEIGHT / 2f, STEP_DURATION));
                MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
            } else if (v.x > m.target.x) {
                a.name = "walk_left";
                actor.addAction(Actions.moveTo(p.position.x - TILE_WIDTH / 2f, p.position.y - TILE_HEIGHT / 2f, STEP_DURATION));
                MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
            } else if (v.y < m.target.y) {
                a.name = "walk_down";
                actor.addAction(Actions.moveTo(p.position.x + TILE_WIDTH / 2f, p.position.y - TILE_HEIGHT / 2f, STEP_DURATION));
                MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
            } else if (v.y > m.target.y) {
                a.name = "walk_up";
                actor.addAction(Actions.moveTo(p.position.x - TILE_WIDTH / 2f, p.position.y + TILE_HEIGHT / 2f, STEP_DURATION));
                MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
            } else if (v.x == m.target.x && v.y == m.target.y) {
                if (mapSystem.digMapTile((int) m.target.y, (int) m.target.x)) {
                    energyComponentMap.get(playerSystem.player).level -= 20;
                }
                m.target = null;
            }
        }

        actor.act(deltaTime);
        p.position.x = actor.getX();
        p.position.y = actor.getY();
    }

    void setTileCoords(int x, int y, int entity) {
        PositionComponent pos = posMap.get(entity);
        pos.position.x = ((x + y) * TILE_WIDTH / 2f) + ((TILE_WIDTH - PLAYER_DIM) / 2f);
        pos.position.y = ((x - y) * TILE_HEIGHT / 2f) + ((TILE_HEIGHT - PLAYER_DIM) / 2f);
    }

    Vector2 getTileCoords(int x, int y) {
        float x2 = x / TILE_WIDTH - y / TILE_HEIGHT;
        float y2 = y / TILE_HEIGHT + x / TILE_WIDTH;

        return new Vector2((float)Math.floor(x2), (float)Math.floor(y2));
    }
}
