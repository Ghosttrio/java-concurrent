package example.basic;

public class JoinExample {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("첫 번째 스레드 종료");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(10000);
                System.out.println("두 번째 스레드 종료");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();

//        thread1.join();
        thread2.join();

        System.out.println("메인 스레드 종료");
    }
}
