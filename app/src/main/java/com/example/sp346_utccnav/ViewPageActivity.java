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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class ViewPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        hideSystemUI();

        HorizontalScrollView scroll = findViewById(R.id.panoramaScroll);
        LinearLayout container = findViewById(R.id.panoramaContainer);
        TextView pixelIndicator = findViewById(R.id.pixelIndicator);

        // Get image from Intent or default to pos4
        int panoImg = R.drawable.pos1;
        if (getIntent().hasExtra("image_resource")) {
            panoImg = getIntent().getIntExtra("image_resource", R.drawable.pos1);
        }

        // Decode the image with sampling to prevent OutOfMemory crashes
        // We target a width of about 1024px for good enough quality while saving RAM
        Bitmap sampledBitmap = decodeSampledBitmapFromResource(panoImg, 200, 200);

        // Apply sampled bitmap to the three ImageViews
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof ImageView) {
                ((ImageView) child).setImageBitmap(sampledBitmap);
            }
        }

        final ImageView referenceImage = (ImageView) container.getChildAt(1);

        scroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollX = scroll.getScrollX();
            int imageWidth = referenceImage.getWidth();
            int screenWidth = scroll.getWidth();

            if (imageWidth > 0) {
                if (scrollX >= imageWidth * 2) {
                    scroll.setScrollX(scrollX - imageWidth);
                } else if (scrollX <= 0) {
                    scroll.setScrollX(scrollX + imageWidth);
                }
                
                // Calculate pixel position (mapping current center to the 2048px original width)
                float centerInContent = scroll.getScrollX() + (screenWidth / 2f);
                float pixelPos = (centerInContent % imageWidth) * (2048f / imageWidth);
                pixelIndicator.setText("Pixel: " + (int)pixelPos);
            }
        });

        // Start scroll position at the beginning of the second image (the middle one)
        scroll.post(() -> {
            if (referenceImage.getWidth() > 0) {
                scroll.setScrollX(referenceImage.getWidth());
            }
        });

        findViewById(R.id.homeBtn).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
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