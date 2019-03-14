package nz.kiwi.loomans.canyoudigit.stages;

import com.artemis.World;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import nz.kiwi.loomans.canyoudigit.systems.AssetSystem;
import nz.kiwi.loomans.canyoudigit.systems.GuiRenderingSystem;
import nz.kiwi.loomans.canyoudigit.systems.InputSystem;

abstract class BaseStage extends Stage {
    AssetSystem assetSystem;
    GuiRenderingSystem guiRenderingSystem;
    InputSystem inputSystem;

    Skin skin;

    BaseStage(World world) {
        super();
        world.inject(this);
        skin = assetSystem.manager.get("ui/uiskin.json", Skin.class);
    }

    void sendMessage(int msg) {
        MessageManager.getInstance().dispatchMessage(
                0f,
                null,
                this.guiRenderingSystem.fsm,
                msg,
                null);
    }

    public void enter() {
        inputSystem.inputMultiplexer.addProcessor(this);
    }

    public void exit() {
        inputSystem.inputMultiplexer.removeProcessor(this);
    }
}
