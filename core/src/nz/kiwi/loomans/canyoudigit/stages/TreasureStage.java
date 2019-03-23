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

public class TreasureStage extends BaseStage {
    private PlayerSystem playerSystem;
    private ComponentMapper<EnergyComponent> nrgMap;

    private Label energyLabel;

    public TreasureStage(World world) {
        super(world, true);

        energyLabel = new Label("Energy Label", skin);
        energyLabel.setSize(Gdx.graphics.getWidth(),50);
        energyLabel.setPosition(0,Gdx.graphics.getHeight()-50*2);
        energyLabel.setAlignment(Align.center);

        TextButton treasureButton = new TextButton("Map", skin);
        treasureButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            sendMessage(GuiState.MAP.ordinal());
            }
        });

        table.add(energyLabel).expand().top().colspan(1);
        table.row();
        table.add(treasureButton);
    }

    public void draw() {
        EnergyComponent energyCmp = nrgMap.get(playerSystem.player);
        energyLabel.setText(energyCmp.level + "/" + energyCmp.max);
        super.draw();
    }
}
