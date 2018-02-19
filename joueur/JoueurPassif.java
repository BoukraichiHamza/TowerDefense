package joueur;

import elementTerrain.NiveauDeJeu;
import elementTerrain.TerrainDeJeu;
import objetJeu.VagueDeMobiles;

public class JoueurPassif extends Joueur {

	public JoueurPassif(String nom) {
		super(nom);
	}

	@Override
	public void jouerTour(VagueDeMobiles vagues, TerrainDeJeu terrain, NiveauDeJeu level) {
	}

}
