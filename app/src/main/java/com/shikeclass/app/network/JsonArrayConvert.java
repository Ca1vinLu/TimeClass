package com.shikeclass.app.network;

import com.blankj.utilcode.util.LogUtils;
import com.lzy.okgo.convert.Converter;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Response;

public class JsonArrayConvert implements Converter<JSONArray> {
    @Override
    public JSONArray convertResponse(Response response) throws Throwable {
        if (response.body() != null) {
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            int code1;
            try {
                code1 = jsonObject.getInt("status");
            } catch (Exception e) {
                code1 = -1;
            }
            if (code1 != 200)
                throw new IllegalStateException(String.valueOf(code1), new Throwable(jsonObject.optString("data")));

            LogUtils.d("json", jsonObject.toString());
            if (!jsonObject.optString("data").equals(""))
                return new JSONArray(jsonObject.optString("data"));
            else return new JSONArray();
        } else return null;
    }
}
