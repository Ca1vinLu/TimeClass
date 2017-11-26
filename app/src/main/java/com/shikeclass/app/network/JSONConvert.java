package com.shikeclass.app.network;

import com.blankj.utilcode.util.LogUtils;
import com.lzy.okgo.convert.Converter;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public class JSONConvert implements Converter<JSONObject> {
    @Override
    public JSONObject convertResponse(Response response) throws Throwable {
        if (response.body() != null) {
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            int code1;
            try {
                code1 = jsonObject.getInt("status");
            } catch (JSONException e) {
                code1 = -1;
            }
            if (code1 != 200)
                throw new IllegalStateException(String.valueOf(code1), new Throwable(jsonObject.optString("msg")));

            LogUtils.d("json", jsonObject.toString());
            if (!jsonObject.optString("result").equals(""))
                return new JSONObject(jsonObject.optString("result"));
            else return new JSONObject();
        } else return null;
    }
}
