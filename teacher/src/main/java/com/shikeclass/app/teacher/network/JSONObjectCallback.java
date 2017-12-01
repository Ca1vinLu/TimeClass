package com.shikeclass.app.teacher.network;

import com.blankj.utilcode.util.LogUtils;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.Converter;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class JSONObjectCallback extends AbsCallback<JSONObject> {
    private JSONObjectConvert convert;

    public JSONObjectCallback() {
        this.convert = new JSONObjectConvert();
    }

    @Override
    public JSONObject convertResponse(Response response) throws Throwable {

        JSONObject jsonObject = convert.convertResponse(response);
        response.close();
        return jsonObject;
    }

    class JSONObjectConvert implements Converter<JSONObject> {

        @Override
        public JSONObject convertResponse(Response response) throws Throwable {
            ResponseBody body = response.body();
            if (body == null) return null;
            JSONObject jsonObject = new JSONObject(body.string());
            int code1;
            try {
                code1 = jsonObject.getInt("status");
            } catch (JSONException e) {
                code1 = -1;
            }
            if (code1 != 200) {
                response.close();
                throw new IllegalStateException(String.valueOf(code1), new Throwable(jsonObject.optString("msg")));
            }
            response.close();
            LogUtils.d("json", jsonObject.toString());
            if (!jsonObject.optString("result").equals(""))
                return new JSONObject(jsonObject.optString("result"));
            else return new JSONObject();
        }
    }
}
