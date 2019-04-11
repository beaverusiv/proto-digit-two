package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.components.TextureComponent;

public class TextureRenderingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<TextureComponent> texMap;

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private SpriteBatch sb = new SpriteBatch();

    public TextureRenderingSystem() {
        super(Aspect.all(PositionComponent.class, TextureComponent.class));
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void dispose() {
        super.dispose();
        sb.dispose();
    }

    @Override
    protected void begin() {
        super.begin();
        cameraSystem.mapCamera.update();
        sb.setProjectionMatrix(cameraSystem.mapCamera.combined);
        sb.begin();
    }

    @Override
    protected void end() {
        super.end();
        sb.end();
    }

    @Override
    protected void process(int entityId) {
        final PositionComponent pos = posMap.get(entityId);
        final TextureComponent tex = texMap.get(entityId);

        sb.draw(
            assetSystem.manager.get(tex.name),
            pos.position.x,
            pos.position.y,
            (int)tex.origin.x,
            (int)tex.origin.y,
            (int)tex.dimensions.x,
            (int)tex.dimensions.y
        );
    }
}
