package objetJeu;

import elementJeu.ElementJeu;
import elementTerrain.Case;
import elementTerrain.TerrainDeJeu;

public abstract class ObjetJeu implements ElementJeu {
	private String nom;
	private Case positionActuelle;

	public ObjetJeu(Case positionActuelle) {
		super();
		this.positionActuelle = positionActuelle;
	}

	public Case getPositionActuelle() {
		return positionActuelle;
	}

	public void setPositionActuelle(Case positionActuelle) {
		this.positionActuelle = positionActuelle;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public abstract int getEnergie();

	public abstract void setEnergie(int energie);

	public abstract int getVolume();

	/**
	 * Déplacer un objet sur le terrain
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 */
	public abstract void seDeplacer(TerrainDeJeu terrain);

	/**
	 * Activer un objet sur le terrain
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 */
	public abstract void activer(TerrainDeJeu terrain);

}
