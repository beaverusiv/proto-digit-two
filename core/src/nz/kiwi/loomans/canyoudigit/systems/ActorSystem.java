package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

import nz.kiwi.loomans.canyoudigit.components.ActorComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;

public class ActorSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;

    private HashMap<Integer, Actor> actors = new HashMap<>();

    public ActorSystem() {
        super(Aspect.all(ActorComponent.class, PositionComponent.class));
    }

    @Override
    protected void process(int entityId) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        PositionComponent positionComponent = posMap.get(entityId);

        Actor actor = actors.get(entityId);

        actor.act(deltaTime);
        positionComponent.position.x = actor.getX();
        positionComponent.position.y = actor.getY();
    }

    void addAction(int entityId, Action action) {
        Actor actor = actors.get(entityId);
        actor.addAction(action);
    }

    @Override
    protected void inserted(int entityId) {
        super.inserted(entityId);

        PositionComponent positionComponent = posMap.get(entityId);
        if (!actors.containsKey(entityId)) {
            actors.put(entityId, new Actor());
            actors.get(entityId).setPosition(positionComponent.position.x, positionComponent.position.y);
        }
    }
}
