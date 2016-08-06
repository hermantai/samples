package developer.htaihm.minfirebaseapp.models;

import com.google.firebase.database.DatabaseReference;

public class ProfileDao extends Dao<Profile> {
    public ProfileDao(DatabaseReference databaseReference) {
        super("profiles", databaseReference);
    }
}