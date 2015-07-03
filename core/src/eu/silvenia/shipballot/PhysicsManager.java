package eu.silvenia.shipballot;

/**
 * Created by Johnnie Ho on 26-6-2015.
 */
public class PhysicsManager {
    public final static float PIXELS_TO_METERS = 0.01f;
    public final static int METERS_TO_PIXELS = 100;

    public final static short   COL_PLAYER              = -1;
    public final static short   COL_ENEMY               = -2;
    public final static short   COL_LEVEL               = -3;
    public final static short   COL_FRIENDLY_BULLET     = -4;
    public final static short   COL_ENEMY_BULLET        = -5;
    public final static short   COL_MONEY               = -6;
    public final static short   COL_HEALTH              = -7;
    public final static short   COL_GUN                 = -8;
    public final static short   COL_GRENADE             = -9;

    public final static short FOOT_SENSOR =      0x0001;
    public final static short BULLET =      0x0002;
}
