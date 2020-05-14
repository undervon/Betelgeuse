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
 * \class OptionsButtons
 * \brief Clasa ce va crea butoanele de din meniul de optiuni.
 * */
public class OptionsButtons {
    //elemente principale
    private GameMain game;                          /**< obiect de tip GameMain pentru a utiliza SpriteBatch-ul ce deseneaza obiectele in joc*/
    private Stage stage;                            /**< similar cu camera(pentru a se vedea butoanele din highscore menu)*/
    private Viewport gameViewport;                  /**< ceea ce se vedE(butoanele)*/

    ///elementele din meniu (butoane/imagini)
    private ImageButton easyButton;                             /**< buton de dificultate easy*/
    private ImageButton mediumButton;                           /**< buton de dificultate medium*/
    private ImageButton hardButton;                             /**< buton de dificultate hard*/
    private ImageButton backButton;                             /**< buton de back*/

    private Image sign;                                         /**< imagine cu bifat-ul de la butanele de dificultate*/

    /**
     * \fn public HighscoreButtons(GameMain game)
     * \brief Constructor.
     *
     * \param game pentru utilizarea SpriteBatch-ului
     */
    public OptionsButtons(GameMain game) {
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
     * \fn private void createAndPositioningButtons()
     * \brief Metoda ce creaza si pozitioneaza butoanele in meniu.
     */
    private void createAndPositioningButtons() {
        ///crearea butonelor
        easyButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("7.Options Buttons/Easy Button.png"))));
        mediumButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("7.Options Buttons/Medium Button.png"))));
        hardButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("7.Options Buttons/Hard Button.png"))));
        backButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("7.Options Buttons/Back Button.png"))));

        ///creaza imagine de bifat
        sign = new Image(new Texture("7.Options Buttons/Check Sign.png"));

        ///pozitioneaza butoanele
        easyButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 30f, Align.center);
        mediumButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 60f, Align.center);
        hardButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 150f, Align.center);
        backButton.setPosition(7, 7, Align.bottomLeft);

        ///pozitioneaza bifatul in functie de dificultatea selectata
        setPositionSign();

        ///adaugarea lor in meniu
        stage.addActor(easyButton);
        stage.addActor(mediumButton);
        stage.addActor(hardButton);
        stage.addActor(backButton);
        stage.addActor(sign);
    }

    /**
     * \fn private void addButtonsListener()
     * \brief Metoda ce adauga utilitate butoanelor din meniul de optuni.
     */
    private void addButtonsListener() {
        easyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///daca se apasa pe butonu de dificultate easy se pozitioneaza bifa la acesta
                sign.setY(easyButton.getY() + 8f);
                ///se schimba dificultatea jocului
                changeDifficultyOptions(true, false, false);
            }
        });

        mediumButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                sign.setY(mediumButton.getY() + 8f);
                changeDifficultyOptions(false, true, false);
            }
        });

        hardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                sign.setY(hardButton.getY() + 8f);
                changeDifficultyOptions(false, false, true);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///cand se apasa de butonul de back se revine in meniul principal
                game.setScreen(new MainMenu(game));
            }
        });
    }

    /**
     * \fn public void setPositionSign()
     * \brief Metoda ce seteaza pozitia semnului de bifat in functie de dificultatea selectata.
     */
    public void setPositionSign() {
        ///daca este selectata dificultatea easy
        if (GameManager.getInstance().gameData.isEasyDifficulty()) {
            ///se seteaza semnul de bifat in dreptul dificultatii easy
            sign.setPosition(GameInfo.WIDTH / 2f + 100f, easyButton.getY() + 33f, Align.right);
        }

        if (GameManager.getInstance().gameData.isMediumFifficulty()) {
            sign.setPosition(GameInfo.WIDTH / 2f + 100f, mediumButton.getY() + 33f, Align.right);
        }

        if (GameManager.getInstance().gameData.isHardDIfficulty()) {
            sign.setPosition(GameInfo.WIDTH / 2f + 100f, hardButton.getY() + 33f, Align.right);
        }
    }

    /**
     * \fn public void changeDifficultyOptions(boolean easyDifficulty, boolean mediumDifficulty, boolean hardDifficulty)
     * \brief Metoda ce seteaza dificultatea in functie de optiunea aleasa.
     *
     * \param easyDifficulty true daca se apasa pe easy, false daca nu se apasa pe easy
     * \param mediumDifficulty true daca se apasa pe medium, false daca nu se apasa pe medium
     * \param hardDifficulty true daca se apasa pe hard, false daca nu se apasa pe hadr
     */
    public void changeDifficultyOptions(boolean easyDifficulty, boolean mediumDifficulty, boolean hardDifficulty) {
        GameManager.getInstance().gameData.setEasyDifficulty(easyDifficulty);
        GameManager.getInstance().gameData.setMediumFifficulty(mediumDifficulty);
        GameManager.getInstance().gameData.setHardDIfficulty(hardDifficulty);

        ///se salveaza modificarile facute
        GameManager.getInstance().saveData();
    }

    /**
     * \fn public Stage getStage()
     * \brief Metoda ce returneaza stage(getter).
     */
    public Stage getStage() { return this.stage; }
}
