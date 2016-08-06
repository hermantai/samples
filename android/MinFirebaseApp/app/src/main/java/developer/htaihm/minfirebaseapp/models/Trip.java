package developer.htaihm.minfirebaseapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import developer.htaihm.minfirebaseapp.StringUtil;

public class Trip extends Model implements Parcelable {
    private static final String FIELD_CREATOR = "creator";
    private static final String FIELD_ORIGIN = "origin";
    private static final String FIELD_DESTINATION = "destination";
    private static final String FIELD_WATCHERS  = "watchers";
    private static final String FIELD_ORIGIN_LAT = "originLat";
    private static final String FIELD_ORIGIN_LNG = "originLng";
    private static final String FIELD_DEST_LAT = "destLat";
    private static final String FIELD_DEST_LNG = "destLng";
    private static final String FIELD_IS_ACTIVE = "active";
    private static final String FIELD_IS_SENSORS_PAUSED = "isSensorsPaused";
    private static final String FIELD_IS_HEADPHONE_SENSOR_ENABLED = "isHeadphoneSensorEnabled";
    private static final String FIELD_IS_INACTIVITY_SENSOR_ENABLED = "isInactivitySensorEnabled";
    private static final String FIELD_IS_SHAKE_SENSOR_ENABLED = "isShakeSensorEnabled";
    private static final String FIELD_IS_SPEED_SENSOR_ENABLED = "isSpeedSensorEnabled";
    private static final String FIELD_SEVERITY = "severity";
    private static final String FIELD_IS_SPEED_INCREASED_SENT = "isSpeedIncreasedSent";
    private static final String FIELD_IS_HEADPHONE_UNPLUGGED_SENT = "isHeadphoneUnpluggedSent";
    private static final String FIELD_IS_CREATOR_INACTIVITY_SENT = "isCreatorInactivitySent";

    private String mCreator;
    private String mOrigin;
    private String mDestination;
    private List<String> mWatchers;
    private double mOriginLat;
    private double mOriginLng;
    private double mDestLag;
    private double mDestLng;
    private boolean mIsActive = true;
    private boolean mIsSensorsPaused;
    private boolean mIsHeadphoneSensorEnabled = true;
    private boolean mIsInactivitySensorEnabled = true;
    private boolean mIsShakeSensorEnabled = true;
    private boolean mIsSpeedSensorEnabled = true;
    private boolean mIsSpeedIncreasedSent;
    private boolean mIsHeadphoneUnpluggedSent;
    private boolean mIsCreatorInactivitySent;

    @Nullable
    public static Trip findInTrips(String tripId, List<Trip> trips) {
        Trip t = null;
        for (Trip trip : trips) {
            if (trip.getId().equals(tripId)) {
                t = trip;
            }
        }

        return t;
    }

    public void resetSensors() {
        setSpeedIncreasedSent(false);
        setHeadphoneUnpluggedSent(false);
        setCreatorInactivitySent(false);
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String creator) {
        mCreator = creator;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public List<String> getWatchers() {
        return mWatchers;
    }

    public void setWatchers(List<String> watchers) {
        List<String> cleanedWatchers = new ArrayList<>();

        for (String watcher : watchers) {
            cleanedWatchers.add(StringUtil.cleanPhoneNumber(watcher));
        }
        mWatchers = watchers;
    }

    public double getOriginLat() {
        return mOriginLat;
    }

    public void setOriginLat(double originLat) {
        mOriginLat = originLat;
    }

    public double getOriginLng() {
        return mOriginLng;
    }

    public void setOriginLng(double originLng) {
        mOriginLng = originLng;
    }

    public double getDestLag() {
        return mDestLag;
    }

    public void setDestLag(double destLag) {
        mDestLag = destLag;
    }

    public double getDestLng() {
        return mDestLng;
    }

    public void setDestLng(double destLng) {
        mDestLng = destLng;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }

    public boolean isSensorsPaused() {
        return mIsSensorsPaused;
    }

    public void setSensorsPaused(boolean sensorsPaused) {
        mIsSensorsPaused = sensorsPaused;
    }

    public boolean isHeadphoneSensorEnabled() {
        return mIsHeadphoneSensorEnabled;
    }

    public void setHeadphoneSensorEnabled(boolean headphoneSensorEnabled) {
        mIsHeadphoneSensorEnabled = headphoneSensorEnabled;
    }

    public boolean isInactivitySensorEnabled() {
        return mIsInactivitySensorEnabled;
    }

    public void setInactivitySensorEnabled(boolean inactivitySensorEnabled) {
        mIsInactivitySensorEnabled = inactivitySensorEnabled;
    }

    public boolean isShakeSensorEnabled() {
        return mIsShakeSensorEnabled;
    }

    public void setShakeSensorEnabled(boolean shakeSensorEnabled) {
        mIsShakeSensorEnabled = shakeSensorEnabled;
    }

    public boolean isSpeedSensorEnabled() {
        return mIsSpeedSensorEnabled;
    }

    public void setSpeedSensorEnabled(boolean speedSensorEnabled) {
        mIsSpeedSensorEnabled = speedSensorEnabled;
    }

    public boolean isSpeedIncreasedSent() {
        return mIsSpeedIncreasedSent;
    }

    public void setSpeedIncreasedSent(boolean speedIncreasedSent) {
        mIsSpeedIncreasedSent = speedIncreasedSent;
    }

    public boolean isHeadphoneUnpluggedSent() {
        return mIsHeadphoneUnpluggedSent;
    }

    public void setHeadphoneUnpluggedSent(boolean headphoneUnpluggedSent) {
        mIsHeadphoneUnpluggedSent = headphoneUnpluggedSent;
    }

    public boolean isCreatorInactivitySent() {
        return mIsCreatorInactivitySent;
    }

    public void setCreatorInactivitySent(boolean creatorInactivitySent) {
        mIsCreatorInactivitySent = creatorInactivitySent;
    }

    @Override
    public String toString() {
        return "TripFirebase{" +
                "mId='" + getId() + '\'' +
                ", mUpdatedAt='" + new Date(getUpdatedAt()) + '\'' +
                ", mCreator='" + mCreator + '\'' +
                ", mOrigin='" + mOrigin + '\'' +
                ", mDestination='" + mDestination + '\'' +
                ", mWatchers=" + mWatchers +
                ", mOriginLat=" + mOriginLat +
                ", mOriginLng=" + mOriginLng +
                ", mDestLag=" + mDestLag +
                ", mDestLng=" + mDestLng +
                ", mIsActive=" + mIsActive +
                ", mIsSensorsPaused=" + mIsSensorsPaused +
                ", mIsHeadphoneSensorEnabled=" + mIsHeadphoneSensorEnabled +
                ", mIsInactivitySensorEnabled=" + mIsInactivitySensorEnabled +
                ", mIsShakeSensorEnabled=" + mIsShakeSensorEnabled +
                ", mIsSpeedSensorEnabled=" + mIsSpeedSensorEnabled +
                ", mIsSpeedIncreasedSent=" + mIsSpeedIncreasedSent +
                ", mIsHeadphoneUnpluggedSent=" + mIsHeadphoneUnpluggedSent +
                ", mIsCreatorInactivitySent=" + mIsCreatorInactivitySent +
                '}';
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = getBaseMap();
        map.put(FIELD_CREATOR, mCreator);
        map.put(FIELD_ORIGIN, mOrigin);
        map.put(FIELD_DESTINATION, mDestination);
        map.put(FIELD_WATCHERS, mWatchers);
        map.put(FIELD_ORIGIN_LAT, mOriginLat);
        map.put(FIELD_ORIGIN_LNG, mOriginLng);
        map.put(FIELD_DEST_LAT, mDestLag);
        map.put(FIELD_DEST_LNG, mDestLng);
        map.put(FIELD_IS_ACTIVE, mIsActive);
        map.put(FIELD_IS_SENSORS_PAUSED, mIsSensorsPaused);
        map.put(FIELD_IS_HEADPHONE_SENSOR_ENABLED, mIsHeadphoneSensorEnabled);
        map.put(FIELD_IS_INACTIVITY_SENSOR_ENABLED, mIsInactivitySensorEnabled);
        map.put(FIELD_IS_SHAKE_SENSOR_ENABLED, mIsShakeSensorEnabled);
        map.put(FIELD_IS_SPEED_SENSOR_ENABLED, mIsSpeedSensorEnabled);
        map.put(FIELD_IS_SPEED_INCREASED_SENT, mIsSpeedIncreasedSent);
        map.put(FIELD_IS_HEADPHONE_UNPLUGGED_SENT, mIsHeadphoneUnpluggedSent);
        map.put(FIELD_IS_CREATOR_INACTIVITY_SENT, mIsCreatorInactivitySent);
        return map;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mCreator);
        dest.writeString(this.mOrigin);
        dest.writeString(this.mDestination);
        dest.writeStringList(this.mWatchers);
        dest.writeDouble(this.mOriginLat);
        dest.writeDouble(this.mOriginLng);
        dest.writeDouble(this.mDestLag);
        dest.writeDouble(this.mDestLng);
        dest.writeByte(this.mIsActive ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsSensorsPaused ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsHeadphoneSensorEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsInactivitySensorEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsShakeSensorEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsSpeedSensorEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsSpeedIncreasedSent ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsHeadphoneUnpluggedSent ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsCreatorInactivitySent ? (byte) 1 : (byte) 0);
        dest.writeString(getId());
        dest.writeLong(getUpdatedAt());
    }

    public Trip() {
    }

    protected Trip(Parcel in) {
        super(in);
        this.mCreator = in.readString();
        this.mOrigin = in.readString();
        this.mDestination = in.readString();
        this.mWatchers = in.createStringArrayList();
        this.mOriginLat = in.readDouble();
        this.mOriginLng = in.readDouble();
        this.mDestLag = in.readDouble();
        this.mDestLng = in.readDouble();
        this.mIsActive = in.readByte() != 0;
        this.mIsSensorsPaused = in.readByte() != 0;
        this.mIsHeadphoneSensorEnabled = in.readByte() != 0;
        this.mIsInactivitySensorEnabled = in.readByte() != 0;
        this.mIsShakeSensorEnabled = in.readByte() != 0;
        this.mIsSpeedSensorEnabled = in.readByte() != 0;
        this.mIsSpeedIncreasedSent = in.readByte() != 0;
        this.mIsHeadphoneUnpluggedSent = in.readByte() != 0;
        this.mIsCreatorInactivitySent = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
