package elementJeu;

public enum NatureTerrain {
	Entree, Sortie, Campement, Decoration, Chemin;

	public static String toString(NatureTerrain nature) {
		switch (nature) {
		case Entree:
			return "Entree";
		case Campement:
			return "Campement";
		case Sortie:
			return "Sortie";
		case Decoration:
			return "Decoration";
		case Chemin:
			return "Chemin";
		default:
			return "";
		}
	}
}