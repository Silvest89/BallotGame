package eu.silvenia.shipballot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import eu.silvenia.shipballot.ShipBallot;
import eu.silvenia.shipballot.data.Assets;

/**
 *
 * @author Johnnie
 */
public class Splash implements Screen {
    ShipBallot game;

    private Image splashImage;

    private Stage stage = new Stage(new ExtendViewport(1280, 720));

    public boolean animationDone = false;

    public Splash(ShipBallot game){
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if(Assets.update()){ // check if all files are loaded
            if(animationDone){ // when the animation is finished, go to MainMenu()
                Assets.setMenuSkin(); // uses files to create menuSkin
                game.goToMainMenu();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getViewport().apply();
    }

    @Override
    public void show() {
        splashImage = new Image(new Texture(Gdx.files.internal("splash.png")));
        animationDone = true;
        splashImage.addAction(Actions.sequence(Actions.alpha(0)
                ,Actions.delay(1f),Actions.fadeIn(1.0f),Actions.delay(2f),Actions.fadeOut(1.5f), Actions.run(new Runnable() {
            @Override
            public void run() {
                animationDone = true;
            }
        })));

        stage.addActor(splashImage);
        //Assets.manager.clear();
        //not necessary, only when splash called more then once
        Assets.queueLoading();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        //texture.dispose();
    }

}