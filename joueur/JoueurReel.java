package joueur;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import elementJeu.NatureTerrain;
import elementJeu.TactiqueType;
import elementTerrain.Case;
import elementTerrain.NiveauDeJeu;
import elementTerrain.TerrainDeJeu;
import objetJeu.ObjetJeu;
import objetJeu.Obstacle;
import objetJeu.Projectile;
import objetJeu.VagueDeMobiles;

public class JoueurReel extends Joueur {
	private Scanner sc;

	public JoueurReel(String nom) {
		super(nom);
		this.sc = new Scanner(System.in);
	}

	@Override
	public void jouerTour(VagueDeMobiles vague, TerrainDeJeu terrain, NiveauDeJeu level) {
		int choix = 7000; // Valeur quelconque
		// Ajout gestion de l'energie
		int energieRestante = this.getEnergie();

		while (choix != 3) {
			choix = choixAction();
			energieRestante = effectuerAction(vague, terrain, level, energieRestante, choix);
		}
		this.setEnergie(energieRestante);
	}

	/**
	 * Effectuer une action par le joueur.
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
			terrain.afficherTerrain();
			break;
		case 1:
			effectuerAffichageCase(terrain);
			break;
		case 2:
			energieRestante = effectuerAction2(vague, terrain, level, energieRestante);
			break;
		case 3:
			break;
		default:
			break;
		}
		return energieRestante;
	}

	/**
	 * Effectuer l'affichage d'une case
	 * 
	 * @param terrain
	 */
	private void effectuerAffichageCase(TerrainDeJeu terrain) {
		Case aAfficher = choixCase(terrain);
		aAfficher.print();
	}

	/**
	 * Récupérer le choix du joueur pour la première action
	 * 
	 * @return int : le choix du joueur.
	 */
	private int choixAction() {
		int choix = 1000; // Valeur quelconque
		boolean choixValide = false;
		while (!choixValide) {
			try {
				afficherAction();
				choix = Integer.parseInt(sc.nextLine());
				if (choix >= 0 || choix <= 3) {
					choixValide = true;
				} else {
					System.out.println("Choix invalide.");
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Entrer uniquement des entiers.");
			}
		}
		return choix;
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
		int choix = choixAction2(nbrCoup, energieDispo);
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
				effectuerDeplacementObstacle(terrain);
				energieRestante--;
				level.setDureePause(--nbrCoup);
				break;
			default:
				break;
			}
		} else if (energieRestante == 0) {
			System.out.println("Vous n'avez plus d'énergie.");
		} else if (nbrCoup == 0) {
			System.out.println("La pause est terminée.");
		}
		return energieRestante;
	}

	/**
	 * Récupérer le choix du joueur si il a choisi d'effectuer l'action n02
	 * 
	 * @param nbrAction
	 *            : nombre d'action qu'il peut effectuer
	 * @param energieDispo
	 *            : l'energie à consommer
	 * @return le choix du joueur
	 */
	private int choixAction2(int nbrAction, int energieDispo) {
		int choix = 1000; // Valeur quelconque
		boolean choixValide = false;
		while (!choixValide) {
			try {
				afficherAction2(nbrAction, energieDispo);
				choix = Integer.parseInt(sc.nextLine());
				if (choix >= 0 || choix <= 3) {
					choixValide = true;
				} else {
					System.out.println("Choix invalide.");
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Entrer uniquement des entiers.");
			}
		}
		return choix;
	}

	/** Effectuer l'affichage du menu. */
	private void afficherAction() {
		System.out.println("Choisir action :");
		System.out.println("0 - Afficher tout le damier.");
		System.out.println("1 - Afficher les éléments d'une case.");
		System.out.println("2 - Modifier les obstacles.");
		System.out.println("3 - Passer le tour.");
	}

	/**
	 * Effectue l'affichage du menu de l'action n02
	 * 
	 * @param nbrAction
	 *            : le nombre d'action qu'il peut effectuer
	 * @param energieDispo
	 *            : la quantité d'energie qu'on peut consommer
	 */
	private void afficherAction2(int nbrAction, int energieDispo) {
		System.out.println("Nombre d'actions possibles : " + nbrAction + " .");
		System.out.println("Energie disponible : " + energieDispo + " .");
		System.out.println("Choisir action :");
		System.out.println("0 - Reparer obstacle.");
		System.out.println("1 - Acheter obstacle.");
		System.out.println("2 - Vendre obstacle.");
		System.out.println("3 - Deplacer obstacle.");
	}

	/**
	 * Choisir une case sur le terrain.
	 * 
	 * @param terrain
	 *            : le terrain actuel.
	 * @return la case choisie par le joueur.
	 */
	private Case choixCase(TerrainDeJeu terrain) {
		boolean choixvalide = false;
		int nblignes = terrain.getNbLignes();
		int nbcolonnes = terrain.getNbColonnes();
		int ligne = 0;
		int colonne = 0;
		while (!choixvalide) {
			try {
				System.out.println("Entrer ligne de la case.");
				ligne = Integer.parseInt(sc.nextLine());
				System.out.println("Entrer colonne de la case.");
				colonne = Integer.parseInt(sc.nextLine());
				if ((ligne > nblignes) || (colonne > nbcolonnes)) {
					System.out.println("Erreur : lignes : de 0 à " + nblignes + ", colonnes : " + nbcolonnes + " .");
				} else {
					choixvalide = true;
				}

			} catch (IllegalArgumentException e) {
				System.out.println("Erreur : ecrire uniquement des entiers.");
			}
		}
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
			System.out.println("Choisir Case qui contient l'obstacle.");
			choix = choixCase(terrain);
			if (choix.getNatureTerrain().equals(NatureTerrain.Campement)) {
				if ((choix.getObjetsdispo().isEmpty()) && (doitcontenir)) {
					System.out.println("La case choisie ne contient pas d'obstacle.");
				} else if ((!choix.getObjetsdispo().isEmpty()) && (!doitcontenir)) {
					System.out.println("La case choisie contient déjà un obstacle.");
				} else {
					choixvalide = true;
				}
			} else {
				System.out.println("La case choisie n'est pas un campement.");
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
	private void effectuerDeplacementObstacle(TerrainDeJeu terrain) {
		System.out.println("Choisir case initiale.");
		Case caseInit = choixCaseAdequate(terrain, true);
		System.out.println("Choisir case cible.");
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
		System.out.println("Choisir case de l'obstacle à vendre");
		Case caseVente = choixCaseAdequate(terrain, true);

		/* Vente de l'obstacle */
		ObjetJeu aVendre = caseVente.getObjetsdispo().get(0);
		caseVente.deleteObject(aVendre);
		List<ObjetJeu> newObstacle = vague.getObstacles();
		newObstacle.remove(aVendre);
		vague.setObstacles(newObstacle);
		int energieGagnee = aVendre.getEnergie();

		return energieGagnee;
	}

	private void affichagechoixTactique() {
		System.out.println("Choisir Tactique :");
		System.out.println(" 0 - PlusFaible.");
		System.out.println(" 1 - PlusFort.");
		System.out.println(" 2 - PlusProche.");
	}

	/**
	 * Récupérer le choix du joueur pour la tactique de l'obstacle
	 * 
	 * @return la tactique de l'obstacle
	 */
	private TactiqueType choixTactique() {
		boolean choixvalide = false;
		TactiqueType tactique = null;
		while (!choixvalide) {
			affichagechoixTactique();
			int choix = Integer.parseInt(sc.nextLine());
			switch (choix) {
			case 0:
				tactique = TactiqueType.PlusFaible;
				choixvalide = true;
				break;
			case 1:
				tactique = TactiqueType.PlusFort;
				choixvalide = true;
				break;
			case 2:
				tactique = TactiqueType.PlusFort;
				choixvalide = true;
				break;
			default:
				System.out.println("Choix invalide.");
				break;

			}
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
		boolean choixValide = false;
		int choix = 0;
		while (!choixValide) {
			System.out.println("Choisir Energie de l'obstacle.");
			choix = Integer.parseInt(sc.nextLine());
			if (choix > energieDispo) {
				System.out.println("Vous n'avez pas assez d'énergie.");
			} else if (choix == 0) {
				System.out.println("Un obstacle ne peut pas avoir une énergie nulle.");
			} else {
				choixValide = true;
			}
		}
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
		System.out.println("Choisir la case de l'obstacle.");
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

	/** Effectuer l'affichage du menu de l'action de Réparation. */
	private void affichageActionReparation() {
		System.out.println("Choisir Action :");
		System.out.println(" 0 - Soigner Mobile.");
		System.out.println(" 1 - Ajouter Projectile");
	}

	/**
	 * Récupérer le choix du joueur pour le type de réparation à effectuer.
	 * 
	 * @return le choix du joueur.
	 */
	private int choixActionReparation() {
		boolean choixValide = false;
		int choix = 0;
		while (!choixValide) {
			affichageActionReparation();
			try {
				choix = Integer.parseInt(sc.nextLine());
				if (choix == 0 || choix == 1) {
					choixValide = true;
				} else {
					System.out.println("Choix invalide.");
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Entrer uniquement des entiers");
			}
		}
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
			try {
				System.out.println("Choisir Portée du projectile : ");
				choixp = Integer.parseInt(sc.nextLine());
				System.out.println("Choisir energie du projectile : ");
				choixe = Integer.parseInt(sc.nextLine());
				if (choixe + choixp > energieRestante) {
					System.out.println("Vous n'avez pas assez d'energie.");
				} else {
					p.setEnergie(p.getEnergie() + choixe);
					p.setPortee(p.getPortee() + choixp);
					choixTermine = true;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Entrer uniquement des entiers.");
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
		System.out.println("Choisir la case de l'obstacle.");
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
			break;
		default:
			break;
		}
		return energieRestante;
	}

}
