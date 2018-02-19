package objetJeu;

import java.util.List;

import elementJeu.NatureTerrain;
import elementJeu.TactiqueType;
import elementTerrain.Case;
import elementTerrain.TerrainDeJeu;

public class Mobile extends LanceurProjectile {
	private int volume;
	private int vitesse;
	private int position;
	private Case caseDepart;
	private Case caseArrivee;
	private List<Case> chemin;

	/**
	 * Initialiser un mobile
	 * 
	 * @param volume
	 *            : le volume occupé par le mobile
	 * @param energie
	 *            : l'energie dont dispose le mobile
	 * @param caseDepart
	 *            : la case de départ du mobile
	 * @param caseArrivee
	 *            : la case d'arrivée du mobile
	 * @param tactique
	 *            : la tactique du mobile
	 * @param position
	 *            : la position du mobile dans la vague
	 * @param vitesse
	 *            : la vitesse du mobile
	 * @param projectiles
	 *            : la liste de projectile du mobile
	 * @param terrain
	 *            : le terrain de la partie ( pour calculer le chemin du mobile)
	 */
	public Mobile(int volume, int energie, Case caseDepart, Case caseArrivee, TactiqueType tactique, int position,
			int vitesse, List<Projectile> projectiles, TerrainDeJeu terrain) {
		super(caseDepart, energie, tactique, projectiles);
		this.volume = volume;
		this.caseDepart = caseDepart;
		this.caseArrivee = caseArrivee;
		this.position = position;
		this.vitesse = vitesse;
		this.chemin = this.getCaseDepart().chemin(this.getCaseArrivee(), terrain);
	}

	public int getVolume() {
		return this.volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Case getCaseDepart() {
		return caseDepart;
	}

	public void setCaseDepart(Case casedepart) {
		this.caseDepart = casedepart;
	}

	public Case getCaseArrivee() {
		return caseArrivee;
	}

	public void setCaseArrivee(Case casearrivee) {
		this.caseArrivee = casearrivee;
	}

	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}

	public List<Case> getChemin() {
		return chemin;
	}

	public void setChemin(List<Case> chemin) {
		this.chemin = chemin;
	}

	@Override
	public void lancerProjectile(TerrainDeJeu terrain) {
		lancerProjectile(terrain, 0);
	}

	@Override
	public void seDeplacer(TerrainDeJeu terrain) {
		/* Le mobile doit se deplacer selon sa vitesse */
		int vitesseDeBase = this.getVitesse();
		int vitesseActuelle = vitesseDeBase;

		while (vitesseActuelle > 0) {

			/* Récupération d'une case adjacente */
			Case cible = this.getPositionActuelle().getAdjacente(this.chemin);
			if (cible != null) {

				/* Test si deplacement possible */
				if ((cible.getVolumeMaximal() - this.getVolume() >= 0 && (vitesseActuelle >= cible.getEnergieRequise()))
						|| cible.getNatureTerrain().equals(NatureTerrain.Sortie)) {

					/* Suppression de l'objet de la case précédente */
					this.getPositionActuelle().deleteObject(this);
					/* On veut se déplacer sur la case trouvée */
					setPositionActuelle(cible);
					this.getPositionActuelle().addObject(this);
					this.chemin.remove(cible);
					/* On réduit la vitesse du mobile */
					vitesseActuelle = vitesseActuelle - cible.getEnergieRequise();
				} else {
					/* Mouvement impossible, on met la vitese à 0 */
					vitesseActuelle = 0;
				}
			} else {
				/* Mouvement impossible, on met la vitese à 0 */
				vitesseActuelle = 0;
			}
		}
	}

	@Override
	public void print() {
		System.out.println("Mobile : " + this.getTactique().toString() + " Energie : " + this.getEnergie() + " .");
	}
}