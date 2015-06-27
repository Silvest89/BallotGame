package eu.silvenia.shipballot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Johnnie Ho on 26-6-2015.
 */
public class SoundManager {
    private static Sound music;
    private static Sound sfx;
    private static float masterVolume = 1.0f;

    public static void playMusic(String filePath, float volume, float pitch, float pan){
        if(music != null) {
            music.stop();
            music.dispose();
        }
        music = Gdx.audio.newSound(Gdx.files.internal(filePath));
        music.loop(volume * masterVolume, pitch, pan);
    }

    public static void playSoundEffect(String filePath, float volume, float pitch, float pan){
        if(sfx != null) {
            sfx.stop();
            sfx.dispose();
        }
        sfx = Gdx.audio.newSound(Gdx.files.internal(filePath));
        sfx.play(volume, pitch, pan);
    }

    public static void dispose(){
        if(music != null)
            music.dispose();
        if(sfx != null)
            sfx.dispose();
    }
}
