package helpers;

/**
 * \class GameData
 * \brief Clasa ce va implementa settere si gettere pentru salvarea datelor in fisierul de salvari.
 * */
public class GameData {
    private int highscore;                      /**< atribut de highscore*/
    private int coinHighscore;                  /**< atribut de hicghscore pentru bani*/

    private boolean easyDifficulty;             /**< dificultate easy*/
    private boolean mediumFifficulty;           /**< dificultate medium*/
    private boolean hardDIfficulty;             /**< dificultate hard*/

    private boolean redCharacter;               /**< caracter rosu*/
    private boolean blueCharacter;              /**< caracter albastru*/
    private boolean greenCharacter;             /**< caracter verde*/

    private boolean musicOn;                    /**< muzica on/of*/

    /**
     * \fn   public set...()
     * \brief Metoda ce seteaza ....
     * */
    public void setHighscore(int highscore) { this.highscore = highscore; }
    public void setCoinHighscore(int coinHighscore) { this.coinHighscore = coinHighscore; }

    public void setEasyDifficulty(boolean easyDifficulty) { this.easyDifficulty = easyDifficulty; }
    public void setMediumFifficulty(boolean mediumFifficulty) { this.mediumFifficulty = mediumFifficulty; }
    public void setHardDIfficulty(boolean hardDIfficulty) { this.hardDIfficulty = hardDIfficulty; }

    public void setRedCharacter(boolean redCharacter) { this.redCharacter = redCharacter; }
    public void setBlueCharacter(boolean blueCharacter) { this.blueCharacter = blueCharacter; }
    public void setGreenCharacter(boolean greenCharacter) { this.greenCharacter = greenCharacter; }

    public void setMusicOn(boolean musicOn) { this.musicOn = musicOn; }

    /**
     * \fn   public get/is...()
     * \brief Metoda ce returneaza....
     * */
    public int getHighscore() { return this.highscore; }
    public int getCoinHighscore() { return this.coinHighscore; }

    public boolean isEasyDifficulty() { return this.easyDifficulty; }
    public boolean isMediumFifficulty() { return this.mediumFifficulty; }
    public boolean isHardDIfficulty() { return this.hardDIfficulty; }

    public boolean isRedCharacter() { return this.redCharacter; }
    public boolean isBlueCharacter() { return this.blueCharacter; }
    public boolean isGreenCharacter() { return this.greenCharacter; }

    public boolean isMusicOn() { return this.musicOn; }
}
