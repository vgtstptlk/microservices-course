import com.vgtstptlk.services.service.MySoapService;
import com.vgtstptlk.services.service.MySoapServiceService;

public class RunNed {
    public static void main(String[] args) {
        System.out.println("Digli-digli!");
        MySoapServiceService ms = new MySoapServiceService();
        MySoapService client = ms.getMySoapServicePort();

        System.out.println(client.hello("Magerram"));
    }
}
