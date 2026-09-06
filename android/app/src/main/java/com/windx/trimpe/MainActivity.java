package com.windx.trimpe;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        registerPlugin(TrimPlugin.class);

        super.onCreate(savedInstanceState);
    }
}
