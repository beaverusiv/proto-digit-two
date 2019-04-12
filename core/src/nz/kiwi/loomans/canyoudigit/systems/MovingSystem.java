package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Stack;

import nz.kiwi.loomans.canyoudigit.components.ActorComponent;
import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.states.PlayerState;

public class MovingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<MovingComponent> moveMap;
    private ComponentMapper<AnimationComponent> aniMap;
    private ComponentMapper<ActorComponent> actorMap;

    private MapSystem mapSystem;
    private PlayerSystem playerSystem;
    private ActorSystem actorSystem;

    private static final int PLAYER_DIM = 30;
    private static final float TILE_WIDTH = 128;
    private static final float TILE_HEIGHT = 64;
    public static final float STEP_DURATION = 0.8f;

    public MovingSystem() {
        super(Aspect.all(PositionComponent.class, MovingComponent.class).exclude(AnimationComponent.class, ActorComponent.class));
    }

    @Override
    protected void process(int entityId) {
        MovingComponent m = moveMap.get(entityId);
        PositionComponent p = posMap.get(entityId);

        if (m.movements.empty()) {
            if (mapSystem.canDigMapTile((int) p.position.y, (int) p.position.x)) {
                MessageManager.getInstance().dispatchMessage(MovingSystem.STEP_DURATION, playerSystem, playerSystem.fsm, PlayerState.DIGGING.ordinal());
            }
            return;
        }

        AnimationComponent a = aniMap.create(playerSystem.player);
        String movement = m.movements.pop();
        actorMap.create(entityId);
        // Have to manually call as we're immediately going to reference the Actor
        actorSystem.inserted(entityId);

        switch (movement) {
            case "walk_right":
                a.name = "walk_right";
                actorSystem.addAction(entityId, Actions.moveTo(p.position.x + TILE_WIDTH / 2f, p.position.y + TILE_HEIGHT / 2f, STEP_DURATION));
                MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
            break;
            case "walk_left":
                a.name = "walk_left";
                actorSystem.addAction(entityId, Actions.moveTo(p.position.x - TILE_WIDTH / 2f, p.position.y - TILE_HEIGHT / 2f, STEP_DURATION));
                MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
            break;
            case "walk_down":
                a.name = "walk_down";
                actorSystem.addAction(entityId, Actions.moveTo(p.position.x + TILE_WIDTH / 2f, p.position.y - TILE_HEIGHT / 2f, STEP_DURATION));
                MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
            break;
            case "walk_up":
                a.name = "walk_up";
                actorSystem.addAction(entityId, Actions.moveTo(p.position.x - TILE_WIDTH / 2f, p.position.y + TILE_HEIGHT / 2f, STEP_DURATION));
                MessageManager.getInstance().dispatchMessage(null, playerSystem.fsm, PlayerState.WALKING.ordinal());
            break;
            default:
            break;
        }
    }

    public void setMovements(MovingComponent movingComponent, PositionComponent positionComponent, Vector2 target) {
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
