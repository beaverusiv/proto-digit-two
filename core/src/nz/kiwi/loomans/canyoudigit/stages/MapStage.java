package nz.kiwi.loomans.canyoudigit.stages;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.states.GuiState;
import nz.kiwi.loomans.canyoudigit.systems.PlayerSystem;

public class MapStage extends BaseStage {
    private PlayerSystem playerSystem;
    private ComponentMapper<EnergyComponent> nrgMap;

    private Label energyLabel;

    public MapStage(World world) {
        super(world, false);

        energyLabel = new Label("Energy Label", skin);
        energyLabel.setSize(Gdx.graphics.getWidth(),50);
        energyLabel.setPosition(0,Gdx.graphics.getHeight()-50*2);
        energyLabel.setAlignment(Align.center);

        TextButton treasureButton = new TextButton("Treasure", skin);
        treasureButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            sendMessage(GuiState.TREASURE.ordinal());
            }
        });

        TextButton backpackButton = new TextButton("Backpack", skin);

        TextButton continentsButton = new TextButton("Continents", skin);
        continentsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sendMessage(GuiState.CONTINENTS.ordinal());
            }
        });

        table.add(energyLabel).expand().top().colspan(3);
        table.row();
        table.add(treasureButton).bottom().right().padLeft(Value.maxWidth);
        table.add(backpackButton).bottom();
        table.add(continentsButton).bottom().left().padRight(Value.maxWidth);
    }

    public void draw() {
        EnergyComponent energyCmp = nrgMap.get(playerSystem.player);
        energyLabel.setText(energyCmp.level + "/" + energyCmp.max);
        super.draw();
    }
}
