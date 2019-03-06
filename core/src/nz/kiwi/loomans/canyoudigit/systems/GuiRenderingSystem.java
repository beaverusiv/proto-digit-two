package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nz.kiwi.loomans.canyoudigit.components.GuiComponent;

public class GuiRenderingSystem extends IteratingSystem {
    private ComponentMapper<GuiComponent> guiMap;
    private CameraSystem cameraSystem;

    private SpriteBatch sb = new SpriteBatch();
    private Texture texture;

    public GuiRenderingSystem() {
        super(Aspect.all(GuiComponent.class));
        texture = new Texture(Gdx.files.internal("ui/uipack_rpg_sheet.png"));
    }

    @Override
    protected void initialize() {
        super.initialize();
        cameraSystem.hudCamera.setToOrtho(true);
        // creating dummy component to trigger processing
        int gc = world.create();
        guiMap.create(gc);
    }

    @Override
    protected void process(int entityId) {
        sb.draw(
            texture,
            0,
            0,
            0,
            0,
            192,
            50
        );
    }

    @Override
    protected void dispose() {
        super.dispose();
        texture.dispose();
        sb.dispose();
    }

    @Override
    protected void begin() {
        super.begin();
        sb.setProjectionMatrix(cameraSystem.hudCamera.combined);
        cameraSystem.hudCamera.update();
        sb.begin();
    }

    @Override
    protected void end() {
        super.end();
        sb.end();
    }
}
