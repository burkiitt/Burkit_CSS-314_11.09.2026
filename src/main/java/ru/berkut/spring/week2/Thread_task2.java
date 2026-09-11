package ru.berkut.spring.week2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Thread_task2 {
    private static final int LIMIT = 5_000_000;
    private static final int[] THREADS_CONFIG = {1, 2, 4, 8, 16, 32};
    private static final int ITERATIONS = 3;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Starting Multi-Thread Scaling Benchmark ===");
        System.out.println("CPU: Intel Core i5-12450H (4P + 4E Cores, 12 Threads)");
        System.out.println("Workload: Prime Check up to " + LIMIT + "\n");
        System.out.printf("%-10s %-10s %-10s %-10s %-12s%n", "Threads(N)", "Run 1 (s)", "Run 2 (s)", "Run 3 (s)", "Avg (s)");
        System.out.println("------------------------------------------------------------------");

        // Прогрев JVM (JIT compiler warm-up)
        runWorkload(1);

        for (int threads : THREADS_CONFIG) {
            double[] runs = new double[ITERATIONS];
            double sum = 0;

            for (int i = 0; i < ITERATIONS; i++) {
                long startTime = System.nanoTime();
                runWorkload(threads);
                long endTime = System.nanoTime();

                runs[i] = (endTime - startTime) / 1_000_000_000.0;
                sum += runs[i];
            }
            double avg = sum / ITERATIONS;
            System.out.printf("%-10d %-10.3f %-10.3f %-10.3f %-12.3f%n", threads, runs[0], runs[1], runs[2], avg);
        }
    }

    private static void runWorkload(int threadCount) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Callable<Long>> tasks = new ArrayList<>();

        int chunk = LIMIT / threadCount;
        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunk + 1;
            final int end = (i == threadCount - 1) ? LIMIT : (start + chunk - 1);
            tasks.add(() -> countPrimesInRange(start, end));
        }

        executor.invokeAll(tasks);
        executor.shutdown();
    }

    private static long countPrimesInRange(int start, int end) {
        long count = 0;
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) count++;
        }
        return count;
    }

    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
