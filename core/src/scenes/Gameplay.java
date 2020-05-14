package scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undervon.betelgeuse.GameMain;
import helpers.GameInfo;
import helpers.GameManager;
import huds.UIHud;
import platforms.PlatformsController;
import player.Player;

/**
 * \class Gameplay
 * \brief Clasa ce va crea gameplay-ul jocului.
 *
 * 	Aceasta se apeleaza in GameMain.
 *	Implementeaza interfetele Screen si ContactListener.
 *  Interfata Screen este utilizata pentru afisarea elementelor din joc.
 *  Interfata ContactListener este utilizata pentru coliziunea dintre obiecte.
 * */
public class Gameplay implements Screen, ContactListener {
    ///elemente principale
    private GameMain game;                              /**< atribut care face apel la clasa GameMin pentru utilizarea SpriteBatch-ului*/
    private World world;                                /**< lumea fixica a jocului*/

    ///miscarea camerei in timpul jocului
    private OrthographicCamera mainCamera;              /**< camera principala a jocului*/
    private Viewport gameViewport;                      /**< ceea ce se vede in joc*/

    ///box-ul din jurul obiectelor
    private OrthographicCamera box2DCamera;             /**< camera pentru fiecare obiect*/
    private Box2DDebugRenderer box2DDebugRenderer;      /**< randarea box-ului pentru fiecare obiect*/

    ///miscarea camerei in timpul jocului
    private float cameraSpeed = 10;                     /**< viteza cu care se misca, camera in timpul jocului*/
    private float acceleration = 10;                    /**< cu cat sa creasca viteza camerei la fiecare randare*/
    private float maxCameraSpeed = 10;                  /**< viteza maxima pe care o poate avea camera*/

    ///background-ul principal din joc
    private Sprite[] backgrounds;                       /**< background-urile din joc*/
    private float lastBackgroundPositionY;              /**< ultima pozitie a background-ului pe coordonata Y*/

    ///user interface hud-ul din joc
    private UIHud uiHud;                                /**< obiect cu hud-ul din joc*/

    ///platformele din joc
    private PlatformsController platformsController;    /**< obiect pentru controlul platformelor din joc*/

    ///player-ul
    private Player player;                              /**< obiect cu player-ul*/
    private float lastPlayerPositionY;                  /**< ultima pozitie a playerului pe coordonata Y*/

    ///prima atingere pe ecran
    private boolean touchGameFirstTime;                 /**< atribut ce returneaza daca ecranul din joc a fost atins pentri prima data*/

    ///sound-uri
    private Sound coinSound;                            /**< sunet pentru coliziunea cu banii din joc*/
    private Sound lifeSound;                            /**< sunet pentru coliziunea cu vietile din joc*/
    private Sound deathSound;                           /**< sunet atunci cand player-ul moare*/

    /**
     * \fn public Gameplay(GameMain game)
     * \brief Constructor.
     */
    public Gameplay(GameMain game) {
        this.game = game;

        ///construire camera principala din joc si ii seteaza pozitia la mijocul ecranului
        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0f);

        ///construire viewport(ceea ce se vede pe ecran) utilizand mainCamera
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        ///construire camera de pe obiectele din joc si seteaza pozitia pe centrul fiecarui obiect
        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM, GameInfo.HEIGHT / GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        //construire randare obiectului de tip box
        box2DDebugRenderer = new Box2DDebugRenderer();

        ///construire obiect de tip UIHud
        uiHud = new UIHud(game);

        ///construire lumea prinzipala a jocului
        ///Vector2(0, -9.8f) este utilizat pentru garvitatia din joc
        world = new World(new Vector2(0, -9.8f), true);

        ///informeaza word ca, ContactListener este clasa Gameplay(pentru coliziune)
        world.setContactListener(this);

        ///construire obiect de tip PlatformsController
        platformsController = new PlatformsController(world);

        ///construire obiect de tip Player apeland metoda din clasa PlatformsController pentru pozitionarea player-ului pe prima platforma
        player = platformsController.setPlayerPositionOnTheFirstPlatform(player);

        ///apel metoda ce creeaza background-urile
        createBackgrounds();

        ///metoda ce seteaza viteza de miscare a camerei in joc
        setCameraSpeed();

        ///initializare obiecte de sunet
        coinSound = Gdx.audio.newSound(Gdx.files.internal("9.Sounds/Coin Sound.wav"));
        lifeSound = Gdx.audio.newSound(Gdx.files.internal("9.Sounds/Life Sound.wav"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("9.Sounds/Death Sound.wav"));
    }

    /**
     * \fn public void keyboardInput(float delta)
     * \brief Metoda pentru miscarea playerului utilizand tastatura.
     */
    public void keyboardInput(float delta) {
        ///daca se apasa tasta stanga/A
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
            ///playerul se misca pe axa Y cu -2.25 pixeli
            player.movePlayer(-2.25f);
        ///daca se apasa tasta dreapta/D
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
            ///playerul se misca pe axa Y cu +2.25 pixeli
            player.movePlayer(2.25f);
        ///daca nu se apasa nici o tasta
        else
            ///playerul nu se misca
            player.setWalking(false);
    }

    /**
     * \fn public void keyboardInput(float delta)
     * \brief Metoda pentru miscarea playerului utilizand mouse-ul.
     */
        public void mouseInput(float delta) {
        ///daca se apasa pe ecran
        if (Gdx.input.isTouched()) {
            ///daca se apasa in partea stanga a ecranului
            if (Gdx.input.getX() < GameInfo.WIDTH / 2f)
                ///playerul se misca pe axa Y cu -2.25 pixeli
                player.movePlayer(-2.25f);
            ///daca se apasa in partea dreapta a ecranului
            else if (Gdx.input.getX() > GameInfo.WIDTH / 2f)
                ///playerul se misca pe axa Y cu +2.25 pixeli
                player.movePlayer(2.25f);
          ///daca nu se apasa pe ecran
        } else
            ///playerul nu se misca
            player.setWalking(false);
    }

    /**
     * \fn public void checkFirstTimeTouchGameScreen()
     * \brief Metoda ce verifica daca sa atins ecranul jocului pentru prima data.
     */
    public void checkFirstTimeTouchGameScreen() {
        ///daca nu sa atins inca ecranul jocului
        if (!touchGameFirstTime) {
            ///daca se atinge
            if (Gdx.input.justTouched()) {
                ///se seteaza ca sa atins pentru prima data
                touchGameFirstTime = true;
                ///jocul se scoate de pe pauza
                GameManager.getInstance().isPaused = false;
                ///se salveaza ultima pozitie a playerului pe axa Y
                lastPlayerPositionY = player.getY();
            }
        }
    }

    /**
     * \fn public void update(float delta)
     * \brief Metoda ce updateaza datele din joc.
     *
     * delta -> timpul pentru fiecare frame
     * Se apeleaza metodele ce au nevoie de un update in functie de delta-time
     */
    public void update(float delta) {
        checkFirstTimeTouchGameScreen();
        ///daca jocul nu este pe pauza
        if (!GameManager.getInstance().isPaused) {
            keyboardInput(delta);
            ///mouseInput(delta);
            moveCameraY(delta);
            ///verificare daca background-urile au iesit din ceea ce se vede pe ecran
            checkBakgroundsOutOfView();
            ///seteaza pozitia camerei pe coordonata Y in functie de camera (pentru platforme)
            platformsController.setPositionCameraY(mainCamera.position.y);
            ///creaza si aranjeaza noi platforme
            platformsController.createAndArrangeNewPlatforms();
            ///sterge itemul de colectat de pe ecran daca acesta a iesit din raza de vedere
            platformsController.removeCollectablesOnTheScreenAfterOutOfBounds();
            ///verificare daca playerul a iesit din ceea ce se vede pe ecran si-l omoara
            checkPlayerOutOfView();
            ///incrementare scor
            countScore();
        }
    }

    /**
     * \fn public void moveCameraY(float delta)
     * \brief Metoda ce misca pozitia camerei pe axa Y.
     *
     * delta -> timpul pentru fiecare frame
     */
    public void moveCameraY(float delta) {
        ///setare pozitie camera principala pe axa Y
        mainCamera.position.y -= cameraSpeed * delta;
        ///creaste viteza de miscare a camerei (10 <-acceleration * delta)
        cameraSpeed += acceleration * delta;
        ///daca viteza camerei e mai mare decat maxumul vitezei camerei
        if (cameraSpeed > maxCameraSpeed)
            cameraSpeed = maxCameraSpeed;
    }

    /**
     * \fn public void setCameraSpeed()
     * \brief Metoda ce seteaza viteza de miscare a camerei in functie de dificultate.
     */
    public void setCameraSpeed() {
        ///daca este selectata dificultatea easy
        if (GameManager.getInstance().gameData.isEasyDifficulty()) {
            ///se seteaza viteza camerei
            cameraSpeed = 100;
            ///se seteaza maximul vitezei camerei
            maxCameraSpeed = 150;
        }
        if (GameManager.getInstance().gameData.isMediumFifficulty()) {
            cameraSpeed = 180;
            maxCameraSpeed = 230;
        }
        if (GameManager.getInstance().gameData.isHardDIfficulty()) {
            cameraSpeed = 260;
            maxCameraSpeed = 310;
        }
    }

    /**
     * \fn public void createBackgrounds()
     * \brief Metoda ce creeaza 3 background-uri unul dupa altul.
     */
    public void createBackgrounds() {
        ///se creeaza 3 background-uri
        backgrounds = new Sprite[3];

        ///pentru fiecare background
        for (int i = 0; i < backgrounds.length; i++) {
            ///se creeaza obiectul
            backgrounds[i] = new Sprite(new Texture("1.Backgrounds/Game Dynamic Background.png"));
            ///se seteaza pozitia la pe coordonata Y la capatul predecesorului
            ///in cazul prmului background acesta este pozitionat la coordonata Y = 0
            backgrounds[i].setPosition(0, -(i * backgrounds[i].getHeight()));
            ///se salveaza ultima pozitia pe coordonata Y a background-ului
            lastBackgroundPositionY = Math.abs(backgrounds[i].getY());
        }
    }

    /**
     * \fn public void drawBackgrounds()
     * \brief Metoda ce deseneaza background-urile.
     */
    public void drawBackgrounds() {
        ///pentru fiecare background
        for (Sprite background : backgrounds)
            ///se deseneaza utilizand SpriteBatch din clasa GameMain
            game.getSpriteBatch().draw(background, background.getX(), background.getY());
    }

    /**
     * \fn public void checkBakgroundsOutOfView()
     * \brief Metoda ce verifica daca background-ul a iesit din ceea ce se vede pe ecran.
     */
    public void checkBakgroundsOutOfView() {
        ///pentru fiecare background
        for (Sprite background : backgrounds) {
            //daca background-ul a iesit din raza de vedere
            //.getHeight() -> dimensiunea imaginii pe inaltime (800px)
            //.getY -> pozitia pe coordonata Y
            if ((background.getY() - background.getHeight() / 2f - 5f) > mainCamera.position.y) {
                ///se creeaza o variabila ce retine noua pozitie a background-ului
                float newBackgroundPosition = background.getHeight() + lastBackgroundPositionY;
                ///se repozitioneaza background-ul
                background.setPosition(0, -newBackgroundPosition);
                ///se determina din nou ultima pozitie a background-ului
                lastBackgroundPositionY = Math.abs(newBackgroundPosition);
            }
        }
    }

    /**
     * \fn public void checkPlayerOutOfView()
     * \brief Metoda ce verifica daca playerul a iesit din ceea ce se vede pe ecran.
     */
    public void checkPlayerOutOfView() {
        ///daca playerul a iesit din ceea ce se vede pe ecran in partea de sus
        if (player.getY() - GameInfo.HEIGHT / 2f - player.getHeight() / 2f > mainCamera.position.y) {
            ///daca playerul nu este mort
            if (!player.getDead()) {
                ///porneste sunetul de omorare a playerului
                deathSound.play();
                ///omoara playerul
                killPlayer();
            }
        }

        ///daca playerul a iesit din ceea ce se vede pe ecran in partea de jos
        if (player.getY() + GameInfo.HEIGHT / 2f + player.getHeight() / 2f < mainCamera.position.y) {
            if (!player.getDead()) {
                deathSound.play();
                killPlayer();
            }
        }

        ///daca playerul a iesit din ceea ce se vede pe ecran in stanga
        if (player.getX() + 35f < 0) {
            if (!player.getDead()) {
                deathSound.play();
                killPlayer();
            }
        } else {
            ///daca playerul a iesit din ceea ce se vede pe ecran in dreapta
            if (player.getX() - 25f > GameInfo.WIDTH) {
                if (!player.getDead()) {
                    deathSound.play();
                    killPlayer();
                }
            }
        }
    }

    /**
     * \fn public void killPlayer()
     * \brief Metoda ce omoara playerul.
     */
    public void killPlayer() {
        ///pune jocul pe pauza
        GameManager.getInstance().isPaused = true;

        ///decrementeaza o viata
        uiHud.decrementLifes();

        ///seteaza ca playerul este mort
        player.setDead(true);
        ///seteaza pozitia inafara partii de se vede pe ecran ca sa se simuleze o moarte
        player.setPosition(1000, 1000);

        ///daca playerul nu mai are vieti
        if (GameManager.getInstance().lifeScore < 0) {
            ///playerul nu mai are vieti pentru a continua jocul
            ///se verifica daca are un nou highscore
            GameManager.getInstance().newHighscoreChecking();

            ///se afiseaza panou de final de joc
            uiHud.createAndPositioningGameOverPanel();

            ///se incarca main meniul printr-o animatie
            RunnableAction run = new RunnableAction();
            ///ceea ce va afisat printr-o animatie
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new MainMenu(game));
                }
            });
            ///adauga o animatie
            SequenceAction sequenceAction = new SequenceAction();
            ///un delay de 2 secunde
            sequenceAction.addAction(Actions.delay(3f));
            ///un efect de fade out de o secunda
            sequenceAction.addAction(Actions.fadeOut(1f));
            ///se adauga imaginea in animatie
            sequenceAction.addAction(run);
            ///se deneseaza animatia
            uiHud.getStage().addAction(sequenceAction);

            ///schimbare melodie
            GameManager.getInstance().changeMusic("Background");
          ///daca playerul mai are macar o viata
        } else {
            ///se reincarca jocul playerul continuand de la prima platforma cu aceeasi animatie
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new Gameplay(game));
                }
            });
            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(2f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);
            uiHud.getStage().addAction(sa);
        }
    }

    /**
     * \fn public void countScoreBySpeed()
     * \brief Metoda ce incrementeaza scorul in functie de viteza camerei.
     */
    public void countScoreBySpeed() {
        if (GameManager.getInstance().gameData.isEasyDifficulty()) { uiHud.incrementScore(1); }
        if (GameManager.getInstance().gameData.isMediumFifficulty()) { uiHud.incrementScore(2); }
        if (GameManager.getInstance().gameData.isHardDIfficulty()) { uiHud.incrementScore(3); }
    }

    /**
     * \fn public void countLifeScoreBySpeed()
     * \brief Metoda ce incrementeaza scorul la fiecare viata luata in functie de viteza camerei.
     */
    public void countLifeScoreBySpeed() {
        if (GameManager.getInstance().gameData.isEasyDifficulty()) { uiHud.incrementLifes(500); }
        if (GameManager.getInstance().gameData.isMediumFifficulty()) { uiHud.incrementLifes(600); }
        if (GameManager.getInstance().gameData.isHardDIfficulty()) { uiHud.incrementLifes(700); }
    }

    /**
     * \fn public void countCoinScoreBySpeed()
     * \brief Metoda ce incrementeaza scorul la fiecare banut luat in functie de viteza camerei.
     */
    public void countCoinScoreBySpeed() {
        if (GameManager.getInstance().gameData.isEasyDifficulty()) { uiHud.incrementCoins(200); }
        if (GameManager.getInstance().gameData.isMediumFifficulty()) { uiHud.incrementCoins(300); }
        if (GameManager.getInstance().gameData.isHardDIfficulty()) { uiHud.incrementCoins(400); }
    }

    /**
     * \fn public void countScore()
     * \brief Metoda ce incrementeaza scorul.
     */
    public void countScore() {
        ///daca ultima pozitie a playerului este mai mare decat pozitia actuala a playerului
        if (lastPlayerPositionY > player.getY()) {
            ///se apeleaza incrementarea scorului in functie de viteza camerei
            countScoreBySpeed();
            ///se memoreaza din nou ultima pozitie a playerului pe axa Y
            lastPlayerPositionY = player.getY();
        }
    }

    @Override
    public void show() { }

    /**
     * \fn public void render(float delta)
     * \brief Metoda ce randeaza totul pe ecran
     *
     * delta -> timpul pentru fiecare frame
     */
    @Override
    public void render(float delta) {
        ///apel metoda de update
        update(delta);

        ///stergere culoare
        Gdx.gl.glClearColor(0, 0, 0, 1);
        ///stergere ecran
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ///se deseneaza o matrice de proiectie a camerei principale
        game.getSpriteBatch().setProjectionMatrix(mainCamera.combined);
        ///se updateaza pozitia camerei principale
        mainCamera.update();

        ///inceputul desenarii pe ecran
        game.getSpriteBatch().begin();
        ///apel metoda de desenare a background-urilor
        drawBackgrounds();
        ///apel metoda de desenat pe ecran a itemelor de colectat
        platformsController.drawCollectables(game.getSpriteBatch());
        //apel metoda de desenare a platformelor
        platformsController.drawPlatforms(game.getSpriteBatch());
        ///aple metoda de desenare a playerului
        player.drawPlayerImage(game.getSpriteBatch());
        ///aple metoda de desenare a animatiei playerului
        player.drawPlayerAnimation(game.getSpriteBatch());
        ///sfarsitul desenarii pe ecran
        game.getSpriteBatch().end();

        ///deseneaza box-ul din jurul obiectelor
        //box2DDebugRenderer.render(world, box2DCamera.combined);

        ///se deseneaza o matrice de proiectie a UIHud-ului
        game.getSpriteBatch().setProjectionMatrix(uiHud.getStage().getCamera().combined);
        ///se deseneaza hud-ul
        uiHud.getStage().draw();
        uiHud.getStage().getBatch().begin();
        ///se deseneaza itemele cu animatie din hud
        uiHud.drawAnimations(game.getSpriteBatch());
        uiHud.getStage().getBatch().end();
        ///se randeaza actiunile din hud
        uiHud.getStage().act();

        ///apel update pozitie player
        player.updatePlayerPosition();
        ///etapa de randare a imaginii pe ecran
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void resize(int width, int height) { gameViewport.update(width, height); }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    /**
     * \fn public void dispose()
     * \brief Metoda ce elibereaza memoria.
     *
     * delta -> timpul pentru fiecare frame
     * Se apeleaza automat atunci cand se termina jocul.
     */
    @Override
    public void dispose() {
        world.dispose();
        ///eliberare memorie pentru toate background-urile
        for (Sprite background : backgrounds)
            background.getTexture().dispose();
        player.getTexture().dispose();
        uiHud.getStage().dispose();
        box2DDebugRenderer.dispose();
        coinSound.dispose();
        lifeSound.dispose();
        deathSound.dispose();
        GameManager.getInstance().clickSouns.dispose();
    }

    /**
     * \fn public void beginContact(Contact contact)
     * \brief Metoda ce implementeaza coliziune din joc.
     */
    @Override
    public void beginContact(Contact contact) {
        ///contactul dintre 2 corpuri
        ///mereu playerul
        Fixture body1;
        ///celalalt obiect cu care intra in contact
        Fixture body2;

        ///daca prima textura este Player
        if (contact.getFixtureA().getUserData() == "Player") {
            ///se seteaza playerul in primul corp
            body1 = contact.getFixtureA();
            ///se seteaza celalalt obiect decontact in al 2-lea corp
            body2 = contact.getFixtureB();
        }
        ///daca prima textura nu este player
        else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        ///daca primul corp este Player si al 2-lea Coin
        if (body1.getUserData() == "Player" && body2.getUserData() == "Coin") {
            ///coliziune cu Coin
            ///porneste sunetul de contact cu banul
            coinSound.play();
            countCoinScoreBySpeed();
            ///seteaza banul de tip "Remove" pentru a fi sters ulterior
            body2.setUserData("Remove");
            ///se sterge obiectul apeland metoda din clasa PlatformsController
            platformsController.removeCollectables();
        }

        ///daca primul corp este Player si al 2-lea Life
        if (body1.getUserData() == "Player" && body2.getUserData() == "Life") {
            ///coliziune cu Life
            lifeSound.play();
            countLifeScoreBySpeed();
            body2.setUserData("Remove");
            platformsController.removeCollectables();
        }

        ///daca primul corp este Player si al 2-lea Bad(platforma rea)
        if (body1.getUserData() == "Player" && body2.getUserData() == "Bad") {
            ///daca playerul nu este mort
            if (!player.getDead()) {
                ///porneste sunetul de omorat playerul
                deathSound.play();
                ///apel metoda de omorat playerul
                killPlayer();
            }
        }
    }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
