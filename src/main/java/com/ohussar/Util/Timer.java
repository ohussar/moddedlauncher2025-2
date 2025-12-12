package com.ohussar.Util;

import com.ohussar.Window.Components.Trigger;

public class Timer {

    public static void createTimer(long milliseconds, Trigger onTime){

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            onTime.trigger(null);
        });
        t.setName("timer-thread");
        t.start();


    }


}
