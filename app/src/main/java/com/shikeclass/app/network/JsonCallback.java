package com.shikeclass.app.network;

import com.google.gson.Gson;
import com.lzy.okgo.callback.AbsCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class JsonCallback<T> extends AbsCallback<T> {
    private JSONConvert jsonConvert;
    private JsonArrayConvert jsonArrayConvert;

    private Type type;
    private Class<T> clazz;


    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public T convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;

        T data = null;
        Gson gson = new Gson();

        if (type != null) {
            jsonArrayConvert = new JsonArrayConvert();
            JSONArray jsonArray = jsonArrayConvert.convertResponse(response);
            data = gson.fromJson(jsonArray.toString(), type);
        }
        if (clazz != null) {
            jsonConvert = new JSONConvert();
            JSONObject jsonObject = jsonConvert.convertResponse(response);
            data = gson.fromJson(jsonObject.toString(), clazz);
        }

        response.close();
        return data;
    }
}
