package com.example.sp346_utccnav;

import android.content.Intent;
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
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import java.util.List;

public class ViewPageActivity extends AppCompatActivity {
    
    private int panoImg; 
    private LinearLayout container;
    private int currentIndex = 0; 
    private List<Building> panoList; 
    private HorizontalScrollView scroll;
    private TextView pixelIndicator;
    private TextView imageNameIndicator;

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

        //Debugger button
        findViewById(R.id.prevBtn).setOnClickListener(v -> {
            currentIndex--;
            if (currentIndex < 0) {
                currentIndex = panoList.size() - 1;
            }
            panoImg = panoList.get(currentIndex).getImageResourceId();
            imageNameIndicator.setText(BuildingRepository.getPanolocate().get(currentIndex).getName());
            displayImg();
        });

        findViewById(R.id.nextBtn).setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex >= panoList.size()) {
                currentIndex = 0;
            }
            panoImg = panoList.get(currentIndex).getImageResourceId();
            imageNameIndicator.setText(BuildingRepository.getPanolocate().get(currentIndex).getName());
            displayImg();
            setCenter();
        });

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
    }
    //This method using the starting point from buildingRepository to set
    // start which is need current pixel into change thing
    public void setCenter(){
        List<Building> panolocate = BuildingRepository.getPanolocate();
        if (currentIndex >= 0 && currentIndex < panolocate.size()) {
            Integer startPixel = panolocate.get(currentIndex).getStartPixel();
            if (startPixel != null) {
                // 1. Get the widths exactly like displayPixel does
                final ImageView referenceImage = (ImageView) container.getChildAt(1);
                float screenImgWidth = referenceImage.getWidth();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), panoImg, options);
                float originalFileWidth = options.outWidth;

                if (screenImgWidth > 0) {
                    // 2. REVERSE MATH: Convert File Pixel to Screen Pixel
                    // (Target / Total File) * Total Screen
                    float targetXOnScreen = (startPixel / originalFileWidth) * screenImgWidth;

                    // 3. OFFSET: Jump to the 2nd image and subtract half screen to center it
                    // This ensures the "startPixel" lands in the DEAD CENTER of your tablet
                    float screenWidth = scroll.getWidth();
                    int finalScrollX = (int) (targetXOnScreen + screenImgWidth - (screenWidth / 2f));
                    scroll.setScrollX(finalScrollX);
                }
            }
        }
    }

    public void displayPixel(){
        final ImageView referenceImage = (ImageView) container.getChildAt(1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), panoImg, options);

        float screenImgWidth = referenceImage.getWidth();
        float originalFileWidth = options.outWidth;

// Calculate current position relative to the middle of the screen
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
        }

    }
    //Calculate this ass clamp
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
    //...............................TF with just leave the code do not change my code bro AI...........////
    private void hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }
}
