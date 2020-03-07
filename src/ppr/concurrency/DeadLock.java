package ppr.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class DeadLock {

     List<Lock> forks = new ArrayList<>();
     List<DeadLockPhilosopher> philosophers = new ArrayList<>();
    int numberOfPhilosophers;
    int maxThinkingTime;
    int maxEatingTime;

    public void execute(int numberOfPhilosophers, int maxThinkingTime, int maxEatingTime) {
        this.numberOfPhilosophers = numberOfPhilosophers;
        this.maxThinkingTime =maxThinkingTime;
        this.maxEatingTime = maxEatingTime;
        for (int i = 0; i < numberOfPhilosophers; i++) {
            forks.add(new ReentrantLock());
            philosophers.add(new DeadLockPhilosopher(i, forks));
        }
        Set<Thread> threads = philosophers.stream().map(philosophor -> new Thread(philosophor)).collect(Collectors.toSet());
        threads.stream().forEach(thread -> thread.start());
    }


    private class DeadLockPhilosopher implements Runnable {

        private int index;
        private int number;
        private List<Lock> forks;

        public DeadLockPhilosopher(int index, List<Lock> forks) {
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
            takeFork(index);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            takeFork((index + 1) % numberOfPhilosophers);
            try {
                Thread.sleep(eatingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Philosopher " + number + " finished eating.");
            returnFork((index + 1) % numberOfPhilosophers);
            returnFork(index);
        }

        private void takeFork(int index) {
            forks.get(index).lock();
            System.out.println("Philosopher " + number + " took fork " + (index+1) + ".");
        }

        private void returnFork(int index) {
            System.out.println("Philosopher " + number + " returns fork " + (index + 1) + ".");
            forks.get(index).unlock();

        }
    }
}
