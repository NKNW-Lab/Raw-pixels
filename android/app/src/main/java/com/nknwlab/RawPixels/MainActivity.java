package com.nknwlab.RawPixels;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.getcapacitor.BridgeActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends BridgeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();

        WebView webView = getBridge().getWebView();
        if (webView != null) {
            webView.addJavascriptInterface(new AndroidNativeInterface(this), "AndroidNative");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            controller.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
            controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    public class AndroidNativeInterface {
        private final Context context;

        public AndroidNativeInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public boolean saveToGallery(String base64Data, String filename) {
            try {
                if (base64Data == null || base64Data.isEmpty()) return false;

                String cleanBase64 = base64Data;
                if (cleanBase64.contains(",")) {
                    cleanBase64 = cleanBase64.split(",")[1];
                }

                byte[] decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (bitmap == null) return false;

                boolean success = false;
                String name = (filename != null && !filename.isEmpty()) ? filename : ("nknw_wall_" + System.currentTimeMillis() + ".png");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, name);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/NKNW_Tags");
                    values.put(MediaStore.Images.Media.IS_PENDING, 1);

                    ContentResolver resolver = context.getContentResolver();
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    if (imageUri != null) {
                        try (OutputStream out = resolver.openOutputStream(imageUri)) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        }
                        values.clear();
                        values.put(MediaStore.Images.Media.IS_PENDING, 0);
                        resolver.update(imageUri, values, null, null);
                        success = true;
                    }
                } else {
                    File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File appDir = new File(picturesDir, "NKNW_Tags");
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }
                    File imageFile = new File(appDir, name);
                    try (FileOutputStream out = new FileOutputStream(imageFile)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    }

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(Uri.fromFile(imageFile));
                    context.sendBroadcast(mediaScanIntent);
                    success = true;
                }

                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Immagine salvata in Galleria!", Toast.LENGTH_SHORT).show();
                });

                return success;
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Errore durante il salvataggio in Galleria", Toast.LENGTH_SHORT).show();
                });
                return false;
            }
        }

        @JavascriptInterface
        public void shareImage(String base64Data, String filename) {
            try {
                if (base64Data == null || base64Data.isEmpty()) return;

                String cleanBase64 = base64Data;
                if (cleanBase64.contains(",")) {
                    cleanBase64 = cleanBase64.split(",")[1];
                }

                byte[] decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (bitmap == null) return;

                File cachePath = new File(context.getCacheDir(), "images");
                if (!cachePath.exists()) cachePath.mkdirs();

                String name = (filename != null && !filename.isEmpty()) ? filename : ("nknw_wall_" + System.currentTimeMillis() + ".png");
                File file = new File(cachePath, name);
                try (FileOutputStream stream = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }

                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);

                if (contentUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    shareIntent.setType("image/png");
                    context.startActivity(Intent.createChooser(shareIntent, "Condividi immagine"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
