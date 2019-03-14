package nz.kiwi.loomans.canyoudigit.stages;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.states.GuiState;
import nz.kiwi.loomans.canyoudigit.systems.PlayerSystem;

public class MapStage extends BaseStage {
    private PlayerSystem playerSystem;
    private ComponentMapper<EnergyComponent> nrgMap;

    private Label energyLabel;
    private TextButton treasureButton;

    public MapStage(World world) {
        super(world);

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
    }

    public void draw() {
        EnergyComponent energyCmp = nrgMap.get(playerSystem.player);
        energyLabel.setText(energyCmp.level + "/" + energyCmp.max);
        super.draw();
    }
}
