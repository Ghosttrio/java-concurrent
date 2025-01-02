package example;

public class ThreadPriorityExample {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> System.out.println("첫 번째 스레드"));
        Thread thread2 = new Thread(() -> System.out.println("두 번째 쓰레드"));

        thread1.setPriority(1);
        thread2.setPriority(2);

        thread1.start();
        thread2.start();
    }
}
