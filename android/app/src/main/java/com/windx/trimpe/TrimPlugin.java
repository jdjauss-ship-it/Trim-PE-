package com.windx.trimpe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TrimPlugin")
public class TrimPlugin extends Plugin {

    private PluginCall savedCall;

    @PluginMethod
    public void testConnection(PluginCall call) {

        JSObject result = new JSObject();
        result.put("message", "Trim PE Native Engine Connected!");

        call.resolve(result);
    }


    @PluginMethod
    public void selectWorld(PluginCall call) {

        savedCall = call;

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION |
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
            Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION |
            Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        );

        startActivityForResult(call, intent, "selectWorld");
    }


    @Override
    protected void handleOnActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.handleOnActivityResult(
            requestCode,
            resultCode,
            data
        );

        if (savedCall == null) {
            return;
        }

        if (resultCode != Activity.RESULT_OK || data == null) {

            savedCall.reject("No world selected");

            savedCall = null;

            return;
        }

        Uri uri = data.getData();

        if (uri == null) {

            savedCall.reject("Invalid world folder");

            savedCall = null;

            return;
        }

        int flags = data.getFlags()
                & (Intent.FLAG_GRANT_READ_URI_PERMISSION |
                   Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {

            getActivity()
                .getContentResolver()
                .takePersistableUriPermission(uri, flags);

        } catch (Exception ignored) {
        }

        JSObject response = new JSObject();

        response.put("uri", uri.toString());

        savedCall.resolve(response);

        savedCall = null;
    }
}
