package com.steven.utils;

import java.util.concurrent.TimeUnit;

public class Threads {
    public static void sleeps(long time) {
        try {
            Thread.sleep(time);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleeps(long druation, TimeUnit unit) {
        sleeps(unit.toMillis(druation));
    }

    public static void wait(Object target) {
        try {
            target.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
