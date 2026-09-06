package com.windx.trimpe;

import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResult;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TrimPlugin")
public class TrimPlugin extends Plugin {

    @PluginMethod
    public void testConnection(PluginCall call) {

        JSObject result = new JSObject();
        result.put("message", "Trim PE Native Engine Connected!");

        call.resolve(result);
    }


    @PluginMethod
    public void selectWorld(PluginCall call) {

        Intent intent =
                new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION |
                Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        );

        startActivityForResult(
                call,
                intent,
                "selectWorldResult"
        );
    }


    @ActivityCallback
    private void selectWorldResult(
            PluginCall call,
            ActivityResult result
    ) {

        if (call == null) {
            return;
        }

        if (result.getResultCode()
                != android.app.Activity.RESULT_OK) {

            call.reject("No world selected");
            return;
        }

        Intent data = result.getData();

        if (data == null) {
            call.reject("No folder selected");
            return;
        }

        Uri uri = data.getData();

        if (uri == null) {
            call.reject("Invalid world folder");
            return;
        }

        int flags = data.getFlags()
                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {

            getActivity()
                    .getContentResolver()
                    .takePersistableUriPermission(
                            uri,
                            flags
                    );

        } catch (Exception ignored) {
        }

        JSObject response = new JSObject();

        response.put("uri", uri.toString());

        call.resolve(response);
    }
            }
