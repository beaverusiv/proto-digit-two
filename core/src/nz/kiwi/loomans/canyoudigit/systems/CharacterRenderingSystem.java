package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.components.TextureComponent;

public class CharacterRenderingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<AnimationComponent> aniMap;
    private ComponentMapper<TextureComponent> texMap;

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();
    private SpriteBatch sb = new SpriteBatch();
    private float stateTime = 0f;

    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 4;

    public CharacterRenderingSystem() {
        super(Aspect.all(PositionComponent.class, TextureComponent.class, AnimationComponent.class));
    }

    @Override
    protected void initialize() {
        // TODO: move to asset system and load via asset manager
        Texture texture = assetSystem.manager.get("sprites/char.png");
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        TextureRegion[] walkDownFrames = new TextureRegion[FRAME_COLS];
        System.arraycopy(tmp[0], 0, walkDownFrames, 0, FRAME_COLS);

        TextureRegion[] walkLeftFrames = new TextureRegion[FRAME_COLS];
        System.arraycopy(tmp[1], 0, walkLeftFrames, 0, FRAME_COLS);

        TextureRegion[] walkRightFrames = new TextureRegion[FRAME_COLS];
        System.arraycopy(tmp[2], 0, walkRightFrames, 0, FRAME_COLS);

        TextureRegion[] walkUpFrames = new TextureRegion[FRAME_COLS];
        System.arraycopy(tmp[3], 0, walkUpFrames, 0, FRAME_COLS);

        Animation<TextureRegion> walkDownAnimation = new Animation<TextureRegion>(0.125f, walkDownFrames);
        Animation<TextureRegion> walkLeftAnimation = new Animation<TextureRegion>(0.125f, walkLeftFrames);
        Animation<TextureRegion> walkRightAnimation = new Animation<TextureRegion>(0.125f, walkRightFrames);
        Animation<TextureRegion> walkUpAnimation = new Animation<TextureRegion>(0.125f, walkUpFrames);
        animations.put("walk_down", walkDownAnimation);
        animations.put("walk_left", walkLeftAnimation);
        animations.put("walk_right", walkRightAnimation);
        animations.put("walk_up", walkUpAnimation);
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

        if (anim != null && anim.name != null) {
            TextureRegion currentFrame = animations.get(anim.name).getKeyFrame(stateTime, true);

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
