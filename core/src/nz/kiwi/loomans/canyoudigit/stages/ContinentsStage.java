package nz.kiwi.loomans.canyoudigit.stages;

import com.artemis.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import nz.kiwi.loomans.canyoudigit.states.GuiState;
import nz.kiwi.loomans.canyoudigit.systems.PlayerSystem;

public class ContinentsStage extends BaseStage {
    private PlayerSystem playerSystem;

    public ContinentsStage(World world) {
        super(world);

        TextButton treasureButton = new TextButton("Map", skin);

        treasureButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            sendMessage(GuiState.MAP.ordinal());
            }
        });
        table.add(treasureButton);
    }

    public void draw() {
        super.draw();
    }
}
