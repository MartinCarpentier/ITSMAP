package com.example.norgaard.barty.Utilities;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by marti on 30-05-2017.
 */

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
