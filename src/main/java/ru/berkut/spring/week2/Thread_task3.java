package ru.berkut.spring.week2;

public class Thread_task3 {

    static int counter = 0;
    static final int THREADS = 10;
    static final int ITERATIONS = 1_000_000;

    public static void main(String[] args) throws InterruptedException {

        for (int run = 1; run <= 10; run++) {

            counter = 0;

            Thread[] threads = new Thread[THREADS];
            for (int i = 0; i < THREADS; i++) {
                threads[i] = new Thread(() -> {

                    for (int j = 0; j < ITERATIONS; j++) {
                        counter++;
                    }

                });

                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            int error = 10_000_000 - counter;

            System.out.println(
                    "Run #" + run +
                            " | Output: " + counter +
                            " | Error: " + error
            );
        }
    }
}