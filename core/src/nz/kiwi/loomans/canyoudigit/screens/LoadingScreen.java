package nz.kiwi.loomans.canyoudigit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import nz.kiwi.loomans.canyoudigit.CanYouDigIt;
import nz.kiwi.loomans.canyoudigit.LoadingBarPart;

public class LoadingScreen implements Screen {
    private CanYouDigIt parent;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion title;
    private Animation<TextureRegion> flameAnimation;
    private TextureAtlas.AtlasRegion dash;
    private int currentLoadingStage = 0;
    private Stage stage;
    private Table table;

    private float countDown = 5.1f;

    private final int IMAGE = 1;
    private final int FONT = 2;
    private final int PARTY = 3;
    private final int SOUND = 4;
    private final int MUSIC = 5;
    private final int DONE_LOADING = 6;

    public LoadingScreen(CanYouDigIt game) {
        parent = game;
        stage = new Stage(new ScreenViewport());

        // load loading images and wait until finished
        parent.assetSystem.queueAddLoadingImages();
        parent.assetSystem.manager.finishLoading();

        // get images used to display loading progress
        atlas = parent.assetSystem.manager.get("images/loading.atlas");
        title = atlas.findRegion("staying-alight-logo");
        dash = atlas.findRegion("loading-dash");
        flameAnimation = new Animation<TextureRegion>(0.07f, atlas.findRegions("flames/flames"), Animation.PlayMode.LOOP);
    }

    private Table loadingTable;

    @Override
    public void show() {
        Image titleImage = new Image(title);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        loadingTable = new Table();
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));


        table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(12);
        table.row(); // move to next row
        table.add(loadingTable).width(400);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (parent.assetSystem.manager.update()) {
            currentLoadingStage+= 1;
            if(currentLoadingStage <= DONE_LOADING){
                loadingTable.getCells().get((currentLoadingStage-1)*2).getActor().setVisible(true);
                loadingTable.getCells().get((currentLoadingStage-1)*2+1).getActor().setVisible(true);
            }
            switch(currentLoadingStage){
                case IMAGE:
                    parent.assetSystem.queueAddImages();
                case FONT:
                    parent.assetSystem.queueAddFonts();
                    break;
                case PARTY:
                    parent.assetSystem.queueAddAnimations();
                    break;
                case SOUND:
                    parent.assetSystem.queueAddSounds();
                    break;
                case MUSIC:
                    parent.assetSystem.queueAddMusic();
                    break;
                case DONE_LOADING:
                    break;
            }
            if (currentLoadingStage > DONE_LOADING){
                countDown -= delta;
                currentLoadingStage = DONE_LOADING;
                if(countDown < 0){
                    MessageManager.getInstance().dispatchMessage(null, this.parent.fsm, DONE_LOADING);
                }
            }
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
