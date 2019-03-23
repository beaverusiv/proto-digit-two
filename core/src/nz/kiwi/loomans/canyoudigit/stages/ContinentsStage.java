package nz.kiwi.loomans.canyoudigit.stages;

import com.artemis.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import nz.kiwi.loomans.canyoudigit.states.GuiState;
import nz.kiwi.loomans.canyoudigit.systems.PlayerSystem;

public class ContinentsStage extends BaseStage {
    private PlayerSystem playerSystem;

    public ContinentsStage(World world) {
        super(world, true);

        TextButton treasureButton = new TextButton("Map", skin);
        treasureButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            sendMessage(GuiState.MAP.ordinal());
            }
        });

        TextButton islandOneButton = new TextButton("Island One", skin);

        TextButton islandTwoButton = new TextButton("Island Two", skin);

        TextButton islandThreeButton = new TextButton("Island Three", skin);

        Window window = new Window("Select Stage", skin);
        window.add(islandOneButton).expandX();
        window.row();
        window.add(islandTwoButton);
        window.row();
        window.add(islandThreeButton);

        table.add(window).expand();
        table.row();
        table.add(treasureButton).expand().bottom();
    }

    public void draw() {
        super.draw();
    }
}
