package nz.kiwi.loomans.canyoudigit.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import nz.kiwi.loomans.canyoudigit.components.PositionComponent;
import nz.kiwi.loomans.canyoudigit.components.TextureComponent;

public class RenderingSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posMap;
    private ComponentMapper<TextureComponent> texMap;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public RenderingSystem(OrthographicCamera cam) {
        super(Aspect.all(PositionComponent.class, TextureComponent.class));
        camera = cam;
    }

    @Override
    protected void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }

    @Override
    protected void process(int entityId) {
        final PositionComponent pos = posMap.get(entityId);
        final TextureComponent tex = texMap.get(entityId);

        if (pos == null || tex == null) {
            System.out.println("Entity not available");
            return;
        }

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        shapeRenderer.rect(pos.position.x, pos.position.y, tex.dimensions.x, tex.dimensions.y);
        shapeRenderer.end();
    }
}
