package platforms;

import collectables.Collectable;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import helpers.GameInfo;
import helpers.GameManager;
import player.Player;
import java.util.Random;


/**
 * \class PlatformsController
 * \brief Clasa ce va crea controlul platformelor.
 * */
public class PlatformsController {
    ///corp
    private World world;                                                 /**< lumea propriu zisa a platformei*/

    ///platforma
    private Array<Platform> platforms = new Array<>();                   /**< vector cu platforme*/
    private Array<Collectable> collectables = new Array<>();             /**< vector cu iteme de colectat*/

    private static final float DISTANCE_BEETWIN_PLATFORMS = 250f;        /**< distanta dintre 2 platforme*/
    private float minX;                                                  /**< minumul coordonatei X*/
    private float maxX;                                                  /**< maximul coordonatei X*/
    private float positionCameraY;                                       /**< pozitia camerei pe coordonata Y*/
    private float lastPlatformPositionY;                                 /**< ultima pozitie a platformei pe coordonata Y*/

    ///random
    private Random random = new Random();                                /**< obiect de randomizare*/

    ///pentru animatia obiectelor de colectat
    private Animation<TextureRegion> collectablesAnimation;              /**< animatia itemelor de colectat*/
    private float elapsedTime;                                           /**< timpul dintre 2 imagini ale animatiei*/

    /**
     * \fn public PlatformsController(World world)
     * \brief Constructor.
     *
     * \param world lumea propriuzisa
     * */
    public PlatformsController(World world) {
        this.world = world;

        ///seteaza valoarea maxima pe coordonata X
        minX = GameInfo.WIDTH / 2f - 130;
        ///seteaza valoarea minima pe coordonata X
        maxX = GameInfo.WIDTH / 2f + 130;

        ///creare platforme
        createPlatforms();
        ///pozitionare platforme
        setPlatformsPosition(true);
    }

    /**
     * \fn public void createPlatforms()
     * \brief Metoda ce creaza platformele.
     *
     * 6 platforme normale.
     * 2 platforme ucigase.
     * Set de 8 platforme care se tot repeta aleatoriu.
     * */
    public void createPlatforms() {
        ///creare platforme bune
        for (int i = 0; i < 6; i++) { platforms.add(new Platform(world, "Good")); }

        ///creare platforme ucigase
        for (int i = 0; i < 2; i++) { platforms.add(new Platform(world, "Bad")); }

        ///amestecare platforme(pentru randomizarea lor)
        platforms.shuffle();
    }

    /**
     * \fn public void setPlatformsPosition(boolean isFirstTimeArranging)
     * \brief Metoda ce seteaza pozitia platformelor.
     *
     * \param isFirstTimeArranging pentru verificarea daca se construiesc pentru prima data platformele.
     * */
    public void setPlatformsPosition(boolean isFirstTimeArranging) {
        ///atat timp cat prima platforma este ucigasa se re-amesteca platformele
        while (platforms.get(0).getPlatformName().equals("Bad")) {
            platforms.shuffle();
        }

        float platformsPositionY = 0;                           /**< pozitia pe axa Y a platformelor*/
        ///daca se genereaza pentru prima data platformele
        if (isFirstTimeArranging)
            ///se pozitioneaza in centrul ecranului
            platformsPositionY = GameInfo.HEIGHT / 2f;
            ///daca NU se genereaza pentru prima data platformele
        else
            ///se pozitioneaza de la ultima pozitie a platformei de pe axa Y
            platformsPositionY = lastPlatformPositionY;

        int controlX = 0;                                       /**< controlare pe axa X (0 -> stanga, 1 -> dreapta)*/

        for (Platform platform : platforms) {
            ///daca pozitia platformelor este 0 pe ambele coordonate
            if (platform.getX() == 0 && platform.getY() == 0) {
                float positionX = 0;                            /**< pozitia pe axa X*/
                if (controlX == 0) {
                    ///stanga
                    ///se randomizeaza pozitia X
                    positionX = randomBeetwenNumbers(minX + 30, minX);
                    ///se trece pe partea dreapta
                    controlX = 1;
                    ///se seteaza ca se deseneaza platformele pe partea stanga
                    platform.setDrawLeft(true);
                } else {
                    //dreapta
                    ///se randomizeaza pozitia X
                    positionX = randomBeetwenNumbers(maxX - 30, maxX);
                    ///se trece pe partea stanga
                    controlX = 0;
                    ///se seteaza ca se deseneaza platformele pe partea dreapta
                    platform.setDrawLeft(false);
                }
                ///se seteaza pozitia platformelor si se creaza acestea
                platform.createAndSetPositionPlatforms(positionX, platformsPositionY);
                ///se seteaza urmatoare pozitie a platformei pe axa Y
                platformsPositionY -= PlatformsController.DISTANCE_BEETWIN_PLATFORMS;
                ///se seteaza ultima pozitie a platformei pe axa Y
                lastPlatformPositionY = platformsPositionY;

                ///daca nu s-au randat pentru prima data platformele pe ecran si daca platforma nu este Bad
                if (!isFirstTimeArranging && !platform.getPlatformName().equals("Bad")) {
                    int rand = random.nextInt(10);                  /**< variabila care randomizeaza crearea platformelor (10 timpi)*/
                    ///daca valoare este mai mare decat 7 se creaza iteme de colectat
                    if (rand > 7) {
                        int randCollectable = random.nextInt(2);    /**< variabila care randomizeaza crearea celor 2 tipuri de obiecte (2 timpi)*/

                        ///creaza obiect de tip viata (randCollectable == 0)
                        if (randCollectable == 0) {
                            ///daca numarul de vieti == 0
                            if (GameManager.getInstance().lifeScore == 0) {
                                ///se creeaza obiect de colectat de tip LIFE(viata)
                                Collectable collectable = new Collectable(world, "Life");
                                ///se seteaza pozitia
                                collectable.setCollectablePosition(platform.getX() - 45f, platform.getY() + 25f);
                                ///se adauga in vectorul de obiecte de colectat
                                collectables.add(collectable);
                              ///daca numarul de vieti > 0
                            } else {
                                ///se creeaza obiect de colectat de tip COIN(ban)
                                Collectable collectable = new Collectable(world, "Coin");
                                collectable.setCollectablePosition(platform.getX() - 45f, platform.getY() + 25f);
                                collectables.add(collectable);
                            }
                          ///creaza obiect de tip ban (randCollectable == 1)
                        } else {
                            ///se creeaza obiect de colectat de tip COIN(ban)
                            Collectable collectable = new Collectable(world, "Coin");
                            collectable.setCollectablePosition(platform.getX() - 45f, platform.getY() + 25f);
                            collectables.add(collectable);
                        }
                    }
                }
            }
        }
    }

    /**
     * \fn public void drawPlatforms(SpriteBatch spriteBatch)
     * \brief Metoda ce deseneaza platformele.
     *
     * \param spriteBatch pentru desenarea acestora.
     * */
    public void drawPlatforms(SpriteBatch spriteBatch) {
        for (Platform platform : platforms)
            ///daca platforma se deseneaza la stanga
            if (platform.getDrawLeft())
                spriteBatch.draw(platform, platform.getX() - platform.getWidth() / 2f - 23f, platform.getY() - platform.getHeight() / 2f);
            ///daca platforma se deseneaza la dreapta
            else
                spriteBatch.draw(platform, platform.getX() - platform.getWidth() / 2f + 12f, platform.getY() - platform.getHeight() / 2f);
    }

    /**
     * \fn public void createAndArrangeNewPlatforms()
     * \brief Metoda ce creeaza si aranjeaza noi platforme.
     * */
    public void createAndArrangeNewPlatforms() {
        for (int i = 0; i < platforms.size; i++) {
            ///daca platformele au iesit din ceea ce se vede pe ecran
            if ((platforms.get(i).getY() - GameInfo.HEIGHT / 2f - 15f) > positionCameraY) {
                ///se elibereaza memoria pentru acea platforma
                platforms.get(i).getTexture().dispose();
                ///se sterge indexul acesteia
                platforms.removeIndex(i);
            }
        }
        ///daca numarul de platforme create este 4
        if (platforms.size == 4) {
            ///se creaza noi platforme
            createPlatforms();
            ///se seteaza pozitia platformelor si ca acestea nu mai sunt la prima creare (param pe false)
            setPlatformsPosition(false);
        }
    }

    /**
     * \fn public float randomBeetwenNumbers(float min, float max)
     * \brief Metoda ce randomizeaza 2 numere.
     *
     * \param min numarul minim.
     * \param max numarul maxim.
     * */
    public float randomBeetwenNumbers(float min, float max) { return random.nextFloat() * (max - min) + min; }

    /**
     * \fn public void drawCollectables(SpriteBatch spriteBatch)
     * \brief Metoda ce deseneaza itemele de colectat.
     *
     * \param spriteBatch pentru desenarea acestora.
     * */
    public void drawCollectables(SpriteBatch spriteBatch) {
        for (Collectable collectable : collectables) {
            ///updateaza pozitia elementelor de colectat
            collectable.updateCollectablsPosition();

            ///animatie
            elapsedTime += Gdx.graphics.getDeltaTime();

            Array<TextureAtlas.AtlasRegion> frames = collectable.getCollectablesAtlas().getRegions();
            collectablesAnimation = new Animation(1f / 12.5f, collectable.getCollectablesAtlas().getRegions());
            spriteBatch.draw(collectablesAnimation.getKeyFrame(elapsedTime, true), collectable.getX(), collectable.getY());
        }
    }

    /**
     * \fn public void removeCollectables()
     * \brief Metoda ce sterge itemele de colectat.
     * */
    public void removeCollectables() {
        for (int i = 0; i < collectables.size; i++) {
            ///daca itemul este de tip "Remove"(dupa ce a intrat in contact cu playerul i se schimba numele in "Remove") se sterge
            if (collectables.get(i).getFixture().getUserData() == "Remove") {
                //se schimba filtrul obiectului de colectat in DESTROYED
                collectables.get(i).changeCollectablesFiler();
                ///se elibereaza memoria
                collectables.get(i).getCollectablesAtlas().dispose();
                ///se sterge indexul
                collectables.removeIndex(i);
            }
        }
    }

    /**
     * \fn public void removeCollectablesOnTheScreenAfterOutOfBounds()
     * \brief Metoda ce sterge itemele de colectat dupa ce au iesit din raza de vedere a imaginii.
     * */
    public void removeCollectablesOnTheScreenAfterOutOfBounds() {
        for (int i = 0; i < collectables.size; i++) {
            ///daca au iesit din ceea ce se vede pe ecran
            if ((collectables.get(i).getY() - GameInfo.HEIGHT / 2f - 15) > positionCameraY) {
                collectables.get(i).getCollectablesAtlas().dispose();
                collectables.removeIndex(i);
            }
        }
    }

    /**
     * \fn public Player setPlayerPositionOnTheFirstPlatform(Player player)
     * \brief Metoda ce seteaza playerul pe prima platforma (la mijlocul acesteia).
     *
     * \param player obiect de tip player.
     * */
    public Player setPlayerPositionOnTheFirstPlatform(Player player) {
        ///daca s-a selectat caracterul rosu
        if (GameManager.getInstance().gameData.isRedCharacter())
            ///se construieste un player cu caracter rosu
            player = new Player(world, platforms.get(0).getX() - 40f, platforms.get(0).getY() + 85f, "Red");

        if (GameManager.getInstance().gameData.isBlueCharacter())
            player = new Player(world, platforms.get(0).getX() - 40f, platforms.get(0).getY() + 85f, "Blue");

        if (GameManager.getInstance().gameData.isGreenCharacter())
            player = new Player(world, platforms.get(0).getX() - 40f, platforms.get(0).getY() + 85f, "Green");
        return player;
    }

    /**
     * \fn public void setPositionCameraY(float positionCameraY)
     * \brief Metoda ce seteaza pozitia camerei pe coordonata Y.
     *
     * \param positionCameraY pozitia camerei pe coordonata Y
     * */
    public void setPositionCameraY(float positionCameraY) { this.positionCameraY = positionCameraY; }
}
