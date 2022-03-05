package com.example.gblocalchat.LVL3Lesson4.FirstTaskFrom4HW;

public class WaitAndNotify {

    private final Object monitor = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {

        WaitAndNotify currentInstance = new WaitAndNotify();

        Thread thread1 = new Thread(currentInstance::letterA);
        Thread thread2 = new Thread(currentInstance::letterB);
        Thread thread3 = new Thread(currentInstance::letterC);
        thread1.start();
        thread2.start();
        thread3.start();


    }

    public void letterA() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'A') {
                        monitor.wait();
                    }
                    System.out.print("A");
                    currentLetter = 'B';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void letterB() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'B') {
                        monitor.wait();
                    }
                    System.out.print("B");
                    currentLetter = 'C';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void letterC() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'C') {
                        monitor.wait();
                    }
                    System.out.print("C");
                    currentLetter = 'A';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

