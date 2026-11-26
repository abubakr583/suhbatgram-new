package org.telegram.messenger;

import android.text.TextUtils;
import java.util.HashMap;

public class AyuAutoMessageManager {

    private static final HashMap<Long, Long> lastReplyTime = new HashMap<>();
    private static final long COOLDOWN_MS = 60_000L;

    public static void startScheduledSender(int account) {
    }

    public static void checkIncomingMessage(int currentAccount, MessageObject msgObj) {
        if (msgObj == null || msgObj.messageOwner == null) return;
        if (!AyuConfig.autoReplyEnabled) return;
        if (msgObj.isOut() || msgObj.isOutOwner()) return;

        long dialogId = msgObj.getDialogId();
        if (dialogId <= 0) return;

        CharSequence textSeq = msgObj.messageText;
        if (TextUtils.isEmpty(textSeq)) return;

        String incoming = textSeq.toString().toLowerCase().trim();
        String keyword = AyuConfig.autoReplyKeyword != null ? AyuConfig.autoReplyKeyword.toLowerCase().trim() : "";

        if (!TextUtils.isEmpty(keyword) && !incoming.contains(keyword)) {
            return;
        }

        long now = System.currentTimeMillis();
        Long last = lastReplyTime.get(dialogId);
        if (last != null && (now - last) < COOLDOWN_MS) {
            return;
        }
        lastReplyTime.put(dialogId, now);

        String replyText = AyuConfig.autoReplyText;
        if (TextUtils.isEmpty(replyText)) return;

        AndroidUtilities.runOnUIThread(() -> {
            SendMessagesHelper.SendMessageParams params = new SendMessagesHelper.SendMessageParams();
            params.message = replyText;
            params.peer = dialogId;
            params.notify = true;
            SendMessagesHelper.getInstance(currentAccount).sendMessage(params);
        });
    }
}
