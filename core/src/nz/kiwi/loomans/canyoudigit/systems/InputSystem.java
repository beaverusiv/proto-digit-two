package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.components.InputComponent;
import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;

public class InputSystem extends IteratingSystem implements InputProcessor {
    private ComponentMapper<InputComponent> inputMap;
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<MovingComponent> moveMap;
    private ComponentMapper<EnergyComponent> energyMap;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private PlayerSystem playerSystem;
    private MovingSystem movingSystem;

    public InputSystem() {
        super(Aspect.all(InputComponent.class, PositionComponent.class, MovingComponent.class));
    }

    @Override
    protected void initialize() {
        setMapOnlyInput();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    protected void process(int entityId) {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        MovingComponent m = moveMap.get(playerSystem.player);
        EnergyComponent e = energyMap.get(playerSystem.player);
        InputComponent i = inputMap.get(playerSystem.player);
        PositionComponent p = posMap.get(playerSystem.player);
        // TODO: refactor and check if tile needs energy to move to - part of player fsm
        if (!i.acceptingInput) {
            System.out.println("not processing input");
        }
        if (i.acceptingInput && (m == null || m.movements == null || m.movements.empty()) && e.level >= 20) {
            m = moveMap.create(playerSystem.player);
            movingSystem.setMovements(m, p, new Vector2(movingSystem.getTileCoords(screenX - 256, screenY - 507)));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setGuiInput(InputProcessor input) {
        inputMultiplexer.setProcessors(input, this);
    }

    public void setGuiOnlyInput(InputProcessor input) {
        inputMultiplexer.setProcessors(input);
    }

    public void removeGuiInput(InputProcessor input) {
        inputMultiplexer.removeProcessor(input);
    }

    public void setMapOnlyInput() {
        inputMultiplexer.setProcessors(this);
    }
}
