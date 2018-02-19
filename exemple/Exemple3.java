package exemple;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import elementJeu.NatureTerrain;
import elementJeu.TactiqueType;
import elementTerrain.Case;
import elementTerrain.NiveauDeJeu;
import elementTerrain.TerrainDeJeu;
import jeu.Partie;
import joueur.Joueur;
import joueur.JoueurReel;
import objetJeu.Mobile;
import objetJeu.ObjetJeu;
import objetJeu.Obstacle;
import objetJeu.Projectile;
import objetJeu.VagueDeMobiles;

public class Exemple3 {
	public static void main(String args[]) {
		/* Creation du terrain de Jeu */
		Case[][] damier = new Case[5][6];

		/* Specification des Nature de terrain */
		for (int j = 1; j < 5; j++) {
			damier[0][j] = new Case(0, j, NatureTerrain.Decoration, 0, 0);
			damier[1][j] = new Case(1, j, NatureTerrain.Campement, 0, 0);
			damier[2][j] = new Case(2, j, NatureTerrain.Chemin, 1, 2);
			damier[3][j] = new Case(3, j, NatureTerrain.Campement, 0, 0);
			damier[4][j] = new Case(4, j, NatureTerrain.Decoration, 0, 0);
		}

		for (int i = 1; i < 4; i++) {
			damier[i][0] = new Case(i, 0, NatureTerrain.Chemin, 1, 1);
			damier[i][5] = new Case(i, 5, NatureTerrain.Chemin, 1, 1);
		}

		damier[0][0] = new Case(0, 0, NatureTerrain.Entree, 0, 0);
		damier[4][0] = new Case(4, 0, NatureTerrain.Entree, 0, 0);
		damier[0][5] = new Case(0, 5, NatureTerrain.Sortie, 0, 0);
		damier[4][5] = new Case(4, 5, NatureTerrain.Sortie, 0, 0);

		TerrainDeJeu terrain = new TerrainDeJeu(5, 6, damier);

		/* Creation de la liste de niveaux de jeu */
		NiveauDeJeu level1 = new NiveauDeJeu("niveau1", 0, 3, 2);
		NiveauDeJeu level2 = new NiveauDeJeu("niveau2", 2, 0, 2);
		List<NiveauDeJeu> levels = new LinkedList<NiveauDeJeu>();
		levels.add(level1);
		levels.add(level2);

		/* Creation des Mobiles de la 1ère vague */
		List<Projectile> proj3 = new LinkedList<Projectile>();
		Projectile p3 = new Projectile(null, 1, 1, 1, 1);
		proj3.add(p3);
		Mobile mob3 = new Mobile(1, 1, damier[0][0], damier[0][5], TactiqueType.PlusFort, 1, 1, proj3,terrain);

		Map<Integer, ObjetJeu> listemobile1 = new HashMap<Integer, ObjetJeu>();
		listemobile1.put(mob3.getPosition(), mob3);

		/* Creation des obstacles de la 1ère vague */
		List<Projectile> projobs1 = new LinkedList<Projectile>();
		Projectile pobs1 = new Projectile(null, 1, 1, 1, 1);
		projobs1.add(pobs1);
		Obstacle obs1 = new Obstacle(damier[1][3], 1, TactiqueType.PlusFaible, projobs1);

		List<Projectile> projobs2 = new LinkedList<Projectile>();
		Projectile pobs2 = new Projectile(null, 1, 1, 1, 1);
		projobs2.add(pobs2);
		Obstacle obs2 = new Obstacle(damier[3][3], 1, TactiqueType.PlusFort, projobs2);

		List<ObjetJeu> listeobstacle1 = new LinkedList<ObjetJeu>();
		listeobstacle1.add(obs1);
		listeobstacle1.add(obs2);

		/* Creation de la vague1 */

		VagueDeMobiles vague1 = new VagueDeMobiles("Vague", listemobile1, listeobstacle1, damier[0][0], damier[0][5],
				3);

		/* Creation des Mobiles de la 2ieme vague */

		List<Projectile> proj22 = new LinkedList<Projectile>();
		Projectile p22 = new Projectile(null, 1, 1, 1, 1);
		proj22.add(p22);
		Mobile mob22 = new Mobile(1, 1, damier[4][0], damier[4][5], TactiqueType.PlusFaible, 1, 1, proj22,terrain);
		
		Map<Integer, ObjetJeu> listemobile2 = new HashMap<Integer, ObjetJeu>();
		listemobile2.put(mob22.getPosition(), mob22);
		



		/* Creation de la vague2 */

		VagueDeMobiles vague2 = new VagueDeMobiles("Vague2", listemobile2, null, damier[4][0], damier[4][5],
				3);

		List<VagueDeMobiles> vagues = new LinkedList<VagueDeMobiles>();
		vagues.add(vague1);
		vagues.add(vague2);
		/* Creation de la partie */
		Joueur joueur = new JoueurReel("Hamza");
		Partie game = new Partie(vagues, levels, terrain, joueur);

		/* Lancement de la partie */
		game.lancerPartie();
	}
}
