package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

import nz.kiwi.loomans.canyoudigit.components.GuiComponent;
import nz.kiwi.loomans.canyoudigit.stages.ContinentsStage;
import nz.kiwi.loomans.canyoudigit.stages.MapStage;
import nz.kiwi.loomans.canyoudigit.stages.TreasureStage;
import nz.kiwi.loomans.canyoudigit.states.GuiState;

public class GuiRenderingSystem extends BaseSystem {
    private ComponentMapper<GuiComponent> guiMap;

    private InputSystem inputSystem;
    private PlayerSystem playerSystem;
    private AssetSystem assetSystem;

    public StateMachine<GuiRenderingSystem, GuiState> fsm;
    public HashMap<String, Stage> stages = new HashMap<>();

    public GuiRenderingSystem() {
        fsm = new DefaultStateMachine<>(this, GuiState.MAP);
    }

    @Override
    protected void initialize() {
        super.initialize();
        stages.put("MAP", new MapStage(world));
        stages.put("TREASURE", new TreasureStage(world));
        stages.put("CONTINENTS", new ContinentsStage(world));

        // initialise initial stage
        fsm.getCurrentState().enter(this);
    }

    @Override
    protected void processSystem() {
        fsm.update();
    }

    @Override
    protected void dispose() {
        super.dispose();
        for (Map.Entry<String, Stage> item : stages.entrySet()) {
            item.getValue().dispose();
        }
    }
}
