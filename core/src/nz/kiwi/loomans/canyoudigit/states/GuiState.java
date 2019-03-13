package nz.kiwi.loomans.canyoudigit.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import nz.kiwi.loomans.canyoudigit.stages.MapStage;
import nz.kiwi.loomans.canyoudigit.stages.TreasureStage;
import nz.kiwi.loomans.canyoudigit.systems.GuiRenderingSystem;

public enum GuiState implements State<GuiRenderingSystem> {

    MAP() {
        @Override
        public void enter(GuiRenderingSystem system) {
            MapStage stage = (MapStage)system.stages.get("MAP");
            stage.enter();
        }

        @Override
        public void exit(GuiRenderingSystem system) {
            MapStage stage = (MapStage)system.stages.get("MAP");
            stage.exit();
        }

        @Override
        public void update(GuiRenderingSystem system) {
            MapStage stage = (MapStage)system.stages.get("MAP");
            stage.draw();
        }

        @Override
        public boolean onMessage(GuiRenderingSystem system, Telegram telegram) {
            if (telegram.message == GuiState.TREASURE.ordinal()) {
                system.fsm.changeState(GuiState.TREASURE);
            }
            return false;
        }
    },

    TREASURE() {
        @Override
        public void enter(GuiRenderingSystem system) {
            TreasureStage stage = (TreasureStage) system.stages.get("TREASURE");
            stage.enter();
        }

        @Override
        public void exit(GuiRenderingSystem system) {
            TreasureStage stage = (TreasureStage)system.stages.get("TREASURE");
            stage.exit();
        }

        @Override
        public void update(GuiRenderingSystem system) {
            TreasureStage stage = (TreasureStage)system.stages.get("TREASURE");
            stage.draw();
        }

        @Override
        public boolean onMessage(GuiRenderingSystem system, Telegram telegram) {
            if (telegram.message == GuiState.MAP.ordinal()) {
                system.fsm.changeState(GuiState.MAP);
            }
            return false;
        }
    };

    @Override
    public void enter(GuiRenderingSystem system) {

    }

    @Override
    public void update(GuiRenderingSystem system) {

    }

    @Override
    public void exit(GuiRenderingSystem system) {

    }

    @Override
    public boolean onMessage(GuiRenderingSystem system, Telegram telegram) {
        return false;
    }
}
