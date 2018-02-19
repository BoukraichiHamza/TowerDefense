package elementTerrain;

import elementJeu.NatureTerrain;

public class TerrainDeJeu {
	private int nbLignes;
	private int nbColonnes;
	private Case[][] damier;

	/**
	 * Initialiser un Terrain de Jeu.
	 * 
	 * @param nbLignes
	 *            : nombres de lignes du damier.
	 * @param nbColonnes
	 *            : nombres des colonnes du damier.
	 * @param damier
	 *            : le damier du jeu.
	 */
	public TerrainDeJeu(int nbLignes, int nbColonnes, Case[][] damier) {
		super();
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		this.damier = damier;
	}

	public int getNbLignes() {
		return nbLignes;
	}

	public void setNbLignes(int nbLignes) {
		this.nbLignes = nbLignes;
	}

	public int getNbColonnes() {
		return nbColonnes;
	}

	public void setNbColonnes(int nbColonnes) {
		this.nbColonnes = nbColonnes;
	}

	public Case[][] getDamier() {
		return damier;
	}

	public void setDamier(Case[][] damier) {
		this.damier = damier;
	}

	/** Afficher le terrain. */
	public void afficherTerrain() {
		System.out.println("-----------------------------------");
		Case[][] damier = this.getDamier();
		for (int i = 0; i < this.getNbLignes(); i++) {
			for (int j = 0; j < this.getNbColonnes(); j++) {
				damier[i][j].print();
			}
		}
		System.out.println("-----------------------------------");
	}

	/** Afficher le terrain sous forme matricielle. */
	public void afficherMatrice() {
		System.out.println("====================================");
		Case[][] damier = this.getDamier();
		for (int i = 0; i < this.getNbLignes(); i++) {
			for (int j = 0; j < this.getNbColonnes(); j++) {
				damier[i][j].printMatrice();
				System.out.print(" | ");
			}
			System.out.print("\n");
			for (int j = 0; j < this.getNbColonnes(); j++) {
				System.out.print("--------------- ");
			}
			System.out.print("\n");
		}
		System.out.println("====================================");
	}

	/**
	 * Vérifier si il existe encore des mobiles activables sur le damier.
	 * 
	 * @return boolean : true si il y au moins un mobile activable.
	 */
	public boolean mobileActivableDisponible() {
		boolean res = false;
		for (int i = 0; i < nbLignes; i++) {
			for (int j = 0; j < nbColonnes; j++) {
				if (this.damier[i][j].getNatureTerrain().equals(NatureTerrain.Chemin)) {
					res = res || !(this.damier[i][j].getObjetsdispo().isEmpty());
				}
			}
		}
		return res;
	}
}
