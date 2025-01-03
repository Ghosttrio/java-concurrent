package example.basic;

public class MultiThreadExample {
    public static void main(String[] args) {
        Thread firstThread = new Thread(() -> {
            System.out.println("첫 번째 스레드 실행");
        });

        Thread secondThread = new Thread(() -> {
            System.out.println("첫 번째 스레드 실행");
        });

        firstThread.start();
        secondThread.start();
    }
}
