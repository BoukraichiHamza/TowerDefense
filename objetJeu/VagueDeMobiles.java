package objetJeu;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elementTerrain.Case;
import elementTerrain.NiveauDeJeu;
import elementTerrain.TerrainDeJeu;
import joueur.Joueur;

public class VagueDeMobiles {
	private String description;
	private Map<Integer, ObjetJeu> mobiles;
	private List<ObjetJeu> obstacles;
	private Case caseEntree;
	private Case caseSortie;
	private int energieGagnee;

	/**
	 * Initialiser une vague de mobiles
	 * 
	 * @param description
	 *            : la description de la vague
	 * @param mobiles
	 *            : l'ensemble des mobiles de la vagues
	 * @param obstacles
	 *            : la liste des obstacles de la vague
	 * @param caseentree
	 *            : la case d'entree des mobiles de la vague
	 * @param casesortie
	 *            : la case de sortie des mobiles de la vague
	 * @param energieGagnee
	 *            : l'energie gagnee apres le deploiement de la vague
	 */
	public VagueDeMobiles(String description, Map<Integer, ObjetJeu> mobiles, List<ObjetJeu> obstacles, Case caseentree,
			Case casesortie, int energieGagnee) {
		super();
		this.description = description;
		this.mobiles = mobiles;
		this.obstacles = obstacles;
		this.caseEntree = caseentree;
		this.caseSortie = casesortie;
		this.energieGagnee = energieGagnee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Case getCaseEntree() {
		return caseEntree;
	}

	public void setCasEentree(Case caseEntree) {
		this.caseEntree = caseEntree;
	}

	public Case getCaseSortie() {
		return caseSortie;
	}

	public void setCaseSortie(Case caseSortie) {
		this.caseSortie = caseSortie;
	}

	public Map<Integer, ObjetJeu> getMobiles() {
		return mobiles;
	}

	public void setMobiles(Map<Integer, ObjetJeu> mobiles) {
		this.mobiles = mobiles;
	}

	public List<ObjetJeu> getObstacles() {
		return obstacles;
	}

	public void setObstacles(List<ObjetJeu> obstacles) {
		this.obstacles = obstacles;
	}

	public int getEnergieGagnee() {
		return energieGagnee;
	}

	public void setEnergieGagnee(int energieGagnee) {
		this.energieGagnee = energieGagnee;
	}

	public void setCaseEntree(Case caseEntree) {
		this.caseEntree = caseEntree;
	}

	/** Lancer une vague de mobiles */

	/**
	 * Déployer une vague
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 * @param level
	 *            : le niveau actuel
	 * @param joueur
	 *            : le joueur de la partie
	 * @return boolean : true si la partie est terminé ( le joueur a perdu)
	 */
	public boolean lancer(TerrainDeJeu terrain, NiveauDeJeu level, Joueur joueur) {
		boolean vagueTerminee = false;
		boolean objectifAtteint = false;
		/* Récupération la position maximum disponible */
		int positionMax = this.getMobiles().size();
		/* Affichage EtatInitiale */
		terrain.afficherMatrice();
		while (!vagueTerminee) {
			int positionActuelle = 1;
			Iterator<ObjetJeu> iob = this.obstacles.iterator();
			Mobile mobactu;
			Obstacle obactu;

			/* Boucle pour activer tous les mobiles de la vagues */
			while (positionActuelle <= positionMax) {
				mobactu = (Mobile) this.getMobiles().get(positionActuelle);

				/* Vérifier d'abord si le mobile doit être détruit */
				if (mobactu != null) {
					if (mobactu.getEnergie() == 0) {
						this.getMobiles().remove(positionActuelle);
						mobactu.getPositionActuelle().deleteObject(mobactu);

						// Récompenser le joueur
						joueur.addEnergie(1);

					} else {
						mobactu.activer(terrain);
					}
				}
				positionActuelle++;
			}
			/* Boucle pour activer tous les obstacles de la vagues */
			while (iob.hasNext()) {
				obactu = (Obstacle) iob.next();
				/* vérifier d'abord si l'obstacle doit être détruit */
				if (obactu.getEnergie() == 0) {
					iob.remove();
					obactu.getPositionActuelle().deleteObject(obactu);
				} else {
					obactu.activer(terrain);
				}
			}
			objectifAtteint = this.caseSortie.getObjetsdispo().size() >= level.getNbMobilesSortants();
			vagueTerminee = this.mobiles.isEmpty() || objectifAtteint || (!terrain.mobileActivableDisponible());
			terrain.afficherMatrice();
		}
		return objectifAtteint;
	}

	/** Nettoyer le terrain de jeu après le déploiement d'une vague. */
	public void nettoyerVague() {
		for (ObjetJeu m : this.getMobiles().values()) {
			this.getMobiles().remove(m);
			m.getPositionActuelle().deleteObject(m);
		}
		for (ObjetJeu o : this.getObstacles()) {
			o.getPositionActuelle().deleteObject(o);
		}
	}

	/**
	 * Enregistrer les objets sur leurs cases respectifs au début de la vague.
	 */

	public void saveObject() {
		for (ObjetJeu m : this.getMobiles().values()) {
			m.getPositionActuelle().addObject(m);
		}
		for (ObjetJeu o : this.getObstacles()) {
			o.getPositionActuelle().addObject(o);
		}

	}
}
