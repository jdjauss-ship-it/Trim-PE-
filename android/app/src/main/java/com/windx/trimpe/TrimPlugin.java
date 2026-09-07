package com.windx.trimpe;

import android.app.Activity;
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

import java.io.InputStream;
import java.io.OutputStream;

@CapacitorPlugin(name = "TrimPlugin")
public class TrimPlugin extends Plugin {

    private String selectedWorldUri = null;
    private PluginCall backupCall = null;


    // =========================
    // TEST CONNECTION
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
    // SELECT WORLD
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


    @ActivityCallback
    private void selectWorldResult(
            PluginCall call,
            ActivityResult result
    ) {

        if (call == null) return;

        if (result.getResultCode() != Activity.RESULT_OK) {

            call.reject("No world selected");
            return;
        }

        Intent data = result.getData();

        if (data == null || data.getData() == null) {

            call.reject("Invalid world folder");
            return;
        }

        Uri uri = data.getData();

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

        selectedWorldUri = uri.toString();

        JSObject response = new JSObject();
        response.put("uri", selectedWorldUri);

        call.resolve(response);
    }


    // =========================
    // VERIFY WORLD
    // =========================

    @PluginMethod
    public void verifyWorld(PluginCall call) {

        String uriString = call.getString("uri");

        if (uriString == null || uriString.isEmpty()) {

            call.reject("No world selected");
            return;
        }

        try {

            Uri uri = Uri.parse(uriString);

            DocumentFile worldFolder =
                    DocumentFile.fromTreeUri(
                            getContext(),
                            uri
                    );

            if (worldFolder == null ||
                    !worldFolder.exists() ||
                    !worldFolder.isDirectory()) {

                call.reject("Cannot access world folder");
                return;
            }

            DocumentFile levelDat =
                    worldFolder.findFile("level.dat");

            DocumentFile dbFolder =
                    worldFolder.findFile("db");

            boolean hasLevelDat =
                    levelDat != null && levelDat.exists();

            boolean hasDbFolder =
                    dbFolder != null &&
                    dbFolder.exists() &&
                    dbFolder.isDirectory();

            JSObject response = new JSObject();

            response.put("hasLevelDat", hasLevelDat);
            response.put("hasDbFolder", hasDbFolder);

            response.put(
                    "validWorld",
                    hasLevelDat && hasDbFolder
            );

            call.resolve(response);

        } catch (Exception error) {

            call.reject(
                    "Verification failed: "
                            + error.getMessage()
            );
        }
    }


    // =========================
    // BACKUP WORLD
    // =========================

    @PluginMethod
    public void backupWorld(PluginCall call) {

        String uriString = call.getString("uri");

        if (uriString == null || uriString.isEmpty()) {

            call.reject("No world selected");
            return;
        }

        selectedWorldUri = uriString;
        backupCall = call;

        Intent intent =
                new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );

        startActivityForResult(
                call,
                intent,
                "backupFolderResult"
        );
    }


    @ActivityCallback
    private void backupFolderResult(
            PluginCall call,
            ActivityResult result
    ) {

        if (call == null || backupCall == null) {
            return;
        }

        if (result.getResultCode() != Activity.RESULT_OK) {

            call.reject("Backup cancelled");
            backupCall = null;
            return;
        }

        Intent data = result.getData();

        if (data == null || data.getData() == null) {

            call.reject("Invalid backup folder");
            backupCall = null;
            return;
        }

        Uri backupUri = data.getData();

        int flags = data.getFlags()
                & (
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        );

        try {

            getActivity()
                    .getContentResolver()
                    .takePersistableUriPermission(
                            backupUri,
                            flags
                    );

        } catch (Exception ignored) {
        }

        try {

            Uri worldUri =
                    Uri.parse(selectedWorldUri);

            DocumentFile source =
                    DocumentFile.fromTreeUri(
                            getContext(),
                            worldUri
                    );

            DocumentFile backupRoot =
                    DocumentFile.fromTreeUri(
                            getContext(),
                            backupUri
                    );

            if (source == null || backupRoot == null) {

                call.reject("Cannot access folders");
                backupCall = null;
                return;
            }

            String backupName =
                    "TrimPE_Backup_"
                            + System.currentTimeMillis();

            DocumentFile destination =
                    backupRoot.createDirectory(
                            backupName
                    );

            if (destination == null) {

                call.reject("Could not create backup folder");
                backupCall = null;
                return;
            }

            copyFolder(source, destination);

            JSObject response = new JSObject();

            response.put(
                    "message",
                    "Backup created successfully!"
            );

            response.put(
                    "backupName",
                    backupName
            );

            call.resolve(response);

        } catch (Exception error) {

            call.reject(
                    "Backup failed: "
                            + error.getMessage()
            );
        }

        backupCall = null;
    }


    // =========================
    // COPY FOLDER RECURSIVELY
    // =========================

    private void copyFolder(
            DocumentFile source,
            DocumentFile destination
    ) throws Exception {

        DocumentFile[] files =
                source.listFiles();

        for (DocumentFile file : files) {

            if (file.isDirectory()) {

                DocumentFile newFolder =
                        destination.createDirectory(
                                file.getName()
                        );

                if (newFolder != null) {

                    copyFolder(
                            file,
                            newFolder
                    );
                }

            } else {

                DocumentFile newFile =
                        destination.createFile(
                                file.getType(),
                                file.getName()
                        );

                if (newFile != null) {

                    copyFile(
                            file.getUri(),
                            newFile.getUri()
                    );
                }
            }
        }
    }


    // =========================
    // COPY FILE
    // =========================

    private void copyFile(
            Uri sourceUri,
            Uri destinationUri
    ) throws Exception {

        InputStream input =
                getContext()
                        .getContentResolver()
                        .openInputStream(sourceUri);

        OutputStream output =
                getContext()
                        .getContentResolver()
                        .openOutputStream(destinationUri);

        byte[] buffer =
                new byte[8192];

        int length;

        while (
                input != null &&
                (length = input.read(buffer)) > 0
        ) {

            output.write(
                    buffer,
                    0,
                    length
            );
        }

        if (input != null) {
            input.close();
        }

        if (output != null) {
            output.close();
        }
    }
                                         }
