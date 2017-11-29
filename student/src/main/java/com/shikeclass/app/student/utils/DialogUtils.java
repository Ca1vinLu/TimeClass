package com.shikeclass.app.student.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.TextView;

import com.shikeclass.app.R;


/**
 * Created by LYZ on 2017/8/4 0004.
 */

public class DialogUtils {


    public static void showDialog(final Context context, String message) {
        showDialog(context, "提示", message);
    }

    public static void showDialog(final Context context, String titleStr, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextView title = new TextView(context);
        title.setText(titleStr);
        title.setTextColor(ContextCompat.getColor(context, R.color.black));
        title.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.padding_16dp), 0, context.getResources().getDimensionPixelSize(R.dimen.padding_16dp));
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.show();

//        TextView mes = (TextView) dialog.findViewById(android.R.id.message);
//        mes.setGravity(Gravity.START);
    }


    public static void showDialog(final Context context, String titleStr, String message, String posStr, DialogInterface.OnClickListener posListener, String negStr, DialogInterface.OnClickListener negListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextView title = new TextView(context);
        title.setText(titleStr);
        title.setTextColor(ContextCompat.getColor(context, R.color.black));
        title.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.padding_16dp), 0, context.getResources().getDimensionPixelSize(R.dimen.padding_16dp));
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(posStr, posListener);
        builder.setNegativeButton(negStr, negListener);

        AlertDialog dialog = builder.show();

//        TextView mes = (TextView) dialog.findViewById(android.R.id.message);
//        mes.setGravity(Gravity.START);
    }


}
