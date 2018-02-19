package joueur;

import elementTerrain.NiveauDeJeu;
import elementTerrain.TerrainDeJeu;
import objetJeu.VagueDeMobiles;

public abstract class Joueur {
	private String nom;
	private int energie;

	public Joueur(String nom) {
		this.nom = nom;
		this.energie = 0;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getEnergie() {
		return energie;
	}

	public void setEnergie(int energie) {
		this.energie = energie;
	}

	public void addEnergie(int nrj) {
		this.energie = this.energie + nrj;
	}

	/**
	 * Jouer un tour du jeu.
	 * 
	 * @param vague
	 *            : la vague actuelle déployée.
	 * @param terrain
	 *            : le terrain actuel.
	 * @param level
	 *            : le niveau de jeu actuel.
	 */
	public abstract void jouerTour(VagueDeMobiles vague, TerrainDeJeu terrain, NiveauDeJeu level);

}
