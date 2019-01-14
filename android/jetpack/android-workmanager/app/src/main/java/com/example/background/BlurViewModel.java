/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.background;

import static com.example.background.Constants.KEY_IMAGE_URI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.text.TextUtils;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import com.example.background.workers.BlurWorker;
import com.example.background.workers.CleanupWorker;
import com.example.background.workers.SaveImageToFileWorker;
import java.util.List;

public class BlurViewModel extends ViewModel {

  private Uri mImageUri;
  private WorkManager mWorkManager;
  private LiveData<List<WorkInfo>> mSavedWorkInfo;
  // for the WorkInfo
  private Uri mOutputUri;

  public BlurViewModel() {
    mWorkManager = WorkManager.getInstance();

    mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(Constants.TAG_OUTPUT);
  }

  /**
   * Create the WorkRequest to apply the blur and save the resulting image
   *
   * @param blurLevel The amount to blur the image
   */
  void applyBlur(int blurLevel) {
//    WorkContinuation continuation = mWorkManager.beginWith(OneTimeWorkRequest.from(CleanupWorker.class));
    WorkContinuation continuation = mWorkManager.beginUniqueWork(Constants.IMAGE_MANIPULATION_WORK_NAME,
        ExistingWorkPolicy.REPLACE,
        OneTimeWorkRequest.from(CleanupWorker.class)
        );

    // Add WorkRequests to blur the image the number of times requested
    for (int i = 0; i < blurLevel; i++) {
      OneTimeWorkRequest.Builder blurBuilder =
          new OneTimeWorkRequest.Builder(BlurWorker.class);

      // Input the Uri if this is the first blur operation
      // After the first blur operation the input will be the output of previous
      // blur operations.
      if ( i == 0 ) {
        blurBuilder.setInputData(createInputDataForUri());
      }

      continuation = continuation.then(blurBuilder.build());
    }

    // Create charging constraint, so the save work is only done if the device is charging.
    // Another constraint to try is setRequiresStorageNotLow
    Constraints constraints = new Constraints.Builder()
        .setRequiresCharging(true)
        .build();

    // Add WorkRequest to save the image to the filesystem
    continuation = continuation.then(new OneTimeWorkRequest.Builder(SaveImageToFileWorker.class)
        .addTag(Constants.TAG_OUTPUT)
        .setConstraints(constraints)
        .build());

    // Actually start the work
    continuation.enqueue();
  }

  /**
   * Cancel work using the work's unique name
   */
  void cancelWork() {
    mWorkManager.cancelUniqueWork(Constants.IMAGE_MANIPULATION_WORK_NAME);
  }

  private Uri uriOrNull(String uriString) {
    if (!TextUtils.isEmpty(uriString)) {
      return Uri.parse(uriString);
    }
    return null;
  }

  /**
   * Setters
   */
  void setImageUri(String uri) {
    mImageUri = uriOrNull(uri);
  }

  /**
   * Getters
   */
  Uri getImageUri() {
    return mImageUri;
  }

  LiveData<List<WorkInfo>> getOutputWorkInfo() { return mSavedWorkInfo; }

  void setOutputUri(String outputImageUri) {
    mOutputUri = uriOrNull(outputImageUri);
  }

  Uri getOutputUri() { return mOutputUri; }

  private Data createInputDataForUri() {
    Data.Builder builder = new Data.Builder();
    if (mImageUri != null) {
      builder.putString(KEY_IMAGE_URI, mImageUri.toString());
    }

    return builder.build();
  }

}