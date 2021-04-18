package ru.geekbrains.javaLevel2;
public class Main {

    private static final int SIZE = 10000000;
    private static final int HALF = SIZE / 2;
    private static float[] arr = new float[SIZE];

    public static void main(String[] args) {
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1;
        }

        long singleThreadTime = singleThread(arr);
        long multiThreadTime = multiThread(arr);

        increase(singleThreadTime, multiThreadTime);
    }

    private static long singleThread(float[] arr) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        long singleTime = System.currentTimeMillis() - start;

        System.out.printf("single thread time: %d%n", singleTime);
        return singleTime;
    }

    private static long multiThread(float[] arr) {
        float[] a = new float[HALF];
        float[] b = new float[HALF];
        long start = System.currentTimeMillis();

        System.arraycopy(arr, 0, a, 0, HALF);
        System.arraycopy(arr, HALF, b, 0, HALF);

        MyThread trd1 = new MyThread("a", a);
        MyThread trd2 = new MyThread("b", b);
        trd1.start();
        trd2.start();

        try {
            trd1.join();
            trd2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        a = trd1.getArr();
        b = trd2.getArr();

        System.arraycopy(a, 0, arr, 0, HALF);
        System.arraycopy(b, 0, arr, a.length, b.length);

        long multiTime = System.currentTimeMillis() - start;
        System.out.printf("multi thread time: %d%n", multiTime);
        return multiTime;
    }

    private static void increase(long singleTime, long multiTime) {
        double diff = ((double) singleTime / (double) multiTime) - 1;
        int increase = (int) (diff * 100);
        System.out.printf("increase: %d%%%n", increase);
    }
}