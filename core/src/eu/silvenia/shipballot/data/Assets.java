package eu.silvenia.shipballot.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Johnnie Ho on 23-6-2015.
 */
public class Assets {
    public static AssetManager manager = new AssetManager();
    public static Skin menuSkin;

    // In here we'll put everything that needs to be loaded in this format:
    // manager.load("file location in assets", fileType.class);
    //
    // libGDX AssetManager currently supports: Pixmap, Texture, BitmapFont,
    //     TextureAtlas, TiledAtlas, TiledMapRenderer, Music and Sound.
    public static void queueLoading() {
        manager.load("skins/menuSkin.pack", TextureAtlas.class);
    }

    //In here we'll create our skin, so we only have to create it once.
    public static void setMenuSkin() {
        if (menuSkin == null) {

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quicksand-Bold.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 45;
            parameter.minFilter = Texture.TextureFilter.Nearest;
            parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
            parameter.color = Color.WHITE;

            generator.scaleForPixelHeight(45);

            BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
            generator.dispose();

            menuSkin = new Skin();
            menuSkin.addRegions(manager.get("skins/menuSkin.pack", TextureAtlas.class));
            menuSkin.add("quicksand-bold", font12, BitmapFont.class);
            menuSkin.load(Gdx.files.internal("skins/menuSkin.json"));
        }
    }
    // This function gets called every render() and the AssetManager pauses the loading each frame
    // so we can still run menus and loading screens smoothly
    public static boolean update() {
        return manager.update();
    }
}