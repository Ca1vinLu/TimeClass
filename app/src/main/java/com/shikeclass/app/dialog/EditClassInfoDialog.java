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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shikeclass.app.R;
import com.shikeclass.app.bean.ClassBean;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LYZ on 2018/3/20 0020.
 */

public class EditClassInfoDialog extends DialogFragment {

    public static final int EDIT_MODE = 1;
    public static final int ADD_MODE = 2;


    public interface EditCallback {
        void onEdit(ClassBean bean);

        void onDelete(ClassBean bean);
    }

    @BindView(R.id.til_class_name)
    TextInputLayout tilClassName;
    @BindView(R.id.til_class_address)
    TextInputLayout tilClassAddress;
    @BindView(R.id.til_class_start_week)
    TextInputLayout tilClassStartWeek;
    @BindView(R.id.til_class_end_week)
    TextInputLayout tilClassEndWeek;
    @BindView(R.id.til_class_start_class)
    TextInputLayout tilClassStartClass;
    @BindView(R.id.til_class_end_class)
    TextInputLayout tilClassEndClass;
    @BindView(R.id.et_class_name)
    TextInputEditText etClassName;
    @BindView(R.id.et_class_address)
    TextInputEditText etClassAddress;
    @BindView(R.id.sp_class_week)
    NiceSpinner spClassWeek;
    @BindView(R.id.et_class_start_week)
    TextInputEditText etClassStartWeek;
    @BindView(R.id.et_class_end_week)
    TextInputEditText etClassEndWeek;
    @BindView(R.id.et_class_start_class)
    TextInputEditText etClassStartClass;
    @BindView(R.id.et_class_end_class)
    TextInputEditText etClassEndClass;
    @BindView(R.id.rb_no_limit)
    RadioButton rbNoLimit;
    @BindView(R.id.rb_single)
    RadioButton rbSingle;
    @BindView(R.id.rb_double)
    RadioButton rbDouble;
    @BindView(R.id.rg_week)
    RadioGroup rgWeek;

    private String[] weekArray = {
            "周一",
            "周二",
            "周三",
            "周四",
            "周五",
            "周六",
            "周日"
    };

    private int mode = EDIT_MODE;
    private ClassBean bean;
    private EditCallback editCallback;


    public static EditClassInfoDialog newInstance(ClassBean bean, EditCallback callback) {
        EditClassInfoDialog fragment = new EditClassInfoDialog();
        fragment.setBean(bean);
        fragment.setEditCallback(callback);
        return fragment;
    }

    public static EditClassInfoDialog newInstance(ClassBean bean, EditCallback callback, int mode) {
        EditClassInfoDialog fragment = new EditClassInfoDialog();
        fragment.setBean(bean);
        fragment.setEditCallback(callback);
        fragment.setMode(mode);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("课程信息");
        builder.setPositiveButton(mode == EDIT_MODE ? "修改" : "添加", null);
        if (mode == EDIT_MODE)
            builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editCallback.onDelete(bean);
                }
            });

        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        View view = View.inflate(getActivity(), R.layout.dialog_edit_class_info, null);
        ButterKnife.bind(this, view);
        builder.setView(view);

        builder.setCancelable(false);
        initData();
        Dialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (checkData()) {
                            getData();
                            editCallback.onEdit(bean);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }


    private void initData() {
        spClassWeek.attachDataSource(Arrays.asList(weekArray));
        if (bean == null)
            return;
        spClassWeek.setSelectedIndex(bean.weekDay - 1);
        etClassName.setText(bean.name);
        etClassAddress.setText(bean.address);
        etClassStartWeek.setText(String.valueOf(bean.startWeek));
        etClassEndWeek.setText(String.valueOf(bean.endWeek));
        etClassStartClass.setText(String.valueOf(bean.startClass));
        etClassEndClass.setText(String.valueOf(bean.endClass));
        switch (bean.isSingleOrDouble) {
            case 0:
                rbNoLimit.setChecked(true);
                break;
            case 1:
                rbSingle.setChecked(true);
                break;
            case 2:
                rbDouble.setChecked(true);
                break;
        }
    }

    private void getData() {
        if (bean == null)
            bean = new ClassBean();
        bean.weekDay = spClassWeek.getSelectedIndex() + 1;
        bean.name = etClassName.getText().toString();
        bean.address = etClassAddress.getText().toString();
        bean.startWeek = Integer.parseInt(etClassStartWeek.getText().toString());
        bean.endWeek = Integer.parseInt(etClassEndWeek.getText().toString());
        bean.startClass = Integer.parseInt(etClassStartClass.getText().toString());
        bean.endClass = Integer.parseInt(etClassEndClass.getText().toString());

        if (rbNoLimit.isChecked())
            bean.isSingleOrDouble = 0;
        else if (rbSingle.isChecked())
            bean.isSingleOrDouble = 1;
        else if (rbDouble.isChecked())
            bean.isSingleOrDouble = 2;
    }

    private boolean checkData() {
        tilClassName.setErrorEnabled(false);
        tilClassAddress.setErrorEnabled(false);
        tilClassStartClass.setErrorEnabled(false);
        tilClassEndClass.setErrorEnabled(false);
        tilClassStartWeek.setErrorEnabled(false);
        tilClassEndWeek.setErrorEnabled(false);

        if (etClassName.getText().toString().trim().equals("")) {
            showError(tilClassName, "请输入课程名称");
            return false;
        } else if (etClassAddress.getText().toString().trim().equals("")) {
            showError(tilClassAddress, "请输入上课地点");
            return false;
        } else if (etClassStartWeek.getText().toString().equals("")) {
            showError(tilClassStartWeek, "请输入开始周数");
            return false;
        } else if (etClassEndWeek.getText().toString().equals("")) {
            showError(tilClassEndWeek, "请输入结束周数");
            return false;
        } else if (etClassStartClass.getText().toString().equals("")) {
            showError(tilClassStartClass, "请输入开始节数");
            return false;
        } else if (etClassEndClass.getText().toString().equals("")) {
            showError(tilClassEndClass, "请输入结束节数");
            return false;
        } else if (Integer.valueOf(etClassStartWeek.getText().toString()) <= 0) {
            showError(tilClassStartWeek, "开始周数最小为1");
            return false;
        } else if (Integer.valueOf(etClassStartWeek.getText().toString()) > Integer.valueOf(etClassEndWeek.getText().toString())) {
            showError(tilClassEndWeek, "结束周数必须大于开始周数");
            return false;
        } else if (Integer.valueOf(etClassStartClass.getText().toString()) <= 0) {
            showError(tilClassStartClass, "开始节数最小为1");
            return false;
        } else if (Integer.valueOf(etClassStartClass.getText().toString()) > Integer.valueOf(etClassEndClass.getText().toString())) {
            showError(tilClassEndClass, "结束节数必须大于开始节数");
            return false;
        } else if (Integer.valueOf(etClassEndClass.getText().toString()) > 13) {
            showError(tilClassEndClass, "结束节数最大为13");
            return false;
        }

        return true;
    }

    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().requestFocus();
    }


    public void setBean(ClassBean bean) {
        this.bean = bean;
    }

    public void setEditCallback(EditCallback editCallback) {
        this.editCallback = editCallback;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
