import java.util.Locale;

public class TestMutex {
    private static final int NOMBRE_TESTS = 50;
    private static final int NOMBRE_THREADS_PAR_DEFAUT = 9;
    private static final int INCREMENTS_PAR_THREAD_PAR_DEFAUT = 1_111_111;

    public static void main(String[] args) throws InterruptedException {
        int nombreThreads = args.length > 0
                ? Integer.parseInt(args[0])
                : NOMBRE_THREADS_PAR_DEFAUT;
        int incrementsParThread = args.length > 1
                ? Integer.parseInt(args[1])
                : INCREMENTS_PAR_THREAD_PAR_DEFAUT;

        if (nombreThreads <= 0 || incrementsParThread <= 0) {
            System.err.println("Le nombre de threads et d'increments doit etre positif.");
            return;
        }

        long resultatAttendu = (long) nombreThreads * incrementsParThread;
        if (resultatAttendu > Integer.MAX_VALUE) {
            System.err.println("Le resultat attendu depasse la capacite du compteur.");
            return;
        }

        int nombreReussites = 0;
        int resultatMinimal = Integer.MAX_VALUE;
        int resultatMaximal = Integer.MIN_VALUE;

        System.out.printf(
                "Configuration : %d threads x %d increments, %d tests%n",
                nombreThreads, incrementsParThread, NOMBRE_TESTS);
        System.out.println("Resultat attendu par test : " + resultatAttendu);

        for (int numeroTest = 1; numeroTest <= NOMBRE_TESTS; numeroTest++) {
            CompteurSecurise.resetCompteur();
            Thread[] threads = new Thread[nombreThreads];

            for (int i = 0; i < nombreThreads; i++) {
                Runnable incrementeur = new CompteurSecurise.Incrementeur(
                        "Thread-" + (i + 1), incrementsParThread);
                threads[i] = new Thread(incrementeur);
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            int resultat = CompteurSecurise.getCompteur();
            resultatMinimal = Math.min(resultatMinimal, resultat);
            resultatMaximal = Math.max(resultatMaximal, resultat);

            boolean correct = resultat == resultatAttendu;
            if (correct) {
                nombreReussites++;
            }

            System.out.printf("Test %2d/%d : resultat = %d, %s%n",
                    numeroTest, NOMBRE_TESTS, resultat,
                    correct ? "correct" : "incorrect");
        }

        double pourcentageReussite = nombreReussites * 100.0 / NOMBRE_TESTS;
        System.out.printf(Locale.ROOT,
                "%nBilan : %d/%d resultats corrects (%.2f %%)%n",
                nombreReussites, NOMBRE_TESTS, pourcentageReussite);
        System.out.println("Resultat minimal : " + resultatMinimal);
        System.out.println("Resultat maximal : " + resultatMaximal);
    }
}