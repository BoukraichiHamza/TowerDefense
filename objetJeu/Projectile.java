package objetJeu;

import elementTerrain.Case;
import elementTerrain.TerrainDeJeu;

public class Projectile extends ObjetJeu {
	private int portee;
	private int masse;
	private int vitesse;
	private int quantiteEnergie;

	/**
	 * Initialiser un projectile
	 * 
	 * @param positionActuelle
	 *            : sa position actuelle
	 * @param portee
	 *            : la portee du projectile
	 * @param masse
	 *            : la masse du projectile
	 * @param vitesse
	 *            : la vitesse du projectile
	 * @param quantiteEnergie
	 *            : la quantité d'énergie dont dispose le projectile
	 */
	public Projectile(Case positionActuelle, int portee, int masse, int vitesse, int quantiteEnergie) {
		super(positionActuelle); // Pour cette version le projectile arrive en
									// un tour, on a pas besoin de sauvegarder
									// sa position
		this.portee = portee;
		this.masse = masse;
		this.vitesse = vitesse;
		this.quantiteEnergie = quantiteEnergie;
	}

	public int getPortee() {
		return portee;
	}

	public void setPortee(int portee) {
		this.portee = portee;
	}

	public int getMasse() {
		return masse;
	}

	public void setMasse(int masse) {
		this.masse = masse;
	}

	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}

	public int getQuantiteEnergie() {
		return quantiteEnergie;
	}

	public void setQuantiteEnergie(int quantiteEnergie) {
		this.quantiteEnergie = quantiteEnergie;
	}

	@Override
	public void recevoirProjectile(Projectile p) {
		// Rien
	}

	@Override
	public void seDeplacer(TerrainDeJeu Terrain) {
		/* Pour le moment les projectiles touchent directement les cibles */
	}

	@Override
	public void activer(TerrainDeJeu terrain) {
		/* Rien a faire dans l'activation d'un projectile */
	}

	@Override
	public void print() {
		// Rien pour cette version

	}

	@Override
	public int getVolume() {
		return 0;
	}

	@Override
	public int getEnergie() {
		return this.quantiteEnergie;
	}

	@Override
	public void setEnergie(int energie) {
		this.quantiteEnergie = energie;
	}

}
