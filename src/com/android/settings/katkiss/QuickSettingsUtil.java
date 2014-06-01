/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.katkiss;

import static org.meerkats.katkiss.QSConstants.TILES_DEFAULT;
import static org.meerkats.katkiss.QSConstants.TILE_AIRPLANE;
import static org.meerkats.katkiss.QSConstants.TILE_AUTOROTATE;
import static org.meerkats.katkiss.QSConstants.TILE_BATTERY;
import static org.meerkats.katkiss.QSConstants.TILE_BLUETOOTH;
import static org.meerkats.katkiss.QSConstants.TILE_BRIGHTNESS;
import static org.meerkats.katkiss.QSConstants.TILE_CAMERA;
import static org.meerkats.katkiss.QSConstants.TILE_DELIMITER;
import static org.meerkats.katkiss.QSConstants.TILE_EXPANDEDDESKTOP;
import static org.meerkats.katkiss.QSConstants.TILE_GPS;
import static org.meerkats.katkiss.QSConstants.TILE_LOCKSCREEN;
import static org.meerkats.katkiss.QSConstants.TILE_NETWORKMODE;
import static org.meerkats.katkiss.QSConstants.TILE_SETTINGS;
import static org.meerkats.katkiss.QSConstants.TILE_SYNC;
import static org.meerkats.katkiss.QSConstants.TILE_USER;
import static org.meerkats.katkiss.QSConstants.TILE_VOLUME;
import static org.meerkats.katkiss.QSConstants.TILE_WIFI;
import static org.meerkats.katkiss.QSConstants.TILE_ETHERNET;
import static org.meerkats.katkiss.QSConstants.TILE_WIFIAP;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.Phone;
import org.meerkats.katkiss.QSUtils;
import com.android.settings.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.meerkats.katkiss.KKC;

public class QuickSettingsUtil {
    private static final String TAG = "QuickSettingsUtil";

    public static final Map<String, TileInfo> TILES;

    private static final Map<String, TileInfo> ENABLED_TILES = new HashMap<String, TileInfo>();
    private static final Map<String, TileInfo> DISABLED_TILES = new HashMap<String, TileInfo>();

    static {
        TILES = Collections.unmodifiableMap(ENABLED_TILES);
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_AIRPLANE, R.string.title_tile_airplane,
                "com.android.systemui:drawable/ic_qs_airplane_off"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_BATTERY, R.string.title_tile_battery,
                "com.android.systemui:drawable/ic_qs_battery_neutral"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_BLUETOOTH, R.string.title_tile_bluetooth,
                "com.android.systemui:drawable/ic_qs_bluetooth_neutral"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_BRIGHTNESS, R.string.title_tile_brightness,
                "com.android.systemui:drawable/ic_qs_brightness_auto_off"));
        registerTile(new QuickSettingsUtil.TileInfo(
                 TILE_CAMERA, R.string.title_tile_camera,
                "com.android.systemui:drawable/ic_qs_camera"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_GPS, R.string.title_tile_gps,
                "com.android.systemui:drawable/ic_qs_location_on"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_EXPANDEDDESKTOP, R.string.title_tile_expanded_desktop,
                "com.android.systemui:drawable/ic_qs_expanded_desktop_neutral"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_LOCKSCREEN, R.string.title_tile_lockscreen,
                "com.android.systemui:drawable/ic_qs_lock_screen_off"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_NETWORKMODE, R.string.title_tile_networkmode,
                "com.android.systemui:drawable/ic_qs_2g3g_on"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_AUTOROTATE, R.string.title_tile_autorotate,
                "com.android.systemui:drawable/ic_qs_auto_rotate"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_SETTINGS, R.string.title_tile_settings,
                "com.android.systemui:drawable/ic_qs_settings"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_SYNC, R.string.title_tile_sync,
                "com.android.systemui:drawable/ic_qs_sync_off"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_USER, R.string.title_tile_user,
                "com.android.systemui:drawable/ic_qs_default_user"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_VOLUME, R.string.title_tile_volume,
                "com.android.systemui:drawable/ic_qs_volume"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_WIFI, R.string.title_tile_wifi,
                "com.android.systemui:drawable/ic_qs_wifi_4"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_ETHERNET, R.string.title_tile_ethernet,
                "com.android.systemui:drawable/stat_sys_ethernet_disconnected"));
        registerTile(new QuickSettingsUtil.TileInfo(
                TILE_WIFIAP, R.string.title_tile_wifiap,
                "com.android.systemui:drawable/ic_qs_wifi_ap_neutral"));
    }

    private static void registerTile(QuickSettingsUtil.TileInfo info) {
        ENABLED_TILES.put(info.getId(), info);
    }

    private static void removeTile(String id) {
        ENABLED_TILES.remove(id);
        DISABLED_TILES.remove(id);
        TILES_DEFAULT.remove(id);
    }

    private static void disableTile(String id) {
        if (ENABLED_TILES.containsKey(id)) {
            DISABLED_TILES.put(id, ENABLED_TILES.remove(id));
        }
    }

    private static void enableTile(String id) {
        if (DISABLED_TILES.containsKey(id)) {
            ENABLED_TILES.put(id, DISABLED_TILES.remove(id));
        }
    }

    private static boolean sUnsupportedRemoved = false;

    private static synchronized void removeUnsupportedTiles(Context context) {
        if (sUnsupportedRemoved) {
            return;
        }

        // Don't show mobile data options if not supported
        if (!QSUtils.deviceSupportsMobileData(context)) {
//            removeTile(TILE_MOBILEDATA);
            removeTile(TILE_WIFIAP);
            removeTile(TILE_NETWORKMODE);
        }

        // Don't show the bluetooth options if not supported
        if (!QSUtils.deviceSupportsBluetooth()) {
            removeTile(TILE_BLUETOOTH);
        }


        sUnsupportedRemoved = true;
    }

    private static synchronized void refreshAvailableTiles(Context context) {
        ContentResolver resolver = context.getContentResolver();

        // Some phones run on networks not supported by the networkmode tile,
        // so make it available only where supported
        int networkState = -99;
        try {
            networkState = Settings.Global.getInt(resolver,
                    Settings.Global.PREFERRED_NETWORK_MODE);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Unable to retrieve PREFERRED_NETWORK_MODE", e);
        }

        switch (networkState) {
            // list of supported network modes
            case Phone.NT_MODE_WCDMA_PREF:
            case Phone.NT_MODE_WCDMA_ONLY:
            case Phone.NT_MODE_GSM_UMTS:
            case Phone.NT_MODE_GSM_ONLY:
                enableTile(TILE_NETWORKMODE);
                break;
            default:
                disableTile(TILE_NETWORKMODE);
                break;
        }

    }

    public static synchronized void updateAvailableTiles(Context context) {
        removeUnsupportedTiles(context);
        refreshAvailableTiles(context);
    }

    public static boolean isTileAvailable(String id) {
        return ENABLED_TILES.containsKey(id);
    }

    public static String getCurrentTiles(Context context) {
        String tiles = Settings.System.getString(context.getContentResolver(),
                KKC.S.QUICK_SETTINGS_TILES);
        if (tiles == null) {
            tiles = getDefaultTiles(context);
        }
        return tiles;
    }

    public static void saveCurrentTiles(Context context, String tiles) {
        Settings.System.putString(context.getContentResolver(),
                KKC.S.QUICK_SETTINGS_TILES, tiles);
    }

    public static void resetTiles(Context context) {
        String defaultTiles = getDefaultTiles(context);
        Settings.System.putString(context.getContentResolver(),
                KKC.S.QUICK_SETTINGS_TILES, defaultTiles);
    }

    public static String mergeInNewTileString(String oldString, String newString) {
        ArrayList<String> oldList = getTileListFromString(oldString);
        ArrayList<String> newList = getTileListFromString(newString);
        ArrayList<String> mergedList = new ArrayList<String>();

        // add any items from oldlist that are in new list
        for (String tile : oldList) {
            if (newList.contains(tile)) {
                mergedList.add(tile);
            }
        }

        // append anything in newlist that isn't already in the merged list to
        // the end of the list
        for (String tile : newList) {
            if (!mergedList.contains(tile)) {
                mergedList.add(tile);
            }
        }

        // return merged list
        return getTileStringFromList(mergedList);
    }

    public static ArrayList<String> getTileListFromString(String tiles) {
        return new ArrayList<String>(Arrays.asList(tiles.split("\\|")));
    }

    public static String getTileStringFromList(ArrayList<String> tiles) {
        if (tiles == null || tiles.size() <= 0) {
            return "";
        } else {
            String s = tiles.get(0);
            for (int i = 1; i < tiles.size(); i++) {
                s += TILE_DELIMITER + tiles.get(i);
            }
            return s;
        }
    }

    public static String getDefaultTiles(Context context) {
        removeUnsupportedTiles(context);
        return TextUtils.join(TILE_DELIMITER, TILES_DEFAULT);
    }

    public static class TileInfo {
        private String mId;
        private int mTitleResId;
        private String mIcon;

        public TileInfo(String id, int titleResId, String icon) {
            mId = id;
            mTitleResId = titleResId;
            mIcon = icon;
        }

        public String getId() {
            return mId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public String getIcon() {
            return mIcon;
        }
    }
}
