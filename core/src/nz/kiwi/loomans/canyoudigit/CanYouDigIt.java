package nz.kiwi.loomans.canyoudigit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.audio.Music;

import nz.kiwi.loomans.canyoudigit.screens.MenuScreen;
import nz.kiwi.loomans.canyoudigit.screens.PlayScreen;
import nz.kiwi.loomans.canyoudigit.states.GameState;

public class CanYouDigIt extends Game {
    public StateMachine<CanYouDigIt, GameState> fsm;
	public MenuScreen menuScreen;
	public PlayScreen playScreen;
	public AssetManager assMan = new AssetManager();
	private Music playingSong;

	@Override
	public void create () {
	    fsm = new DefaultStateMachine<>(this, GameState.LOADING);

		assMan.queueAddMusic();
		assMan.manager.finishLoading();
		playingSong = assMan.manager.get("sounds/Rolemusic_-_pl4y1ng.mp3");

		playingSong.play();
	}

	@Override
	public void render () {
        MessageManager.getInstance().update();
        fsm.update();
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		playingSong.dispose();
		assMan.manager.dispose();
	}
}
