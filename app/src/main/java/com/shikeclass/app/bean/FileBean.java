package com.shikeclass.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LYZ on 2018/3/27 0027.
 */

public class FileBean implements Parcelable {

    public int file_id;
    public String file_type;
    public String file_name;
    public int file_size;
    public String lesson_id;
    public String lesson_name;
    public long load_time;
    public String file_link;

    protected FileBean(Parcel in) {
        file_id = in.readInt();
        file_type = in.readString();
        file_name = in.readString();
        file_size = in.readInt();
        lesson_id = in.readString();
        lesson_name = in.readString();
        load_time = in.readLong();
        file_link = in.readString();
    }

    public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel in) {
            return new FileBean(in);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(file_id);
        dest.writeString(file_type);
        dest.writeString(file_name);
        dest.writeInt(file_size);
        dest.writeString(lesson_id);
        dest.writeString(lesson_name);
        dest.writeLong(load_time);
        dest.writeString(file_link);
    }
}
