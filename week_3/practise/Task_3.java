package CSS314;

import java.util.Random;

public class Task_3 {

    static final long TOTAL_POINTS = 100_000_000;

    public static long calculate(int threadCount) throws InterruptedException {

        Thread[] threads = new Thread[threadCount];
        long[] localHits = new long[threadCount];

        long pointsPerThread = TOTAL_POINTS / threadCount;

        long start = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {

            final int index = i;

            threads[i] = new Thread(() -> {

                Random random = new Random();

                long hits = 0;

                for (long j = 0; j < pointsPerThread; j++) {

                    double x = random.nextDouble();
                    double y = random.nextDouble();

                    if (x * x + y * y <= 1.0) {
                        hits++;
                    }
                }

                // No shared counter!
                localHits[index] = hits;
            });

            threads[i].start();
        }

        // Wait for all threads
        for (Thread thread : threads) {
            thread.join();
        }

        // Reduction
        long totalHits = 0;

        for (long hits : localHits) {
            totalHits += hits;
        }

        long end = System.nanoTime();

        double pi = 4.0 * totalHits / TOTAL_POINTS;

        System.out.printf(
                "Threads: %d | Pi: %.6f | Time: %.2f ms%n",
                threadCount,
                pi,
                (end - start) / 1_000_000.0
        );

        return end - start;
    }

    public static void main(String[] args) throws InterruptedException {

        int[] threadCounts = {1, 2, 4, 8, 16, 32};

        long[] times = new long[threadCounts.length];

        // Warm-up
        calculate(1);

        System.out.println("\n--- Benchmark ---");

        for (int i = 0; i < threadCounts.length; i++) {
            times[i] = calculate(threadCounts[i]);
        }

        double baseline = times[0] / 1_000_000.0;

        System.out.println("\nThreads | Runtime (ms) | Speedup | Efficiency");

        for (int i = 0; i < threadCounts.length; i++) {

            double runtime = times[i] / 1_000_000.0;
            double speedup = baseline / runtime;
            double efficiency = speedup / threadCounts[i] * 100;

            System.out.printf(
                    "%7d | %12.2f | %7.2fx | %9.2f%%%n",
                    threadCounts[i],
                    runtime,
                    speedup,
                    efficiency
            );
        }
    }
}
