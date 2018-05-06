package com.shikeclass.app.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.shikeclass.app.R;
import com.shikeclass.app.activity.FileActivity;
import com.shikeclass.app.activity.SignInListActivity;
import com.shikeclass.app.bean.ServerClassBean;
import com.shikeclass.app.dialog.InputCodeDialog;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.DialogUtils;
import com.shikeclass.app.utils.NetworkErrorUtils;
import com.shikeclass.app.utils.SharedPreUtil;
import com.shikeclass.app.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LYZ on 2018/3/27 0027.
 */

public class ServerClassAdapter extends BaseQuickAdapter<ServerClassBean, BaseViewHolder> {


    private int userType;

    private String[] weeks = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private String[] beforeStartTime = {"7:50", "8:50", "10:00", "11:00", "", "", "14:20", "15:20", "16:20", "17:20", "19:00", "20:00", "21:00"};
    private String[] startTime = {"8:00", "9:00", "10:10", "11:10", "", "", "14:30", "15:30", "16:30", "17:30", "19:10", "20:10", "21:10"};
    private String[] endTime = {"8:50", "9:50", "11:00", "12:00", "", "", "15:20", "16:20", "17:20", "18:20", "20:00", "21:00", "22:00"};


    public ServerClassAdapter(int type) {
        super(R.layout.item_class);
        this.userType = type;

        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (userType == 0) {
                    switch (view.getId()) {
                        case R.id.btn_file:
                            Intent intent = new Intent(mContext, FileActivity.class);
                            intent.putExtra("lessonName", mData.get(position).lesson_name);
                            intent.putExtra("lessonId", mData.get(position).lesson_id);
                            mContext.startActivity(intent);
                            break;
                        case R.id.btn_sign:
                            final ServerClassBean bean = mData.get(position);
                            if ((bean.status == 1 || bean.status == 2) && bean.exist.equals("0")) {
                                InputCodeDialog dialog = InputCodeDialog.newInstance(new InputCodeDialog.CodeCallback() {
                                    @Override
                                    public void onCommit(String code) {
                                        sign(bean, code, position);
                                    }
                                });

                                dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "input_code_dialog");
                            }
                            break;
                    }
                } else {
                    switch (view.getId()) {
                        case R.id.btn_file:
                            Intent intent = new Intent(mContext, SignInListActivity.class);
                            intent.putExtra("lesson_id", mData.get(position).lesson_id);
                            mContext.startActivity(intent);
                            break;
                        case R.id.btn_sign:
                            generateCode(mData.get(position).lesson_id);
                            break;
                    }
                }
            }
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, ServerClassBean item) {
        helper.setText(R.id.class_name, item.lesson_name);
        String time = weeks[Integer.valueOf(item.lesson_id.substring(0, 1))] + " " + startTime[Integer.valueOf(item.lesson_id.substring(1, 2)) - 1] + "-" + endTime[Integer.valueOf(item.lesson_id.substring(2, 3)) - 1];
        helper.setText(R.id.class_time, time);
        helper.setText(R.id.class_place, item.lesson_id.substring(3));

        if (userType == 0) {
            helper.setText(R.id.class_file, item.lesson_name + " 课件");
            if (item.status==0) {
                helper.setText(R.id.btn_sign, mContext.getString(R.string.imme_sign_in));
                helper.setBackgroundColor(R.id.btn_sign, ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else if (item.status==1) {
                helper.setText(R.id.btn_sign, mContext.getString(R.string.having_lesson_soon));
                helper.setBackgroundColor(R.id.btn_sign, ContextCompat.getColor(mContext, R.color.yellow_ffcc00));
            } else if (item.status==2) {
                if (item.getExist().equals("0")) {
                    helper.setText(R.id.btn_sign, mContext.getString(R.string.imme_sign_in));
                    helper.setBackgroundColor(R.id.btn_sign, ContextCompat.getColor(mContext, R.color.colorPrimary));
                } else {
                    helper.setText(R.id.btn_sign, mContext.getString(R.string.is_having_lesson));
                    helper.setBackgroundColor(R.id.btn_sign, ContextCompat.getColor(mContext, R.color.colorPrimary));
                }
            } else if (item.status==3){
                helper.setText(R.id.btn_sign, mContext.getString(R.string.class_is_over));
                helper.setBackgroundColor(R.id.btn_sign, ContextCompat.getColor(mContext, R.color.grey_7a7a7a));
            }

        } else {
            helper.setText(R.id.class_file, "查看签到表");
            helper.setText(R.id.btn_sign, mContext.getString(R.string.start_sign_in));
            helper.setBackgroundColor(R.id.btn_sign, ContextCompat.getColor(mContext, R.color.colorPrimary));
        }
        helper.addOnClickListener(R.id.btn_file);
        helper.addOnClickListener(R.id.btn_sign);
    }

    private void sign(ServerClassBean item, String code, final int pos) {
        OkGo.<String>post(UrlUtils.studentSign)
                .tag(mContext)
                .params("student_id", SharedPreUtil.getStringValue(mContext, CommonValue.SHA_STU_ID, ""))
                .params("lesson_id", item.lesson_id)
                .params("code", code)
                .params("isStudent", 0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int status = jsonObject.optInt("status");
                            if (status == 200) {
                                DialogUtils.showDialog(mContext, "签到成功");
                                notifyItemChanged(pos);
                            } else if (status == 100) {
                                DialogUtils.showDialog(mContext, jsonObject.optString("data"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (NetworkErrorUtils.isNetworkError(response.getException()))
                            DialogUtils.showDialog(mContext, "网络异常，签到失败");
                        else
                            DialogUtils.showDialog(mContext, "签到失败");
                    }
                });

    }

    private void generateCode(String lessonId) {
        OkGo.<String>post(UrlUtils.teacherSign)
                .tag(mContext)
                .params("lesson_id", lessonId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int status = jsonObject.optInt("status");
                            if (status == 200) {
                                DialogUtils.showDialog(mContext, "签到码", jsonObject.optString("data"));
                            } else if (status == 100) {
                                DialogUtils.showDialog(mContext, jsonObject.optString("data"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (NetworkErrorUtils.isNetworkError(response.getException()))
                            DialogUtils.showDialog(mContext, "网络异常，生成签到码失败");
                        else
                            DialogUtils.showDialog(mContext, "生成签到码失败");
                    }
                });
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
