package example.basic;

public class ThreadLocalExample {

    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

//    ThreadLocal은 각 스레드마다 독립적인 데이터를 저장할 수 있게 해주는 클래스입니다.
//    즉, 여러 스레드가 동시에 ThreadLocal에 접근하더라도 각 스레드는 자신만의 데이터 공간을 가지게 됩니다.

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            threadLocal.set(100);
            System.out.println("Thread1:" + threadLocal.get());
        });

        Thread thread2 = new Thread(() -> {
            threadLocal.set(200);
            System.out.println("Thread2:" + threadLocal.get());
        });

        thread1.start();
        thread2.start();
    }

}
