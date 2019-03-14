package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.components.TextureComponent;

public class CharacterRenderingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<AnimationComponent> aniMap;
    private ComponentMapper<TextureComponent> texMap;

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private SpriteBatch sb = new SpriteBatch();
    private float stateTime = 0f;

    public CharacterRenderingSystem() {
        super(Aspect.all(PositionComponent.class, TextureComponent.class, AnimationComponent.class));
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
        final TextureComponent tex = texMap.get(entityId);

        stateTime += Gdx.graphics.getDeltaTime();

        if (anim != null && anim.name != null && anim.running) {
            TextureRegion currentFrame = assetSystem.animations.get(anim.name).getKeyFrame(stateTime, true);

            sb.draw(
                    currentFrame,
                    pos.position.x,
                    pos.position.y
            );
        } else {
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
}
