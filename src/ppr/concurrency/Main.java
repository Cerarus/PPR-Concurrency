package ppr.concurrency;

public class Main {

    public static void main(String[] args) {
//        new DeadLock().execute(5, 50, 5000);
        new NoCircularWait().execute(5, 50, 5000);
    }
}
