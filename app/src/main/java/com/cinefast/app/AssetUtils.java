package com.cinefast.app;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class AssetUtils {
    private AssetUtils() {}

    public static String readAssetFile(Context context, String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}

