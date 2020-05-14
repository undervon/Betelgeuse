package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undervon.betelgeuse.GameMain;
import helpers.GameInfo;
import huds.MainMeniuButtons;

/**
 * \class MainMenu
 * \brief Clasa ce va crea meniul principal.
 *
 *	Implementeaza interfata Screen.
 *  Interfata Screen este utilizata pentru afisarea elementelor din meniu.
 * */
public class MainMenu implements Screen {
    ///elemente principale
    private GameMain game;                                  /**< atribut care face apel la clasa GameMin pentru utilizarea SpriteBatch-ului*/

    ///camera
    private OrthographicCamera mainCamera;                  /**< camera principala a jocului*/
    private Viewport gameViewport;                          /**< ceea ce se vede pe ecran*/

    ///texturi din meniu
    private Texture background;                             /**< imaginea de background*/
    private MainMeniuButtons mainMeniuButtons;              /**< elementele din meniu*/

    /**
     * \fn public MainMenu(GameMain game)
     * \brief Constructor.
     *
     *\param game pentru utilizarea SpriteBatchulului.
     */
    public MainMenu(GameMain game) {
        this.game = game;

        ///camera din meniu si setarea pozitiei acesteia
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        ///ceea ce se vede in meniu
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        ///textura background-ului
        background = new Texture("1.Backgrounds/Menu Background.png");

        ///crearea butoanelor din meniu
        mainMeniuButtons = new MainMeniuButtons(game);
    }

    @Override
    public void show() { }

    /**
     * \fn public void render(float delta)
     * \brief Metoda Override pentru randarea elementelor pe ecran.
     *
     *\param delta -> timpul pentru fiecare frame(fps).
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getSpriteBatch().begin();
        ///desenare imagine de background incepant din coltul din stanga jos coordonatele 0/0
        game.getSpriteBatch().draw(background, 0, 0);
        game.getSpriteBatch().end();

        ///seteaza ansamblul de butoane din meniu intr-o matrice de proiectie
        game.getSpriteBatch().setProjectionMatrix(mainMeniuButtons.getStage().getCamera().combined);
        ///le deseneaza
        mainMeniuButtons.getStage().draw();
        ///adauga animatia din meniu
        mainMeniuButtons.getStage().act();
    }

    /**
     * \fn public void resize(int width, int height)
     * \brief Metoda Override ce repozitioneaza imaginea ce se vede pe ecrna.
     *
     *\param width latimea
     * \param height inaltimea
     */
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
     * \brief Metoda Override ce elibereaza memoria la finalul executiei programului.
     */
    @Override
    public void dispose() {
        ///elibereaza memoria pentru background
        background.dispose();
        ///pentru butoanele din meniu
        mainMeniuButtons.getStage().dispose();
    }
}
