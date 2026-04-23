package com.example.sp346_utccnav;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public TextView currentLocationValue;
    double latitude, longitude;
    FusedLocationProviderClient fusedLocationClient;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();

        currentLocationValue = findViewById(R.id.currentLocationValue);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        loadSampledImages();

        // Navigation buttons
        findViewById(R.id.homeBtn).setOnClickListener(v -> getLocation());
        findViewById(R.id.aboutBtn).setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        findViewById(R.id.historyBtn).setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        findViewById(R.id.gmapBtn).setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));
        findViewById(R.id.settingBtn).setOnClickListener(v -> startActivity(new Intent(this, SettingActivity.class)));

        // Box click listeners
        setupBoxClickListener(R.id.box1, "ตึก 24");
        setupBoxClickListener(R.id.box2, "ตึก 7");
        setupBoxClickListener(R.id.box3, "ตึก 1");
        setupBoxClickListener(R.id.box4, "ตึก 5");
        setupBoxClickListener(R.id.box5, "ตึก 10");
        setupBoxClickListener(R.id.box6, "ตึก 15");
        setupBoxClickListener(R.id.box7, "ตึก 21");
        setupBoxClickListener(R.id.box8, "ตึก 23");
    }

    private void setupBoxClickListener(int viewId, String buildingName) {
        findViewById(viewId).setOnClickListener(v -> {
            saveHistory(buildingName);
            Intent intent = new Intent(MainActivity.this, ViewPageActivity.class);
            intent.putExtra("clickedBoxName", buildingName);
            startActivity(intent);
        });
    }

    private void saveHistory(String buildingName) {
        String startPoint = currentLocationValue.getText().toString();
        
        // Find the building details from BuildingRepository
        double targetLat = 0.0;
        double targetLong = 0.0;
        List<Building> buildings = BuildingRepository.getBuildings();
        for (Building b : buildings) {
            if (b.getName().equals(buildingName)) {
                targetLat = b.getLatitude();
                targetLong = b.getLongitude();
                break;
            }
        }

        HistoryEntry entry = new HistoryEntry(buildingName, startPoint, targetLat, targetLong);
        String json = gson.toJson(entry);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("http://192.168.1.105:8080/api/nav/history")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Silently fail or log
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response.close();
            }
        });
    }

    private void loadSampledImages() {
        int[] boxIds = {R.id.box1, R.id.box2, R.id.box3, R.id.box4, R.id.box5, R.id.box6, R.id.box7, R.id.box8};
        int[] resIds = {R.drawable.b24, R.drawable.b07, R.drawable.b01, R.drawable.b05, R.drawable.b10, R.drawable.b15, R.drawable.b21, R.drawable.b23};

        for (int i = 0; i < resIds.length; i++) {
            ImageView img = findViewById(boxIds[i]);
            if (img != null) {
                img.setImageBitmap(decodeSampledBitmapFromResource(resIds[i], 200, 200));
            }
        }
    }

    private Bitmap decodeSampledBitmapFromResource(int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getResources(), resId, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                this.latitude = 13.780329795615001;
                this.longitude = 100.5604076955666;
                calculateNear cn = new calculateNear(this.latitude, this.longitude);
                currentLocationValue.setText(cn.getBuildingName());
            } else {
                currentLocationValue.setText("Location not found");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }
}