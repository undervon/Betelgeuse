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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import scenes.Characters;
import scenes.Gameplay;
import scenes.Highscore;
import scenes.Options;

/**
 * \class MainMeniuButtons
 * \brief Clasa ce va crea butoanele de din meniul principal.
 * */
public class MainMeniuButtons {
    //elemente principale
    private GameMain game;                          /**< obiect de tip GameMain pentru a utiliza SpriteBatch-ul ce deseneaza obiectele in joc*/
    private Stage stage;                            /**< similar cu camera(pentru a se vedea butoanele din highscore menu)*/
    private Viewport gameViewport;                  /**< ceea ce se vedE(butoanele)*/

    ///butoane
    private ImageButton playButton;                         /**< buton de play*/
    private ImageButton highscoreButton;                    /**< buton de highscore menu*/
    private ImageButton optionsButton;                      /**< buton de options menu*/
    private ImageButton charactersButton;                   /**< buton de selectare a caracterului*/
    private ImageButton quitButton;                         /**< buton de quit*/
    private ImageButton infoButton;                         /**< buton de informatii pentru joc*/
    private ImageButton musicOnButton;                      /**< buton de music on*/
    private ImageButton musicOffButton;                     /**< buton de music off*/

    ///elemente panou de informatii
    private Image infoPanelImage;                           /**< imaginea cu panoul de informatii*/
    private Label textScoreImageLabel;                      /**< nume panou*/
    private Label textLabel;                                /**< textul din panou*/
    private ImageButton backButton;                         /**< butonul de back*/

    /**
     * \fn public HighscoreButtons(GameMain game)
     * \brief Constructor.
     *
     * \param game pentru utilizarea SpriteBatch-ului
     */
    public MainMeniuButtons(GameMain game) {
        this.game = game;

        ///creeaza un viewport de tip fit(care incadreaza perfect butoanele)
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        ///creeaza un obiect de tip stage pentru a se vedea butoanele
        stage = new Stage(gameViewport, game.getSpriteBatch());
        ///adauga butoanele un inpuls de tip input(apasare)
        Gdx.input.setInputProcessor(stage);

        ///creare si pozitionare butoane in meniu
        createAndPositiongButtons();

        ///adauga functionalitati butoanelor
        addAllButtonsListeners();

        ///verificare daca muzica este sau nu pornita cand se porneste jocul
        checkMusicStartGame();
    }

    /**
     * \fn public void createAndPositiongButtons()
     * \brief Metoda ce creaza si pozitioneaza butoanele in meniu.
     */
    public void createAndPositiongButtons() {
        ///crearea butonelor
        playButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("5.Main Menu Buttons/Play Button.png"))));
        highscoreButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("5.Main Menu Buttons/Highscore Button.png"))));
        optionsButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("5.Main Menu Buttons/Options Button.png"))));
        charactersButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("5.Main Menu Buttons/Characters Button.png"))));
        quitButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("5.Main Menu Buttons/Quit Button.png"))));
        infoButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("5.Main Menu Buttons/Info Button.png"))));
        musicOnButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("5.Main Menu Buttons/Music Button On.png"))));
        musicOffButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("5.Main Menu Buttons/Music Button Off.png"))));

        ///pozitionarea lor
        playButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 80f, Align.center);
        highscoreButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 10f, Align.center);
        optionsButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 100f, Align.center);
        charactersButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 190f, Align.center);
        quitButton.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 280f, Align.center);
        infoButton.setPosition(17f, 17f, Align.bottomLeft);
        musicOnButton.setPosition(GameInfo.WIDTH - 17f, 17f, Align.bottomRight);
        musicOffButton.setPosition(GameInfo.WIDTH - 17f, 17f, Align.bottomRight);

        ///adaugarea lor in meniu
        stage.addActor(playButton);
        stage.addActor(highscoreButton);
        stage.addActor(optionsButton);
        stage.addActor(charactersButton);
        stage.addActor(quitButton);
        stage.addActor(infoButton);
        ///daca muzica este pornita, cand se porneste jocul, se pune butonul de music on
        if (GameManager.getInstance().gameData.isMusicOn()) { stage.addActor(musicOnButton); }
        ///daca muzica este oprita, cand se porneste jocul, se pune butonul de music off
        else { stage.addActor(musicOffButton); }
    }

    /**
     * \fn public void addButtonsListener()
     * \brief Metoda ce adauga utilitate butoanelor din meniu.
     */
    public void addAllButtonsListeners() {
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///se seteaza ca jocul a pornit din meniul principal
                GameManager.getInstance().gameStartefFromMaineMenu = true;

                ///adaugare model de animatie de intrare in joc
                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));
                    }
                });
                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);

                ///adauga animatia
                stage.addAction(sa);

                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();

                ///schimbare melodie
                GameManager.getInstance().changeMusic("Game");
            }
        });

        ///cand se apasa pe butonul de highscore
        highscoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///se deschide meniul de highscore
                game.setScreen(new Highscore(game));
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                game.setScreen(new Options(game));
            }
        });

        charactersButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                game.setScreen(new Characters(game));
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                try {
                    ///adaugare un timp de 150[ms] pana se inchide jocul
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ///se iese din rularea jocului
                System.exit(0);
            }
        });

        infoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                createAndPositionInfoPanel();
            }
        });

        musicOnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///daca muzica este on si se apasa de butonul de music
                if (GameManager.getInstance().gameData.isMusicOn()) {
                    ///se sterge imaginea de music on
                    musicOnButton.remove();
                    ///se pune imaginea de music off
                    stage.addActor(musicOffButton);
                    ///se seteaza ca muzica sa fie pe off
                    GameManager.getInstance().gameData.setMusicOn(false);
                    ///si se opreste muzica
                    GameManager.getInstance().stopMusic();
                }
                ///se salveaza modificarile facute in fisier-ul Json*/
                GameManager.getInstance().saveData();
            }
        });

        musicOffButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                ///daca muzica este oprita si se apasa de butonu de music
                if (!GameManager.getInstance().gameData.isMusicOn()) {
                    ///se sterge imaginea de music off
                    musicOffButton.remove();
                    ///se pune imaginea de music on
                    stage.addActor(musicOnButton);
                    ///se seteaza ca muzica sa fie pornita
                    GameManager.getInstance().gameData.setMusicOn(true);
                    ///si se porneste
                    GameManager.getInstance().playMusic("Background");
                }
                ///se salveaza modificarile facute in fisier-ul Json
                GameManager.getInstance().saveData();
            }
        });
    }

    /**
     * \fn public void createAndPositionInfoPanel()
     * \brief Metoda ce creaza si pozitioneaza panoul de informatii.
     */
    public void createAndPositionInfoPanel() {
        ///creare elemente din meniul de informatii
        infoPanelImage = new Image(new Texture("17.Info Panel/Info.png"));
        backButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("17.Info Panel/Back Button.png"))));

        ///fontul textului din panou
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("8.Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.BLACK;

        BitmapFont imageTextFont = generator.generateFont(parameter);
        parameter.size = 35;
        BitmapFont textFont = generator.generateFont(parameter);

        ///creare label-uri
        textScoreImageLabel = new Label("INFORMATIONS:", new Label.LabelStyle(imageTextFont, Color.BLACK));
        textLabel = new Label("good luck!", new Label.LabelStyle(textFont, Color.BLACK));

        ///pozitionare elemente din meniul de informatii
        infoPanelImage.setPosition(GameInfo.WIDTH / 2f + 8f, GameInfo.HEIGHT / 2f, Align.center);
        backButton.setPosition(GameInfo.WIDTH - 20f, GameInfo.HEIGHT - 90f, Align.topRight);
        textScoreImageLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 250f, Align.center);
        textLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 250f, Align.center);

        ///creare ansamblu
        stage.addActor(infoPanelImage);
        stage.addActor(backButton);
        stage.addActor(textScoreImageLabel);
        stage.addActor(textLabel);

        ///adauga functionalitate butonului de back
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ///adaugare sunet de click
                GameManager.getInstance().clickSouns.play();
                removeInfoPanel();
            }
        });
    }

    /**
     * \fn public void removeInfoPanel()
     * \brief Metoda ce sterge panoul de informatii.
     */
    public void removeInfoPanel() {
        infoPanelImage.remove();
        backButton.remove();
        textScoreImageLabel.remove();
        textLabel.remove();
    }

    /**
     * \fn public void checkMusicStartGame()
     * \brief Metoda ce verifica daca muzica este pornita la rularea jocului.
     */
    public void checkMusicStartGame() {
        ///daca muzica este pornita la rularea jocului
        if (GameManager.getInstance().gameData.isMusicOn()) {
            ///se porneste
            GameManager.getInstance().playMusic("Background");
        }
    }

    /**
     * \fn public Stage getStage()
     * \brief Metoda ce returneaza stage(getter).
     */
    public Stage getStage() { return this.stage; }
}
