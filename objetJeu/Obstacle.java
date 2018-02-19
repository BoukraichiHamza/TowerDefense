package objetJeu;

import elementJeu.*;
import elementTerrain.*;
import java.util.*;

public class Obstacle extends LanceurProjectile {

	/**
	 * Initialiser un obstacle
	 * 
	 * @param positionActuelle
	 *            : la position actuelle de l'obstacle
	 * @param energie
	 *            : l'energie dont dispose l'obstacle
	 * @param tactique
	 *            : la tactique de l'obstacle
	 * @param projectiles
	 *            : la listes de projectiles de l'obstacle
	 */
	public Obstacle(Case positionActuelle, int energie, TactiqueType tactique, List<Projectile> projectiles) {
		super(positionActuelle, energie, tactique, projectiles);
	}

	@Override
	public void seDeplacer(TerrainDeJeu terrain) {
		/* Rien : un obstacle ne se déplace pas */
	}

	@Override
	public void lancerProjectile(TerrainDeJeu terrain) {
		lancerProjectile(terrain, 1);
	}

	@Override
	public int getVolume() {
		return 0;
	}

	@Override
	public void print() {
		// System.out.println("Obstacle : ." );
		System.out.println("Obstacle : " + this.getTactique().toString() + " Energie : " + this.getEnergie() + " .");
	}

}
