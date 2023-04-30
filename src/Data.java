import java.io.Serializable;

public class Data implements Serializable {
    int workers_n;
    int seed;
    int number_of_simulations;

    public Data(int workers_n, int seed, int number_of_simulations) {
        this.seed = seed;
        this.workers_n = workers_n;
        this.number_of_simulations = number_of_simulations;
    }
}