package com.example.sp346_utccnav;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    private TextView currentLocationValue;
    double latitude, longitude;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();

        currentLocationValue = findViewById(R.id.currentLocationValue);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        // Restore image resolution reduction logic
        loadSampledImages();

        // Navigation buttons
        findViewById(R.id.homeBtn).setOnClickListener(v -> {
            getLocation();
        });
        findViewById(R.id.aboutBtn).setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        findViewById(R.id.historyBtn).setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        findViewById(R.id.gmapBtn).setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));
        findViewById(R.id.settingBtn).setOnClickListener(v -> startActivity(new Intent(this, SettingActivity.class)));

        // Individual click listeners for each box
        //Each box gonna tell the navigateView to start the closest panoview to play first
        findViewById(R.id.box1).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box2).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box3).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box4).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box5).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box6).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box7).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box8).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box9).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
        });
        findViewById(R.id.box10).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewPageActivity.class));
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

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
//                this.latitude = location.getLatitude();
//                this.longitude = location.getLongitude();
                this.latitude = 13.780329795615001;
                this.longitude = 100.5604076955666;
                //13.780329795615001, 100.5604076955666

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