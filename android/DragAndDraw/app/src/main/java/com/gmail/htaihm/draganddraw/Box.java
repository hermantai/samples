package com.gmail.htaihm.draganddraw;

import android.graphics.PointF;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Box implements Serializable {
    private transient PointF mOrigin;
    private transient PointF mCurrent;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeFloat(mOrigin.x);
        out.writeFloat(mOrigin.y);
        out.writeFloat(mCurrent.x);
        out.writeFloat(mCurrent.x);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        float x = in.readFloat();
        float y = in.readFloat();
        mOrigin = new PointF(x, y);

        x = in.readFloat();
        y = in.readFloat();
        mCurrent = new PointF(x, y);
    }
}
