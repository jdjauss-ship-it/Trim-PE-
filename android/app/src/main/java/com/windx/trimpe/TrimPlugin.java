package com.windx.trimpe;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.PluginMethod;

@CapacitorPlugin(name = "TrimPlugin")
public class TrimPlugin extends Plugin {

    @PluginMethod
    public void testConnection(PluginCall call) {

        JSObject result = new JSObject();

        result.put(
            "message",
            "Trim PE Native Engine Connected!"
        );

        call.resolve(result);
    }
}
