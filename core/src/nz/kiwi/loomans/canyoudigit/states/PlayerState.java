package nz.kiwi.loomans.canyoudigit.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;

import nz.kiwi.loomans.canyoudigit.systems.MovingSystem;
import nz.kiwi.loomans.canyoudigit.systems.PlayerSystem;

public enum PlayerState implements State<PlayerSystem> {

    IDLE() {
        @Override
        public void enter(PlayerSystem system) {
            System.out.println("entering IDLE");
            system.inputMap.get(system.player).acceptingInput = true;
        }

        @Override
        public boolean onMessage(PlayerSystem system, Telegram telegram) {
            System.out.println("got message in IDLE: " + telegram.message + " " + IDLE.ordinal());
            if (telegram.message == WALKING.ordinal()) {
                system.fsm.changeState(WALKING);
                return true;
            }
            return false;
        }
    },

    WALKING() {
        @Override
        public void enter(PlayerSystem system) {
            System.out.println("entering WALKING");
            system.inputMap.get(system.player).acceptingInput = false;
            MessageManager.getInstance().dispatchMessage(MovingSystem.STEP_DURATION, system, system.fsm, IDLE.ordinal());
        }

        @Override
        public boolean onMessage(PlayerSystem system, Telegram telegram) {
            System.out.println("got message in WALKING: " + telegram.message + " " + IDLE.ordinal());
            if (telegram.message == IDLE.ordinal()) {
                system.fsm.changeState(IDLE);
                return true;
            }
            return false;
        }
    },

    DIGGING() {
        @Override
        public void enter(PlayerSystem system) {
            System.out.println("entering DIGGING");
            system.inputMap.get(system.player).acceptingInput = false;
            MessageManager.getInstance().dispatchMessage(MovingSystem.STEP_DURATION, system, system.fsm, IDLE.ordinal());
        }

        @Override
        public boolean onMessage(PlayerSystem system, Telegram telegram) {
            System.out.println("got message in WALKING: " + telegram.message + " " + IDLE.ordinal());
            if (telegram.message == IDLE.ordinal()) {
                system.nrgMap.get(system.player).level -= 20;
                system.fsm.changeState(IDLE);
                return true;
            }
            return false;
        }
    };

    @Override
    public void enter(PlayerSystem system) {

    }

    @Override
    public void update(PlayerSystem system) {

    }

    @Override
    public void exit(PlayerSystem system) {

    }

    @Override
    public boolean onMessage(PlayerSystem system, Telegram telegram) {
        System.out.println("base onMessage()");
        return false;
    }
}
