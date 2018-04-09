package com.shikeclass.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.shikeclass.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LYZ on 2018/3/28 0028.
 */

public class InputCodeDialog extends DialogFragment {

    @BindView(R.id.et_code)
    TextInputEditText etCode;
    @BindView(R.id.til_code)
    TextInputLayout tilCode;

    public interface CodeCallback {
        void onCommit(String code);
    }

    private CodeCallback codeCallback;


    public static InputCodeDialog newInstance(CodeCallback codeCallback) {


        InputCodeDialog fragment = new InputCodeDialog();
        fragment.setCodeCallback(codeCallback);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入签到码");
        View view = View.inflate(getActivity(), R.layout.dialog_input_code, null);
        ButterKnife.bind(this, view);
        builder.setView(view);

        builder.setPositiveButton("签到", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                codeCallback.onCommit(etCode.getText().toString());
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    public void setCodeCallback(CodeCallback codeCallback) {
        this.codeCallback = codeCallback;
    }
}
