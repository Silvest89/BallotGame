package eu.silvenia.shipballot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import eu.silvenia.shipballot.ShipBallot;
import eu.silvenia.shipballot.data.Assets;

/**
 * Created by Johnnie Ho on 23-6-2015.
 */
public class MainMenu implements Screen {

    ShipBallot game;
    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin = Assets.menuSkin;
    private TextButton buttonPlay, buttonExit;

    public MainMenu(ShipBallot game){
        this.game = game;
    }

    @Override
    public void show() {

        Label title = new Label("Ship Ballot", skin);
        buttonPlay = new TextButton("Start Game", skin);
        buttonPlay.setName("Start Game");
        buttonExit = new TextButton("Exit Game", skin);
        buttonExit = new TextButton("Exit Game", skin);
        buttonExit.setName("Exit Game");

        buttonPlay.addListener(clickListener);
        buttonExit.addListener(clickListener);

        table.align(Align.top);
        table.padTop(150);
        table.add(title).padBottom(40).row();
        table.add(buttonPlay).size(300,100).padBottom(30).row();
        table.add(buttonExit).size(300,100).padBottom(30).row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    private ClickListener clickListener = new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y){
            System.out.println(event.getListenerActor().getName());
            switch(event.getListenerActor().getName()){
                case "Start Game": {
                    // go to game screen
                    break;
                }
                case "Exit Game": {
                    Gdx.app.exit();
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
