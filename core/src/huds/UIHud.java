package huds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undervon.betelgeuse.GameMain;
import helpers.GameInfo;
import helpers.GameManager;
import scenes.MainMenu;

/**
 * \class UIHud
 * \brief Clasa ce va crea butoanele, label-urile din hud-ul jocului.
 * */
public class UIHud {
    //elemente principale
    private GameMain game;                          /**< obiect de tip GameMain pentru a utiliza SpriteBatch-ul ce deseneaza obiectele in joc*/
    private Stage stage;                            /**< similar cu camera(pentru a se vedea butoanele din highscore menu)*/
    private Viewport gameViewport;                  /**< ceea ce se vedE(butoanele)*/

    ///butoane, label-uri, imagini din joc
    private Image coinImage;                        /**< imagine cu banul*/
    private Image lifeImage;                        /**< imagine cu viata*/

    private Label scoreImageLabel;                  /**< label cu imaginea de scor*/

    private Label coinLabel;                        /**< label cu numarul de bani*/
    private Label lifeLabel;                        /**< label cu numarul de vieti*/
    private Label scoreLabel;                       /**< label cu scorul din joc*/

    private ImageButton pauseButton;                /**< buton de pauza*/

    ///pentru panoul de pauza
    private Image pausePanelImage;                  /**< imagine cu panoul de pauza*/

    private ImageButton resumeButton;               /**< buton de resum*/
    private ImageButton quitButton;                 /**< buton de quit*/

    ///pentru animatie
    private TextureAtlas coinAtlas;                 /**< atlas pentru ban*/
    private TextureAtlas lifeAtlas;                 /**< atlas pentru viata*/
    private Animation<TextureRegion> coinAnimation; /**< animatie pentru ban*/
    private Animation<TextureRegion> lifeAnimation; /**< animatie pentru viata*/
    private float elapsedTime;                      /**< timpul dintre fiecare 2 imagini din animatie*/

    /**
     * \fn public UIHud(GameMain game)
     * \brief Constructor.
     *
     * \param game pentru utilizarea SpriteBatch-ului
     */
    public UIHud(GameMain game) {
        this.game = game;

        ///creeaza un viewport de tip fit(care incadreaza perfect butoanele)
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        ///creeaza un obiect de tip stage pentru a se vedea butoanele
        stage = new Stage(gameViewport, game.getSpriteBatch());
        ///adauga butoanele un inpuls de tip input(apasare)
        Gdx.input.setInputProcessor(stage);

        ///seteaza principalele setari cand se porneste jocul din meniul principal
        createStartFromMainMenuSetings();

        ///creaza label-urile
        createLabels();

        ///creaza imaginile din joc
        createIamges();

        ///creaza si pozitioneaza butoanele din joc
        createAndPositionButtons();

        ///adauga functionalitati butoanelor din joc
        addButtonsListener();

        ///crearea unui tabel cu hud-ul din joc
        Table lifeAndCoinTable = new Table();
        lifeAndCoinTable.top().left();
        ///setare pentru a se putea modifica continutul patartului respectiv
        lifeAndCoinTable.setFillParent(true);

        ///pozitionare label-uri/imagini
        lifeAndCoinTable.add(lifeImage).padLeft(10).padTop(7);
        lifeAndCoinTable.add(lifeLabel).padLeft(9).padTop(7);
        lifeAndCoinTable.row();
        lifeAndCoinTable.add(coinImage).padLeft(10).padTop(7);
        lifeAndCoinTable.add(coinLabel).padLeft(9).padTop(7);

        Table scoreTable = new Table();
        scoreTable.top().top().right();
        scoreTable.setFillParent(true);

        scoreTable.add(scoreImageLabel).padRight(15).padTop(22);
        scoreTable.row();
        scoreTable.add(scoreLabel).padRight(15).padTop(30);

        ///adaugare butoanele/label-urile pe ecran
        stage.addActor(lifeAndCoinTable);
        stage.addActor(scoreTable);
        stage.addActor(pauseButton);

        ///adauga atlas-urile animatiilor pentru bani si viata
        coinAtlas = new TextureAtlas("12.Coin Animation/pack.atlas");
        lifeAtlas = new TextureAtlas("12.Life Animation/pack.atlas");
    }

    /**
     * \fn public void createStartFromMainMenuSetings()
     * \brief Metoda ce seteaza principalele setari cand se intra in joc din main menu.
     */
    public void createStartFromMainMenuSetings() {
        ///daca jocul a pornit din main menu
        if (GameManager.getInstance().gameStartefFromMaineMenu) {
            ///se seteaza ca a fost pornit din meniu pe false
            GameManager.getInstance().gameStartefFromMaineMenu = false;
            ///se seteaza scorul, banii, vietile de inceput
            GameManager.getInstance().score = 0;
            GameManager.getInstance().coinScore = 0;
            GameManager.getInstance().lifeScore = 2;
        }
    }

    /**
     * \fn public void drawAnimations(SpriteBatch batch)
     * \brief Metoda ce deseneaza animatiile.
     *
     * \param spriteBatch pentru desenare
     */
    public void drawAnimations(SpriteBatch spriteBatch) {
        ///timpul dintre 2 imagini din animatie
        elapsedTime += Gdx.graphics.getDeltaTime();

        ///se creaza un vector cu frame-urile din animatie
        Array<TextureAtlas.AtlasRegion> frames;
        ///seteaza frame-urile cu atlasul respectivei animatii
        frames = coinAtlas.getRegions();
        ///adauga animatia
        coinAnimation = new Animation(1f / 12.5f, coinAtlas.getRegions());
        ///deseneaza animatia
        spriteBatch.draw(coinAnimation.getKeyFrame(elapsedTime, true), coinImage.getX(), coinImage.getY());

        frames = lifeAtlas.getRegions();
        lifeAnimation = new Animation(1f / 12.5f, lifeAtlas.getRegions());
        spriteBatch.draw(lifeAnimation.getKeyFrame(elapsedTime, true), lifeImage.getX(), lifeImage.getY());
    }

    /**
     * \fn public void createLabels()
     * \brief Metoda ce creaza label-urile din hud.
     */
    public void createLabels() {
        ///creare font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("8.Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = 43;

        BitmapFont font = generator.generateFont(parameter);

        ///creare label-uri
        scoreImageLabel = new Label("SCORE:", new Label.LabelStyle(font, Color.BLACK));
        scoreLabel = new Label(String.valueOf(GameManager.getInstance().score), new Label.LabelStyle(font, Color.WHITE));
        coinLabel = new Label("x" + GameManager.getInstance().coinScore, new Label.LabelStyle(font, Color.WHITE));
        lifeLabel = new Label("x" + GameManager.getInstance().lifeScore, new Label.LabelStyle(font, Color.WHITE));
    }

    /**
     * \fn public void createIamges()
     * \brief Metoda ce creaza imaginile din hud.
     */
    public void createIamges() {
        coinImage = new Image(new Texture("10.Gameplay/Coin.png"));
        lifeImage = new Image(new Texture("10.Gameplay/Life.png"));
    }

    /**
     * \fn public void createAndPositionButtons()
     * \brief Metoda ce creaza si pozitioneaza butoanele din hud.
     */
    public void createAndPositionButtons() {
        pauseButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("10.Gameplay/Pause Button.png"))));
        pauseButton.setPosition(GameInfo.WIDTH - 17, 17, Align.bottomRight);
    }

    /**
     * \fn public void addButtonsListener()
     * \brief Metoda ce adauga functionalitate butonului din hud.
     */
    public void addButtonsListener() {
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///daca jocul nu este in pauza
                if (!GameManager.getInstance().isPaused) {
                    ///se pune pe pauza
                    GameManager.getInstance().isPaused = true;
                    ///se creaza panoul de pauza
                    createAndPositioningPausePanel();
                }
            }
        });
    }

    /**
     * \fn public void createAndPositioningPausePanel()
     * \brief Metoda ce creaza si pozitioneaza panoul de pauza.
     */
    public void createAndPositioningPausePanel() {
        pausePanelImage = new Image(new Texture("14.Pause Panel/Pause Panel.png"));

        resumeButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("14.Pause Panel/Resume Button.png"))));
        quitButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("14.Pause Panel/Quit Button.png"))));

        pausePanelImage.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center);

        resumeButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 40f, Align.center);
        quitButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 50f, Align.center);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///daca se apasa pe butonul de resume se scoate jocul din pauza
                GameManager.getInstance().isPaused = false;
                ///se sterge panoul de pauza
                removePausePanel();
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                game.setScreen(new MainMenu(game));
                ///se schimba melodia daca jucatorul iese din jos in meniul principal
                GameManager.getInstance().changeMusic("Background");
            }
        });

        ///adauga butoanele si imaginile in joc
        stage.addActor(pausePanelImage);
        stage.addActor(resumeButton);
        stage.addActor(quitButton);
    }

    /**
     * \fn public void removePausePanel()
     * \brief Metoda ce sterge panoul de pauza.
     */
    public void removePausePanel() {
        pausePanelImage.remove();
        resumeButton.remove();
        quitButton.remove();
    }

    /**
     * \fn public void createAndPositioningGameOverPanel()
     * \brief Metoda creaza si pozitioneaza panoul de game over.
     */
    public void createAndPositioningGameOverPanel() {
        Image gameOverPanel = new Image(new Texture("15.Final Panel/Final Panel.png"));
        gameOverPanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center);

        ///pentru fontul utilizat in panoul de game over
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("8.Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.WHITE;

        BitmapFont scoreTextFont = generator.generateFont(parameter);
        parameter.size = 45;
        BitmapFont scoreFont = generator.generateFont(parameter);
        BitmapFont coinFont = generator.generateFont(parameter);

        Label finalNameScore = new Label("SCORE:", new Label.LabelStyle(scoreTextFont, Color.BLACK));
        Label finalScore = new Label(String.valueOf(GameManager.getInstance().score), new Label.LabelStyle(scoreFont, Color.WHITE));
        Label finalCoin = new Label(String.valueOf(GameManager.getInstance().coinScore), new Label.LabelStyle(coinFont, Color.WHITE));

        finalNameScore.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 100f, Align.center);
        finalScore.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 20f, Align.center);
        finalCoin.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 65f, Align.center);

        ///adauga label-urile si imaginea in joc
        stage.addActor(gameOverPanel);
        stage.addActor(finalNameScore);
        stage.addActor(finalScore);
        stage.addActor(finalCoin);
    }

    /**
     * \fn public void incrementScore(int score)
     * \brief Metoda ce incrementeaza scorul.
     *
     * \param score valoarea cu cat sa se incrementeze scorul.
     */
    public void incrementScore(int score) {
        ///se creste valoare din GameManager cu scor cu noul scor
        GameManager.getInstance().score += score;
        ///se afiseaza scorul nou creat
        scoreLabel.setText(String.valueOf(GameManager.getInstance().score));
    }

    /**
     * \fn public void incrementCoins(int score)
     * \brief Metoda ce incrementeaza banii si scorul furnizat de bani.
     *
     * \param score valoarea cu cat sa se incrementeze scorul atunci cand se ia un ban.
     */
    public void incrementCoins(int score) {
        ///se incrementeaza banii
        GameManager.getInstance().coinScore++;
        ///se afiseaza noii bani incrementati
        coinLabel.setText("x" + GameManager.getInstance().coinScore);
        ///se incrementeaza si scorul
        incrementScore(score);
    }

    /**
     * \fn public void incrementLifes(int score)
     * \brief Metoda ce incrementeaza vietile si scorul furnizat de vieti.
     *
     * \param score valoarea cu cat sa se incrementeze scorul atunci cand se ia o viata.
     */
    public void incrementLifes(int score) {
        GameManager.getInstance().lifeScore++;
        lifeLabel.setText("x" + GameManager.getInstance().lifeScore);
        incrementScore(score);
    }

    /**
     * \fn public void decrementLifes()
     * \brief Metoda ce decrementeaza vietile.
     */
    public void decrementLifes() {
        ///se decrementeaza vietile
        GameManager.getInstance().lifeScore--;
        ///daca sunt mai mult se 0 vieti se afiseaza
        if (GameManager.getInstance().lifeScore >= 0) { lifeLabel.setText("x" + GameManager.getInstance().lifeScore); }
    }

    /**
     * \fn public Stage getStage()
     * \brief Metoda ce returneaza stage(getter).
     */
    public Stage getStage() { return this.stage; }
}
