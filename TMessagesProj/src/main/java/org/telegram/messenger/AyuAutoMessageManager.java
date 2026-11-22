package org.telegram.messenger;

import android.os.Handler;
import android.os.Looper;
import org.telegram.tgnet.TLRPC;

public class AyuAutoMessageManager {
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static boolean isTimerRunning = false;

    public static void checkIncomingMessage(int currentAccount, MessageObject messageObject) {
        if (!AyuConfig.autoReplyDirect || messageObject == null || messageObject.isOut()) {
            return;
        }
        long dialogId = messageObject.getDialogId();
        if (dialogId <= 0) {
            return;
        }
        CharSequence text = messageObject.messageText;
        if (text == null) {
            return;
        }
        String lowerText = text.toString().toLowerCase().trim();
        String trigger = AyuConfig.keywordTrigger.toLowerCase().trim();

        if (!trigger.isEmpty() && lowerText.contains(trigger)) {
            SendMessagesHelper.getInstance(currentAccount).sendMessage(
                SendMessagesHelper.SendMessageParams.of(
                    AyuConfig.keywordReplyText,
                    dialogId,
                    messageObject,
                    null,
                    null,
                    true,
                    null,
                    null,
                    null,
                    true,
                    0,
                    null,
                    false
                )
            );
        }
    }

    public static void startScheduledSender(int currentAccount) {
        if (isTimerRunning) {
            return;
        }
        isTimerRunning = true;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (AyuConfig.scheduledAutoMessage && !AyuConfig.targetChatIds.isEmpty()) {
                    for (String chatIdStr : AyuConfig.targetChatIds) {
                        try {
                            long chatId = Long.parseLong(chatIdStr.trim());
                            SendMessagesHelper.getInstance(currentAccount).sendMessage(
                                SendMessagesHelper.SendMessageParams.of(
                                    AyuConfig.scheduledText,
                                    chatId,
                                    null,
                                    null,
                                    null,
                                    true,
                                    null,
                                    null,
                                    null,
                                    true,
                                    0,
                                    null,
                                    false
                                )
                            );
                        } catch (Exception ignored) {
                        }
                    }
                }
                long delayMs = Math.max(1, AyuConfig.scheduledIntervalMinutes) * 60 * 1000L;
                handler.postDelayed(this, delayMs);
            }
        };
        handler.postDelayed(task, 10000);
    }
}
