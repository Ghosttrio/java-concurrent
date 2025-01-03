package example.adv;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueueManager {

    private static final int THREAD_POOL_SIZE = 4;
    private static final int QUEUE_CAPACITY = 20;
    private ExecutorService executorService;
    private BlockingQueue<Runnable> taskQueue;

    public TaskQueueManager() {
        taskQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void addTask(Task task) {
        taskQueue.add(task);
    }
    public void startProcessing() {
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executorService.submit(new TaskProcessor(taskQueue));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    static class Task implements Runnable {

        private String taskName;

        public Task(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            try {
                System.out.println("task " + taskName + " is being processed by "+ Thread.currentThread().getName());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class TaskProcessor implements Runnable {
        private BlockingQueue<Runnable> taskQueue;

        public TaskProcessor(BlockingQueue<Runnable> taskQueue) {
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            try {
                while (!taskQueue.isEmpty()) {
                    Runnable task = taskQueue.take();
                    task.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TaskQueueManager taskQueueManager = new TaskQueueManager();

        for (int i = 1; i <= 20; i++) {
            Task task = new Task("Task " + i);
            taskQueueManager.addTask(task);
        }

        taskQueueManager.startProcessing();

        Thread.sleep(10000);
        taskQueueManager.shutdown();
    }
}
