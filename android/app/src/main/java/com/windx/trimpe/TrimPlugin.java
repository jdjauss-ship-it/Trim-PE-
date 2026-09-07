package com.windx.trimpe;

import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.documentfile.provider.DocumentFile;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TrimPlugin")
public class TrimPlugin extends Plugin {

    // =========================
    // TEST NATIVE CONNECTION
    // =========================

    @PluginMethod
    public void testConnection(PluginCall call) {

        JSObject result = new JSObject();

        result.put(
            "message",
            "Trim PE Native Engine Connected!"
        );

        call.resolve(result);
    }


    // =========================
    // SELECT WORLD FOLDER
    // =========================

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


    // =========================
    // FOLDER PICKER RESULT
    // =========================

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
                & (
                        Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );

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


    // =========================
    // VERIFY MINECRAFT WORLD
    // =========================

    @PluginMethod
    public void verifyWorld(PluginCall call) {

        String uriString = call.getString("uri");

        if (uriString == null || uriString.isEmpty()) {

            call.reject("No world folder selected");

            return;
        }

        try {

            Uri uri = Uri.parse(uriString);

            DocumentFile worldFolder =
                    DocumentFile.fromTreeUri(
                            getContext(),
                            uri
                    );

            if (worldFolder == null
                    || !worldFolder.exists()
                    || !worldFolder.isDirectory()) {

                call.reject("Cannot access world folder");

                return;
            }

            DocumentFile levelDat =
                    worldFolder.findFile("level.dat");

            DocumentFile dbFolder =
                    worldFolder.findFile("db");

            boolean hasLevelDat =
                    levelDat != null
                    && levelDat.exists()
                    && levelDat.isFile();

            boolean hasDbFolder =
                    dbFolder != null
                    && dbFolder.exists()
                    && dbFolder.isDirectory();

            JSObject result = new JSObject();

            result.put("hasLevelDat", hasLevelDat);
            result.put("hasDbFolder", hasDbFolder);

            result.put(
                    "validWorld",
                    hasLevelDat && hasDbFolder
            );

            if (hasLevelDat && hasDbFolder) {

                result.put(
                        "message",
                        "Valid Minecraft Bedrock World!"
                );

            } else {

                result.put(
                        "message",
                        "This folder is not a valid Minecraft world."
                );
            }

            call.resolve(result);

        } catch (Exception error) {

            call.reject(
                    "World verification failed: "
                            + error.getMessage()
            );
        }
    }
            }
