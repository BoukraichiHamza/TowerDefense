package elementTerrain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import elementJeu.ElementJeu;
import elementJeu.NatureTerrain;
import objetJeu.ObjetJeu;
import objetJeu.Obstacle;
import objetJeu.Projectile;

/** Case d'un terrain de Jeu. */
public class Case implements ElementJeu {
	private int x;
	private int y;
	private int energieRequise;
	private int volumeMaximal;
	private NatureTerrain natureTerrain;
	private List<ObjetJeu> objetsdispo;

	/**
	 * Construire une case.
	 * 
	 * @param x
	 *            : abscisse de la case.
	 * @param y
	 *            : ordonnée de la case.
	 * @param natureTerrain
	 *            : la nature de Terrain de la case.
	 * @param energie
	 *            : energie nécessaire pour se placer dans la case.
	 * @param volumeMaximal
	 *            : le volume que peut contenir la case.
	 */
	public Case(int x, int y, NatureTerrain natureTerrain, int energie, int volumeMaximal) {
		super();
		this.x = x;
		this.y = y;
		this.natureTerrain = natureTerrain;
		this.objetsdispo = new LinkedList<ObjetJeu>();
		/*
		 * l'energie et le volume Max ne sont des valeurs significatives que
		 * pour les cases de types chemin
		 */
		if (natureTerrain.equals(NatureTerrain.Chemin)) {
			this.energieRequise = energie;
			this.volumeMaximal = volumeMaximal;
		} else {
			this.energieRequise = 0;
			this.volumeMaximal = 0;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public NatureTerrain getNatureTerrain() {
		return natureTerrain;
	}

	public void setNatureTerrain(NatureTerrain natureTerrain) {
		this.natureTerrain = natureTerrain;
	}

	public List<ObjetJeu> getObjetsdispo() {
		return objetsdispo;
	}

	public void setObjetsdispo(List<ObjetJeu> objetsdispo) {
		this.objetsdispo = objetsdispo;
	}

	public int getEnergieRequise() {
		return energieRequise;
	}

	public void setEnergieRequise(int energieRequise) {
		this.energieRequise = energieRequise;
	}

	public int getVolumeMaximal() {
		return volumeMaximal;
	}

	public void setVolumeMaximal(int volumeMaximal) {
		if (this.natureTerrain.equals(NatureTerrain.Chemin)) {
			this.volumeMaximal = volumeMaximal;
		}
	}

	public void recevoirProjectile(Projectile p) {
		for (ObjetJeu o : this.objetsdispo) {
			o.recevoirProjectile(p);
		}
	}

	/**
	 * Ajouter Un ObjetJeu à la liste d'objet.
	 * 
	 * @param newob
	 *            l'objet à ajouter
	 */
	public void addObject(ObjetJeu newob) {
		this.objetsdispo.add(newob);
		this.setVolumeMaximal(this.volumeMaximal - newob.getVolume());
	}

	/**
	 * Supprimer Un ObjetJeu de la liste d'objet.
	 * 
	 * @param oldob
	 *            : l'objet à supprimer
	 */
	public void deleteObject(ObjetJeu oldob) {
		this.objetsdispo.remove(oldob);
		this.setVolumeMaximal(this.volumeMaximal + oldob.getVolume());
		;
	}

	/**
	 * Calcule de distance entre 2 cases.
	 * 
	 * @param autrecase
	 *            : la 2ieme case.
	 * @return : double : la distance cartésienne entre les 2 cases.
	 */
	public double distance(Case autrecase) {
		return Math.sqrt(Math.pow((this.getX() - autrecase.getX()), 2) + Math.pow((this.getY() - autrecase.getY()), 2));
	}

	/**
	 * Calcule d'un chemin entre 2 cases.
	 * 
	 * @param arrivee
	 *            : la case cible
	 * @param terrain
	 *            : le terrain sur lequel le chemin sera calculé
	 * @return : List<Case> la liste des cases formant le chemin de la case de
	 *         départ à la case d'arrivée.
	 */
	public List<Case> chemin(Case arrivee, TerrainDeJeu terrain) {
		List<Case> chemin = new LinkedList<>();
		Case[][] damier = terrain.getDamier();
		int xsuivante = this.x;
		int ysuivante = this.y;
		int xFin = arrivee.getX();
		int yFin = arrivee.getY();
		boolean rebouclage = false;

		// on ajoute la premiere case pour éviter de reboucler
		chemin.add(this);

		int nbcolonnes = terrain.getNbColonnes();
		int nblignes = terrain.getNbLignes();
		boolean caseSuivanteTrouvee = false;
		while (((xsuivante != xFin) || (ysuivante != yFin)) && !rebouclage) {
			caseSuivanteTrouvee = false;
			/* Recherche Normale */
			if ((xsuivante > 0) && (xFin < xsuivante)) {
				if (damier[xsuivante - 1][ysuivante].praticable(chemin)) {
					chemin.add(damier[xsuivante - 1][ysuivante]);
					xsuivante--;
					caseSuivanteTrouvee = true;
				}
			}
			if ((xsuivante < nblignes - 1) && (xsuivante < xFin) && (!caseSuivanteTrouvee)) {
				if (damier[xsuivante + 1][ysuivante].praticable(chemin)) {
					chemin.add(damier[xsuivante + 1][ysuivante]);
					xsuivante++;
					caseSuivanteTrouvee = true;
				}
			}
			if ((ysuivante < nbcolonnes - 1) && (ysuivante < yFin) && (!caseSuivanteTrouvee)) {
				if (damier[xsuivante][ysuivante + 1].praticable(chemin)) {
					chemin.add(damier[xsuivante][ysuivante + 1]);
					ysuivante++;
					caseSuivanteTrouvee = true;
				}
			}
			if ((ysuivante > 0) && (ysuivante > yFin) && (!caseSuivanteTrouvee)) {
				if (damier[xsuivante][ysuivante - 1].praticable(chemin)) {
					chemin.add(damier[xsuivante][ysuivante - 1]);
					ysuivante--;
					caseSuivanteTrouvee = true;
				}
			}

			/*
			 * Teste des cases adjacentes à la recherche d'un chemin quelconque
			 */
			if (!caseSuivanteTrouvee) {
				if ((xsuivante > 0)) {
					if (damier[xsuivante - 1][ysuivante].praticable(chemin)) {
						chemin.add(damier[xsuivante - 1][ysuivante]);
						xsuivante--;
						caseSuivanteTrouvee = true;
					}
				}
				if ((xsuivante < nblignes - 1) && (!caseSuivanteTrouvee)) {
					if (damier[xsuivante + 1][ysuivante].praticable(chemin)) {
						chemin.add(damier[xsuivante + 1][ysuivante]);
						xsuivante++;
						caseSuivanteTrouvee = true;
					}
				}

				if ((ysuivante < nbcolonnes - 1) && (!caseSuivanteTrouvee)) {
					if (damier[xsuivante][ysuivante + 1].praticable(chemin)) {
						chemin.add(damier[xsuivante][ysuivante + 1]);
						ysuivante++;
						caseSuivanteTrouvee = true;
					}
				}
				if ((ysuivante > 0) && (!caseSuivanteTrouvee)) {
					if (damier[xsuivante][ysuivante - 1].praticable(chemin)) {
						chemin.add(damier[xsuivante][ysuivante - 1]);
						ysuivante--;
						caseSuivanteTrouvee = true;
					}
				}
			}
			rebouclage = !caseSuivanteTrouvee;
		}
		/* On retire la premiere case */
		chemin.remove(this);
		return rebouclage ? new LinkedList<Case>() : chemin;
	}

	/**
	 * Teste si une case est praticable pour un mobile.
	 * 
	 * @param chemin
	 *            : chemin pour vérifier si la case à tester n'a pas déjà été
	 *            visitée.
	 * @return boolean : true si la case est praticable
	 */
	private boolean praticable(List<Case> chemin) {
		return (this.getNatureTerrain().equals(NatureTerrain.Chemin)
				|| this.getNatureTerrain().equals(NatureTerrain.Sortie)) && (!chemin.contains(this));
	}

	/**
	 * Renvoie la case adjacente à la case actuelle dans un chemin donné.
	 * 
	 * @param chemin
	 *            : le chemin sur lequel le calcul est effectué.
	 * @return Case : la case adjacente.
	 */
	public Case getAdjacente(List<Case> chemin) {
		Iterator<Case> icase = chemin.iterator();
		Case caseactu = null;
		Case caseAdj = null;

		while ((caseAdj == null) && (icase.hasNext())) {
			caseactu = icase.next();
			if (this.estAdjacente(caseactu)) {
				caseAdj = caseactu;
			}

		}
		return caseAdj;
	}

	/**
	 * Teste si 2 cases sont adjacentes
	 * 
	 * @param other
	 *            : l'autre case à tester.
	 * @return boolean : true si les cases sont adjacentes.
	 */
	private boolean estAdjacente(Case other) {
		return ((this.getX() == other.getX()) || (this.getY() == other.getY()));
	}

	@Override
	public void print() {
		System.out.print("Case : " + this.getX() + " " + this.getY() + " : ");
		System.out.print(this.getNatureTerrain().toString());
		System.out.println(" Objets : ");
		for (ObjetJeu o : this.objetsdispo) {
			System.out.print(" \t \t");
			o.print();
		}
	}

	/** Afficher les cases sous forme matricielle. */

	public void printMatrice() {
		switch (this.getNatureTerrain()) {
		case Campement:
			System.out.print("CAMPEMENT : ");
			this.printElement();
			break;
		case Decoration:
			System.out.print("  DECORATION ");
			break;
		case Chemin:
			System.out.print("  CHEMIN :  ");
			this.printElement();
			break;
		case Entree:
			System.out.print("   ENTREE : ");
			this.printElement();
			break;
		case Sortie:
			System.out.print("  SORTIE :  ");
			this.printElement();
			break;
		default:
			break;
		}
	}

	/** Afficher les éléments d'une case sous forme matricielle. */

	public void printElement() {
		int taille = this.getObjetsdispo().size();
		switch (taille) {
		case 0:
			System.out.print("V");
			break;
		case 1:
			if (this.getObjetsdispo().get(0) instanceof Obstacle) {
				System.out.print("O");
			} else {
				System.out.print("m");
			}
			break;
		default:
			System.out.print("M");
			break;
		}
	}

	/**
	 * Afficher les cases d'un chemin.
	 * 
	 * @param chemin
	 *            : le chemin à afficher.
	 */
	public static void printChemin(List<Case> chemin) {
		for (Case c : chemin) {
			c.print();
		}
	}

}
