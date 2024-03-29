package com.example.calendarapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Event implements Parcelable {
    public Date eventDate;
    public String name;
    public Date startTime;
    public Integer type;
    public String info;
    public String id;
    public String imageUrl;

    public Event() {
    }

    public Event(String name) {
        this.name = name;
    }

    protected Event(Parcel in) {
        eventDate = new Date(in.readLong());
        name = in.readString();
        startTime = new Date(in.readLong());
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readInt();
        }
        info = in.readString();
        id = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(eventDate != null ? eventDate.getTime() : 0L);
        dest.writeString(name);
        dest.writeLong(startTime != null ? startTime.getTime() : 0L);
        if (type == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(type);
        }
        dest.writeString(info);
        dest.writeString(id);
    }

}

