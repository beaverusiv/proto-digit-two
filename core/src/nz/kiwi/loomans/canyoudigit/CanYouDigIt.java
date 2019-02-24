package nz.kiwi.loomans.canyoudigit;

import com.badlogic.gdx.Game;

import nz.kiwi.loomans.canyoudigit.screens.PlayScreen;

public class CanYouDigIt extends Game {
	@Override
	public void create () {
		setScreen(new PlayScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
