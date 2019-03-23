package nz.kiwi.loomans.canyoudigit.stages;

import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import nz.kiwi.loomans.canyoudigit.states.GuiState;
import nz.kiwi.loomans.canyoudigit.systems.AssetSystem;
import nz.kiwi.loomans.canyoudigit.systems.PlayerSystem;

public class ContinentsStage extends BaseStage {
    private PlayerSystem playerSystem;
    private AssetSystem assetSystem;

    public ContinentsStage(World world) {
        super(world, true);

        TextButton treasureButton = new TextButton("Map", skin);
        treasureButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            sendMessage(GuiState.MAP.ordinal());
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sendMessage(GuiState.MAP.ordinal());
            }
        });

        TextureRegionDrawable islandOneImage = new TextureRegionDrawable(assetSystem.manager.get("maps/tutorial/first_preview.png", Texture.class));
        ImageButton islandOneButton = new ImageButton(islandOneImage);

        TextureRegionDrawable islandTwoImage = new TextureRegionDrawable(assetSystem.manager.get("maps/tutorial/first_preview.png", Texture.class));
        ImageButton islandTwoButton = new ImageButton(islandTwoImage);

        TextureRegionDrawable islandThreeImage = new TextureRegionDrawable(assetSystem.manager.get("maps/tutorial/first_preview.png", Texture.class));
        ImageButton islandThreeButton = new ImageButton(islandThreeImage);

        Window window = new Window("Select Stage", skin);
        window.add(islandOneButton).expandY();
        window.add(islandTwoButton).expandY();
        window.row();
        window.add(islandThreeButton).expandY().colspan(2);
        window.row();
        window.add(backButton).expandX().colspan(2).bottom().right();

        table.add(window).expand();
        table.row();
        table.add(treasureButton).expand().bottom();
    }

    public void draw() {
        super.draw();
    }
}
