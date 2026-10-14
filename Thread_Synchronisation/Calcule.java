public class Calcule extends Thread {

    private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    int valeur;
    int multiplicateur;
    int index;

    public Calcule(int valeur, int multiplicateur, int index) {
        this.valeur = valeur;
        this.multiplicateur = multiplicateur;
        this.index = index;
    }

    long debut = System.nanoTime();
    long sommeTotal = 0;

    @Override
    public void run() {
        // Implémenter le calcul intensif
        // Même logique que dans la boucle interne de CalculSequentiel
        for (int i = 0; i < DONNEES.length; i++) {
            int valeur = DONNEES[i];

            
            long resultat = 0;
            for (int j = 0; j < multiplicateur; j++) {
                resultat += valeur * valeur + valeur;
                               
            }
            sommeTotal += resultat;
            System.out.println("Traitement de " + valeur + " terminé : " + resultat);

        }
    
        long duree = (System.nanoTime() - debut) / 1_000_000;
            System.out.println("Résultat total : " + sommeTotal);
            System.out.println("Durée : " + duree + " ms");
    
    }

    int getResultat(){
            return resultat;
        }
}
