package org.problems.company3;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JobScheduler<T> implements Runnable {

    private int MAX_PARALLELISM;

    private AtomicInteger parallelism;

    private ExecutorService service;

    private Queue<Task<T>> queue;

    public JobScheduler(int parallelism) {
        this.parallelism = new AtomicInteger(parallelism);
        this.MAX_PARALLELISM = parallelism;
        queue = new PriorityQueue<>();
        service = Executors.newFixedThreadPool(parallelism);
    }

    private class Task<T> implements Delayed {

        private Long timeToRun;

        private Job<T> job;

        public Task(Job<T> job, Long delay) {
            this.timeToRun = System.currentTimeMillis() + delay;
            this.job = job;
        }


        @Override
        public long getDelay(TimeUnit unit) {
            return timeToRun;
        }

        @Override
        public int compareTo(Delayed o) {
            return timeToRun.compareTo(o.getDelay(TimeUnit.MILLISECONDS));
        }
    }

    private class Job<T> implements Callable<T> {

        @Override
        public T call() throws Exception {
            start();
            Thread.sleep((long) (Math.random() * 1000));
            done();
            return null;
        }

        private void start() {
            parallelism.decrementAndGet();
        }

        private void done() {
            parallelism.incrementAndGet();
        }
    }

    void scheduleJob(Job<T> job, long delayMillis) {
        queue.add(new Task<T>(job, delayMillis));
    }


    @Override
    public void run() {
        while (true) {
            Task<T> task = queue.peek();
            long sleepFor = task.timeToRun - System.currentTimeMillis();
            try {
                if (sleepFor > 0) {
                    Thread.sleep(task.timeToRun);
                } else {
                    service.submit(queue.poll().job);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


}
