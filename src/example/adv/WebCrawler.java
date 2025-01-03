package example.adv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WebCrawler {

    /**
     * CopyOnWriteArrayList는 스레드 안전한 리스트 구현체로, 멀티스레드 환경에서 안전하게 사용할 수 있는 컬렉션 클래스입니다.
     * 이 클래스는 java.util.concurrent 패키지에 포함되어 있으며, 읽기 작업이 많은 경우에 유용하게 사용됩니다. 그 동작 방식은 ArrayList와 비슷하지만, 쓰기 작업이 있을 때마다 복사본을 만들어 처리하는 방식입니다.
     *
     * 주요 특징
     * 스레드 안전성:
     * CopyOnWriteArrayList는 멀티스레드 환경에서 읽기 작업과 쓰기 작업이 동시에 일어날 때 발생할 수 있는 경쟁 조건(race condition)을 방지할 수 있도록 설계되었습니다.
     * 이를 통해 여러 스레드가 동시에 리스트를 읽거나 쓸 때에도 안전하게 사용할 수 있습니다.
     * 쓰기 작업 시 복사본 생성:
     * CopyOnWriteArrayList는 쓰기가 발생할 때마다 내부 배열의 복사본을 생성하여 데이터를 변경합니다. 즉, 원본 배열을 수정하지 않고, 복사본에 대한 작업을 진행하는 방식입니다.
     * 이로 인해 쓰기 작업이 상대적으로 비효율적이지만, 읽기 작업은 매우 빠르고 안전하게 처리할 수 있습니다.
     * 읽기 작업 최적화:
     * 쓰기 작업이 이루어질 때마다 복사본을 생성하므로, 읽기 작업은 매우 빠르게 수행됩니다.
     * 읽기 작업을 수행하는 동안에는 다른 스레드에서 쓰기를 하더라도 영향을 받지 않기 때문에 읽기 작업이 잠금 없이 빠르게 처리됩니다.
     */
    private static final List<String> results = new CopyOnWriteArrayList<>();
    private static final Lock lock = new ReentrantLock();

    public static class CrawlTask implements Callable<String> {
        private final String url;

        public CrawlTask(String url) {
            this.url = url;
        }

        @Override
        public String call() {
            StringBuilder result = new StringBuilder();

            try {
                System.out.println("Crawling " + url + "...");
                Thread.sleep(10000);

                result.append("Crawled content from ").append(url);
                lock.lock();

                try {
                    results.add(result.toString());
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                System.out.println("Error : " + e.getCause());
            }
            return result.toString();
        }
    }

    public static void crawl(List<String> urls) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(urls.size());

        List<Future<String>> list = new ArrayList<>();

        for(String url : urls) {
            list.add(executorService.submit(new CrawlTask(url)));
        }

        for (Future<String> future : list) {
            future.get();
        }

        executorService.shutdown();

    }

    public static void main(String[] args) {
        List<String> urls = List.of("url1", "url2", "url3");

        try {
            crawl(urls);

            for (String result : results) {
                System.out.println("Result : " + result);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
