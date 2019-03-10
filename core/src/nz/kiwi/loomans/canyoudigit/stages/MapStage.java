package nz.kiwi.loomans.canyoudigit.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;

public class MapStage extends Stage {
    private Label energyLabel;
    private TextButton treasureButton;
    private EnergyComponent energyCmp;
    private Skin skin;

    public boolean transitionToTreasure = false;

    public MapStage(EnergyComponent energyComponent, AssetManager manager) {
        manager.finishLoading();
        energyCmp = energyComponent;
        skin = manager.get("ui/uiskin.json");
        // TODO: figure out how to set input processing for hud and map
        Gdx.input.setInputProcessor(this);

        energyLabel = new Label("Energy Label", skin);
        energyLabel.setSize(Gdx.graphics.getWidth(),50);
        energyLabel.setPosition(0,Gdx.graphics.getHeight()-50*2);
        energyLabel.setAlignment(Align.center);
        addActor(energyLabel);

        treasureButton = new TextButton("Treasure", skin);

        treasureButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Transition to TREASURE");
                transitionToTreasure = true;
            }
        });
        addActor(treasureButton);
    }

    public void draw() {
        energyLabel.setText(energyCmp.level + "/" + energyCmp.max);
        super.draw();
    }
}
