package nz.kiwi.loomans.canyoudigit.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.states.GuiState;
import nz.kiwi.loomans.canyoudigit.systems.GuiRenderingSystem;
import nz.kiwi.loomans.canyoudigit.systems.InputSystem;

public class MapStage extends BaseStage {
    private Label energyLabel;
    private TextButton treasureButton;
    private EnergyComponent energyCmp;

    public MapStage(EnergyComponent energyComponent, Skin skin, GuiRenderingSystem guiRenderingSystem, InputSystem inputSystem) {
        this.guiRenderingSystem = guiRenderingSystem;
        this.inputSystem = inputSystem;
        energyCmp = energyComponent;

        energyLabel = new Label("Energy Label", skin);
        energyLabel.setSize(Gdx.graphics.getWidth(),50);
        energyLabel.setPosition(0,Gdx.graphics.getHeight()-50*2);
        energyLabel.setAlignment(Align.center);
        addActor(energyLabel);

        treasureButton = new TextButton("Treasure", skin);

        treasureButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            sendMessage(GuiState.TREASURE.ordinal());
            }
        });
        addActor(treasureButton);

        // This is the default stage
        this.enter();
    }

    public void draw() {
        energyLabel.setText(energyCmp.level + "/" + energyCmp.max);
        super.draw();
    }
}
