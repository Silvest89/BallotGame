package eu.silvenia.shipballot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import eu.silvenia.shipballot.ShipBallot;
import eu.silvenia.shipballot.data.Assets;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created by Johnnie Ho on 23-6-2015.
 */
public class MainMenu implements Screen {

    ShipBallot game;
    private Stage stage = new Stage(new ExtendViewport(1280, 720));
    private Table table = new Table();

    private Skin skin = Assets.menuSkin;
    private TextButton buttonPlay, buttonExit;

    public MainMenu(ShipBallot game){
        this.game = game;
    }

    @Override
    public void show() {
        //TexturePacker.process("test/", "test/", "test");


        Label title = new Label("Ship Ballot", skin);

        buttonPlay = new TextButton("Start Game", skin);
        buttonPlay.setName("Start Game");

        Button buttonOptions = new TextButton("Options", skin);
        buttonOptions.setName("Options");

        buttonExit = new TextButton("Exit Game", skin);
        buttonExit.setName("Exit Game");

        buttonPlay.addListener(clickListener);
        buttonOptions.addListener(clickListener);
        buttonExit.addListener(clickListener);

        table.align(Align.top);
        table.padTop(150);
        table.add(title).padBottom(40).row();
        table.add(buttonPlay).size(300, 90).padBottom(30).row();
        table.add(buttonOptions).size(300,90).padBottom(30).row();
        table.add(buttonExit).size(300,90).padBottom(30).row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    private ClickListener clickListener = new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y){
            switch(event.getListenerActor().getName()){
                case "Start Game": {
                    // go to game screen
                    game.goToGame();
                    break;
                }
                case "Exit Game": {
                    Gdx.app.exit();
                    break;
                }
                default: {
                    Dialog dialog = new Dialog("Warning", skin) {
                        public void result(Object obj) {
                            if((boolean) obj)
                                Gdx.app.exit();
                        }
                    };
                    dialog.padTop(100);
                    dialog.text("Are you sure you want to quit?");
                    dialog.button("Yes", true); //sends "true" as the result
                    dialog.button("No", false);  //sends "false" as the result
                    dialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
                    stage.addActor(dialog);
                    dialog.show(stage);
                    break;
                }
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
       stage.getViewport().update(width, height);
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
