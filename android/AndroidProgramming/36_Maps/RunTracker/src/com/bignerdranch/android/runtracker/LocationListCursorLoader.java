package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.database.Cursor;

public class LocationListCursorLoader extends SQLiteCursorLoader {
    private long mRunId;
    
    public LocationListCursorLoader(Context c, long runId) {
        super(c);
        mRunId = runId;
    }

    @Override
    protected Cursor loadCursor() {
        return RunManager.get(getContext()).queryLocationsForRun(mRunId);
    }
}