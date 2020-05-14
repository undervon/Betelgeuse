package com.undervon.betelgeuse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import helpers.GameManager;
import scenes.MainMenu;

/**
 * \class GameMain
 * \brief Clasa ce va crea si randa jocul.
 *
 * 	Aceasta se apeleaza in main.
 * */
public class GameMain extends Game {
	private SpriteBatch spriteBatch;            /**< atribut care deseneaza tot ceea ce se vede pe ecran*/

	/**
	 * \fn public void create()
	 * \brief Metoda ce creaza jocul.
	 *
	 * Creaza obiectul de tip batch.
	 * Initializeaza jocul prin salvarea scorului/banilor/setarilor intr-un fisier de tip Json.
	 * Seteaza ce screen sa fie afisat cand se porneste jocul.
	 */
	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		GameManager.getInstance().gameDateInitialization();
		setScreen(new MainMenu(this));
	}

	/**
	 * \fn public void render()
	 * \brief Metoda ce randeaza jocul.
	 *
	 * Apel super la metoda de randare din clasa parinte.
	 */
	@Override
	public void render() { super.render(); }

	/**
	 * \fn public SpriteBatch getSpriteBatch()
	 * \brief Metoda ce returneaza spriteBatch-ul(getter).
	 */
	public SpriteBatch getSpriteBatch() { return this.spriteBatch; }
}
