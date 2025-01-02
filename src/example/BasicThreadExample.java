package example;

public class BasicThreadExample extends Thread {

    @Override
    public void run() {
        System.out.println("스레드 실행 중");
    }

    public static void main(String[] args) {
        BasicThreadExample thread = new BasicThreadExample();
        thread.start();
    }
}
