import parcs.AM;
import parcs.AMInfo;
import java.util.Random;


public class StringConverter implements AM {
    private CipherScheme scheme;

    public String convert(String message) throws IllegalStateException{
        if (scheme == null) {
            throw new IllegalStateException("Scheme not set");
        }
        StringBuilder builder = new StringBuilder();
        for (Character c : message.toCharArray()) {
            builder.append(scheme.convertLetter(c));
        }
        return builder.toString();
    }

    private String process(CipherScheme scheme, String message) {
        this.setScheme(scheme);
        System.out.println("[" + scheme.toString() + "] Schema set.");
        System.out.println("[" + message + "] Message processed...");
        String result = convert(message);
        System.out.println("[" + result + "] done.");
        return result;
    }

    public void setScheme(CipherScheme scheme) {
        this.scheme = scheme;
    }


    @Override
    public void run(AMInfo amInfo) {

	System.out.println("Running worker...");

        Data data = (Data)amInfo.parent.readObject();

	System.out.println("Read data: " + data);

        System.out.println("Read data: " + data.number_of_simulations);
        System.out.println("Read data: " + data.seed);
        System.out.println("Read data: " + data.workers_n);

        //CipherScheme cipherScheme = data.scheme;
        //System.out.println("[" + cipherScheme.toString() + "] Schema set.");
        //String message = data.message;
        Random random = new Random(data.seed);
        double INITIAL = 100;
        int good = 0;
        for (int iteration = 0; iteration < data.number_of_simulations; iteration++) {
            double initial = INITIAL;
            for (int i = 1; i < 1000; i++) {
                double randomNum = random.nextGaussian() * 0.01 + 0.005; // mean 0.005, standard deviation 0.01
                initial = initial * (1 + randomNum);
            }
            if (initial > INITIAL) good++;
        }
        String result = "Completed " + Integer.toString(data.workers_n) +" "+Double.toString(good*1.0/data.number_of_simulations);
	    System.out.println("Writing result...");

        amInfo.parent.write(result);
	    System.out.println("over and out.");
    }
}
