package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class AssetSystem extends BaseSystem {
    public final AssetManager manager = new AssetManager();
    public HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();

    // Sounds
    public final String boingSound = "sounds/boing.wav";
    public final String pingSound = "sounds/ping.wav";

    // Music
    public final String playingSong = "sounds/Rolemusic_-_pl4y1ng.mp3";

    // Skin
    public final String skin = "ui/uiskin.json";

    // Textures
    public final String gameImages = "images/game.atlas";
    public final String loadingImages = "images/loading.atlas";

    public void queueAddImages(){
        manager.load(gameImages, TextureAtlas.class);
        manager.load("sprites/char.png", Texture.class);
    }

    public void queueAddLoadingImages(){
        manager.load(loadingImages, TextureAtlas.class);
    }

    public void queueAddSounds(){
        manager.load(boingSound, Sound.class);
        manager.load(pingSound, Sound.class);
    }

    public void queueAddMusic(){
        manager.load(playingSong, Music.class);
    }

    public void queueAddSkin(){
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("ui/uiskin.atlas");
        manager.load(skin, Skin.class, params);
    }

    public void queueAddFonts(){
    }

    public void queueAddAnimations(){
        final int FRAME_COLS = 4;
        final int FRAME_ROWS = 4;

        Texture texture = manager.get("sprites/char.png");
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
    protected void processSystem() {
        // TODO: maybe some background loading or memory management?
    }
}
