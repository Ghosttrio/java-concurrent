package example.adv;

public class DaemonThreadExample {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("데몬 스레드 실행 중...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        System.out.println("메인 스레드 종료");
    }
}
