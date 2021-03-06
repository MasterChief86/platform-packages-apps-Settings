
package com.android.settings.katkiss;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

public class TextInfoFragment extends DialogFragment {
    String mContent;
    public final static String TITLE_KEY = "TITLE_KEY";
    public final static String TEXT_RES_KEY = "TEXT_RES_KEY";

    static String mTitle;
    static int mContentRes;

    public static TextInfoFragment newInstance(Bundle b) {
        mTitle = b.getString(TITLE_KEY);
        mContentRes = b.getInt(TEXT_RES_KEY);
        return new TextInfoFragment();
    }

    public TextInfoFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContent = Utils.readRawTextFile(getActivity(), mContentRes);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ScrollView vScroll = new ScrollView(getActivity());
        HorizontalScrollView hScroll = new HorizontalScrollView(getActivity());
        TextView mText = new TextView(getActivity());
        mText.setPadding(20, 10, 10, 10);
        mText.setText(mContent);
        hScroll.addView(mText);
        vScroll.addView(hScroll);

        builder.setView(vScroll)
                .setTitle(mTitle)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
