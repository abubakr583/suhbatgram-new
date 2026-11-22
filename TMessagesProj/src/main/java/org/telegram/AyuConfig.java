package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class AyuConfig {
    private static SharedPreferences prefs;

    // 1. O'ziga ko'rinadigan Local Premium
    public static boolean localPremium = true;

    // 2. Faqat Offline ko'rsatish (Ghost Mode)
    public static boolean ghostMode = true;
    public static boolean hideOnlineStatus = true;
    public static boolean dontSendReadPackets = true;
    public static boolean dontSendTyping = true;

    // 3. O'chirilgan xabar va chatlarni saqlab qolish (Anti-Delete)
    public static boolean saveDeletedMessages = true;
    public static boolean saveDeletedChats = true;

    // 4. Avto-xabar (Kalit so'z bilan Direct + Tanlangan chatlarga avto-xabar)
    public static boolean autoReplyDirect = false;
    public static String keywordTrigger = "salom";
    public static String keywordReplyText = "Assalomu alaykum! Hozir bandman, tez orada javob yozaman.";

    public static boolean scheduledAutoMessage = false;
    public static String scheduledText = "Reklama yoki avto-xabar matni";
    public static int scheduledIntervalMinutes = 30;
    public static Set<String> targetChatIds = new HashSet<>();

    public static void loadConfig() {
        if (prefs == null) {
            prefs = ApplicationLoader.applicationContext.getSharedPreferences("suhbatgram_ayu_config", Context.MODE_PRIVATE);
        }
        localPremium = prefs.getBoolean("localPremium", true);
        ghostMode = prefs.getBoolean("ghostMode", true);
        hideOnlineStatus = prefs.getBoolean("hideOnlineStatus", true);
        dontSendReadPackets = prefs.getBoolean("dontSendReadPackets", true);
        dontSendTyping = prefs.getBoolean("dontSendTyping", true);
        saveDeletedMessages = prefs.getBoolean("saveDeletedMessages", true);
        saveDeletedChats = prefs.getBoolean("saveDeletedChats", true);
        autoReplyDirect = prefs.getBoolean("autoReplyDirect", false);
        keywordTrigger = prefs.getString("keywordTrigger", "salom");
        keywordReplyText = prefs.getString("keywordReplyText", "Assalomu alaykum! Hozir bandman, tez orada javob yozaman.");
        scheduledAutoMessage = prefs.getBoolean("scheduledAutoMessage", false);
        scheduledText = prefs.getString("scheduledText", "Reklama yoki avto-xabar matni");
        scheduledIntervalMinutes = prefs.getInt("scheduledIntervalMinutes", 30);
        targetChatIds = new HashSet<>(prefs.getStringSet("targetChatIds", new HashSet<>()));
    }

    public static void saveConfig() {
        if (prefs == null) {
            loadConfig();
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("localPremium", localPremium);
        editor.putBoolean("ghostMode", ghostMode);
        editor.putBoolean("hideOnlineStatus", hideOnlineStatus);
        editor.putBoolean("dontSendReadPackets", dontSendReadPackets);
        editor.putBoolean("dontSendTyping", dontSendTyping);
        editor.putBoolean("saveDeletedMessages", saveDeletedMessages);
        editor.putBoolean("saveDeletedChats", saveDeletedChats);
        editor.putBoolean("autoReplyDirect", autoReplyDirect);
        editor.putString("keywordTrigger", keywordTrigger);
        editor.putString("keywordReplyText", keywordReplyText);
        editor.putBoolean("scheduledAutoMessage", scheduledAutoMessage);
        editor.putString("scheduledText", scheduledText);
        editor.putInt("scheduledIntervalMinutes", scheduledIntervalMinutes);
        editor.putStringSet("targetChatIds", targetChatIds);
        editor.apply();
    }
}
