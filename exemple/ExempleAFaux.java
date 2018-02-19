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
import joueur.JoueurReel;
import objetJeu.Mobile;
import objetJeu.ObjetJeu;
import objetJeu.Obstacle;
import objetJeu.Projectile;
import objetJeu.VagueDeMobiles;

public class ExempleAFaux {

	public static void main (String args [] ) {

		Map<Integer,ObjetJeu> listemobile;
		List<Projectile> proj;
		List<Projectile> projobs;
		List<ObjetJeu> listeobstacle;
		VagueDeMobiles vague;
		Obstacle obs;
		Projectile p;
		Mobile mob;


		/*Creation du terrain de Jeu*/
		Case [][] damier = new Case [3][5];
		

		/*Specification des Nature de terrain*/
		damier[1][0] = new Case(1,0,NatureTerrain.Entree,0,0);
		damier[1][4] = new Case(1,4,NatureTerrain.Sortie,0,0);
		damier[2][0] = new Case(2,0,NatureTerrain.Campement,0,0);
		damier[2][1] = new Case(2,1,NatureTerrain.Campement,0,0);
		damier[2][2] = new Case(2,2,NatureTerrain.Campement,0,0);
		damier[2][3] = new Case(2,3,NatureTerrain.Campement,0,0);
		damier[2][4] = new Case(2,4,NatureTerrain.Campement,0,0);


for (int i=0;i<0+1;i++) {
	for (int j=0;j<4+1;j++) {
		damier[i][j] = new Case(i,j,NatureTerrain.Decoration,0,0);

}
}


for (int i=1;i<1+1;i++) {
	for (int j=1;j<3+1;j++) {
		damier[i][j] = new Case(i,j,NatureTerrain.Chemin,1,1);

}
}

		TerrainDeJeu terrain = new TerrainDeJeu(3,5, damier);


		/*Creation de la liste de niveaux de jeu*/

		List<NiveauDeJeu> levels = new LinkedList<NiveauDeJeu>();

		NiveauDeJeu niveau1 = new NiveauDeJeu("niveau1",1, 0,2);
		levels.add(niveau1);


		/*Creation de la liste de vague*/

		List<VagueDeMobiles> vagues = new LinkedList<VagueDeMobiles>();
		int ivag = 1;

		/*Création des vagues*/


		listemobile = new HashMap<Integer,ObjetJeu>();
		/*Génére les mobiles de cette vague*/

		proj  = new LinkedList<Projectile>();
		mob = new Mobile(1, 1, damier[1][0],damier[1][4], TactiqueType.PlusProche, 1, 1, proj,terrain);
		listemobile.put(mob.getPosition(),mob);
		proj  = new LinkedList<Projectile>();
		mob = new Mobile(1, 1, damier[1][0],damier[1][4], TactiqueType.PlusProche, 1, 1, proj,terrain);
		listemobile.put(mob.getPosition(),mob);
		proj  = new LinkedList<Projectile>();
		mob = new Mobile(1, 1, damier[1][0],damier[1][4], TactiqueType.PlusProche, 1, 1, proj,terrain);
		listemobile.put(mob.getPosition(),mob);

		/*Creation des obstacles de cette vague*/

		listeobstacle = new LinkedList<ObjetJeu>();


		projobs  = new LinkedList<Projectile>();

		p = new Projectile(null, 1, 1,1,1);
		projobs.add(p);
		obs = new Obstacle(damier[2][2],1, TactiqueType.PlusFaible, projobs);
		listeobstacle.add(obs);


		vague = new VagueDeMobiles("Vague"+ivag,listemobile,listeobstacle,damier[1][0],damier[1][4], 3);
		vagues.add(vague);
		ivag++;


		/*Creation de la partie*/
		Joueur joueur = new JoueurReel("JoueurReel");
		Partie game = new Partie(vagues, levels, terrain,joueur);
		
		/*Lancement de la partie*/
		game.lancerPartie();

}

}

