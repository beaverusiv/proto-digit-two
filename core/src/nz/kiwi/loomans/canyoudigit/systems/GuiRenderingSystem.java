package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.components.GuiComponent;
import nz.kiwi.loomans.canyoudigit.stages.MapStage;
import nz.kiwi.loomans.canyoudigit.stages.TreasureStage;
import nz.kiwi.loomans.canyoudigit.states.GuiState;

public class GuiRenderingSystem extends IteratingSystem {
    public StateMachine<GuiRenderingSystem, GuiState> fsm;
    private ComponentMapper<GuiComponent> guiMap;
    private ComponentMapper<EnergyComponent> nrgMap;

    public HashMap<String, Stage> stages = new HashMap<String, Stage>();
    private int player;
    private AssetManager man;

    public GuiRenderingSystem(int player, AssetManager manager) {
        super(Aspect.all(GuiComponent.class));
        fsm = new DefaultStateMachine<GuiRenderingSystem, GuiState>(this, GuiState.MAP);
        this.player = player;
        man = manager;
    }

    @Override
    protected void initialize() {
        super.initialize();
        // creating dummy component to trigger processing
        // TODO: figure out how to do this properly
        int gc = world.create();
        guiMap.create(gc);

        EnergyComponent e = nrgMap.get(player);
        stages.put("MAP", new MapStage(e, man));
        stages.put("TREASURE", new TreasureStage(e, man));
    }

    @Override
    protected void process(int entityId) {
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
