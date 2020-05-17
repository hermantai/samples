package com.gmail.htaihm.myplaygroundinjava.focusjumpwithdpad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gmail.htaihm.myplaygroundinjava.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Demo of how to move the focus from edit text to the flags RecyclerView below, using a D-pad only.
 */
public class FocusJumpWithDpadActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_focus_jump_with_dpad);

    List<String> flags = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      flags.add("flag " + i);
    }
    RecyclerView rv = findViewById(R.id.rvFlagsContainer);
    rv.setLayoutManager(new LinearLayoutManager(this));
    rv.setAdapter(new FlagsAdapter(this, flags));

    // This captures the actionDone event and request focus on the flags container, so that the
    // focus is jumped from the edit text to the flags container.
    EditText etSearch = findViewById(R.id.etSearch);
    etSearch.setOnEditorActionListener(
        new OnEditorActionListener() {
          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
              InputMethodManager imm =
                  (InputMethodManager)
                      FocusJumpWithDpadActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
              rv.requestFocus();
            }
            return false;
          }
        });
  }

  public static Intent createIntent(Context context) {
    return new Intent(context, FocusJumpWithDpadActivity.class);
  }

  static class FlagsAdapter extends RecyclerView.Adapter<FlagsViewHolder> {

    private final Context context;
    private final List<String> flags = new ArrayList<>();

    FlagsAdapter(Context context, List<String> items) {
      this.context = context;
      flags.addAll(items);
    }

    @NonNull
    @Override
    public FlagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      TextView tv = new TextView(context);
      tv.setOnClickListener(v -> {
        Toast.makeText(context, tv.getText(),Toast.LENGTH_SHORT).show();
      });
      return new FlagsViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull FlagsViewHolder holder, int position) {
      holder.bindView(flags.get(position));
    }

    @Override
    public int getItemCount() {
      return flags.size();
    }
  }

  static class FlagsViewHolder extends RecyclerView.ViewHolder {

    public FlagsViewHolder(@NonNull View itemView) {
      super(itemView);
    }

    public void bindView(String text) {
      TextView tv =(TextView) itemView;
      tv.setText(text);
    }
  }
}
