package com.windx.trimpe;

public class TrimEngine {

    static {
        System.loadLibrary("trimpe");
    }

    public native String testNativeEngine();
}
