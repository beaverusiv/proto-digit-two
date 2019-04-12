package nz.kiwi.loomans.canyoudigit.screens;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import nz.kiwi.loomans.canyoudigit.CanYouDigIt;
import nz.kiwi.loomans.canyoudigit.systems.ActorSystem;
import nz.kiwi.loomans.canyoudigit.systems.AnimationRenderingSystem;
import nz.kiwi.loomans.canyoudigit.systems.CameraSystem;
import nz.kiwi.loomans.canyoudigit.systems.EnergySystem;
import nz.kiwi.loomans.canyoudigit.systems.GuiRenderingSystem;
import nz.kiwi.loomans.canyoudigit.systems.InputSystem;
import nz.kiwi.loomans.canyoudigit.systems.MapSystem;
import nz.kiwi.loomans.canyoudigit.systems.MovingSystem;
import nz.kiwi.loomans.canyoudigit.systems.TextureRenderingSystem;
import nz.kiwi.loomans.canyoudigit.systems.PlayerSystem;

public class PlayScreen implements Screen {
    private CameraSystem cameraSystem = new CameraSystem();
    private World world;
    private CanYouDigIt parent;

    public PlayScreen(CanYouDigIt game) {
        parent = game;
    }

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
        WorldConfiguration config = new WorldConfigurationBuilder()
            .with(parent.optionsSystem)
            .with(parent.assetSystem)
            .with(parent.saveGameSystem)
            .with(cameraSystem)
            .with(new PlayerSystem())
            .with(new MapSystem())
            .with(new TextureRenderingSystem())
            .with(new AnimationRenderingSystem())
            .with(new ActorSystem())
            .with(new InputSystem())
            .with(new MovingSystem())
            .with(new GuiRenderingSystem())
            .with(new EnergySystem())
            .build();
        world = new World(config);
    }
}
