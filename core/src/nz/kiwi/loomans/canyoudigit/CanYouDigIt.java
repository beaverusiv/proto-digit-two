package nz.kiwi.loomans.canyoudigit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.audio.Music;

import nz.kiwi.loomans.canyoudigit.screens.PlayScreen;
import nz.kiwi.loomans.canyoudigit.states.GameState;
import nz.kiwi.loomans.canyoudigit.systems.AssetSystem;
import nz.kiwi.loomans.canyoudigit.systems.OptionsSystem;
import nz.kiwi.loomans.canyoudigit.systems.SaveGameSystem;

public class CanYouDigIt extends Game {
    public StateMachine<CanYouDigIt, GameState> fsm;
    public PlayScreen playScreen;
    public AssetSystem assetSystem = new AssetSystem();
    public OptionsSystem optionsSystem = new OptionsSystem();
    public SaveGameSystem saveGameSystem = new SaveGameSystem();
    private Music playingSong;

    @Override
    public void create () {
        fsm = new DefaultStateMachine<>(this, GameState.LOADING);

        optionsSystem.init();

        assetSystem.queueAddMusic();
        assetSystem.manager.finishLoading();
        playingSong = assetSystem.manager.get("sounds/Rolemusic_-_pl4y1ng.mp3");

        playingSong.play();
    }

    @Override
    public void render () {
        float delta = Gdx.graphics.getDeltaTime();
        GdxAI.getTimepiece().update(delta);
        MessageManager.getInstance().update();
        fsm.update();
        super.render();
    }

    @Override
    public void dispose () {
        super.dispose();
        playingSong.dispose();
        assetSystem.manager.dispose();
    }
}
