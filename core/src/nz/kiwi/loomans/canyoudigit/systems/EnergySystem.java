package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;

import nz.kiwi.loomans.canyoudigit.components.EnergyComponent;

public class EnergySystem extends IteratingSystem {
    private ComponentMapper<EnergyComponent> nrgMap;
    private float elapsed = 0f;

    public EnergySystem() {
        super(Aspect.all(EnergyComponent.class));
    }

    @Override
    protected void process(int i) {
        elapsed += Gdx.graphics.getDeltaTime();
        EnergyComponent e = nrgMap.get(i);

        if ((elapsed - e.lastIncrement) > e.incrementInterval && e.level < e.max) {
            e.level++;
            e.lastIncrement = elapsed;
        }
    }
}
