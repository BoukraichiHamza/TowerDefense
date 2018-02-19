package joueur;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import elementJeu.NatureTerrain;
import elementJeu.TactiqueType;
import elementTerrain.Case;
import elementTerrain.NiveauDeJeu;
import elementTerrain.TerrainDeJeu;
import objetJeu.ObjetJeu;
import objetJeu.Obstacle;
import objetJeu.Projectile;
import objetJeu.VagueDeMobiles;

public class JoueurAleatoire extends Joueur {
	private Random rd;

	public JoueurAleatoire(String nom) {
		super(nom);
		rd = new Random();
	}

	public void jouerTour(VagueDeMobiles vague, TerrainDeJeu terrain, NiveauDeJeu level) {
		int choix = 7000; // Valeur quelconque
		int energieRestante = this.getEnergie();

		while (choix != 1) {
			int choixproba = rd.nextInt(100);
			if (choixproba >= 80) {
				choix = 1;
			} else {
				choix = 0;
			}
			energieRestante = effectuerAction(vague, terrain, level, energieRestante, choix);
		}
		this.setEnergie(energieRestante);
	}

	/**
	 * Effectuer une action par le joueur passif.
	 * 
	 * @param vague
	 *            : la vague actuelle
	 * @param terrain
	 *            : le terrain actuel
	 * @param level
	 *            : le niveau actuel
	 * @param energieDisponible
	 *            : l'energie disponible
	 * @param choix
	 *            : le choix de l'action à effectuée
	 * @return : l'energie restante du joueur.
	 */
	private int effectuerAction(VagueDeMobiles vague, TerrainDeJeu terrain, NiveauDeJeu level, int energieDisponible,
			int choix) {
		int energieRestante = energieDisponible;
		switch (choix) {
		case 0:
			energieRestante = effectuerAction2(vague, terrain, level, energieRestante);
			break;
		case 1:
			break;
		default:
			break;
		}
		return energieRestante;
	}

	/**
	 * Effectuer une action par le joueur passif si son choix est l'action no2.
	 * 
	 * @param vague
	 *            : la vague actuelle
	 * @param terrain
	 *            : le terrain actuel
	 * @param level
	 *            : le niveau actuel
	 * @param energieDisponible
	 *            : l'energie disponible
	 * @return : l'energie restante du joueur.
	 */
	private int effectuerAction2(VagueDeMobiles vague, TerrainDeJeu terrain, NiveauDeJeu level, int energieDispo) {
		// Valeur à récup
		int nbrCoup = level.getDureePause();
		int choix = rd.nextInt(4);
		// Gestion de l'energie
		int energieRestante = energieDispo;
		if ((energieRestante > 0) && (nbrCoup > 0)) {
			switch (choix) {
			case 0:
				energieRestante = effectuerReparation(terrain, energieDispo);
				level.setDureePause(--nbrCoup);
				break;
			case 1:
				energieRestante = effectuerAchat(terrain, energieDispo, vague);
				level.setDureePause(--nbrCoup);
				break;
			case 2:
				energieRestante = energieRestante + effectuerVente(terrain, vague);
				level.setDureePause(--nbrCoup);
				break;
			case 3:
				effectuerDeplacementObsacle(terrain);
				energieRestante--;
				level.setDureePause(--nbrCoup);
				break;
			default:
				break;
			}
		} else if (energieRestante == 0) {
			// Vous n'avez plus d'énergie
		} else if (nbrCoup == 0) {
			// La pause est terminée
		}
		return energieRestante;
	}

	/**
	 * Choisir une case sur le terrain.
	 * 
	 * @param terrain
	 *            : le terrain actuel.
	 * @return la case choisie par le joueur.
	 */
	private Case choixCase(TerrainDeJeu terrain) {
		int nblignes = terrain.getNbLignes();
		int nbcolonnes = terrain.getNbColonnes();
		int ligne = rd.nextInt(nblignes);
		int colonne = rd.nextInt(nbcolonnes);
		return terrain.getDamier()[ligne][colonne];
	}

	/**
	 * Choisir une case en vérifiant des contraintes spécifiées.
	 * 
	 * @param terrain
	 *            : le terrain actuelle
	 * @param doitcontenir
	 *            : vaut true si la case à choisir doit contenir un campement,
	 *            false sinon
	 * @return la case choisie par le joueur
	 */
	private Case choixCaseAdequate(TerrainDeJeu terrain, boolean doitcontenir) {
		boolean choixvalide = false;
		Case choix = null;
		while (!choixvalide) {
			choix = choixCase(terrain);
			if (choix.getNatureTerrain().equals(NatureTerrain.Campement)) {
				if ((choix.getObjetsdispo().isEmpty()) && (doitcontenir)) {
					// "La case choisie ne contient pas d'obstacle
				} else if ((!choix.getObjetsdispo().isEmpty()) && (!doitcontenir)) {
					// La case choisie contient déjà un obstacle.");
				} else {
					choixvalide = true;
				}
			}
		}
		return choix;
	}

	/**
	 * Effectuer le deplacement d'un Obstacle
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 */
	private void effectuerDeplacementObsacle(TerrainDeJeu terrain) {
		Case caseInit = choixCaseAdequate(terrain, true);
		Case caseCible = choixCaseAdequate(terrain, false);

		/* Déplacement de l'obstacle */
		ObjetJeu adeplacer = caseInit.getObjetsdispo().get(0);
		caseInit.deleteObject(adeplacer);
		caseCible.addObject(adeplacer);
		System.out.println("Déplacement effectué avec succés.");
	}

	/**
	 * effecuter la vente d'un obstacle
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 * @param vague
	 *            : la vague actuelle
	 * @return l'energie gagnée apres la vente
	 */
	private int effectuerVente(TerrainDeJeu terrain, VagueDeMobiles vague) {
		Case caseVente = choixCaseAdequate(terrain, true);

		/* Vente de l'obstacle */
		ObjetJeu aVendre = caseVente.getObjetsdispo().get(0);
		caseVente.deleteObject(aVendre);
		List<ObjetJeu> newObstacle = vague.getObstacles();
		newObstacle.remove(aVendre);
		vague.setObstacles(newObstacle);
		int energieGagnee = aVendre.getEnergie();
		System.out.println("Vente effectuée avec succés. ");
		return energieGagnee;
	}

	/**
	 * Récupérer le choix du joueur pour la tactique de l'obstacle
	 * 
	 * @return la tactique de l'obstacle
	 */

	private TactiqueType choixTactique() {
		TactiqueType tactique = null;
		int choix = rd.nextInt(4);
		switch (choix) {
		case 0:
			tactique = TactiqueType.PlusFaible;
			break;
		case 1:
			tactique = TactiqueType.PlusFort;
			break;
		case 2:
			tactique = TactiqueType.PlusFort;
			break;
		default:
			break;

		}
		return tactique;
	}

	/**
	 * Récupérer le choix du joueur pour l'énérgie d'un obstacle
	 * 
	 * @param energieDispo
	 *            : l'energie à consommer
	 * @return le choix du joueur
	 */
	private int choixEnergie(int energieDispo) {
		int choix = rd.nextInt(energieDispo + 1);
		return choix;
	}

	/**
	 * Effectuer l'opération d'achat
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 * @param energieDispo
	 *            : l'energie à consommer
	 * @param vague
	 *            : la vague actuelle
	 * @return l'energie restante après la vente
	 */
	private int effectuerAchat(TerrainDeJeu terrain, int energieDispo, VagueDeMobiles vague) {
		int energieRestante = energieDispo;

		// Choix de la case qui contiendra l'obstacle
		Case campement = choixCaseAdequate(terrain, false);

		// Choix de l'energie de l'obstacle
		int energieChoisie = choixEnergie(energieRestante);
		energieRestante = energieRestante - energieChoisie;

		// Choix des projectiles
		/* Pour le moment on ajoute un projectile de base */
		Projectile p = new Projectile(campement, 1, 1, 1, 1);
		List<Projectile> newproj = new LinkedList<Projectile>();
		newproj.add(p);

		Obstacle newobs = new Obstacle(campement, energieChoisie, choixTactique(), newproj);

		// Ajout Du projectile a la vague
		List<ObjetJeu> newObstacle = vague.getObstacles();
		newObstacle.add(newobs);
		vague.setObstacles(newObstacle);
		newobs.getPositionActuelle().addObject(newobs);

		System.out.println("Ajout de l'obstacle effectué avec succés.");
		return energieRestante;
	}

	/**
	 * Récupérer le choix du joueur pour le type de réparation à effectuer.
	 * 
	 * @return le choix du joueur.
	 */
	private int choixActionReparation() {
		int choix = rd.nextInt(2);
		return choix;
	}

	/**
	 * Effectuer l'action de création d'un projectile
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 * @param energieDispo
	 *            : l'energie à consommer
	 * @return le projectile créé
	 */
	private Projectile creationDuProjectile(TerrainDeJeu terrain, int energieDispo) {
		boolean choixTermine = false;
		int choixp;
		int choixe;
		int energieRestante = energieDispo;
		Projectile p = new Projectile(null, 1, 1, 1, 1);
		while (!choixTermine) {

			choixp = rd.nextInt(energieRestante + 1);
			choixe = rd.nextInt(energieRestante + 1);
			if (choixe + choixp > energieRestante) {
				// Vous n'avez pas assez d'energie
			} else {
				p.setQuantiteEnergie((p.getQuantiteEnergie() + choixe));
				p.setPortee(p.getPortee() + choixp);
				choixTermine = true;
			}

		}
		return p;
	}

	/**
	 * Effectuer l'action de réparation
	 * 
	 * @param terrain
	 *            : le terrain actuel
	 * @param energieDispo
	 *            : l'energie disponible
	 * @return l'energie restante
	 */
	private int effectuerReparation(TerrainDeJeu terrain, int energieDispo) {
		int energieRestante = energieDispo;
		// Choix de la case qui contiend l'obstacle
		Case campement = choixCaseAdequate(terrain, true);

		// Recupération de l'obstacle
		ObjetJeu obs = campement.getObjetsdispo().get(0);

		// Choix de l'action
		int action = choixActionReparation();

		// Effectuer l'action
		switch (action) {
		case 0:
			int energieChoisie = choixEnergie(energieDispo);
			energieRestante = energieRestante - energieChoisie;
			obs.setEnergie(obs.getEnergie() + energieChoisie);
			System.out.println("Soin de l'objet effectué avec succés.");
			break;
		case 1:
			Projectile p = creationDuProjectile(terrain, energieRestante);
			energieRestante = energieRestante + 2 - p.getEnergie() - p.getPortee();
			List<Projectile> newproj = ((Obstacle) obs).getProjectiles();
			newproj.add(p);
			((Obstacle) obs).setProjectiles(newproj);
			System.out.println("Ajout du projectile effectué avec succés");
			break;
		default:
			break;
		}
		return energieRestante;
	}
}
