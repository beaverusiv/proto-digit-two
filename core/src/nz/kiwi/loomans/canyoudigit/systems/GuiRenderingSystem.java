package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.components.GuiComponent;
import nz.kiwi.loomans.canyoudigit.stages.ContinentsStage;
import nz.kiwi.loomans.canyoudigit.stages.MapStage;
import nz.kiwi.loomans.canyoudigit.stages.TreasureStage;
import nz.kiwi.loomans.canyoudigit.states.GuiState;

public class GuiRenderingSystem extends BaseSystem {
    public StateMachine<GuiRenderingSystem, GuiState> fsm;
    private ComponentMapper<GuiComponent> guiMap;
    private ComponentMapper<EnergyComponent> nrgMap;
    private InputSystem inputSystem;
    private PlayerSystem playerSystem;
    private AssetSystem assetSystem;

    public HashMap<String, Stage> stages = new HashMap<>();

    public GuiRenderingSystem() {
        fsm = new DefaultStateMachine<>(this, GuiState.MAP);
    }

    @Override
    protected void initialize() {
        super.initialize();
        EnergyComponent e = nrgMap.get(playerSystem.player);
        MapStage mapStage = new MapStage(world);
        stages.put("MAP", mapStage);
        stages.put("TREASURE", new TreasureStage(world));
        stages.put("CONTINENTS", new ContinentsStage(world));

        // initialise initial stage
        mapStage.enter();
    }

    @Override
    protected void processSystem() {
        MessageManager.getInstance().update();
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
