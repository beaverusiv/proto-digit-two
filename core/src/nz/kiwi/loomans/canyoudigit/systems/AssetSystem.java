package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetSystem extends BaseSystem {
    public final AssetManager manager = new AssetManager();

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

    public void queueAddParticleEffects(){
    }

    @Override
    protected void processSystem() {
        // TODO: maybe some background loading or memory management?
    }
}
