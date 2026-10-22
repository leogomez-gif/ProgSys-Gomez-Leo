public class TestSemaphore {

    public static void main (String[] args) {

        ProducteurConsommateur usine = new ProducteurConsommateur();

        Runnable tacheProd = usine.new Producteur(); 
        Runnable tacheCons = usine.new Consommateur();

        Thread producteur1 = new Thread(tacheProd);
        producteur1.start();

        Thread producteur2 = new Thread(tacheProd);
        producteur2.start();

        Thread consommateur1 = new Thread(tacheCons);
        consommateur1.start();

        Thread consommateur2 = new Thread(tacheCons);
        consommateur2.start();

         Thread consommateur3 = new Thread(tacheCons);
        consommateur3.start();

        
    }
}