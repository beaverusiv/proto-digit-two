package nz.kiwi.loomans.canyoudigit.screens;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import nz.kiwi.loomans.canyoudigit.systems.CameraSystem;
import nz.kiwi.loomans.canyoudigit.systems.EnergySystem;
import nz.kiwi.loomans.canyoudigit.systems.GuiRenderingSystem;
import nz.kiwi.loomans.canyoudigit.systems.InputSystem;
import nz.kiwi.loomans.canyoudigit.systems.MapSystem;
import nz.kiwi.loomans.canyoudigit.systems.MovingSystem;
import nz.kiwi.loomans.canyoudigit.systems.CharacterRenderingSystem;

public class PlayScreen implements Screen {
    private World world;
    private CameraSystem cameraSystem = new CameraSystem();

    private final AssetManager manager = new AssetManager();

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.setDelta(delta);
        world.process();
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    @Override
    public void resize(int width, int height) {
        cameraSystem.resize(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void show() {
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("ui/uiskin.atlas");
        manager.load("ui/uiskin.json", Skin.class, params);

        CharacterRenderingSystem characterRenderingSystem = new CharacterRenderingSystem();

        int player = characterRenderingSystem.getPlayer();

        WorldConfiguration config = new WorldConfigurationBuilder()
            .with(cameraSystem)
            .with(new MapSystem())
            .with(characterRenderingSystem)
            .with(new InputSystem(player))
            .with(new MovingSystem())
            .with(new GuiRenderingSystem(player, manager))
            .with(new EnergySystem())
            .build();
        world = new World(config);
    }
}
