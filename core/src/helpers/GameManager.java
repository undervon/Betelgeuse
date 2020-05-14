package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

/**
 * \class GameManager
 * \brief Clasa ce implementeaza singleton pattern-ul.
 *
 * Este utilizata pentru creare unui singur obiect care-si modifica atributele.
 * */
public class GameManager {
    ///instanta
    private static volatile GameManager instance = null;                        /**< instanta de singleton*/

    ///atribute pentru joc
    public boolean gameStartefFromMaineMenu;                                    /**< daca jocul este pronit din main menu*/
    public boolean isPaused = true;                                             /**< daca jocul este pus pe pauza*/

    public int lifeScore;                                                       /**< numarul de vieti*/
    public int coinScore;                                                       /**< numarul de bani*/
    public int score;                                                           /**< scorul jocului*/

    ///pentru salvarea jocului
    public GameData gameData;                                                   /**< obiect de tip GameData cu toate datele pentru salvare*/
    private Json json = new Json();                                             /**< obiect Json pentru salvarea datelor intr-un fisier*/
    private FileHandle fileHandler = Gdx.files.local("bin/GameData.json"); /**< fisierul unde se salveaza datele*/

    ///muzica din joc
    private Music music;                                                        /**< obiect de tip music*/

    ///sunetele de apasare
    /**< obiect de tip sound cu sunetul de click din joc*/
    public Sound clickSouns = Gdx.audio.newSound(Gdx.files.internal("9.Sounds/Click Sound.wav"));

    /**
     * \fn public Gameplay(GameMain game)
     * \brief Constructor.
     */
    private GameManager() {}

    /**
     * \fn public void gameDateInitialization()
     * \brief Metoda ce initializeaza datele la momentul pornirii jocului acestea din urma fiind salvate.
     */
    public void gameDateInitialization() {
        ///daca nu exista fisierul fileHandler
        if (!fileHandler.exists()) {
            ///se creeaza un obiect de tip GameData
            gameData = new GameData();

            ///se seteaza parametrii de joc pentru prima pornire a acestuia
            gameData.setHighscore(0);
            gameData.setCoinHighscore(0);

            gameData.setEasyDifficulty(false);
            gameData.setMediumFifficulty(true);
            gameData.setHardDIfficulty(false);

            gameData.setRedCharacter(false);
            gameData.setBlueCharacter(true);
            gameData.setGreenCharacter(false);

            gameData.setMusicOn(true);

            ///se salveaza datele intr-un fisier
            saveData();
          ///daca exista fisierul fileHandler(adica pentru urmatoarele porniri ale jocului)
        } else {
            ///se incarca datele din fisierul cu salvari
            loadData();
        }
    }

    /**
     * \fn public void saveData()
     * \brief Metoda ce salveaza datele intr-un fisier de tip Json.
     *
     * In fisierul fileHandler se salveaza datele sub forma de sir de caractere.
     * Se cripteaza aceste date pentru a nu putea fi modificate din exterior utilizand <Base64Coder.encodeString>.
     * Utilizand json se printeaza aceste date.
     * fileHandler.writeString(..., false) -> pentru a nu se scrie de mai multe ori datele ci sa se suprascrie peste cele deja existente
     */
    public void saveData() {
        //daca exista obiect de tip gameData
        if (gameData != null) { fileHandler.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false); }
    }

    /**
     * \fn private void loadData()
     * \brief Metoda ce incarca datele salvate in fisierul de tip Json.
     *
     * Se decodeaza datele criptate utilizand <Base64Coder.decodeString>.
     * json.fromJson(GameData.class, ...) -> locul de unde se preiau datele.
     */
    private void loadData() {
        gameData = json.fromJson(GameData.class, Base64Coder.decodeString(fileHandler.readString()));
    }

    /**
     * \fn public void newHighscoreChecking()
     * \brief Metoda ce verifica daca exista un nou highscore(ori bani ori scorul din joc).
     */
    public void newHighscoreChecking() {
        int oldHighscore = gameData.getHighscore();                 /**< vechiul highscore*/
        int oldCoinScore = gameData.getCoinHighscore();             /**< vechiul coin highscore*/

        ///daca noul highscore este mai mare decat vechiul highscore se seteaza ca fiind aceste noul highscore
        if (GameManager.getInstance().score > oldHighscore) { gameData.setHighscore(GameManager.getInstance().score); }

        ///daca noul coin highscore este mai mare decat vechiul coin highscore se seteaza ca fiind aceste noul coin highscore
        if(GameManager.getInstance().coinScore > oldCoinScore) { gameData.setCoinHighscore(GameManager.getInstance().coinScore); }

        ///se salveaza datele in fisier
        saveData();
    }

    /**
     * \fn public void playMusic(String musicName)
     * \brief Metoda ce porneste muzica din joc.
     *
     * \param musciName numele melodiei.
     */
    public void playMusic(String musicName) {
        ///daca nu exista obiect de tip music se creaza unul
        if (music == null) { music = Gdx.audio.newMusic(Gdx.files.internal("9.Sounds/" + musicName + " Sound.mp3")); }

        ///daca muzica nu este pornita se porneste si se seteaza sa mearga incontinuu
        if (!music.isPlaying()) {
            music.play();
            music.setLooping(true);
        }
    }

    /**
     * \fn public void stopMusic()
     * \brief Metoda ce opreste muzica din joc.
     */
    public void stopMusic() {
        ///daca muzica este pornita
        if (music.isPlaying()) {
            ///se opreste
            music.stop();
            ///se dealoca memoria
            music.dispose();
            music = null;
        }
    }

    /**
     * \fn public void changeMusic(String musicName)
     * \brief Metoda ce schimba melodiile (o melodie in timpul jocului si o melogie in meniul principal).
     *
     * \param musciName numele melodiei.
     */
    public void changeMusic(String musicName) {
        ///daca muzica este pornita in joc
        if (GameManager.getInstance().gameData.isMusicOn()) {
            ///se opreste melodia actuala
            GameManager.getInstance().stopMusic();
            ///se porneste melogia specificata
            GameManager.getInstance().playMusic(musicName);
        }
    }
    /**
     * \fn public static GameManager getInstance()
     * \brief Metoda getter specifica patternului singleton.
     */
    public static GameManager getInstance() {
        if (instance == null)
            synchronized (GameManager.class) {
                if (instance == null)
                    instance = new GameManager();
            }
        return instance;
    }
}
