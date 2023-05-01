import parcs.AM;
import parcs.AMInfo;
import java.util.Random;


public class StringConverter implements AM {
    //private CipherScheme scheme;

    /*public String convert(String message) throws IllegalStateException{
        if (scheme == null) {
            throw new IllegalStateException("Scheme not set");
        }
        StringBuilder builder = new StringBuilder();
        for (Character c : message.toCharArray()) {
            builder.append(scheme.convertLetter(c));
        }
        return builder.toString();
    }*/

    /*private String process(CipherScheme scheme, String message) {
        this.setScheme(scheme);
        System.out.println("[" + scheme.toString() + "] Schema set.");
        System.out.println("[" + message + "] Message processed...");
        String result = convert(message);
        System.out.println("[" + result + "] done.");
        return result;
    }*/

    /*public void setScheme(CipherScheme scheme) {
        this.scheme = scheme;
    }*/


    @Override
    public void run(AMInfo amInfo) {

	System.out.println("Running worker...");

        Data data = (Data)amInfo.parent.readObject();

	    //System.out.println("Read data: " + data);

        System.out.println("Read data: " + data.number_of_simulations);
        System.out.println("Read data: " + data.seed);
        System.out.println("Read data: " + data.workers_n);

        //CipherScheme cipherScheme = data.scheme;
        //System.out.println("[" + cipherScheme.toString() + "] Schema set.");
        //String message = data.message;
        long seed = 123456; // seed value
        int numDays = 365; // number of days to simulate
        double initialPrice = 100.0; // initial price

        // Generate last prices using the seed value
        double[] lastPrices = generateLastPrices(seed, numDays, initialPrice, 0.0, 0.01);

        for (int i=0; i<numDays; i++){
            System.out.println(i + " "+lastPrices[i]);
        }

        // Calculate the mean and standard deviation of the daily returns
        double mean = calculateMean(lastPrices);
        double stdDev = calculateStdDev(lastPrices);

        // Simulate future prices using Monte Carlo simulation
        double[] futurePrices = new double[numDays];
        futurePrices[0] = lastPrices[lastPrices.length-1]; // use the last price as the initial price
        double profit = 0;

        Random random = new Random();
        for (int j=0; j<data.number_of_simulations; j++) {
            for (int i = 1; i < numDays; i++) {
                double random_change = mean + stdDev * random.nextGaussian();
                futurePrices[i] = futurePrices[i - 1] + random_change;
            }
            profit += futurePrices[numDays-1] - futurePrices[0];
        }
        profit /= data.number_of_simulations;
        String result = "Completed " + Integer.toString(data.workers_n) +" "+Double.toString(profit);
	    System.out.println("Writing result...");

        amInfo.parent.write(result);
	    System.out.println("over and out.");
    }

    private static double[] generateLastPrices(long seed, int numDays, double initialPrice, double trend, double noise) {
        Random random = new Random(seed);
        double[] lastPrices = new double[numDays];
        lastPrices[0] = initialPrice;

        for (int i = 1; i < numDays; i++) {
            double randomNum = random.nextGaussian() * noise;
            lastPrices[i] = lastPrices[i-1] * (1 + trend) + randomNum;
        }

        return lastPrices;
    }

    private static double calculateMean(double[] prices) {
        double sum = 0.0;
        for (double price : prices) {
            sum += price;
        }
        return sum / prices.length;
    }

    private static double calculateStdDev(double[] prices) {
        double mean = calculateMean(prices);
        double sum = 0.0;
        for (double price : prices) {
            sum += Math.pow(price - mean, 2);
        }
        return Math.sqrt(sum / (prices.length - 1));
    }
}
