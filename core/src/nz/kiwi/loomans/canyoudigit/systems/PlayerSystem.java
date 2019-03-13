package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.components.TextureComponent;

public class PlayerSystem extends BaseSystem {
    int player;
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<AnimationComponent> aniMap;
    private ComponentMapper<TextureComponent> texMap;
    private ComponentMapper<MovingComponent> movMap;
    private ComponentMapper<EnergyComponent> nrgMap;
    private MovingSystem movingSystem;

    @Override
    protected void initialize() {
        super.initialize();
        player = world.create();
        PositionComponent pos = posMap.create(player);
        AnimationComponent ani = aniMap.create(player);
        TextureComponent tex = texMap.create(player);
        MovingComponent mov = movMap.create(player);
        EnergyComponent e = nrgMap.create(player);

        ani.name = null;
        pos.position = new Vector2(0, 0);
        tex.dimensions = new Vector2(30, 60);
        tex.origin = new Vector2(0, 0);
        tex.name = "sprites/char.png";
        mov.target = null;
        e.level = 0;
        e.max = 100;
        e.lastIncrement = 0;
        e.incrementInterval = 1;
        movingSystem.setTileCoords(0, 0, player);
    }

    @Override
    protected void processSystem() {
    }
}
