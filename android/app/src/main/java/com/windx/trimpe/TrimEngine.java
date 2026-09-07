package com.windx.trimpe;

public class TrimEngine {

    static {
        System.loadLibrary("trimpe");
    }

    public native String testNativeEngine();

    public native String prepareTrim(
            String worldPath,
            int x1,
            int z1,
            int x2,
            int z2
    );
}
