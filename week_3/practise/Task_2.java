package CSS314;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Task_2 {

    static final long TOTAL_POINTS = 50_000_000;
    static final int THREADS = 4;

    // ---------------- SINGLE THREAD ----------------

    public static void singleThreaded() {

        long hits = 0;

        Random random = new Random();

        long start = System.nanoTime();

        for (long i = 0; i < TOTAL_POINTS; i++) {

            double x = random.nextDouble();
            double y = random.nextDouble();

            if (x * x + y * y <= 1.0) {
                hits++;
            }
        }

        long end = System.nanoTime();

        double pi = 4.0 * hits / TOTAL_POINTS;
        double time = (end - start) / 1_000_000_000.0;

        System.out.println("Single-threaded:");
        System.out.println("Pi = " + pi);
        System.out.println("Time = " + time + " seconds");
    }


    // ---------------- MULTI THREAD ----------------

    public static void multiThreaded() throws InterruptedException {

        AtomicLong totalHits = new AtomicLong(0);

        Thread[] threads = new Thread[THREADS];

        long pointsPerThread = TOTAL_POINTS / THREADS;

        long start = System.nanoTime();

        for (int i = 0; i < THREADS; i++) {

            threads[i] = new Thread(() -> {

                Random random = new Random();

                for (long j = 0; j < pointsPerThread; j++) {

                    double x = random.nextDouble();
                    double y = random.nextDouble();

                    if (x * x + y * y <= 1.0) {
                        totalHits.incrementAndGet();
                    }
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long end = System.nanoTime();

        double pi = 4.0 * totalHits.get() / TOTAL_POINTS;
        double time = (end - start) / 1_000_000_000.0;

        System.out.println("\n4 Threads + AtomicLong:");
        System.out.println("Pi = " + pi);
        System.out.println("Time = " + time + " seconds");
    }


    public static void main(String[] args) throws InterruptedException {

        singleThreaded();
        multiThreaded();
    }
}
