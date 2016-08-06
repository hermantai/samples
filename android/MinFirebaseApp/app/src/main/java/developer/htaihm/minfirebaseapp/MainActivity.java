package developer.htaihm.minfirebaseapp;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import developer.htaihm.minfirebaseapp.models.Dao;
import developer.htaihm.minfirebaseapp.models.Employee;
import developer.htaihm.minfirebaseapp.models.Trip;
import developer.htaihm.minfirebaseapp.models.TripDao;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView displayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayTextView = (TextView) findViewById(R.id.display_text_view);
    }

    public void sayHello(View view) {
        Toast.makeText(this, "Hello, it's now " + new Date(), Toast.LENGTH_LONG).show();
    }

    public void login(View view) {
        FirebaseUtil.login(this, new FirebaseUtil.OnAuthCompleteListener(){
            @Override
            public void onCompleteSuccessfully(FirebaseUser user) {
                UiUtil.logAndToast(
                        MainActivity.this,
                        TAG,
                        "Login successfully: " + FirebaseUtil.userString(user));
            }
        });
    }

    public void create(View view) {
        List<String> watchers1 = new ArrayList<>();
        watchers1.add("111");
        watchers1.add("222");

        Trip trip1 = new Trip();
        trip1.setCreator("1234567");
        trip1.setWatchers(watchers1);

        Trip trip2 = new Trip();
        trip2.setCreator("111");
        List<String> watchers2 = new ArrayList<>();
        watchers2.add("1234567");
        watchers2.add("222");
        trip2.setWatchers(watchers2);

        TripDao tripDao = TripDao.getTripDao();
        tripDao.insert(trip1, new Dao.CompletionListener() {
            @Override
            public void OnCompletion(@Nullable DatabaseError databaseError) {
                UiUtil.logAndToast(
                        MainActivity.this,
                        TAG,
                        "Trip 1 saved!"
                );
            }
        });
        tripDao.insert(trip2, new Dao.CompletionListener() {
            @Override
            public void OnCompletion(@Nullable DatabaseError databaseError) {
                UiUtil.logAndToast(
                        MainActivity.this,
                        TAG,
                        "Trip 2 saved!"
                );
            }
        });
    }

    public void findTripsByCreator(View view) {
        TripDao tripDao = TripDao.getTripDao();
        tripDao.findTripsByCreator("1234567", new Dao.QueryDataFetchedListener<Trip>() {
            @Override
            public void onDataFetched(List<Trip> trips) {
                UiUtil.logAndToast(
                        MainActivity.this,
                        TAG,
                        "Successfully fetched " + trips.size() + " trips"
                );

                StringBuilder sb = new StringBuilder("findTripsByCreator result");
                for (Trip trip : trips) {
                    sb.append(trip.toString());
                    sb.append("\n\n");
                }
                displayTextView.setText(sb);
            }

            @Override
            public void onError(DatabaseError error) {
                ErrorHandlingUtil.logErrorAndToast(
                        MainActivity.this,
                        TAG,
                        "findTripsByCreator error: " + error,
                        error.toException()
                );
            }
        });
    }

    public void findTripsParticipatedBy(View view) {
        TripDao tripDao = TripDao.getTripDao();
        tripDao.findTripsParticipatedBy("1234567", new Dao.QueryDataFetchedListener<Trip>() {
            @Override
            public void onDataFetched(List<Trip> trips) {
                UiUtil.logAndToast(
                        MainActivity.this,
                        TAG,
                        "Successfully fetched " + trips.size() + " trips"
                );

                StringBuilder sb = new StringBuilder("findTripsParticipatedBy result");
                for (Trip trip : trips) {
                    sb.append(trip.toString());
                    sb.append("\n\n");
                }
                displayTextView.setText(sb);
            }

            @Override
            public void onError(DatabaseError error) {
                ErrorHandlingUtil.logErrorAndToast(
                        MainActivity.this,
                        TAG,
                        "findTripsParticipatedBy error: " + error,
                        error.toException()
                );
            }
        });
    }

    public void createEmployee(final View view) {
        final Employee employee = new Employee();
        employee.setName("peter");
        employee.setId("123");

        Map<String, Object> modelMap = employee.toMap();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef
                .child("employees")
                .child(employee.getId())
                .updateChildren(modelMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Snackbar.make(
                                    view.getRootView(),
                                    "Error creating employee: " + databaseError,
                                    Snackbar.LENGTH_INDEFINITE).show();
                            return;
                        }
                        Snackbar.make(
                                view.getRootView(),
                                "Employee created: " + employee,
                                Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    public void showEmployee(final View view) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("employees").child("123").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    Snackbar.make(
                            view.getRootView(),
                            "No employees found",
                            Snackbar.LENGTH_LONG).show();
                }

                Employee e = dataSnapshot.getValue(Employee.class);
                Snackbar.make(
                        view.getRootView(),
                        "Employee found: " + e,
                        Snackbar.LENGTH_INDEFINITE).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(
                        view.getRootView(),
                        "Error getting employee: " + databaseError,
                        Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }
}
