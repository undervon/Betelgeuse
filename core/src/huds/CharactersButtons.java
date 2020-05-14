package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undervon.betelgeuse.GameMain;
import helpers.GameInfo;
import helpers.GameManager;
import scenes.MainMenu;

/**
 * \class CharactersButtons
 * \brief Clasa ce va crea butoanele de din meniul de caractere.
 * */
public class CharactersButtons {
    ///elemente principale
    private GameMain game;                      /**< obiect de tip GameMain pentru a utiliza SpriteBatch-ul ce deseneaza obiectele in joc*/
    private Stage stage;                        /**< similar cu camera(pentru a se vedea butoanele din highscore menu)*/
    private Viewport gameViewport;              /**< ceea ce se vedE(butoanele)*/

    ///elemente din meniu (butoane/imagini)
    private ImageButton redCharacterButton;     /**< buton pentru selectarea caracterului rosu*/
    private ImageButton blueCharacterButton;    /**< buton pentru selectarea caracterului albastru*/
    private ImageButton greenCharacterButton;   /**< buton pentru selectarea caracterului verde*/
    private ImageButton backButton;             /**< buton de back*/

    private Image sign;                         /**< imagine cu semul de bifat*/

    /**
     * \fn public HighscoreButtons(GameMain game)
     * \brief Constructor.
     *
     * \param game pentru utilizarea SpriteBatch-ului
     */
    public CharactersButtons(GameMain game) {
        this.game = game;

        ///creeaza un viewport de tip fit(care incadreaza perfect butoanele)
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        ///creeaza un obiect de tip stage pentru a se vedea butoanele
        stage = new Stage(gameViewport, game.getSpriteBatch());
        ///adauga butoanele un inpuls de tip input(apasare)
        Gdx.input.setInputProcessor(stage);

        ///creaza si pozitioneaza butoanele in meniu
        createAndPositioningButtons();

        ///adauga functionalitati butoanelor
        addButtonsListener();
    }

    /**
     * \fn public void createAndPositioningButtons()
     * \brief Metoda ce creaza si pozitioneaza butoanele in meniu.
     */
    public void createAndPositioningButtons() {
        redCharacterButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("16.Characters Buttons/Red Character.png"))));
        blueCharacterButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("16.Characters Buttons/Blue Character.png"))));
        greenCharacterButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("16.Characters Buttons/Green Character.png"))));
        backButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("16.Characters Buttons/Back Button.png"))));

        redCharacterButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 70f, Align.center);
        blueCharacterButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 80f, Align.center);
        greenCharacterButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 230f, Align.center);
        backButton.setPosition(7, 7, Align.bottomLeft);

        ///adauga butonul de bifat si pozitia lui
        sign = new Image(new Texture("16.Characters Buttons/Check Sign.png"));
        setSignPosition();

        ///adauga butoanele in meniu
        stage.addActor(redCharacterButton);
        stage.addActor(blueCharacterButton);
        stage.addActor(greenCharacterButton);
        stage.addActor(backButton);
        stage.addActor(sign);
    }

    /**
     * \fn public void addButtonsListener()
     * \brief Metoda ce adauga utilitate butoanelor din meniul de caractere.
     */
    public void addButtonsListener() {
        redCharacterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///se seteaza pozitia semnului pe coordonata Y in functie de caracterul selectat
                sign.setY(redCharacterButton.getY() + 37f);
                ///seteaza ce caracter a fost selectat
                setCharacter(true, false, false);
            }
        });

        blueCharacterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                sign.setY(blueCharacterButton.getY() + 37f);
                ///seteaza ce caracter a fost selectat
                setCharacter(false, true, false);
            }
        });

        greenCharacterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                sign.setY(greenCharacterButton.getY() + 37f);
                ///seteaza ce caracter a fost selectat
                setCharacter(false, false, true);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///seteaza screenul pe cel din meniul principal
                game.setScreen(new MainMenu(game));
            }
        });
    }

    /**
     * \fn public void setCharacter(boolean redCharacter, boolean blueCharacter, boolean greenCharacter)
     * \brief Metoda ce seteaza caracterul in functie de caracterul ales.
     *
     * \param redCharacter true daca se apasa pe caracterul rosu, false daca nu se apasa pe caracterul rosu
     * \param blueCharacter true daca se apasa pe caracterul albastru, false daca nu se apasa pe caracterul albastru
     * \param greenCharacter true daca se apasa pe caracterul verde, false daca nu se apasa pe caracterul verde
     */
    public void setCharacter(boolean redCharacter, boolean blueCharacter, boolean greenCharacter) {
        GameManager.getInstance().gameData.setRedCharacter(redCharacter);
        GameManager.getInstance().gameData.setBlueCharacter(blueCharacter);
        GameManager.getInstance().gameData.setGreenCharacter(greenCharacter);

        GameManager.getInstance().saveData();
    }

    /**
     * \fn public void setSignPosition()
     * \brief Metoda ce seteaza pozitia semnului de bifat in functie de caracterul selectat.
     */
    public void setSignPosition() {
        ///daca se alege caracterul rosu
        if (GameManager.getInstance().gameData.isRedCharacter())
            ///se seteaza bifa in dreptul caracterului rosu
            sign.setPosition(GameInfo.WIDTH / 2f + 123f, redCharacterButton.getY() + 62f, Align.center);

        if (GameManager.getInstance().gameData.isBlueCharacter())
            sign.setPosition(GameInfo.WIDTH / 2f + 123f, blueCharacterButton.getY() + 62f, Align.center);

        if (GameManager.getInstance().gameData.isGreenCharacter())
            sign.setPosition(GameInfo.WIDTH / 2f + 123f, greenCharacterButton.getY() + 62f, Align.center);
    }

    /**
     * \fn public Stage getStage()
     * \brief Metoda ce returneaza stage(getter).
     */
    public Stage getStage() { return this.stage; }
}
