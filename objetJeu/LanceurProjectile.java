package objetJeu;

import java.util.*;

import elementJeu.NatureTerrain;
import elementJeu.TactiqueType;
import elementTerrain.Case;
import elementTerrain.TerrainDeJeu;

public abstract class LanceurProjectile extends ObjetJeu {
	private int energie;
	private TactiqueType tactique;
	private List<Projectile> projectiles;

	/**
	 * Initialiser un lanceur de projectile
	 * 
	 * @param positionActuelle
	 *            : la position actuelle du lanceur
	 * @param energie
	 *            : l'energie dont dispose le lanceur de projectile
	 * @param tactique
	 *            : la tactique du lanceur de projectile
	 * @param projectiles
	 *            : la liste des projectiles dont dispose le lanceur
	 */
	public LanceurProjectile(Case positionActuelle, int energie, TactiqueType tactique, List<Projectile> projectiles) {
		super(positionActuelle);
		this.energie = energie;
		this.tactique = tactique;
		this.projectiles = projectiles;
	}

	public int getEnergie() {
		return energie;
	}

	public void setEnergie(int energie) {
		this.energie = energie;
	}

	public TactiqueType getTactique() {
		return tactique;
	}

	public void setTactique(TactiqueType tactique) {
		this.tactique = tactique;
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setProjectiles(List<Projectile> projectiles) {
		this.projectiles = projectiles;
	}

	/*
	 * Un Objet qui reçoit un projectile perd de l'energie de la valeur de la
	 * quantité d'énergie du projectile qui l'a touché. int typesCibles = 1 si
	 * la fonction est appelée par un obstacle et 0 si appelée par un mobile
	 */
	public void recevoirProjectile(Projectile p) {
		this.setEnergie(Math.max(0, this.getEnergie() - p.getQuantiteEnergie()));
	}

	/**
	 * Récupérer la liste de cible à portée
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 * @param alancer
	 *            : le projectile qui sera lancé
	 * @param typeCibles
	 *            : le type de cible à chercher ( = 1 si on veut viser des
	 *            mobiles, 0 pour les obstacles
	 * @return la liste des cases à portée
	 */
	public List<Case> getCibles(TerrainDeJeu terrain, Projectile alancer, int typeCibles) {
		/* Récupération du damier du terrain actuel */
		Case[][] damier = terrain.getDamier();
		/* Création de la liste de cibles */
		List<Case> listeCibles = new ArrayList<>();
		/* Parcours des cases du terrain à la recherche de cible */
		for (int i = 0; i < terrain.getNbLignes(); i++) {
			for (int j = 0; j < terrain.getNbColonnes(); j++) {
				/* LA case doit être dans le rayon de portée du projectile */
				if (this.getPositionActuelle().distance(damier[i][j]) <= alancer.getPortee()) {
					/* Cas recherche de Mobiles comme cibles */
					if (typeCibles == 1) {
						if (damier[i][j].getNatureTerrain().equals(NatureTerrain.Chemin)
								&& !(damier[i][j].getObjetsdispo().isEmpty())) {
							listeCibles.add(damier[i][j]);
						}
					} else if (typeCibles == 0) {
						/* Cas recherche d'Obstacles comme cibles */
						if (damier[i][j].getNatureTerrain().equals(NatureTerrain.Campement)) {
							listeCibles.add(damier[i][j]);
						}
					}
				}
			}
		}
		return listeCibles;
	}

	/**
	 * Renvoyer une cible selon la strategie et l'ensemble des cibles à portée
	 * 
	 * @param tactique
	 *            : la tactique du lanceur de projectile
	 * @param targets
	 *            : la liste des cibles atteignables
	 * @return la case contenant la cible
	 */
	public Case getTarget(TactiqueType tactique, List<Case> targets) {
		switch (tactique) {
		case PlusProche:
			return getClosestTarget(targets);
		case PlusFort:
			return getStrongestTarget(targets);
		case PlusFaible:
			return getWeakestTarget(targets);
		default:
			return null;
		}
	}

	/**
	 * Renvoyer une cible pour la stratégie PlusFaible
	 * 
	 * @param targets
	 *            : la liste de cible atteignable
	 * @return la case contenant la cible
	 */
	public Case getWeakestTarget(List<Case> targets) {
		Case caseactu = null;
		Case caseFaible = null;
		int energieMin = 0;
		int newEnergie = 0;
		Iterator<Case> icase = targets.iterator();
		/* Parcours de la liste de cibles */
		while (icase.hasNext()) {
			caseactu = icase.next();
			List<ObjetJeu> objetdispo = caseactu.getObjetsdispo();
			/*
			 * Parcourir la liste d'objet pour en retrouver le plus faible par
			 * case
			 */
			Iterator<ObjetJeu> iobj = objetdispo.iterator();
			ObjetJeu objactu;
			int nrjMinLocale = 0;
			while (iobj.hasNext()) {
				objactu = iobj.next();
				int newnrj = objactu.getEnergie();
				nrjMinLocale = Math.min(newnrj, nrjMinLocale);
			}
			/* Le minimum Locale de la case a été trouvé */
			newEnergie = nrjMinLocale;

			/* Mise à jour des valeurs */
			if (energieMin == 0) {
				energieMin = newEnergie;
				caseFaible = caseactu;
			} else {
				if (newEnergie < energieMin) {

					energieMin = newEnergie;
					caseFaible = caseactu;
				}
			}
		}

		return caseFaible;
	}

	/**
	 * Renvoyer une cible pour la stratégie PlusProche
	 * 
	 * @param targets
	 *            : la liste de cible atteignable
	 * @return la case contenant la cible
	 */

	public Case getClosestTarget(List<Case> targets) {
		Case caseactu = null;
		Case caseProche = null;
		Case poseactu = this.getPositionActuelle();
		double distanceMin = 0;
		double newdistance = 0;
		Iterator<Case> icase = targets.iterator();
		/* Parcours de la liste de cibles */
		while (icase.hasNext()) {
			caseactu = icase.next();
			newdistance = poseactu.distance(caseactu);
			/* Mise à jour des valeurs */
			if (distanceMin == 0) {
				distanceMin = newdistance;
				caseProche = caseactu;
			} else {
				if (newdistance < distanceMin) {
					distanceMin = newdistance;
					caseProche = caseactu;
				}
			}
		}
		return caseProche;
	}

	/**
	 * Renvoyer une cible pour la stratégie PlusFort
	 * 
	 * @param targets
	 *            : la liste de cible atteignable
	 * @return la case contenant la cible
	 */
	public Case getStrongestTarget(List<Case> targets) {
		Case caseactu = null;
		Case caseForte = null;
		int energieMax = 0;
		int newEnergie = 0;
		Iterator<Case> icase = targets.iterator();
		/* Parcours de la liste de cibles */
		while (icase.hasNext()) {
			caseactu = icase.next();
			List<ObjetJeu> objetdispo = caseactu.getObjetsdispo();
			/*
			 * Parcourir la liste d'objet pour en retrouver le plusfort par case
			 */
			Iterator<ObjetJeu> iobj = objetdispo.iterator();
			ObjetJeu objactu;
			int nrjMaxLocale = 0;
			while (iobj.hasNext()) {
				objactu = iobj.next();
				int newnrj = objactu.getEnergie();
				nrjMaxLocale = Math.max(newnrj, nrjMaxLocale);
			}
			/* Le maximum Locale de la case a été trouvé */
			newEnergie = nrjMaxLocale;

			/* Mise à jour des valeurs */
			if (energieMax == 0) {
				energieMax = newEnergie;
				caseForte = caseactu;
			} else {
				if (newEnergie > energieMax) {
					energieMax = newEnergie;
					caseForte = caseactu;
				}
			}
		}
		return caseForte;
	}

	/*
	 * Methode qui sera appelé par lancerprojectile dans les classes Mobile et
	 * Obstacle
	 */
	/**
	 * Effectuer l'action lancer un projectile selon le type de cible
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 * @param typeCible
	 *            : le type de cible à viser
	 */
	public void lancerProjectile(TerrainDeJeu terrain, int typeCible) {
		/* Récupération de sa stratégie de tir */
		TactiqueType tactique = this.getTactique();
		/* Récupérer un projectile quelconque à lancer */
		// Pour le moment on utilise un iterateur
		Iterator<Projectile> iproj = this.getProjectiles().iterator();
		if (iproj.hasNext()) {
			Projectile alancer = iproj.next();
			/* Récupérer une cible */
			Case cible = this.getTarget(tactique, this.getCibles(terrain, alancer, typeCible));
			if (cible != null) {
				cible.recevoirProjectile(alancer);
				/*
				 * Le projectile a été lancé, on le retire de la liste de
				 * projectile dispo
				 */
				iproj.remove();
			}
		}
	}

	@Override
	public void activer(TerrainDeJeu terrain) {
		seDeplacer(terrain);
		lancerProjectile(terrain);
	}

	/**
	 * Effectuer l'action lancer un projectile
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 */
	public abstract void lancerProjectile(TerrainDeJeu terrain);

}
