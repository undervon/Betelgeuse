package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import helpers.GameInfo;
import helpers.GameManager;

/**
 * \class Player
 * \brief Clasa ce va crea playerul.
 *
 * Mosteneste Sprite, clasa ce este utilizata pentru desenarea obiectelor.
 * */
public class Player extends Sprite {
    ///alcatuire corp player
    private World world;                                                /**< lumea fixica a playerului*/
    private Body body;                                                  /**< corpul propriuzis al playerului*/

    ///pentru animatie
    private TextureAtlas playerAtlas;                                  /**< atlasul pentur animatie*/
    private Animation<TextureRegion> playerAnimation;                  /**< animatia*/
    private float elapsedTime;                                         /**< timpul dintre 2 imagini din animatie*/
    private boolean isWalking;                                         /**< daca playerul este in miscare (merge)*/
    private boolean dead;                                              /**< daca playerul este mort*/

    /**
     * \fn public Player(World world, float x, float y)
     * \brief Constructor.
     *
     * \param world lumea propriuzis
     * \param x coordonata X
     * \param y coordonata Y
     * \param characterName numele caracterului (red/blue/green)
     * */
    public Player(World world, float x, float y, String characterName) {
        ///apel constructor de desenare din clasa Sprite
        super(new Texture("3.Player/" + characterName + " Player 000.png"));

        this.world = world;

        ///seteaza pozitia playerului (metoda din clasa sprite)
        setPosition(x, y);

        ///creaza corpul playerului
        createPlayerBody();

        ///creare atlas player pentru ce caracter este selectat
        playerAtlas = new TextureAtlas("4.Player " + characterName + " Animation/pack.atlas");

        ///seteaza ca playerul nu este mort
        setDead(false);
    }

    /**
     * \fn public void createPlayerBody()
     * \brief Metoda ce creeaza corpul playerului.
     * */
    public void createPlayerBody() {
        BodyDef bodyDef = new BodyDef();
        ///seteaza tipul corpului ca fiind dynamic (Se poate misca)
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        ///setare pozitie a definitiei coprului
        bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);

        ///creaza corpul propriuzis utilizand word si bodyDef
        body = world.createBody(bodyDef);
        ///seteaza corpul playerului ca fiind fix (sa nu se roteasca)
        body.setFixedRotation(true);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox((getWidth() / 2f - 85f) / GameInfo.PPM, (getHeight() / 2f - 32f) / GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        ///densitatea corpului
        fixtureDef.density = 4f;
        ///accesoriul corpului
        fixtureDef.friction = 2f;

        ///playerul ca fiind proprietar al obiectului
        fixtureDef.filter.categoryBits = GameInfo.PLAYER;
        ///intra in contact cu itemele de colectat
        fixtureDef.filter.maskBits = GameInfo.COLLECTABLE;

        Fixture fixture = body.createFixture(fixtureDef);
        ///seteaza numele obiectului
        fixture.setUserData("Player");

        ///eliberare memorie pentru poligon
        polygonShape.dispose();
    }

    /**
     * \fn public void drawPlayerImage(SpriteBatch spriteBatch)
     * \brief Metoda ce deseneaza imaginea playerului.
     * */
    public void drawPlayerImage(SpriteBatch spriteBatch) {
        ///daca playerul nu merge
        if (!isWalking) { spriteBatch.draw(this, getX() - getWidth() / 2f + 30f, getY() - getHeight() / 2f); }
    }

    /**
     * \fn public void drawPlayerAnimation(SpriteBatch spriteBatch)
     * \brief Metoda ce deseneaza animatia playerului.
     * */
    public void drawPlayerAnimation(SpriteBatch spriteBatch) {
        ///daca este in miscare
        if (isWalking) {
            elapsedTime += Gdx.graphics.getDeltaTime();

            Array<TextureAtlas.AtlasRegion> frames = playerAtlas.getRegions();
            /////pentru fiecare frame din animatie
            for (TextureRegion frame : frames) {
                ///verificare daca playerul merge spre stamga si nu a fost rotit
                if (body.getLinearVelocity().x < 0 && !frame.isFlipX())
                    ///se roteste
                    frame.flip(true, false);
                ///verificare daca playerul merge spre dreapta si a fost rotit
                else if (body.getLinearVelocity().x > 0 && frame.isFlipX())
                    ///se roteste din nou
                    frame.flip(true, false);
            }
            playerAnimation = new Animation(1f / 22.5f, playerAtlas.getRegions());
            spriteBatch.draw(playerAnimation.getKeyFrame(elapsedTime, true), getX() - getWidth() / 2f + 30f, getY() - getHeight() / 2f);
        }
    }

    /**
     * \fn public void updatePlayerPosition()
     * \brief Metoda ce updateaza pozitia playerlui.
     * */
    public void updatePlayerPosition() {
        //seteaza pozitia playerului perfect in corpul acestuia
        setPosition(body.getPosition().x * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
    }

    /**
     * \fn public void movePlayer(float x)
     * \brief Metoda pentru miscarea playerului.
     *
     * \param x pentru ca acesta se misca pe coordonata X.
     * */
    public void movePlayer(float x) {
        ///daca playerul se misca spre stanga si nu a fost rotit
        if (x < 0 && !this.isFlipX())
            ///se roteste
            this.flip(true, false);
        ///da playerul se misca spre dreapta si a fost rotit
        else if (x > 0 && this.isFlipX())
            ///se roteste din nou
            this.flip(true, false);
        ///il seteaza ca este in miscare
        setWalking(true);
        ///seteaza o miscare liniara pe coordonata Y a corpului playerului
        body.setLinearVelocity(x, body.getLinearVelocity().y);
    }

    /**
     * \fn public void setWalking(boolean isWalking)
     * \brief Metoda ce seteaza daca este sau nu playerul in miscare.
     *
     * \param isWalking pentru miscare
     * */
    public void setWalking(boolean isWalking) { this.isWalking = isWalking; }

    /**
     * \fn public void setDead(boolean dead)
     * \brief Metoda ce seteaza daca este sau nu playerul mort.
     *
     * \param dead pentru moarte
     * */
    public void setDead(boolean dead) { this.dead = dead; }

    /**
     * \fn public boolean getDead()
     * \brief Metoda ce returneaza daca este sau nu playerul mort.
     * */
    public boolean getDead() { return this.dead; }
}
