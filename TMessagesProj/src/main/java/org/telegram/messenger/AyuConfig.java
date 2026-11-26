package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;

public class AyuConfig {
    private static SharedPreferences preferences;

    public static boolean localPremium = true;
    public static boolean ghostMode = false;
    public static boolean hideOnlineStatus = false;
    public static boolean dontSendReadPackets = false;
    public static boolean dontSendTyping = false;

    public static boolean saveDeletedMessages = true;
    public static boolean saveDeletedChats = true;

    public static boolean autoReplyEnabled = false;
    public static String autoReplyKeyword = "salom";
    public static String autoReplyText = "Assalomu alaykum! Hozir bandman, tez orada javob beraman.";

    static {
        load();
    }

    private static SharedPreferences getPrefs() {
        if (preferences == null && ApplicationLoader.applicationContext != null) {
            preferences = ApplicationLoader.applicationContext.getSharedPreferences("ayuconfig", Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public static void load() {
        SharedPreferences prefs = getPrefs();
        if (prefs == null) return;
        localPremium = prefs.getBoolean("localPremium", true);
        ghostMode = prefs.getBoolean("ghostMode", false);
        hideOnlineStatus = prefs.getBoolean("hideOnlineStatus", false);
        dontSendReadPackets = prefs.getBoolean("dontSendReadPackets", false);
        dontSendTyping = prefs.getBoolean("dontSendTyping", false);
        saveDeletedMessages = prefs.getBoolean("saveDeletedMessages", true);
        saveDeletedChats = prefs.getBoolean("saveDeletedChats", true);
        autoReplyEnabled = prefs.getBoolean("autoReplyEnabled", false);
        autoReplyKeyword = prefs.getString("autoReplyKeyword", "salom");
        autoReplyText = prefs.getString("autoReplyText", "Assalomu alaykum! Hozir bandman, tez orada javob beraman.");
    }

    public static void loadConfig() {
        load();
    }

    private static void saveBoolean(String key, boolean value) {
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            prefs.edit().putBoolean(key, value).apply();
        }
    }

    public static void toggleLocalPremium() {
        localPremium = !localPremium;
        saveBoolean("localPremium", localPremium);
    }

    public static void toggleGhostMode() {
        ghostMode = !ghostMode;
        saveBoolean("ghostMode", ghostMode);
    }

    public static void toggleHideOnlineStatus() {
        hideOnlineStatus = !hideOnlineStatus;
        saveBoolean("hideOnlineStatus", hideOnlineStatus);
    }

    public static void toggleDontSendReadPackets() {
        dontSendReadPackets = !dontSendReadPackets;
        saveBoolean("dontSendReadPackets", dontSendReadPackets);
    }

    public static void toggleDontSendTyping() {
        dontSendTyping = !dontSendTyping;
        saveBoolean("dontSendTyping", dontSendTyping);
    }

    public static void toggleSaveDeletedMessages() {
        saveDeletedMessages = !saveDeletedMessages;
        saveBoolean("saveDeletedMessages", saveDeletedMessages);
    }

    public static void toggleSaveDeletedChats() {
        saveDeletedChats = !saveDeletedChats;
        saveBoolean("saveDeletedChats", saveDeletedChats);
    }

    public static void toggleAutoReplyEnabled() {
        autoReplyEnabled = !autoReplyEnabled;
        saveBoolean("autoReplyEnabled", autoReplyEnabled);
    }

    public static void setAutoReplyKeyword(String keyword) {
        autoReplyKeyword = keyword;
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            prefs.edit().putString("autoReplyKeyword", keyword).apply();
        }
    }

    public static void setAutoReplyText(String text) {
        autoReplyText = text;
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            prefs.edit().putString("autoReplyText", text).apply();
        }
    }
}
