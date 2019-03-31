package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;

import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.components.InputComponent;
import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.components.TextureComponent;
import nz.kiwi.loomans.canyoudigit.states.PlayerState;

public class PlayerSystem extends BaseSystem implements Telegraph {
    private SaveGameSystem saveGameSystem;

    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<AnimationComponent> aniMap;
    private ComponentMapper<TextureComponent> texMap;
    private ComponentMapper<MovingComponent> movMap;
    private ComponentMapper<EnergyComponent> nrgMap;
    public ComponentMapper<InputComponent> inputMap;

    public int player;
    public StateMachine<PlayerSystem, PlayerState> fsm;

    public PlayerSystem() {
        fsm = new DefaultStateMachine<>(this, PlayerState.IDLE);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        System.out.println("handling " + msg.message);
        return fsm.handleMessage(msg);
    }

    @Override
    protected void initialize() {
        super.initialize();
        player = world.create();
        PositionComponent pos = posMap.create(player);
        AnimationComponent ani = aniMap.create(player);
        TextureComponent tex = texMap.create(player);
        MovingComponent mov = movMap.create(player);
        EnergyComponent e = nrgMap.create(player);
        InputComponent i = inputMap.create(player);

        // TODO: load from save
        i.acceptingInput = true;
        ani.name = null;
        pos.position = new Vector2(0, 0);
        tex.dimensions = new Vector2(30, 60);
        tex.origin = new Vector2(0, 0);
        tex.name = "atlas/sprites.png";
        mov.target = null;
        e.level = saveGameSystem.save.energy.level;
        e.max = saveGameSystem.save.energy.max;
        e.lastIncrement = 0;
        e.incrementInterval = saveGameSystem.save.energy.incrementInterval;
    }

    @Override
    protected void processSystem() {
        fsm.update();
    }
}
