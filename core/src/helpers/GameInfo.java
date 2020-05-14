package helpers;

/**
 * \class GameInfo
 * \brief Clasa ce contine informatii despre joc.
 * */
public class GameInfo {
    public static final int WIDTH = 480;                                    /**< latime*/
    public static final int HEIGHT = 800;                                   /**< inaltime*/
    //pixels per meter (pixeli pe metru) -> 1/100 (1m = 100px)
    //default este 1/1 (1m = 1px)
    public static final int PPM = 100;                                      /**< pixels per meter*/


    public static final short PLAYER = 2;                                   /**< obiect player*/
    public static final short COLLECTABLE = 3;                              /**< obiect de colectat*/
    public static final short DESTROYED = 4;                                /**< obiect de distrus*/
}























