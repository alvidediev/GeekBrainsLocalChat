package com.example.gblocalchat.FirstTaskFrom4HW;

public class WaitAndNotify {
    public static void main(String[] args) {

        /**
         * Сказано - сделано!
         * Если честно очень стыдно за такой индусский код, но он работает. Можно оптимизировать разными
         * способами (наверное), но у меня из-за последней рабочей неделе пока что совсем не хватит времени на это...
         */
        Object monitor = new Object();

        new Thread(() -> {
            synchronized (monitor){
                try {
                    System.out.print("A");
                    monitor.wait();
                    System.out.print("A");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("A");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("A");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("A");
                    monitor.notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (monitor){
                try {
                    System.out.print("B");
                    monitor.wait();
                    System.out.print("B");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("B");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("B");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("B");
                    monitor.notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (monitor){
                try {
                    System.out.print("C");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("C");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("C");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("C");
                    monitor.notify();
                    monitor.wait();
                    System.out.print("C");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
