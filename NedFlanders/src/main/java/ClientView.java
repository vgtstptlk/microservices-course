import my.experiments.services.MyPetSOAPService;
import my.experiments.services.MyPetSOAPServiceService;

public class ClientView extends Thread {
    int hunger, health, thirst;
    String face, mood;
    MyPetSOAPService client;


    public ClientView(MyPetSOAPService client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            hunger = client.feel().getHunger();
            health = client.feel().getHealth();
            mood = client.feel().getMood();
            thirst = client.feel().getThirst();
            face = client.feel().getFace();

        }
    }


}
