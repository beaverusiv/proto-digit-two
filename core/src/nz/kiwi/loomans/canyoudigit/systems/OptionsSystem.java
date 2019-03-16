package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import nz.kiwi.loomans.canyoudigit.files.SettingsFile;

public class OptionsSystem extends BaseSystem {
    public SettingsFile settings;

    public void init() {
        FileHandle settingsHandle = Gdx.files.local("settings.json");
        if (settingsHandle.exists()) {
            Json json = new Json();
            settings = json.fromJson(SettingsFile.class, settingsHandle);
        } else {
            System.out.println("settings file not found");
            settings = new SettingsFile();
            // TODO: should there be a default settings file instead?
            // defaults
            settings.musicVolume = 1;
            settings.soundVolume = 1;
        }
    }

    @Override
    protected void dispose() {
        save();
    }

    @Override
    protected void processSystem() {
        //
    }

    public void save() {
        System.out.println("saving settings");
        FileHandle settingsHandle = Gdx.files.local("settings.json");
        Json json = new Json();
        settingsHandle.writeString(json.toJson(settings), false);
    }
}
