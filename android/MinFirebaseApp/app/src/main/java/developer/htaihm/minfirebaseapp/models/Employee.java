package developer.htaihm.minfirebaseapp.models;

import java.util.Date;
import java.util.Map;

public class Employee extends Model {
    private String FIELD_NAME = "name";
    private String mName;

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    /**
     * This is for making Firebase-database to recognize the parent class's field.
     */
    @Override
    public void setId(String id) {
        super.setId(id);
    }

    /**
     * This is for making Firebase-database to recognize the parent class's field.
     */
    @Override
    public String getId() {
        return super.getId();
    }

    /**
     * This is for making Firebase-database to recognize the parent class's field.
     */
    @Override
    public void setUpdatedAt(long updatedAt) {
        super.setUpdatedAt(updatedAt);
    }

    /**
     * This is for making Firebase-database to recognize the parent class's field.
     */
    @Override
    public long getUpdatedAt() {
        return super.getUpdatedAt();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = getBaseMap();
        map.put(FIELD_NAME, mName);

        return map;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "mId='" + getId() + '\'' +
                ", mUpdatedAt='" + new Date(getUpdatedAt()) + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }
}
