package platforms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import helpers.GameInfo;

/**
 * \class Platform
 * \brief Clasa ce va crea platformele.
 *
 * Mosteneste Sprite, clasa ce este utilizata pentru desenarea obiectelor.
 * */
public class Platform extends Sprite {
    ///alcatuire corp platforma
    private World world;                            /**< lumea fixica a platformelor*/
    private Body body;                              /**< corpul propriuzis al platformelor*/

    ///nume
    private String platformName;                    /**< nume platforma*/

    ///utilizat la desenare
    private boolean drawLeft;                       /**< atribut pentru sesenarea platformelor*/

    /**
     * \fn public Platform(World world, String platformName)
     * \brief Constructor.
     *
     * \param world lumea propriuzisa
     * \param platformName numele itemului
     * */
    public Platform(World world, String platformName) {
        ///apel super ce deseneaza platformele
        super(new Texture("2.Platforms/" + platformName + " Platform.png"));
        this.world = world;
        this.platformName = platformName;
    }

    /**
     * \fn public void createPlatformsBody()
     * \brief Metoda ce construieste corpul platformei.
     * */
    public void createPlatformsBody() {
        BodyDef bodyDef = new BodyDef();
        ///seteaza ca, corpul platformei sa fie static
        bodyDef.type = BodyDef.BodyType.StaticBody;
        //getX() -> pozitia X din clasa Sprite
        //getWidth() -> latimea platformei
        bodyDef.position.set((getX() - 45f) / GameInfo.PPM, getY() / GameInfo.PPM);

        ///creare corp propriuzis utilizand word-ul si bodyDef-ul
        body = world.createBody(bodyDef);

        ///creza un poligon in jurul corpului
        PolygonShape polygonShape = new PolygonShape();
        ///il seteaza intr-un box
        polygonShape.setAsBox((getWidth() / 2f - 23f) / GameInfo.PPM, (getHeight() / 2f - 13f) / GameInfo.PPM);

        ///definirea accesoriului formei create mai sus
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;

        ///creaza accesoriul utilizand cody si fixtureDef
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(platformName);

        ///eliberare memorie pentru figura creata
        polygonShape.dispose();
    }

    /**
     * \fn public void createAndSetPositionPlatforms(float x, float y)
     * \brief Metoda ce seteaza pozitia si construieste corpul platformei.
     *
     * \param x coordonata de pe axa X.
     * \param y coordonata de pe axa Y.
     * */
    public void createAndSetPositionPlatforms(float x, float y) {
        ///seteaza pozitia
        setPosition(x, y);
        createPlatformsBody();
    }

    /**
     * \fn public String getPlatformName()
     * \brief Metoda returneaza numele platformei(getter).
     * */
    public String getPlatformName() { return this.platformName; }

    /**
     * \fn public boolean getDrawLeft()
     * \brief Metoda returneaza daca se deseneaza la stanga sau la dreapta platforma.
     * */
    public boolean getDrawLeft() { return this.drawLeft; }

    /**
     * \fn public void setDrawLeft(boolean drawLeft)
     * \brief Metoda ce seteaza in ce parte sa se deseneze platforma(stanga sau dreapta).
     * */
    public void setDrawLeft(boolean drawLeft) { this.drawLeft = drawLeft; }
}
