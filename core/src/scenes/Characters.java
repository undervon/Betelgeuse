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
import huds.CharactersButtons;

/**
 * \class Characters
 * \brief Clasa ce va crea meniul pentru selectarea caracterului.
 *
 *	Implementeaza interfata Screen.
 *  Interfata Screen este utilizata pentru afisarea elementelor din meniu.
 * */
public class Characters implements Screen {
    ///elemente principale
    private GameMain game;                                  /**< atribut care face apel la clasa GameMin pentru utilizarea SpriteBatch-ului*/

    ///camera
    private OrthographicCamera mainCamera;                  /**< camera principala a jocului*/
    private Viewport gameViewport;                          /**< ceea ce se vede pe ecran*/

    ///texturile din meniu
    private Texture backgroundImage;                        /**< imaginea de background*/
    private CharactersButtons charactersButtons;            /**< butoanele din meniul de caractere*/

    /**
     * \fn public Characters(GameMain game)
     * \brief Constructor.
     *
     *\param game pentru utilizarea SpriteBatchulului.
     */
    public Characters(GameMain game) {
        this.game = game;

        ///camera din meniu si setarea pozitiei acesteia
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        ///ceea ce se vede in meniu
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        ///creare imaginea de background
        backgroundImage = new Texture("1.Backgrounds/Characters Background.png");

        ///crearea obiectului de tip butoane din meniul de caractere
        charactersButtons = new CharactersButtons(game);
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
        game.getSpriteBatch().draw(backgroundImage, 0, 0);
        game.getSpriteBatch().end();

        ///seteaza ansamblul de butoane din meniu intr-o matrice de proiectie
        game.getSpriteBatch().setProjectionMatrix(charactersButtons.getStage().getCamera().combined);
        ///le deseneaza
        charactersButtons.getStage().draw();
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
        backgroundImage.dispose();
        charactersButtons.getStage().dispose();
    }
}
