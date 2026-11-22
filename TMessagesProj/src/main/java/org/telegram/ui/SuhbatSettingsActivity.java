package org.telegram.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AyuConfig;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class SuhbatSettingsActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;

    private int premiumHeaderRow;
    private int localPremiumRow;

    private int ghostHeaderRow;
    private int ghostModeRow;
    private int hideOnlineRow;
    private int dontReadRow;
    private int dontTypingRow;

    private int antiDeleteHeaderRow;
    private int saveMessagesRow;
    private int saveChatsRow;

    private int autoReplyHeaderRow;
    private int autoReplyEnabledRow;
    private int autoReplyKeywordRow;
    private int autoReplyTextRow;

    private int rowCount;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        return true;
    }

    private void updateRows() {
        rowCount = 0;

        premiumHeaderRow = rowCount++;
        localPremiumRow = rowCount++;

        ghostHeaderRow = rowCount++;
        ghostModeRow = rowCount++;
        hideOnlineRow = rowCount++;
        dontReadRow = rowCount++;
        dontTypingRow = rowCount++;

        antiDeleteHeaderRow = rowCount++;
        saveMessagesRow = rowCount++;
        saveChatsRow = rowCount++;

        autoReplyHeaderRow = rowCount++;
        autoReplyEnabledRow = rowCount++;
        autoReplyKeywordRow = rowCount++;
        autoReplyTextRow = rowCount++;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("SuhbatGram Sozlamalari");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));

        listAdapter = new ListAdapter(context);

        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView.setOnItemClickListener((view, position) -> {
            if (position == localPremiumRow) {
                AyuConfig.toggleLocalPremium();
                ((TextCheckCell) view).setChecked(AyuConfig.localPremium);
            } else if (position == ghostModeRow) {
                AyuConfig.toggleGhostMode();
                ((TextCheckCell) view).setChecked(AyuConfig.ghostMode);
            } else if (position == hideOnlineRow) {
                AyuConfig.toggleHideOnlineStatus();
                ((TextCheckCell) view).setChecked(AyuConfig.hideOnlineStatus);
            } else if (position == dontReadRow) {
                AyuConfig.toggleDontSendReadPackets();
                ((TextCheckCell) view).setChecked(AyuConfig.dontSendReadPackets);
            } else if (position == dontTypingRow) {
                AyuConfig.toggleDontSendTyping();
                ((TextCheckCell) view).setChecked(AyuConfig.dontSendTyping);
            } else if (position == saveMessagesRow) {
                AyuConfig.toggleSaveDeletedMessages();
                ((TextCheckCell) view).setChecked(AyuConfig.saveDeletedMessages);
            } else if (position == saveChatsRow) {
                AyuConfig.toggleSaveDeletedChats();
                ((TextCheckCell) view).setChecked(AyuConfig.saveDeletedChats);
            } else if (position == autoReplyEnabledRow) {
                AyuConfig.toggleAutoReplyEnabled();
                ((TextCheckCell) view).setChecked(AyuConfig.autoReplyEnabled);
            } else if (position == autoReplyKeywordRow) {
                showInputDialog(context, "Kalit so'z (masalan: salom)", AyuConfig.autoReplyKeyword, text -> {
                    AyuConfig.setAutoReplyKeyword(text);
                    listAdapter.notifyItemChanged(autoReplyKeywordRow);
                });
            } else if (position == autoReplyTextRow) {
                showInputDialog(context, "Avto-javob matni", AyuConfig.autoReplyText, text -> {
                    AyuConfig.setAutoReplyText(text);
                    listAdapter.notifyItemChanged(autoReplyTextRow);
                });
            }
        });

        return fragmentView;
    }

    private interface OnTextSaved {
        void onSave(String text);
    }

    private void showInputDialog(Context context, String title, String currentValue, OnTextSaved callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        int pad = AndroidUtilities.dp(20);
        container.setPadding(pad, AndroidUtilities.dp(10), pad, 0);

        EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setText(currentValue);
        container.addView(editText);

        builder.setView(container);
        builder.setPositiveButton("Saqlash", (dialog, which) -> {
            callback.onSave(editText.getText().toString().trim());
        });
        builder.setNegativeButton("Bekor qilish", null);
        showDialog(builder.create());
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private final Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 1 || type == 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == premiumHeaderRow || position == ghostHeaderRow || position == antiDeleteHeaderRow || position == autoReplyHeaderRow) {
                return 0;
            } else if (position == autoReplyKeywordRow || position == autoReplyTextRow) {
                return 2;
            }
            return 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == 0) {
                view = new HeaderCell(mContext);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (viewType == 2) {
                view = new TextSettingsCell(mContext);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                view = new TextCheckCell(mContext);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int type = holder.getItemViewType();
            if (type == 0) {
                HeaderCell cell = (HeaderCell) holder.itemView;
                if (position == premiumHeaderRow) {
                    cell.setText("Premium");
                } else if (position == ghostHeaderRow) {
                    cell.setText("Ghost Mode (Ko'rinmas rejim)");
                } else if (position == antiDeleteHeaderRow) {
                    cell.setText("Anti-Delete (O'chirilganlarni saqlash)");
                } else if (position == autoReplyHeaderRow) {
                    cell.setText("Direct Avto-Javob (Kalit so'z)");
                }
            } else if (type == 1) {
                TextCheckCell cell = (TextCheckCell) holder.itemView;
                if (position == localPremiumRow) {
                    cell.setTextAndCheck("Local Telegram Premium", AyuConfig.localPremium, false);
                } else if (position == ghostModeRow) {
                    cell.setTextAndCheck("Ghost Mode (Asosiy kalit)", AyuConfig.ghostMode, true);
                } else if (position == hideOnlineRow) {
                    cell.setTextAndCheck("Online holatni yashirish", AyuConfig.hideOnlineStatus, true);
                } else if (position == dontReadRow) {
                    cell.setTextAndCheck("O'qilganlik (2 ta galochka) yubormaslik", AyuConfig.dontSendReadPackets, true);
                } else if (position == dontTypingRow) {
                    cell.setTextAndCheck("'Yozmoqda...' holatini yashirish", AyuConfig.dontSendTyping, false);
                } else if (position == saveMessagesRow) {
                    cell.setTextAndCheck("O'chirilgan xabarlarni saqlash", AyuConfig.saveDeletedMessages, true);
                } else if (position == saveChatsRow) {
                    cell.setTextAndCheck("O'chirilgan chatlarni saqlash", AyuConfig.saveDeletedChats, false);
                } else if (position == autoReplyEnabledRow) {
                    cell.setTextAndCheck("Direct avto-javobni yoqish", AyuConfig.autoReplyEnabled, true);
                }
            } else if (type == 2) {
                TextSettingsCell cell = (TextSettingsCell) holder.itemView;
                if (position == autoReplyKeywordRow) {
                    cell.setTextAndValue("Kalit so'z", AyuConfig.autoReplyKeyword, true);
                } else if (position == autoReplyTextRow) {
                    cell.setTextAndValue("Javob matni", AyuConfig.autoReplyText, false);
                }
            }
        }
    }
}
