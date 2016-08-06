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
     * The following tries to make Firebase-database to recognize the id field but it does not work
     */
    @Override
    public void setId(String id) {
        super.setId(id);
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
