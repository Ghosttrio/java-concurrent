package example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManagerExample {

    public static CountDownLatch countDownLatch;

    static class DownloadA implements Runnable {
        @Override
        public void run() {
            System.out.println("A 파일 다운로드 중...");
            try {
                Thread.sleep(20000);
                System.out.println("A 파일 다운로드 완료!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    static class DownloadB implements Runnable {
        @Override
        public void run() {
            System.out.println("B 파일 다운로드 중...");
            try {
                Thread.sleep(10000);
                System.out.println("B 파일 다운로드 완료!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Runnable> list = new ArrayList<>();
        list.add(new DownloadA());
        list.add(new DownloadB());

        int size = list.size();

        countDownLatch = new CountDownLatch(size);

        ExecutorService executorService = Executors.newFixedThreadPool(size);

        for (int i = 0; i < size; i++) {
            Runnable task = list.get(i);
            executorService.submit(task);
        }

        countDownLatch.await();
        executorService.shutdown();
        System.out.println("다운로드 프로그램 종료...");
    }

}
