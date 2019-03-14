package nz.kiwi.loomans.canyoudigit.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import nz.kiwi.loomans.canyoudigit.CanYouDigIt;
import nz.kiwi.loomans.canyoudigit.screens.LoadingScreen;
import nz.kiwi.loomans.canyoudigit.screens.MenuScreen;
import nz.kiwi.loomans.canyoudigit.screens.PlayScreen;

public enum GameState implements State<CanYouDigIt> {
    LOADING() {
        @Override
        public void update(CanYouDigIt game) {
            if (game.getScreen() == null) {
                game.setScreen(new LoadingScreen(game));
            }
        }

        @Override
        public boolean onMessage(CanYouDigIt game, Telegram telegram) {
            game.fsm.changeState(GameState.MENU);
            return true;
        }
    },

    MENU() {
        @Override
        public void enter(CanYouDigIt game) {
            if(game.menuScreen == null) {
                game.menuScreen = new MenuScreen(game);
            }
            game.setScreen(game.menuScreen);
        }

        @Override
        public boolean onMessage(CanYouDigIt game, Telegram telegram) {
            if (telegram.message == GameState.GAME.ordinal()) {
                game.fsm.changeState(GameState.GAME);
            }
            return true;
        }
    },

    GAME() {
        @Override
        public void enter(CanYouDigIt game) {
            if(game.playScreen == null) {
                game.playScreen = new PlayScreen(game);
            }
            game.setScreen(game.playScreen);
        }
    };

    @Override
    public void enter(CanYouDigIt game) {

    }

    @Override
    public void update(CanYouDigIt game) {

    }

    @Override
    public void exit(CanYouDigIt game) {

    }

    @Override
    public boolean onMessage(CanYouDigIt game, Telegram telegram) {
        return false;
    }
}
