import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import parcs.*;

public class Cipher {

    public static void main(String[] args) throws Exception {
        task curTask = new task();
        curTask.addJarFile("StringConverter.jar");
        Data data = fromFile(curTask.findFile("input"));

        System.out.println("Number of workers = "+data.workers_n);

        AMInfo info = new AMInfo(curTask, null);
        splitTask(info, data);

        curTask.end();
    }

    public static void splitTask(AMInfo info, Data data) {
        List<channel> channels = new ArrayList<>();
        int number_of_simulations = data.number_of_simulations;
        int workers_n = data.workers_n;

        long time = System.currentTimeMillis();
        for (int i = 0; i < workers_n; i++) {
            int chunk = i == workers_n - 1 ?
                    number_of_simulations/workers_n - number_of_simulations/workers_n*(workers_n-1):
                    number_of_simulations/workers_n;
            Data worker_data = new Data(i, data.seed, chunk);

            System.out.println(i + " worker string: " + chunk);

            point newPoint = info.createPoint();
            channel newChannel = newPoint.createChannel();
            System.out.println("Channel created");

            newPoint.execute("StringConverter");
            newChannel.write(worker_data);
            System.out.println("Execution has started. Waiting for result...");

            channels.add(newChannel);
        }
        System.out.println("All channels started. Time: " + (System.currentTimeMillis() - time) + "ms");

        long time2 = System.currentTimeMillis();

        for (int i = 0; i < workers_n; i++) {
            System.out.println("Result: " + channels.get(i).readObject().toString());
        }
        System.out.println("Time from all channels starting: " + (System.currentTimeMillis() - time2) + "ms");
        System.out.println("Full execution time: " + (System.currentTimeMillis() - time) + "ms");
    }

    public static Data fromFile(String filename) throws Exception {
        String __;
        Scanner sc = new Scanner(new File(filename));
        int workers_n = sc.nextInt();
        int seed = sc.nextInt();
        int number_of_simulations = sc.nextInt();
        sc.close();
        return new Data(workers_n, seed, number_of_simulations);
    }
}
