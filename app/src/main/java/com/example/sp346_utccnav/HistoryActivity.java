package com.example.sp346_utccnav;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        hideSystemUI();

        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fetchHistory();

        // Clear History Button logic
        if (findViewById(R.id.clearHistoryBtn) != null) {
            findViewById(R.id.clearHistoryBtn).setOnClickListener(v -> showClearConfirmationDialog());
        }

        findViewById(R.id.homeBtn).setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        findViewById(R.id.aboutBtn).setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        findViewById(R.id.gmapBtn).setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));
        findViewById(R.id.settingBtn).setOnClickListener(v -> startActivity(new Intent(this, SettingActivity.class)));
    }

    private void showClearConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Clear History")
                .setMessage("Do you really want to clear all?")
                .setPositiveButton("Yes", (dialog, which) -> clearAllHistory())
                .setNegativeButton("No", null)
                .show();
    }

    private void clearAllHistory() {
        Request request = new Request.Builder()
                .url(NetworkConfig.BASE_URL + "delete-all")
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(HistoryActivity.this, "Failed to clear history", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        adapter.setHistoryList(new ArrayList<>());
                        Toast.makeText(HistoryActivity.this, "History cleared", Toast.LENGTH_SHORT).show();
                    });
                }
                response.close();
            }
        });
    }

    private void fetchHistory() {
        Request request = new Request.Builder()
                .url(NetworkConfig.BASE_URL + "history")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(HistoryActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    Type listType = new TypeToken<ArrayList<HistoryEntry>>(){}.getType();
                    List<HistoryEntry> historyList = gson.fromJson(json, listType);

                    runOnUiThread(() -> adapter.setHistoryList(historyList));
                }
                response.close();
            }
        });
    }

    private void hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }
}