package developer.htaihm.minfirebaseapp.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Data Access Model for Firebase.
 */
public abstract class Dao<M extends Model> {
    protected final DatabaseReference mDatabaseReference;
    protected final String mModelPath;
    private final Class<M> mModelClass;

    public Dao(String modelPath, DatabaseReference databaseReference) {
        mModelPath = modelPath;
        mDatabaseReference = databaseReference;

        Type type = getClass().getGenericSuperclass();

        while (!(type instanceof ParameterizedType)
                || ((ParameterizedType) type).getRawType() != Dao.class) {
            if (type instanceof ParameterizedType) {
                type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
            } else {
                type = ((Class<?>) type).getGenericSuperclass();
            }
        }

        mModelClass = (Class<M>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    public void upsert(M model, @Nullable final CompletionListener listener) {
        model.setUpdatedAt(new Date().getTime());

        Map<String, Object> modelMap = model.toMap();
        modelMap.put(Model.FIELD_UPDATED_AT, ServerValue.TIMESTAMP);
        mDatabaseReference
                .child(mModelPath)
                .child(model.getId())
                .updateChildren(modelMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (listener != null) {
                            listener.OnCompletion(databaseError);
                        }
                    }
                });
    }

    public static String getPath(String... keys) {
        return TextUtils.join("/", keys);
    }

    protected void fetch(
            Query query,
            final QueryDataFetchedListener<M> queryDataFetchedListener) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<M> models = new ArrayList<>();
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        M m = ds.getValue(mModelClass);
                        m.setId(ds.getKey());
                        models.add(m);
                    }
                }
                queryDataFetchedListener.onDataFetched(models);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                queryDataFetchedListener.onError(databaseError);
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    public void fetch(final String key, final DataFetchedListener<M> dataFetchedListener) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                M model = null;
                if (dataSnapshot != null) {
                    model = dataSnapshot.getValue(mModelClass);
                    model.setId(key);
                }
                dataFetchedListener.onDataFetched(key, model);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dataFetchedListener.onError(key, databaseError);
            }
        };
        mDatabaseReference
                .child(mModelPath)
                .child(key)
                .addListenerForSingleValueEvent(valueEventListener);
    }

    public void insert(M model, @Nullable  final CompletionListener listener) {
        DatabaseReference ref = mDatabaseReference.child(mModelPath).push();
        model.setId(ref.getKey());
        Map<String, Object> modelMap = model.toMap();
        modelMap.put(Model.FIELD_UPDATED_AT, ServerValue.TIMESTAMP);

        ref.setValue(modelMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (listener != null) {
                    listener.OnCompletion(databaseError);
                }
            }
        });
    }

    public void delete(M model, @Nullable  final CompletionListener listener) {
        mDatabaseReference.child(mModelPath).child(model.getId()).removeValue(
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (listener != null) {
                            listener.OnCompletion(databaseError);
                        }
                    }
                });
    }

    public interface DataFetchedListener<M> {
        // model is null if it does not exist with the given key
        void onDataFetched(String key, @Nullable M model);
        void onError(String key, DatabaseError error);
    }

    public interface QueryDataFetchedListener<M> {
        void onDataFetched(List<M> models);
        void onError(DatabaseError error);
    }

    public interface CompletionListener {
        void OnCompletion(@Nullable DatabaseError databaseError);
    }
}