package elementJeu;

import objetJeu.Projectile;

public interface ElementJeu {

	/**
	 * Action qui découle du fait de recevoir un projectile
	 * 
	 * @param p
	 *            : projectile reçu.
	 */
	public void recevoirProjectile(Projectile p);

	/** Afficher l'élément du Jeu. */
	public void print();
}
