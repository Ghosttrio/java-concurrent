package example;

public class RunnableExample implements Runnable {

    @Override
    public void run() {
        System.out.println("쓰레드 실행 중");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableExample());
        thread.start();
    }
}
