package com.gmail.htaihm.myplaygroundinjava.colorchanger;

import android.os.Handler;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A sample ViewModel that provides LiveData of the ViewModel. The loadFruits method is
 * to simulate the mutation of the data by an external source, e.g. loaded from a database, set
 * by a user, etc.
 */
public class FruitViewModel extends ViewModel {
  private String TAG = FruitViewModel.class.getSimpleName();

  private MutableLiveData<List<String>> fruitList;

  LiveData<List<String>> getFruitList() {
    if (fruitList == null) {
      fruitList = new MutableLiveData<>();
      loadFruits();
    }
    return fruitList;
  }

  void loadFruits() {
    // do async operation to fetch users
    Handler myHandler = new Handler();
    myHandler.postDelayed(() -> {
      List<String> fruitsStringList = new ArrayList<>();
      fruitsStringList.add("Mango");
      fruitsStringList.add("Apple");
      fruitsStringList.add("Orange");
      fruitsStringList.add("Banana");
      fruitsStringList.add("Grapes");
      long seed = System.nanoTime();
      Collections.shuffle(fruitsStringList, new Random(seed));

      fruitList.setValue(fruitsStringList);
      Log.i("FruitViewModel", "Fruits delivered");
    }, 5000);

  }

  @Override
  protected void onCleared() {
    super.onCleared();
    Log.d(TAG, "on cleared called");
  }
}
