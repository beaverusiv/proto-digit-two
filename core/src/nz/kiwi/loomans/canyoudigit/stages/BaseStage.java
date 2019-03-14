package nz.kiwi.loomans.canyoudigit.stages;

import com.artemis.World;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.scenes.scene2d.Stage;

import nz.kiwi.loomans.canyoudigit.systems.GuiRenderingSystem;
import nz.kiwi.loomans.canyoudigit.systems.InputSystem;

abstract class BaseStage extends Stage {
    GuiRenderingSystem guiRenderingSystem;
    InputSystem inputSystem;

    public BaseStage(World world) {
        super();
        world.inject(this);
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
