package nz.kiwi.loomans.canyoudigit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import nz.kiwi.loomans.canyoudigit.CanYouDigIt;

public class OptionsScreen implements Screen {
    private CanYouDigIt parent;
    private Stage stage;
    private Skin skin;
    private Label titleLabel;
    private Label volumeMusicLabel;
    private Label volumeSoundLabel;

    public OptionsScreen(CanYouDigIt game) {
        parent = game;
        stage = new Stage(new ScreenViewport());
        skin = parent.assetSystem.manager.get("ui/uiskin.json");
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        stage.clear();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        final Slider volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeMusicSlider.setValue( parent.optionsSystem.settings.musicVolume );
        volumeMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                parent.optionsSystem.settings.musicVolume = volumeMusicSlider.getValue();
                return false;
            }
        });

        final Slider soundMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        soundMusicSlider.setValue( parent.optionsSystem.settings.soundVolume );
        soundMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                parent.optionsSystem.settings.soundVolume = soundMusicSlider.getValue();
                return false;
            }
        });

        final TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MessageManager.getInstance().dispatchMessage(null, parent.fsm, 0);
            }
        });

        titleLabel = new Label( "Preferences", skin );
        volumeMusicLabel = new Label( "Music Volume", skin );
        volumeSoundLabel = new Label( "Sound Volume", skin );

        table.add(titleLabel).colspan(2);
        table.row();
        table.add(volumeMusicLabel).left();
        table.add(volumeMusicSlider);
        table.row();
        table.add(volumeSoundLabel).left();
        table.add(soundMusicSlider);
        table.row();
        table.add(backButton).colspan(2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
