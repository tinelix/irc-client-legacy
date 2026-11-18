package dev.tinelix.holoexperience.library.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import dev.tinelix.holoexperience.library.R;
import dev.tinelix.holoexperience.library.app.HoloAlertDialog;

@SuppressLint("AppCompatCustomView")
public class HoloCompatEditText extends EditText {

    private OnCreateContextMenuListener mClipboardMenuListener = null;

    public HoloCompatEditText(Context context) {
        super(context);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) return;
        
        createClipboardMenulistener();
    }

    public HoloCompatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) return;

        createClipboardMenulistener();
    }

    private void createClipboardMenulistener() {
        mClipboardMenuListener = new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(
                    ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo
            ) {
                menu.setHeaderIcon(null);

                final List<MenuItem> items = new ArrayList<>();

                for(int i = 0; i < menu.size(); i++) {
                    items.add(menu.getItem(i));
                }

                menu.clear();

                if(items.isEmpty()) return;

                ArrayAdapter<MenuItem> adapter = new ArrayAdapter<>(
                        getContext(), R.layout.holo4ux_simple_list_item, items
                );

                final EditText editText = (EditText) view;

                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(getResources().getString(R.string.holo4ux_clipboard))
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editText.onTextContextMenuItem(items.get(which).getItemId());
                            }
                        }).create();

                dialog.show();
            }
        };

        setOnCreateContextMenuListener(mClipboardMenuListener);
    }
}
