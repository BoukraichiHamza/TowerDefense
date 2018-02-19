package elementJeu;

public enum TactiqueType {
	PlusProche, PlusFaible, PlusFort;

	public static String toString(TactiqueType tactique) {
		switch (tactique) {
		case PlusProche:
			return "PlusProche";
		case PlusFaible:
			return "PlusFaible";
		case PlusFort:
			return "PlusFort";
		default:
			return "";
		}
	}
}
