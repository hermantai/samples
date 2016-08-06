package developer.htaihm.minfirebaseapp.models;

import java.util.HashMap;
import java.util.Map;

public class Profile extends Model {
    private static final String FIELD_PHONE_NUMBER = "phoneNumber";
    private static final String FIELD_APP_DOWNLOADED = "appDownloaded";
    private static final String FIELD_DEVICE_NAME = "deviceName";
    private static final String FIELD_BUILD_VERSION = "buildVersion";
    private static final String FIELD_TRIPS = "trips";

    private String mPhoneNumber;
    private boolean mAppDownloaded;
    private String mDeviceName;
    private String mBuildVersion;
    // Trips participated by this user.
    // Key: tripId; value: is creator or not
    private Map<String, Boolean> mTrips = new HashMap<>();

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public boolean isAppDownloaded() {
        return mAppDownloaded;
    }

    public void setAppDownloaded(boolean appDownloaded) {
        mAppDownloaded = appDownloaded;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public String getBuildVersion() {
        return mBuildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        mBuildVersion = buildVersion;
    }

    public Map<String, Boolean> getTrips() {
        return mTrips;
    }

    public void setTrips(Map<String, Boolean> trips) {
        mTrips = trips;
    }

    public void addTrip(String tripId, boolean isCreator) {
        mTrips.put(tripId, isCreator);
    }

    public void removeTrip(String tripId) {
        mTrips.remove(tripId);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = getBaseMap();
        map.put(FIELD_PHONE_NUMBER, mPhoneNumber);
        map.put(FIELD_APP_DOWNLOADED, mAppDownloaded);
        map.put(FIELD_DEVICE_NAME, mDeviceName);
        map.put(FIELD_BUILD_VERSION, mBuildVersion);
        // trips inside Profile are not managed by Profile, see {@link TripDao}.
        // If we set FIELD_TRIPS here, Dao.upsert may override the profile to trips mapping
        // by mistake.
        // map.put(FIELD_TRIPS, mTrips);

        return map;
    }
}