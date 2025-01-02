package example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolTaskQueueManager {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(10);
    private static volatile boolean stopProcessor = false;

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("작업 시작...");
                Thread.sleep(1000);  // 작업 시뮬레이션
                System.out.println("작업 종료...");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("작업이 중단되었습니다.");
            }
        }
    }

    public static void startTaskProcessor() {
        // 작업을 큐에서 꺼내서 처리하는 작업 실행
        Runnable taskProcessor = () -> {
            while (!stopProcessor || !queue.isEmpty()) {  // stopProcessor가 true일 때, 큐에 남아있는 작업이 있으면 계속 처리
                try {
                    // 큐에서 작업을 꺼냄
                    Runnable task = queue.take();  // 큐가 비어있으면 대기
                    // 스레드 풀에 작업 제출
                    executorService.submit(task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("작업 처리기 스레드 종료.");
        };

        // 작업 처리기 스레드를 실행
        Thread thread = new Thread(taskProcessor);
        thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        startTaskProcessor();

        for (int i = 1; i <= 10; i++) {
            queue.add(new Task());  // 큐에 작업 추가
        }

        Thread.sleep(10000);  // 10초 동안 작업을 처리

        // 작업 처리기 스레드 종료 플래그를 설정
        stopProcessor = true;

        // ExecutorService 종료
        executorService.shutdown();

        // 모든 작업이 끝날 때까지 대기
        if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
            System.out.println("타임아웃! 작업이 아직 끝나지 않았습니다.");
            executorService.shutdownNow();  // 작업을 강제로 종료시킬 수 있음
        }

        System.out.println("스레드 풀 종료.");
    }
}
