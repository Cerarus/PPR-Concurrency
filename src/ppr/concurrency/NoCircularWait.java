package ppr.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class NoCircularWait {

    List<Lock> forks = new ArrayList<>();
    List<NoCircularWaitPhilosopher> philosophers = new ArrayList<>();
    int numberOfPhilosophers;
    int maxThinkingTime;
    int maxEatingTime;

    public void execute(int numberOfPhilosophers, int maxThinkingTime, int maxEatingTime) {
        this.numberOfPhilosophers = numberOfPhilosophers;
        this.maxThinkingTime = maxThinkingTime;
        this.maxEatingTime = maxEatingTime;
        for (int index = 0; index < numberOfPhilosophers; index++) {
            forks.add(new ReentrantLock());
            philosophers.add(new NoCircularWaitPhilosopher(index, forks));
        }
        List<Thread> threads = philosophers.stream().map(philosopher -> new Thread(philosopher)).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        threads.stream().forEach(thread -> thread.start());
        for (int index = 0; index < numberOfPhilosophers; index++) {
            try {
                threads.get(index).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("\n\n");
        philosophers.stream().forEach(philosopher -> System.out.println("Philosopher " + philosopher.number + " waited " + philosopher.waitingTime + " ms"));
        int totalWaitingTime = philosophers.stream().mapToInt(philosopher -> (int) philosopher.waitingTime).sum();

        System.out.println("\n\n");
        System.out.println("Total time waiting : " + totalWaitingTime + " ms");
        System.out.println("Average time waiting: " + totalWaitingTime / numberOfPhilosophers + " ms");
        System.out.println("Total executiontime : " + (end - start) + " ms");

    }


    private class NoCircularWaitPhilosopher implements Runnable {

        private int index;
        public int number;
        private List<Lock> forks;
        public long waitingTime = 0;

        public NoCircularWaitPhilosopher(int index, List<Lock> forks) {
            this.index = index;
            this.number = index + 1;
            this.forks = forks;
        }

        @Override
        public void run() {
            Random r = new Random();
            int thinkingTime = r.nextInt(maxThinkingTime);
            int eatingTime = r.nextInt(maxEatingTime);
            try {
                Thread.sleep(thinkingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Philosopher " + number + " finished thinking.");
            if (number % 2 == 0) {

                takeFork(index);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                takeFork((index + 1) % numberOfPhilosophers);
            } else {
                takeFork((index + 1) % numberOfPhilosophers);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                takeFork(index);


            }
            try {
                Thread.sleep(eatingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Philosopher " + number + " finished eating.");
            if (number % 2 == 0) {
                returnFork((index + 1) % numberOfPhilosophers);
                returnFork(index);
            } else {
                returnFork(index);
                returnFork((index + 1) % numberOfPhilosophers);
            }

        }

        private void takeFork(int index) {
            long startTake = System.currentTimeMillis();
            forks.get(index).lock();
            long endTake = System.currentTimeMillis();
            waitingTime += endTake - startTake;
            System.out.println("Philosopher " + number + " took fork " + (index + 1) + ".");
        }

        private void returnFork(int index) {
            System.out.println("Philosopher " + number + " returns fork " + (index + 1) + ".");
            forks.get(index).unlock();
        }
    }
}
