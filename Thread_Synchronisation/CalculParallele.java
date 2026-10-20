/*
 * CalculParallele.java                                 14/10/2026
 * IUT de Rodez aucun copyright
 */

public class CalculParallele  {

	private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int MULTIPLICATEUR = 1000;

    public static void main(String[] args) {
        System.out.println("=== Calcul Parallele ===");

		Calcule[] threads = new Calcule[DONNEES.length];
        long debut = System.nanoTime();
        long sommeTotal = 0;
		

        for (int i = 0; i < DONNEES.length; i++) {
            int valeur = DONNEES[i];
			
			Calcule th = new Calcule(valeur,MULTIPLICATEUR,i);
            th.start();
			threads[i] = th;
		}

		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
		}
		
		for (int i = 0; i < threads.length; i++) {

            sommeTotal += threads[i].getResultat();
            System.out.println("Traitement de " + DONNEES[i] + " terminé : " + threads[i].getResultat());
		}

        long duree = (System.nanoTime() - debut) / 1_000_000;
        System.out.println("Résultat total : " + sommeTotal);
        System.out.println("Durée : " + duree + " ms");
    }
	

}