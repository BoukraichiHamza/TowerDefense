package jeu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import elementJeu.NatureTerrain;
import elementTerrain.Case;
import elementTerrain.NiveauDeJeu;
import elementTerrain.TerrainDeJeu;
import joueur.Joueur;
import objetJeu.LanceurProjectile;
import objetJeu.Mobile;
import objetJeu.ObjetJeu;
import objetJeu.Projectile;
import objetJeu.VagueDeMobiles;

public class Partie {
	private List<VagueDeMobiles> vagues;
	private List<NiveauDeJeu> niveaux;
	private TerrainDeJeu terrain;
	private Joueur joueur;

	/**
	 * Initialiser une partie.
	 * 
	 * @param vagues
	 *            : la liste de vagues de la partie.
	 * @param niveaux
	 *            : la liste de niveaux de la partie.
	 * @param terrain
	 *            : le terrain de la partie.
	 * @param joueur
	 *            : le joueur de la partie.
	 */
	public Partie(List<VagueDeMobiles> vagues, List<NiveauDeJeu> niveaux, TerrainDeJeu terrain, Joueur joueur) {
		super();
		this.vagues = vagues;
		this.niveaux = niveaux;
		this.terrain = terrain;
		this.joueur = joueur;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public List<VagueDeMobiles> getVagues() {
		return vagues;
	}

	public void setVagues(List<VagueDeMobiles> vagues) {
		this.vagues = vagues;
	}

	public List<NiveauDeJeu> getNiveaux() {
		return niveaux;
	}

	public void setNiveaux(List<NiveauDeJeu> niveaux) {
		this.niveaux = niveaux;
	}

	public TerrainDeJeu getTerrain() {
		return terrain;
	}

	public void setTerrain(TerrainDeJeu terrain) {
		this.terrain = terrain;
	}

	/** Lancer et jouer une partie. */
	public void lancerPartie() {
		if (this.verifierPartie()) {
			Boolean partiefinie = false;
			Boolean victoiredefense = false;
			/* Initialisation de l'energie du joueur */
			this.joueur.setEnergie(this.niveaux.get(0).getEnergieInitiale());
			Iterator<VagueDeMobiles> ivag = this.vagues.iterator();
			Iterator<NiveauDeJeu> ilvl = this.niveaux.iterator();
			VagueDeMobiles vagueactu;
			NiveauDeJeu lvlactu;

			/* Sauvegarde de la liste d'obstacle */
			List<ObjetJeu> oldobs = null;
			/*
			 * Boucle pour lancer toutes les vagues avec mise à jour de l'état
			 * de la partie
			 */
			while ((!partiefinie) && (ivag.hasNext())) {
				vagueactu = ivag.next();
				lvlactu = ilvl.next();
				/*
				 * La vague suivante reçoit la liste d'obstacle de la vague
				 * précédente.
				 */
				if (oldobs != null) {
					vagueactu.setObstacles(oldobs);
				}
				/* Lancement de la vague */
				vagueactu.saveObject();
				this.joueur.jouerTour(vagueactu, terrain, lvlactu);
				partiefinie = partiefinie || vagueactu.lancer(this.getTerrain(), lvlactu, this.joueur);
				this.joueur.addEnergie(vagueactu.getEnergieGagnee());

				/* Apres lancement, sauvegarde de la liste d'obstacle */
				oldobs = vagueactu.getObstacles();
				/* Vague terminé on supprime tous les Mobiles présents */
				vagueactu.nettoyerVague();
			}

			/* Cas victoire du joueur qui défend */
			if (!partiefinie) {
				partiefinie = true;
				victoiredefense = true;
			}
			/* Affichage du resultat */
			System.out.println("-----------------------------------");
			if (victoiredefense) {
				System.out.println("Victoire du Joueur !");
			} else {
				System.out.println("Défaite du Joueur  !");
			}
		} else {
			System.out.println("Partie Non Valide.");
		}
	}

	/**
	 * Vérifier qu'une partie est bien formée
	 * 
	 * @return boolean : true si la partie est bien formée.
	 */
	public boolean verifierPartie() {
		boolean terrainBienForme = verifierE10();
		boolean partieInt = verifierE11();
		if (!terrainBienForme) {
			System.out.println("Terrain mal formé.");
		}
		if (!partieInt) {
			System.out.println("L'issue de la partie ne peut pas être changée.");
		}
		return terrainBienForme && partieInt;
	}

	/**
	 * Vérifier si l'issue de la partie peut être changée par le joueur.
	 * 
	 * @return boolean : true si les actions d'un joueur peuvent changer l'issue
	 *         d'une partie.
	 */
	private boolean verifierE11() {
		boolean vagues_valides = true;

		int energie = this.niveaux.get(0).getEnergieInitiale();

		for (int i = 0; i < this.niveaux.size(); i++) {

			VagueDeMobiles v = this.vagues.get(i);
			HashMap<Mobile, Integer> listeMobiles = new HashMap<>();
			List<ObjetJeu> listeObstacles = this.vagues.get(0).getObstacles();
			vagues_valides &= verifierE11a(v, listeMobiles, listeObstacles) && verifierE11b(v, this.niveaux.get(i),energie, listeMobiles);
			energie += v.getEnergieGagnee();
		}
		return vagues_valides;
	}

	/**
	 * Vérifier si un joueur peut perdre une vague dans le pire des cas.
	 * 
	 * @param v
	 *            : la vague à vérifier
	 * @param listeMobiles
	 *            : la liste de mobiles de cette vague.
	 * @param listeObstacles
	 *            : la liste d'obstacle de cette vague.
	 * @return boolean : true si le joueur peut perdre.
	 */
	private boolean verifierE11a(VagueDeMobiles v, HashMap<Mobile, Integer> listeMobiles,
			List<ObjetJeu> listeObstacles) {
		List<Case> cheminMobile;
		List<Case> casesEnCommun;
		boolean flag;

		for (ObjetJeu mobile : v.getMobiles().values()) {
			Mobile m = (Mobile) mobile;
			listeMobiles.put(m, 0);
			cheminMobile = m.getChemin();
			flag = false;

			for (ObjetJeu obstacle : listeObstacles) {
				LanceurProjectile lp = (LanceurProjectile) obstacle;

				for (Projectile p : lp.getProjectiles()) {
					List<Case> cheminProjectile = lp.getCibles(this.terrain, p, 1);
					casesEnCommun = cheminMobile;
					casesEnCommun.retainAll(cheminProjectile);

					if (casesEnCommun.isEmpty()) {
						// Le mobile est safe
					} else {
						flag = true;
						listeMobiles.put(m, listeMobiles.get(m) + p.getQuantiteEnergie());
						if (listeMobiles.get(m) >= m.getEnergie()) {
							listeMobiles.remove(m);
							break;
						}
					}
				}

				if (flag) {
					break;
				}
			}
		}

		return !listeMobiles.isEmpty();
	}

	/**
	 * Vérifier si un joueur peut gagner une vague dans le meilleur des cas.
	 * 
	 * @param v
	 *            : la vague à vérifier.
	 * @param energie
	 *            : l'energie du joueur.
	 * @param listeMobiles
	 *            : la liste des mobiles d'une vague.
	 * @return boolean : true si le joueur peut gagner dans le meilleur des cas.
	 */
	private boolean verifierE11b(VagueDeMobiles v,NiveauDeJeu level, int energie, HashMap<Mobile, Integer> listeMobiles) {
		if (listeMobiles.isEmpty() || level.getDureePause() == 0) {
			return false;
		} else {
			int resteATuer = 0;
			for (Mobile m : listeMobiles.keySet()) {
				resteATuer += m.getEnergie() - listeMobiles.get(m);
			}

			energie -= resteATuer;
			return (energie >= 0);
		}
	}

	/**
	 * Vérifier si le terrain de jeu de la partie est bien formé.
	 * 
	 * @return boolean : true si le terrain est bien formé.
	 */
	private boolean verifierE10() {
		return verifierE10a() && verifierE10b();
	}

	/**
	 * Vérifier si le terrain comporte les bonnes cases.
	 * 
	 * @return boolean : true si le terrain comporte au moins une case campement
	 *         et une case entree et une case sortie.
	 */
	private boolean verifierE10a() {
		boolean campementExiste = false;
		boolean entreeExiste = false;
		boolean sortieExiste = false;

		Case[][] damier = this.getTerrain().getDamier();
		for (int i = 0; i < this.getTerrain().getNbLignes(); i++) {
			for (int j = 0; j < this.getTerrain().getNbColonnes(); j++) {
				if (damier[i][j].getNatureTerrain().equals(NatureTerrain.Campement)) {
					campementExiste = true;
				} else if (damier[i][j].getNatureTerrain().equals(NatureTerrain.Entree)) {
					entreeExiste = true;
				} else if (damier[i][j].getNatureTerrain().equals(NatureTerrain.Sortie)) {
					sortieExiste = true;
				}
			}
		}
		if (!campementExiste){
			System.out.println("Il n'y pas de case campement.");
		}
		if (!entreeExiste){
			System.out.println("Il n'y pas de case entrée.");
		}
		if (!sortieExiste){
			System.out.println("Il n'y pas de case sortie.");
		}
		return campementExiste && entreeExiste && sortieExiste;
	}

	/**
	 * Vérifier si le terrain comporte des chemins entre les cases d'entrées et
	 * de sorties.
	 * 
	 * @return boolean : true si les chemins sont bien formés
	 */
	private boolean verifierE10b() {
		boolean res = true;
		/* Récupérer toutes les cases d'entrée et toutes les cases de sorties */
		List<Case> entrees = new LinkedList<Case>();
		List<Case> sorties = new LinkedList<Case>();

		Case[][] damier = this.getTerrain().getDamier();
		for (int i = 0; i < this.getTerrain().getNbLignes(); i++) {
			for (int j = 0; j < this.getTerrain().getNbColonnes(); j++) {
				if (damier[i][j].getNatureTerrain().equals(NatureTerrain.Entree)) {
					entrees.add(damier[i][j]);
				} else if (damier[i][j].getNatureTerrain().equals(NatureTerrain.Sortie)) {
					sorties.add(damier[i][j]);
				}
			}
		}
		boolean cheminFerme = false;
		/*
		 * Pour chaque entrée calculer des chemins avec chaque sortie et
		 * vérifier si le chemin est non vide
		 */
		for (Case entry : entrees) {
			for (Case exit : sorties) {
				cheminFerme = cheminFerme || (!entry.chemin(exit, this.getTerrain()).isEmpty());
			}
			/* On met à jour le résultat pour chaque entrée */
			res = res && cheminFerme;
		}
		if (!res) {
			System.out.println("Les chemins ne sont pas fermés.");
		}
		return res;
	}

}
