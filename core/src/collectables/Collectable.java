package collectables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import helpers.GameInfo;

/**
 * \class Collectable
 * \brief Clasa ce va crea obiecte de colectat.
 *
 * Mosteneste clasa Sprite.
 * Calasa Sprite este utilizara pentru desenarea obiectelor.
 * */
public class Collectable extends Sprite {
    ///elemente principale
    private World world;                            /**< lumea fizica a itemelor de colectat*/
    private Body body;                              /**< corpul propriuzis al itemelor de colectat*/
    private Fixture fixture;                        /**< accesoriu, utilizat pentru coliziune*/
    private String name;                            /**< numele itemului de colectat*/

    ///animatie
    private TextureAtlas collectablesAtlas;         /**< atlas pentru itemele de colectat*/

    /**
     * \fn public Collectable(World world, String name)
     * \brief Constructor.
     *
     * \param world lumea propriuzisa
     * \param name numele itemului
     * */
    public Collectable(World world, String name) {
        ///desenarea texturii de colectat utilizand constructorul din clasa Sprite
        super(new Texture("11.Collectables/" + name + ".png"));
        this.world = world;
        this.name = name;

        ///atlas-ul animatiei
        collectablesAtlas = new TextureAtlas("12." + name + " Animation/pack.atlas");
    }

    /**
     * \fn public void createCollectableBody()
     * \brief Metoda ce construieste corpul itemului de colectat.
     * */
    public void createCollectableBody() {
        ///creaza o definitie a corpului
        BodyDef bodyDef = new BodyDef();
        ///seteaza tipul coprului(static -> nu se misca)
        bodyDef.type = BodyDef.BodyType.StaticBody;
        ///seteaza imaginea itemului in centrul corpului
        bodyDef.position.set((getX() - getWidth() / 2f) / GameInfo.PPM, (getY() + getHeight() / 2f) / GameInfo.PPM);

        ///creaza corpul propriuzis utilizand word si bodyDef
        body = world.createBody(bodyDef);

        ///creaza un poligon in jurul corpului obiectului de colectat
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox((getWidth() / 2f - 5f) / GameInfo.PPM, (getHeight() / 2f) / GameInfo.PPM);

        ///creaza o definitie a accesoriului corpului si o seteaza in jurul poligonului
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        ///seteaza un filtru pentru obiectul de tip item ca fiid principalul obiect din clasa si acesta intra in contact cu playerul
        fixtureDef.filter.categoryBits = GameInfo.COLLECTABLE;
        ///seteaza obiectul ca fiind senzon(se poate trece prin el)
        fixtureDef.isSensor = true;

        ///se creaza accesoriul corpului utilizand fixtureDef si corpul propriuzis
        fixture = body.createFixture(fixtureDef);
        ///seteaza numele obiectului item de colectat
        fixture.setUserData(name);

        ///la final dealoca memoria pentru poligonul din jurul corpului
        polygonShape.dispose();
    }

    /**
     * \fn public void setCollectablePosition(float x, float y)
     * \brief Metoda ce seteaza pozitia obiectului si il construieste apeland metoda de construire.
     *
     * \param x coordonata X
     * \param y coordonata Y
     * */
    public void setCollectablePosition(float x, float y) {
        setPosition(x, y);
        createCollectableBody();
    }

    /**
     * \fn public void updateCollectablsPosition()
     * \brief Metoda ce updateaza pozitia itemelor de colectat.
     * */
    public void updateCollectablsPosition() {
        setPosition((body.getPosition().x + 0.35f) * GameInfo.PPM, (body.getPosition().y - 0.25f) * GameInfo.PPM);
    }

    /**
     * \fn public void changeCollectablesFiler()
     * \brief Metoda ce schimba filtrul itemului.
     * */
    public void changeCollectablesFiler() {
        ///se creeaza un nou filtru
        Filter filter = new Filter();
        ///il seteaza ca fiind destructibil
        filter.categoryBits = GameInfo.DESTROYED;
        ///il adauga la obiectul accesoriu
        fixture.setFilterData(filter);
    }

    /**
     * \fn public TextureAtlas getCollectablesAtlas()
     * \brief Metoda ce returneaza atlasul animatiei.
     * */
    public TextureAtlas getCollectablesAtlas() { return this.collectablesAtlas; }

    /**
     * \fn public Fixture getFixture()
     * \brief Metoda ce returneaza accesoriul obiectului.
     * */
    public Fixture getFixture() { return this.fixture; }
}
