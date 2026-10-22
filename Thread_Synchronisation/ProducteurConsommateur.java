import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

public class ProducteurConsommateur {
    private static final int TAILLE_BUFFER = 5;
    private final Queue<Integer> buffer = new LinkedList<>();

    // Déclarer 3 sémaphores avec les bonnes valeurs initiales
    private final Semaphore placesLibres = new Semaphore(5);
    private final Semaphore elementsDisponibles = new Semaphore(0);  
    private final Semaphore mutexBuffer = new Semaphore(1); // quel erreur ici ? vous pouvez le résoudre plus tard

    class Producteur implements Runnable {

        private int genererProduit() { 
            return 42; 
        }

        public void run() {
            for (int i = 0; i < 10; i++) {

                
                int produit = genererProduit();

                // Attendre une place libre

                try {

                    placesLibres.acquire();
                
                    //Acquérir l'accès exclusif au buffer
            
                    mutexBuffer.acquire();
                    
                    // Ajouter l'élément au buffer
                    buffer.add(produit);

                    System.out.println("Le produit a été ajouté Taille actuelle du buffer : " + buffer.size());
                
                    // Libérer l'accès au buffer
                
                    mutexBuffer.release();
                    
                    // Signaler qu'un élément est disponible
                
                    elementsDisponibles.release();
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consommateur implements Runnable {
        public void run() {
            for (int i = 0; i < 7; i++) {

                
                  try {
                    //Attendre qu'un élément soit disponible

                    elementsDisponibles.acquire();
                
                    //Acquérir l'accès exclusif au buffer
            
                    mutexBuffer.acquire();
                    
                    // Ajouter l'élément au buffer
                   
                    Integer produit = buffer.poll();    
                    
                    System.out.println("Un produit a été retiré Taille actuelle du buffer : " + buffer.size());
                    
                    // Libérer l'accès au buffer
                
                    mutexBuffer.release();
                    
                    // Signaler qu'une place est libre
                
                    placesLibres.release();
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
        }
    }

}