package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.components.GuiComponent;
import nz.kiwi.loomans.canyoudigit.stages.MapStage;
import nz.kiwi.loomans.canyoudigit.stages.TreasureStage;
import nz.kiwi.loomans.canyoudigit.states.GuiState;

public class GuiRenderingSystem extends IteratingSystem implements Telegraph {
    public StateMachine<GuiRenderingSystem, GuiState> fsm;
    private ComponentMapper<GuiComponent> guiMap;
    private ComponentMapper<EnergyComponent> nrgMap;
    private InputSystem inputSystem;

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
        man.finishLoading();

        EnergyComponent e = nrgMap.get(player);
        stages.put("MAP", new MapStage(e, man.get("ui/uiskin.json"), this, inputSystem));
        stages.put("TREASURE", new TreasureStage(e, man.get("ui/uiskin.json"), this, inputSystem));
    }

    @Override
    protected void process(int entityId) {
        MessageManager.getInstance().update();
        fsm.update();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return fsm.handleMessage(msg);
    }

    @Override
    protected void dispose() {
        super.dispose();
        for (Map.Entry<String, Stage> item : stages.entrySet()) {
            item.getValue().dispose();
        }
    }
}
