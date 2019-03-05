package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import nz.kiwi.loomans.canyoudigit.components.AnimationComponent;
import nz.kiwi.loomans.canyoudigit.components.MovingComponent;
import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.components.TextureComponent;

public class RenderingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<AnimationComponent> aniMap;
    private ComponentMapper<TextureComponent> texMap;
    private ComponentMapper<MovingComponent> movMap;

    private OrthographicCamera camera;
    private int player;
    private HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();
    private HashMap<String, Texture> textures = new HashMap<String, Texture>();
    private SpriteBatch sb = new SpriteBatch();
    private float stateTime = 0f;

    private static final int PLAYER_DIM = 30;
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 4;
    private static final float TILE_WIDTH = 128;
    private static final float TILE_HEIGHT = 64;

    public RenderingSystem(OrthographicCamera cam) {
        super(Aspect.all(PositionComponent.class, TextureComponent.class, MovingComponent.class));
        camera = cam;
    }

    @Override
    protected void initialize() {
        player = world.create();
        PositionComponent pos = posMap.create(player);
        TextureComponent tex = texMap.create(player);
        MovingComponent mov = movMap.create(player);

        pos.position = new Vector2(0, 0);
        setPlayerTileCoords(0, 0);
        tex.dimensions = new Vector2(30, 60);
        tex.origin = new Vector2(0, 0);
        tex.name = "player";
        mov.target = null;
        Texture texture = new Texture(Gdx.files.internal("sprites/char.png"));
        textures.put(tex.name, texture);

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
        for (Map.Entry<String, Texture> item : textures.entrySet()) {
            item.getValue().dispose();
        }
        sb.dispose();
    }

    @Override
    protected void begin() {
        super.begin();
        camera.update();
        sb.setProjectionMatrix(camera.combined);
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

        if (anim != null) {
            TextureRegion currentFrame = animations.get(anim.name).getKeyFrame(stateTime, true);

            sb.draw(
                    currentFrame,
                    pos.position.x,
                    pos.position.y
            );
        } else {
            sb.draw(
                textures.get(tex.name),
                pos.position.x,
                pos.position.y,
                (int)tex.origin.x,
                (int)tex.origin.y,
                (int)tex.dimensions.x,
                (int)tex.dimensions.y
            );
        }
    }

    public int getPlayer() {
        return player;
    }

    private void setPlayerTileCoords(int x, int y) {
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
