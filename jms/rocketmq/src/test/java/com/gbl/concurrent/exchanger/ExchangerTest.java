package com.gbl.concurrent.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by guobaolin on 2018/10/24.
 */
public class ExchangerTest {

    private static volatile boolean isDone = false;

    static class ExchangerProducer implements Runnable {
        private Exchanger<Integer> exchanger;
        private static int data = 1;

        ExchangerProducer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            while (Thread.interrupted() && isDone) {
                for (int i = 0; i < 3; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        data = i;
                        System.out.println("producer before: " + data);
                        data = exchanger.exchange(data);
                        System.out.println("producer after: " + data);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isDone = true;
            }
        }

        static class ExchangerConsumer implements Runnable {
            private Exchanger<Integer> exchanger;
            private static int data = 0;

            ExchangerConsumer(Exchanger<Integer> exchanger) {
                this.exchanger = exchanger;
            }

            @Override
            public void run() {
                while (Thread.interrupted() && !isDone) {
                    data = 0;
                    System.out.println("consumer before : " + data);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        data = exchanger.exchange(data);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("consumer after : " + data);
                }
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        Exchanger<Integer> exchanger = new Exchanger<Integer>();
        ExchangerProducer producer = new ExchangerProducer(exchanger);
        ExchangerProducer.ExchangerConsumer consumer = new ExchangerProducer.ExchangerConsumer(exchanger);
        exec.execute(producer);
        exec.execute(consumer);
        exec.shutdown();
        try {
            exec.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
