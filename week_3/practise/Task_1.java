package CSS314;
import java.util.Random;

public class Task_1 {

    static long totalHits = 0;

    static final long TOTAL_POINTS = 50_000_000;
    static final int THREADS = 4;

    public static void main(String[] args) throws InterruptedException {

        Thread[] threads = new Thread[THREADS];

        long pointsPerThread = TOTAL_POINTS / THREADS;

        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {

                Random random = new Random();

                for (long j = 0; j < pointsPerThread; j++) {

                    double x = random.nextDouble();
                    double y = random.nextDouble();

                    if (x * x + y * y <= 1.0) {
                        totalHits++;   // DATA RACE
                    }
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        double pi = 4.0 * totalHits / TOTAL_POINTS;

        System.out.println("Total hits: " + totalHits);
        System.out.println("Pi: " + pi);
    }
}