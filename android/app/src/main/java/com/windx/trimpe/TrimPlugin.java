package com.windx.trimpe;

import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResult;

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

        result.put(
            "message",
            "Trim PE Native Engine Connected!"
        );

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

        startActivityForResult(
            call,
            intent,
            "selectWorldResult"
        );
    }


    @Override
    protected void handleOnActivityResult(
            String callbackName,
            ActivityResult result) {

        super.handleOnActivityResult(callbackName, result);

        if (!callbackName.equals("selectWorldResult")) {
            return;
        }

        if (savedCall == null) {
            return;
        }

        if (result.getResultCode() != getActivity().RESULT_OK) {

            savedCall.reject("No world selected");

            savedCall = null;

            return;
        }

        Intent data = result.getData();

        if (data == null) {

            savedCall.reject("No folder selected");

            savedCall = null;

            return;
        }

        Uri uri = data.getData();

        if (uri == null) {

            savedCall.reject("Invalid world folder");

            savedCall = null;

            return;
        }

        final int flags =
            data.getFlags()
            & (Intent.FLAG_GRANT_READ_URI_PERMISSION |
               Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        getActivity()
            .getContentResolver()
            .takePersistableUriPermission(uri, flags);

        JSObject response = new JSObject();

        response.put("uri", uri.toString());

        savedCall.resolve(response);

        savedCall = null;
    }
}
