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
import java.util.Stack;

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
        if (m.movements == null) {
            return;
        }
        if (m.movements.empty()) {
            if (mapSystem.canDigMapTile((int) p.position.y, (int) p.position.x)) {
                MessageManager.getInstance().dispatchMessage(MovingSystem.STEP_DURATION, playerSystem, playerSystem.fsm, PlayerState.DIGGING.ordinal());
            }
            return;
        }
        String key = String.valueOf(entityId);
        if (!actors.containsKey(key)) {
            actors.put(key, new Actor());
            actors.get(key).setPosition(p.position.x, p.position.y);
        }
        Actor actor = actors.get(key);

        if (playerSystem.fsm.isInState(PlayerState.IDLE)) {
            String movement = m.movements.pop();
            switch (movement) {
                case "walk_right":
                    a.name = "walk_right";
                    actor.addAction(Actions.moveTo(p.position.x + TILE_WIDTH / 2f, p.position.y + TILE_HEIGHT / 2f, STEP_DURATION));
                    MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
                break;
                case "walk_left":
                    a.name = "walk_left";
                    actor.addAction(Actions.moveTo(p.position.x - TILE_WIDTH / 2f, p.position.y - TILE_HEIGHT / 2f, STEP_DURATION));
                    MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
                break;
                case "walk_down":
                    a.name = "walk_down";
                    actor.addAction(Actions.moveTo(p.position.x + TILE_WIDTH / 2f, p.position.y - TILE_HEIGHT / 2f, STEP_DURATION));
                    MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
                break;
                case "walk_up":
                    a.name = "walk_up";
                    actor.addAction(Actions.moveTo(p.position.x - TILE_WIDTH / 2f, p.position.y + TILE_HEIGHT / 2f, STEP_DURATION));
                    MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
                break;
                default:
                break;
            }
        }

        actor.act(deltaTime);
        p.position.x = actor.getX();
        p.position.y = actor.getY();
    }

    public void setMovements(MovingComponent movingComponent, PositionComponent positionComponent, Vector2 target) {
        if (movingComponent == null || positionComponent == null || target == null) {
            return;
        }

        movingComponent.movements = new Stack<>();
        Vector2 v = getTileCoords(positionComponent.position.x, (positionComponent.position.y) * -1);

        while (v.x != target.x || v.y != target.y) {
            if (v.x < target.x) {
                movingComponent.movements.push("walk_right");
                v.x++;
            } else if (v.x > target.x) {
                movingComponent.movements.push("walk_left");
                v.x--;
            } else if (v.y < target.y) {
                movingComponent.movements.push("walk_down");
                v.y++;
            } else if (v.y > target.y) {
                movingComponent.movements.push("walk_up");
                v.y--;
            }
        }
    }

    void setTileCoords(int x, int y, int entity) {
        PositionComponent pos = posMap.get(entity);
        pos.position.x = ((x + y) * TILE_WIDTH / 2f) + ((TILE_WIDTH - PLAYER_DIM) / 2f);
        pos.position.y = ((x - y) * TILE_HEIGHT / 2f) + ((TILE_HEIGHT - PLAYER_DIM) / 2f);
    }

    public Vector2 getTileCoords(float x, float y) {
        float x2 = x / TILE_WIDTH - y / TILE_HEIGHT;
        float y2 = y / TILE_HEIGHT + x / TILE_WIDTH;

        return new Vector2((float)Math.floor(x2), (float)Math.floor(y2));
    }
}
