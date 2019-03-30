package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;
import nz.kiwi.loomans.canyoudigit.components.MapComponent;
import nz.kiwi.loomans.canyoudigit.files.SavedGameFile;

public class SaveGameSystem extends BaseSystem {
    private PlayerSystem playerSystem;
    private MapSystem mapSystem;

    private ComponentMapper<EnergyComponent> nrgMap;
    private ComponentMapper<MapComponent> mapMap;

    SavedGameFile save;
    private float stateTime = 0f;

    private final float saveInterval = 10f;

    @Override
    protected void processSystem() {
        stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime > saveInterval) {
            save();
            stateTime = 0f;
        }
    }

    private void save() {
        System.out.println("saving game state");
        save.energy = nrgMap.get(playerSystem.player);
        save.mapData = mapMap.get(mapSystem.map);
        FileHandle saveHandle = Gdx.files.local("save.json");
        Json json = new Json();
        saveHandle.writeString(json.toJson(save), false);
        System.out.println(json.toJson(save));
    }

    public void init() {
        System.out.println("loading game state");
        FileHandle saveHandle = Gdx.files.local("save.json");
        if (saveHandle.exists()) {
            System.out.println("loading game state file");
            Json json = new Json();
            save = json.fromJson(SavedGameFile.class, saveHandle);
            System.out.println("energy: " + save.energy.level);
        } else {
            System.out.println("save file not found");
            save = new SavedGameFile();
            // TODO: should there be a default save file instead?
            // defaults
            save.mapData = null;
            save.energy = new EnergyComponent();
            save.energy.level = 0;
            save.energy.max = 110;
            save.energy.lastIncrement = 0;
            save.energy.incrementInterval = 1;
        }
    }
}
