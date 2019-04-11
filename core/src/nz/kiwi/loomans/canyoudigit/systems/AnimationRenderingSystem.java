package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;

public class AnimationRenderingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<AnimationComponent> aniMap;

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private SpriteBatch sb = new SpriteBatch();
    private float stateTime = 0f;

    public AnimationRenderingSystem() {
        super(Aspect.all(PositionComponent.class, AnimationComponent.class));
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
        final AnimationComponent anim = aniMap.get(entityId);

        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = assetSystem.animations.get(anim.name).getKeyFrame(stateTime, true);

        sb.draw(currentFrame, pos.position.x, pos.position.y);
    }
}
