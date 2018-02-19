package elementTerrain;

public class NiveauDeJeu {
	private String nom;
	private int dureePause;
	private int energieInitiale;
	private int nbMobilesSortants;

	/**
	 * Initialiser un niveau de jeu
	 * 
	 * @param nom
	 *            : le nom du jeu
	 * @param dureePause
	 *            : la duree de Pause entre les vagues
	 * @param energieInitiale
	 *            : l'energie initiale attribué au joueur.
	 * @param nbMobilesSortants
	 *            : le nombres de mobiles à faire arriver à la case d'arrivée
	 *            pour gagner.
	 */
	public NiveauDeJeu(String nom, int dureePause, int energieInitiale, int nbMobilesSortants) {
		this.nom = nom;
		this.dureePause = dureePause;
		this.energieInitiale = energieInitiale;
		this.nbMobilesSortants = nbMobilesSortants;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getDureePause() {
		return dureePause;
	}

	public void setDureePause(int dureePause) {
		this.dureePause = dureePause;
	}

	public int getEnergieInitiale() {
		return energieInitiale;
	}

	public void setEnergieInitiale(int energieInitiale) {
		this.energieInitiale = energieInitiale;
	}

	public int getNbMobilesSortants() {
		return nbMobilesSortants;
	}

	public void setNbMobilesSortants(int nbMobilesSortants) {
		this.nbMobilesSortants = nbMobilesSortants;
	}

}
