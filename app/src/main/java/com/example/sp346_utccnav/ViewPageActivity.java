package com.example.sp346_utccnav;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class ViewPageActivity extends AppCompatActivity {
    
    private int panoImg; 
    private LinearLayout container;
    private int currentIndex = 0; 
    private List<Building> panoList; 
    private HorizontalScrollView scroll;
    private TextView pixelIndicator;
    private TextView imageNameIndicator;
    private int startIdx = 0;
    private String currentLocationValue;
    double latitude, longitude;
    double npLat, npLng;
    double targetLat = 0;
    double targetLng = 0;

    private FusedLocationProviderClient fusedLocationClient;
    String[] pathingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        hideSystemUI();

        scroll = findViewById(R.id.panoramaScroll);
        container = findViewById(R.id.panoramaContainer);
        pixelIndicator = findViewById(R.id.pixelIndicator);
        imageNameIndicator = findViewById(R.id.imageNameIndicator);
        
        panoList = BuildingRepository.getPanolocate();

        panoImg = panoList.get(currentIndex).getImageResourceId();
        displayImg();
        displayPixel();

        scroll.post(() -> {
            if (pixelIndicator.getText().toString().equals("Pixel: 0")) {
                setCenter();
            }
        });

        findViewById(R.id.prevBtn).setOnClickListener(v -> prevButton());
        findViewById(R.id.nextBtn).setOnClickListener(v -> nextButton());

        findViewById(R.id.cBtn).setOnClickListener(v -> {
            setCenter();
        });

        findViewById(R.id.homeBtn).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        final ImageView referenceImage = (ImageView) container.getChildAt(1);

        scroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            
            int scrollX = scroll.getScrollX();
            int imageWidth = referenceImage.getWidth();
            if (imageWidth > 0) {
                if (scrollX >= imageWidth * 2) {
                    scroll.setScrollX(scrollX - imageWidth);
                } else if (scrollX <= 0) {
                    scroll.setScrollX(scrollX + imageWidth);
                }
                displayPixel();
            }
        });
        startImage();
        navigateSystem();
    }

    private void prevButton() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = panoList.size() - 1;
        }
        panoImg = panoList.get(currentIndex).getImageResourceId();
        imageNameIndicator.setText(panoList.get(currentIndex).getName());
        displayImg();
    }

    private void nextButton() {
        currentIndex++;
        if (currentIndex >= panoList.size()) {
            currentIndex = 0;
        }
        panoImg = panoList.get(currentIndex).getImageResourceId();
        imageNameIndicator.setText(panoList.get(currentIndex).getName());
        displayImg();
    }

    public void setCenter(){
        List<Building> panolocate = BuildingRepository.getPanolocate();
        if (currentIndex >= 0 && currentIndex < panolocate.size()) {
            Integer startPixel = panolocate.get(currentIndex).getStartPixel();
            if (startPixel != null) {
                // Add scroll.post to ensure the layout is ready before calculating
                scroll.post(() -> {
                    final ImageView referenceImage = (ImageView) container.getChildAt(1);
                    float screenImgWidth = referenceImage.getWidth();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(getResources(), panoImg, options);
                    float originalFileWidth = options.outWidth;

                    if (screenImgWidth > 0) {
                        float targetXOnScreen = (startPixel / originalFileWidth) * screenImgWidth;
                        float screenWidth = scroll.getWidth();
                        int finalScrollX = (int) (targetXOnScreen + screenImgWidth - (screenWidth / 2f));
                        scroll.setScrollX(finalScrollX);
                    }
                });
            }
        }
    }
    public void navigateSystem() {
        // 1. INITIAL CHECK: Ensure we have coordinates
        if (this.latitude == 0 || this.longitude == 0) {
            getLocation();
        }

        findViewById(R.id.forwardArrow).setOnClickListener(v -> {
            // Simple visual feedback
            Toast.makeText(this, "forward", Toast.LENGTH_SHORT).show();

            // --- STEP 3: SET TARGET ---
            double targetLat = 13.779513125254471;
            double targetLng = 100.56108830609556;

            // --- STEP 4: GET CURRENT PANO COORDINATES ---
            List<Building> panoList = BuildingRepository.getPanolocate();

            double currentPanolat = panoList.get(currentIndex).getLatitude();
            double currentPanolng = panoList.get(currentIndex).getLongitude();

            // Logic Variables
            int bestIndex = -1;
            float minTargetDist = Float.MAX_VALUE;

            // --- STEP 5: ITERATE AND COMPARE ---
            for (int i = 0; i < panoList.size(); i++) {
                // Pass if index is equal to currentIndex
                if (i == currentIndex) continue;

                // Get coordinates for point 'i'
                double panoLat = panoList.get(i).getLatitude();
                double panoLng = panoList.get(i).getLongitude();

                // A. Calculate distance from YOU to this point (The "Step" check)
                float[] resultsToMe = new float[1];
                android.location.Location.distanceBetween(
                        currentPanolat, currentPanolng,
                        panoLat, panoLng,
                        resultsToMe);
                float distToMe = resultsToMe[0];

                // B. Calculate distance from THIS POINT to the Target
                float[] resultsToTarget = new float[1];
                android.location.Location.distanceBetween(
                        panoLat, panoLng,
                        targetLat, targetLng,
                        resultsToTarget);
                float distToTarget = resultsToTarget[0];

                // --- THE SELECTION RULE ---
                // Is it nearby (within 35m)?
                if (distToMe < 35.0f) {
                    // Out of all nearby points, pick the one that gets you closest to the target
                    if (distToTarget < minTargetDist) {
                        minTargetDist = distToTarget;
                        bestIndex = i;
                    }
                }
            }

            // --- STEP 6: UPDATE AND DISPLAY ---
            if (bestIndex != -1) {
                // Update the master index
                this.currentIndex = bestIndex;

                // Sync activity variables with the new chosen point
                this.latitude = panoList.get(currentIndex).getLatitude();
                this.longitude = panoList.get(currentIndex).getLongitude();
                this.panoImg = panoList.get(currentIndex).getImageResourceId();

                // Update UI text indicator
                if (imageNameIndicator != null) {
                    imageNameIndicator.setText(panoList.get(currentIndex).getName());
                }

                // Refresh the view
                displayImg();
            } else {
                Toast.makeText(this, "No path forward found", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
                calculateNear cn = new calculateNear(this.latitude, this.longitude);
                this.currentLocationValue = cn.getBuildingName();
            }
        });
    }

    private void startImage() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                double lat = 13.780329795615001;
                double lng = 100.5604076955666;
                calculateNear cn = new calculateNear(lat, lng);
                this.currentLocationValue = cn.getBuildingName();
                startIdx = 0;
                float minStartDist = Float.MAX_VALUE;
                for (int i = 0; i < panoList.size(); i++) {
                    float[] results = new float[1];
                    android.location.Location.distanceBetween(
                            lat, lng,
                            panoList.get(i).getLatitude(),
                            panoList.get(i).getLongitude(),
                            results);

                    if (results[0] < minStartDist) {
                        minStartDist = results[0];
                        startIdx = i;
                    }
                }
                while (currentIndex != startIdx) { nextButton(); }

            }
        });
    }
//    public void navigateSystem() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            return;
//        }
//        String targetBox = getIntent().getStringExtra("clickedBoxName");
//        List<Building> allBuildings = BuildingRepository.getBuildings();
//        Building destinationBuilding = null;
//        double disLat = 0;
//        double disLng = 0;
//        final double targetLat = disLat;
//        final double targetLng = disLng;
//
//        for (Building b : allBuildings) {
//            if (b.getName().equals(targetBox)) {
//                destinationBuilding = b;
//                disLat = b.getLatitude();
//                disLng = b.getLongitude();
//                break;
//            }
//        }
//
//        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
//            if (location != null) {
//                double lat = 13.780329795615001;
//                double lng = 100.5604076955666;
//                calculateNear cn = new calculateNear(lat, lng);
//                this.currentLocationValue = cn.getBuildingName();
//                startIdx = 0;
//                float minStartDist = Float.MAX_VALUE;
//                for (int i = 0; i < panoList.size(); i++) {
//                    float[] results = new float[1];
//                    android.location.Location.distanceBetween(
//                            lat, lng,
//                            panoList.get(i).getLatitude(),
//                            panoList.get(i).getLongitude(),
//                            results);
//
//                    if (results[0] < minStartDist) {
//                        minStartDist = results[0];
//                        startIdx = i;
//                    }
//                }
//                while (currentIndex != startIdx) { nextButton(); }
//
//            }
//        });
//
//        findViewById(R.id.forwardArrow).setOnClickListener(v -> {
//            Toast.makeText(this, "forward", Toast.LENGTH_SHORT).show();
//
//            double curLat = panoList.get(currentIndex).getLatitude();
//            double curLng = panoList.get(currentIndex).getLongitude();
//
//            float[] distNow = new float[1];
//            android.location.Location.distanceBetween(curLat, curLng, targetLat, targetLng, distNow);
//            float shortestDistance = distNow[0];
//            int bestNextIndex = currentIndex;
//
//            for (int i = 0; i < panoList.size(); i++) {
//                float[] distFromMe = new float[1];
//                android.location.Location.distanceBetween(
//                        curLat, curLng,
//                        panoList.get(i).getLatitude(), panoList.get(i).getLongitude(), distFromMe);
//
//                if (distFromMe[0] < 60 && distFromMe[0] > 1) {
//                    float[] distToTarget = new float[1];
//                    android.location.Location.distanceBetween(
//                            panoList.get(i).getLatitude(), panoList.get(i).getLongitude(),
//                            targetLat, targetLng, distToTarget);
//
//                    if (distToTarget[0] < shortestDistance) {
//                        shortestDistance = distToTarget[0];
//                        bestNextIndex = i;
//                    }
//                }
//            }
//
//            if (bestNextIndex > currentIndex) {
//                while (currentIndex != bestNextIndex) { nextButton(); }
//                setCenter();
//            } else if (bestNextIndex < currentIndex) {
//                while (currentIndex != bestNextIndex) { prevButton(); }
//                setCenter();
//            }
//        });
//    }
    public void displayPixel(){
        final ImageView referenceImage = (ImageView) container.getChildAt(1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), panoImg, options);

        float screenImgWidth = referenceImage.getWidth();
        float originalFileWidth = options.outWidth;

        float centerInContent = scroll.getScrollX() + (scroll.getWidth() / 2f);
        float pixelPos = (centerInContent % screenImgWidth) / screenImgWidth * originalFileWidth;

        pixelIndicator.setText("Pixel: " + Math.round(pixelPos));
    }

    private void displayImg(){
        if (panoImg == 0) return;
        Bitmap sampledBitmap = decodeSampledBitmapFromResource(panoImg, 300, 300);
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof ImageView) {
                ((ImageView) child).setImageBitmap(sampledBitmap);
            }
            setCenter();
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
}