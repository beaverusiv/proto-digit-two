package nz.kiwi.loomans.canyoudigit.stages;

import com.artemis.World;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import nz.kiwi.loomans.canyoudigit.systems.AssetSystem;
import nz.kiwi.loomans.canyoudigit.systems.GuiRenderingSystem;
import nz.kiwi.loomans.canyoudigit.systems.InputSystem;

abstract class BaseStage extends Stage {
    AssetSystem assetSystem;
    GuiRenderingSystem guiRenderingSystem;
    InputSystem inputSystem;

    Skin skin;
    Table table;
    private boolean blocksInput;

    BaseStage(World world, boolean blocksInput) {
        super();
        this.blocksInput = blocksInput;
        world.inject(this);
        skin = assetSystem.manager.get("ui/uiskin.json", Skin.class);
        table = new Table();
        table.setFillParent(true);
        this.addActor(table);
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
        if (blocksInput) {
            inputSystem.setGuiOnlyInput(this);
        } else {
            inputSystem.setGuiInput(this);
        }
    }

    public void exit() {
        inputSystem.removeGuiInput(this);
        if (blocksInput) {
            inputSystem.setMapOnlyInput();
        }
    }
}
