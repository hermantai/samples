package developer.htaihm.minfirebaseapp.models;

import com.google.firebase.database.ServerValue;

import android.os.Parcel;

import java.util.HashMap;
import java.util.Map;

public abstract class Model {
    private static final String FIELD_ID = "id";
    protected static final String FIELD_UPDATED_AT = "updatedAt";

    private String mId;
    // milliseconds since Jan. 1, 1970, midnight GMT.
    private long mUpdatedAt = -1;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public long getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public abstract Map<String, Object> toMap();

    protected Map<String, Object> getBaseMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_ID, mId);
        if (mUpdatedAt == -1) {
            map.put(FIELD_UPDATED_AT, ServerValue.TIMESTAMP);
        } else {
            map.put(FIELD_UPDATED_AT, mUpdatedAt);
        }
        return map;
    }

    protected void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeLong(this.mUpdatedAt);
    }

    protected Model(Parcel in) {
        this.mId = in.readString();
        this.mUpdatedAt = in.readLong();
    }

    protected Model() {

    }
}