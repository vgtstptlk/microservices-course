import my.experiments.services.MyPetSOAPService;
import my.experiments.services.MyPetSOAPServiceService;

public class RunNed {



    public static void main(String[] args) throws InterruptedException {

        MyPetSOAPServiceService ms = new MyPetSOAPServiceService();
        MyPetSOAPService client = ms.getMyPetSOAPServicePort();
        ClientView clientView = new ClientView(client);
        clientView.start();
        Thread.sleep(100);
        ClientController clientController = new ClientController(clientView, client);
        clientController.start();

        while (true) {


            System.out.println("-------------------------------------");
            System.out.println("hunger: " + clientView.hunger);
            System.out.println("face: " + clientView.face);
            System.out.println("mood: " + clientView.mood);
            System.out.println("health: " + clientView.health);
            System.out.println("thirst: " + clientView.thirst);
            System.out.println("-------------------------------------");

            Thread.sleep(1000);
        }


    }
}
