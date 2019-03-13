package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import nz.kiwi.loomans.canyoudigit.components.InputComponent;
import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;

public class InputSystem extends IteratingSystem implements InputProcessor {
    private ComponentMapper<InputComponent> inputMap;
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<MovingComponent> moveMap;
    public InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private int player;

    private static final int PLAYER_DIM = 30;
    private static final float TILE_WIDTH = 128;
    private static final float TILE_HEIGHT = 64;

    public InputSystem(int player) {
        super(Aspect.all(InputComponent.class, PositionComponent.class, MovingComponent.class));
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        this.player = player;
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
        MovingComponent m = moveMap.get(player);
        if (m.target == null) {
            m.target = new Vector2(getTileCoords(screenX - 256, screenY - 507));
        }
        return false;
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

    private void setPlayerTileCoords(float x, float y) {
        PositionComponent pos = posMap.get(player);
        pos.position.x = ((x + y) * TILE_WIDTH / 2f) + ((TILE_WIDTH - PLAYER_DIM) / 2f);
        pos.position.y = ((x - y) * TILE_HEIGHT / 2f) + ((TILE_HEIGHT - PLAYER_DIM) / 2f);
    }

    private Vector2 getTileCoords(int x, int y) {
        float x2 = x / TILE_WIDTH - y / TILE_HEIGHT;
        float y2 = y / TILE_HEIGHT + x / TILE_WIDTH;

        return new Vector2((float)Math.floor(x2), (float)Math.floor(y2));
    }
}
