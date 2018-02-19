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
import joueur.JoueurPassif;
import objetJeu.Mobile;
import objetJeu.ObjetJeu;
import objetJeu.Obstacle;
import objetJeu.Projectile;
import objetJeu.VagueDeMobiles;
public class Exemple1 {
	
	public static void main (String args []) {
		
		/*Creation du terrain de Jeu*/
		Case[][] damier = new Case[3][5];
		
		/*Specification des Nature de terrain*/
		for (int j=0; j<5;j++) {
			damier[0][j] = new Case(0,j,NatureTerrain.Decoration,-1,-1);
		}
		
		damier[1][0] = new Case(1,0,NatureTerrain.Entree,-1,-1);
		
		for(int j=1;j<4;j++) {
			damier[1][j] = new Case(1,j,NatureTerrain.Chemin,1,1); //Ajout du constructeur avec energie
		}
		
		damier[1][4] = new Case(1,4,NatureTerrain.Sortie,-1,-1);
		
		for (int j=0;j<5;j++) {
		damier[2][j] = new Case(2,j,NatureTerrain.Campement,-1,-1);
		}
		
		TerrainDeJeu terrain = new TerrainDeJeu(3, 5, damier);
		
		/*Creation de la liste de niveaux de jeu*/
		NiveauDeJeu level = new NiveauDeJeu("niveau1", 1, 3, 2);
		List<NiveauDeJeu> levels = new LinkedList<NiveauDeJeu>();
		levels.add(level);
		
		/*Creation des Mobiles*/
		List<Projectile> proj1 = new LinkedList<Projectile>();
		Mobile mob1 = new Mobile(1, 1, damier[1][0], damier[1][4], TactiqueType.PlusProche, 1, 1, proj1,terrain);
		List<Projectile> proj2 = new LinkedList<Projectile>();
		Mobile mob2 = new Mobile(1, 1, damier[1][0], damier[1][4], TactiqueType.PlusProche, 2, 1, proj2,terrain);
		List<Projectile> proj3 = new LinkedList<Projectile>();
		Mobile mob3 = new Mobile(1, 1, damier[1][0], damier[1][4], TactiqueType.PlusProche, 3, 1, proj3,terrain);
		
		Map<Integer,ObjetJeu> listemobile = new HashMap<Integer,ObjetJeu>();
		listemobile.put(mob1.getPosition(),mob1);
		listemobile.put(mob2.getPosition(),mob2);
		listemobile.put(mob3.getPosition(),mob3);

		/*Creation des obstacles*/
		List<Projectile> projobs = new LinkedList<Projectile>();
		Projectile p1 = new Projectile(null, 1, 1, 1, 1);
		projobs.add(p1);
		Obstacle obs = new Obstacle(damier[2][2],1, TactiqueType.PlusFaible, projobs);
		
		List<ObjetJeu> listeobstacle = new LinkedList<ObjetJeu>();
		listeobstacle.add(obs);
		
		/*Creation de la liste de vague*/
		
		VagueDeMobiles vague = new VagueDeMobiles("Vague",listemobile,listeobstacle,damier[1][0],damier[1][4], 3);
		List<VagueDeMobiles> vagues = new LinkedList<VagueDeMobiles>();
		vagues.add(vague);
		/*Creation de la partie*/
		Joueur joueur = new JoueurPassif("JoeurPassif");
		Partie game = new Partie(vagues, levels, terrain,joueur);
		
		/*Lancement de la partie*/
		game.lancerPartie();
		
	}
}
