package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
 * \class HighscoreButtons
 * \brief Clasa ce va crea butoanele de din meniul de highscore.
 * */
public class HighscoreButtons {
    //elemente principale
    private GameMain game;                          /**< obiect de tip GameMain pentru a utiliza SpriteBatch-ul ce deseneaza obiectele in joc*/
    private Stage stage;                            /**< similar cu camera(pentru a se vedea butoanele din highscore menu)*/
    private Viewport gameViewport;                  /**< ceea ce se vedE(butoanele)*/

    ///butoane/label-uri
    private ImageButton backButton;                 /**< imagine cu butonul de back*/

    private Label scoreLabel;                       /**< text ce afiseaza scorul*/
    private Label coinLabel;                        /**< text de afiseaza numarul de bani*/

    /**
     * \fn public HighscoreButtons(GameMain game)
     * \brief Constructor.
     *
     * \param game pentru utilizarea SpriteBatch-ului
     */
    public HighscoreButtons(GameMain game) {
        this.game = game;

        ///creeaza un viewport de tip fit(care incadreaza perfect butoanele)
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        ///creeaza un obiect de tip stage pentru a se vedea butoanele
        stage = new Stage(gameViewport, game.getSpriteBatch());
        ///adauga butoanele un inpuls de tip input(apasare)
        Gdx.input.setInputProcessor(stage);

        ///creeaza si pozitioneaza butoanele in meniu
        createAndPositionButtonsAndLabels();

        ///adauga functionalitati butoanelor
        addButtonsListener();
    }

    /**
     * \fn public void createAndPositionButtonsAndLabels()
     * \brief Metoda ce creaza si pozitioneaza butoanele si label-urile in meniu.
     */
    public void createAndPositionButtonsAndLabels() {
        //crearea butonului de back
        backButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("6.Highscore Buttons/Back Button.png"))));

        ///pozitionarea butonului de back
        backButton.setPosition(7, 7, Align.bottomLeft);

        ///pentru font se utilizeaza un generator de fonturi
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("8.Fonts/blow.ttf"));
        ///parametrii pentru fonturi
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 55;
        parameter.color = Color.BLACK;

        ///se creaza fonturi pentru label-uri utilizand generator si parametrii
        BitmapFont scoreFont = generator.generateFont(parameter);
        BitmapFont coinFont = generator.generateFont(parameter);

        ///crearea labelurilor
        scoreLabel = new Label(String.valueOf(GameManager.getInstance().gameData.getHighscore()), new Label.LabelStyle(scoreFont, Color.WHITE));            //creare text pentru scor
        coinLabel = new Label(String.valueOf(GameManager.getInstance().gameData.getCoinHighscore()), new Label.LabelStyle(coinFont, Color.BLACK));              //creare text pentru bani

        ///setarea pozitiei label-urilor
        scoreLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 42f, Align.center);
        coinLabel.setPosition(GameInfo.WIDTH / 2f + 25f, GameInfo.HEIGHT / 2f - 120f, Align.center);

        ///adauga butoanele/label-urile in meniu
        stage.addActor(backButton);
        stage.addActor(scoreLabel);
        stage.addActor(coinLabel);
    }

    /**
     * \fn public void addButtonsListener()
     * \brief Metoda ce adauga utilitate butonului de back.
     */
    public void addButtonsListener() {
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///cand se apasa se revine in meniul principal
                game.setScreen(new MainMenu(game));
            }
        });
    }

    /**
     * \fn public Stage getStage()
     * \brief Metoda ce returneaza un obiect de tip stage.
     */
    public Stage getStage() { return this.stage; }
}
