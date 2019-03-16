package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class AssetSystem extends BaseSystem {
    public final AssetManager manager = new AssetManager();
    HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();

    // Sounds
    private final String boingSound = "sounds/boing.wav";
    private final String pingSound = "sounds/ping.wav";

    // Music
    private final String playingSong = "sounds/Rolemusic_-_pl4y1ng.mp3";

    // Skin
    private final String skin = "ui/uiskin.json";

    // Textures
    private final String loadingImages = "atlas/loading.atlas";
    private final String spriteImages = "atlas/sprites.atlas";

    public void queueAddImages(){
        manager.load(spriteImages, TextureAtlas.class);
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
        TextureAtlas atlas = manager.get("atlas/sprites.atlas");
        Animation<TextureRegion> walkDownAnimation = new Animation<>(0.125f, atlas.findRegions("Walk_Down"));
        Animation<TextureRegion> walkLeftAnimation = new Animation<>(0.125f, atlas.findRegions("Walk_Left"));
        Animation<TextureRegion> walkRightAnimation = new Animation<>(0.125f, atlas.findRegions("Walk_Right"));
        Animation<TextureRegion> walkUpAnimation = new Animation<>(0.125f, atlas.findRegions("Walk_Up"));
        Animation<TextureRegion> oceanAnimation = new Animation<>(0.125f, atlas.findRegions("Ocean"));
        animations.put("walk_down", walkDownAnimation);
        animations.put("walk_left", walkLeftAnimation);
        animations.put("walk_right", walkRightAnimation);
        animations.put("walk_up", walkUpAnimation);
        animations.put("ocean", oceanAnimation);
    }

    @Override
    protected void processSystem() {
        // TODO: maybe some background loading or memory management?
    }
}
