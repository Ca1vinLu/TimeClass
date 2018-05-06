package com.shikeclass.app.utils;

import android.content.Context;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.shikeclass.app.network.JSONObjectCallback;

import org.json.JSONObject;

/**
 * Created by LYZ on 2018/3/29 0029.
 */
public class ChangeStatusUtils {

    public static void changeStatus(Context context, String lessonId, int status) {
        Toast.makeText(context, "您已进入暂离状态", Toast.LENGTH_SHORT).show();
        OkGo.<JSONObject>post(UrlUtils.changeStatus)
                .tag(context)
                .params("student_id", SharedPreUtil.getStringValue(context, CommonValue.SHA_STU_ID, ""))
                .params("lesson_id", lessonId)
                .params("exist", status)
                .execute(new JSONObjectCallback() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {

                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                    }
                });
    }
}
