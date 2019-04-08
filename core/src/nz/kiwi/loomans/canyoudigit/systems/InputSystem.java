package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import nz.kiwi.loomans.canyoudigit.components.InputComponent;
import nz.kiwi.loomans.canyoudigit.components.TouchDownComponent;

import static nz.kiwi.loomans.canyoudigit.systems.InputActionType.*;

public class InputSystem extends IteratingSystem implements InputProcessor {
    private ComponentMapper<TouchDownComponent> touchDownMap;

    private InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private InputAction action = new InputAction();

    public InputSystem() {
        super(Aspect.all(InputComponent.class));
    }

    @Override
    protected void initialize() {
        setMapOnlyInput();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    protected void end() {
        action.type = null;
    }

    @Override
    protected void process(int entityId) {
        if (action.type == null) {
            return;
        }

        switch (action.type) {
            case TOUCH_DOWN:
                TouchDownComponent touchDownComponent = touchDownMap.get(entityId);
                if (touchDownComponent == null) {
                    touchDownComponent = touchDownMap.create(entityId);
                }
                touchDownComponent.x = action.x;
                touchDownComponent.y = action.y;
            break;
        }
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
        action.type = TOUCH_DOWN;
        action.x = screenX - 256;
        action.y = screenY - 507;
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
