package example.basic;

public class Test2 {

    public static void main(String[] args) {
        MyCounter myCounter = new MyCounter();
        Runnable runnable = myCounter::count;

        Thread thread1 = new Thread(runnable, "1");
        Thread thread2 = new Thread(runnable, "2");

        thread1.start();
        thread2.start();

    }

    static class MyCounter {
        public void count() {
            int localValue = 0;

            for (int i = 0; i < 1000; i++) {
                localValue = localValue + 1;
            }

            System.out.println("결과: " + localValue);
        }
    }
}
