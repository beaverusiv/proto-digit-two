package nz.kiwi.loomans.canyoudigit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import nz.kiwi.loomans.canyoudigit.stages.MapStage;
import nz.kiwi.loomans.canyoudigit.stages.TreasureStage;
import nz.kiwi.loomans.canyoudigit.systems.GuiRenderingSystem;

public enum GuiState implements State<GuiRenderingSystem> {

    MAP() {
        @Override
        public void enter(GuiRenderingSystem system) {
            System.out.println("MAP entering");
            MapStage stage = (MapStage)system.stages.get("MAP");
            stage.transitionToTreasure = false;
            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void update(GuiRenderingSystem system) {
            System.out.println("MAP updating");
            MapStage stage = (MapStage)system.stages.get("MAP");
            stage.draw();
            if (stage.transitionToTreasure) {
                system.fsm.changeState(GuiState.TREASURE);
            }
        }
    },

    TREASURE() {
        @Override
        public void enter(GuiRenderingSystem system) {
            System.out.println("TREASURE entering");
            TreasureStage stage = (TreasureStage) system.stages.get("TREASURE");
            stage.transitionToMap = false;
            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void update(GuiRenderingSystem system) {
            System.out.println("TREASURE updating");
            TreasureStage stage = (TreasureStage)system.stages.get("TREASURE");
            stage.draw();
            if (stage.transitionToMap) {
                system.fsm.changeState(GuiState.MAP);
            }
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
