import my.experiments.services.MyPetSOAPService;
import my.experiments.services.MyPetSOAPServiceService;

public class ClientController extends Thread{
    int hunger, health, thirst;
    String face, mood;
    MyPetSOAPService client;
    ClientView clientView;

    int hungerCon, healthCon, thirstCon;

    public ClientController(ClientView clientView, MyPetSOAPService client) {
        this.client = client;
        this.clientView = clientView;
    }


    @Override
    public void run() {
        getRandomValues();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            hunger = clientView.hunger;
            health = clientView.health;
            mood = clientView.mood;
            thirst = clientView.thirst;
            face = clientView.face;

            if (hunger > hungerCon){
                System.out.println(client.feed());
            }


            if ("unhappyborringnormaljoyful".contains(mood)){
                System.out.println(client.play());
                getRandomValues();
            }

            if (thirst > thirstCon){
                System.out.println(client.giveDrink());
                getRandomValues();
            }

            if ("@___@".contains(face) && health < healthCon){
                System.out.println(client.treat());
                getRandomValues();
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void getRandomValues(){
        hungerCon = 10 + (int)(10*Math.random());
        healthCon = 50 + (int)(20*Math.random());
        thirstCon = 10 + (int)(10*Math.random());
    }
}
