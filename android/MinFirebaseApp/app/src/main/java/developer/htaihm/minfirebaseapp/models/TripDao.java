package developer.htaihm.minfirebaseapp.models;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TripDao extends Dao<Trip> {
    private static final String TAG = "TripDao";
    private final ProfileDao mProfileDao;

    public TripDao(DatabaseReference databaseReference, ProfileDao profileDao) {
        super("trips", databaseReference);
        mProfileDao = profileDao;
    }

    public static TripDao getTripDao() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        ProfileDao profileDao = new ProfileDao(dbRef);
        return new TripDao(dbRef, profileDao);
    }

    public void findTripsByCreator(
            String phoneNumber,
            QueryDataFetchedListener<Trip> listener) {
        Query query  = mDatabaseReference.child(mModelPath)
                .orderByChild("creator")
                .equalTo(phoneNumber);
        Log.d(TAG, "before fetch");
        fetch(query, listener);
        Log.d(TAG, "after fetch");
    }

    public void findActiveTripsByCreator(
            String phoneNumber,
            final QueryDataFetchedListener<Trip> listener) {
        // We cannot filter by two fields, so filtered by creator, then check for isActive
        // manually.
        findTripsByCreator(phoneNumber, new QueryDataFetchedListener<Trip>() {
            @Override
            public void onDataFetched(List<Trip> trips) {
                List<Trip> activeTrips = new ArrayList<>();
                for (Trip trip : trips) {
                    if (trip.isActive()) {
                        activeTrips.add(trip);
                    }
                }
                listener.onDataFetched(activeTrips);
            }

            @Override
            public void onError(DatabaseError error) {
                listener.onError(error);
            }
        });
    }

    @Override
    public void insert(Trip trip, final CompletionListener listener) {
        DatabaseReference ref = mDatabaseReference.child(mModelPath).push();
        trip.setId(ref.getKey());

        update(trip, listener);
    }

    private void update(Trip trip, @Nullable final CompletionListener listener) {
        Map<String, Object> updateChildren = new HashMap<>();
        Map<String, Object> tripMap = trip.toMap();
        tripMap.put(Model.FIELD_UPDATED_AT, ServerValue.TIMESTAMP);
        updateChildren.put(mModelPath + '/' + trip.getId(), tripMap);
        updateChildren.put(
                getPath(mProfileDao.mModelPath, trip.getCreator(), "trips", trip.getId()),
                true);
        for (String watcher : trip.getWatchers()) {
            updateChildren.put(
                    getPath(mProfileDao.mModelPath, watcher, "trips", trip.getId()),
                    false);
        }

        mDatabaseReference.updateChildren(updateChildren, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (listener != null) {
                    listener.OnCompletion(databaseError);
                }
            }
        });
    }

    @Override
    public void upsert(Trip trip, @Nullable CompletionListener listener) {
        update(trip, listener);
    }

    public void findTripsParticipatedBy(
            final String phoneNumber, final QueryDataFetchedListener<Trip> listener) {
        // TODO(htaihm@gmail.com): I don't know a better way to fetch all Trip objects which
        // are participated by phoneNumber, so this is super ugly.
        mProfileDao.fetch(phoneNumber, new DataFetchedListener<Profile>() {
            @Override
            public void onDataFetched(String key, @Nullable Profile profile) {
                final List<Trip> tripsFetched = new ArrayList<>();
                if (profile == null) {
                    listener.onDataFetched(tripsFetched);
                    return;
                }

                final int numOfTrips = profile.getTrips().size();
                Log.i(TAG, "Number of trips found: " + numOfTrips);
                final AtomicInteger numOfTripsFetched = new AtomicInteger(numOfTrips);

                for (String tripId : profile.getTrips().keySet()) {
                    fetch(
                            tripId,
                            new DataFetchedListener<Trip>() {
                                @Override
                                public void onDataFetched(String key, @Nullable Trip model) {
                                    synchronized (tripsFetched) {
                                        tripsFetched.add(model);
                                        Log.i(TAG, "one trip fetched");
                                        numOfTripsFetched.decrementAndGet();
                                        oneTripDone();
                                    }
                                }

                                @Override
                                public void onError(String key, DatabaseError error) {
                                    synchronized (tripsFetched) {
                                        Log.i(TAG, "one trip fetched with error");
                                        numOfTripsFetched.decrementAndGet();
                                        listener.onError(error);
                                        oneTripDone();
                                    }
                                }

                                private void oneTripDone() {
                                    Log.d(TAG, "Number of trips fetched: " + numOfTripsFetched.get());
                                    synchronized (tripsFetched) {
                                        if (numOfTripsFetched.get() != 0) {
                                            return;
                                        }
                                    }

                                    if (tripsFetched.size() == numOfTrips) {
                                        listener.onDataFetched(tripsFetched);
                                    } else {
                                        Log.e(TAG, String.format("Error findTripsParticipatedBy('%s')", phoneNumber));
                                    }
                                }
                            }
                    );
                }
            }

            @Override
            public void onError(String key, DatabaseError error) {
                listener.onError(error);
            }
        });
    }
}
