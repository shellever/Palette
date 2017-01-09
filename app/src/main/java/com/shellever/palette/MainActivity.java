package com.shellever.palette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

//
// dependency
// compile 'com.android.support:appcompat-v7:25.1.0'
// compile 'com.android.support:palette-v7:25.1.0'
//
// ImageSwitch + Palette
// Palette - 提取图片主色调
public class MainActivity extends AppCompatActivity {

    private LinearLayout mContainerLayout;

    private ImageView mPictureIv;

    private TextView mVibrantColorTv;
    private TextView mDarkVibrantColorTv;
    private TextView mLightVibrantColorTv;

    private TextView mMutedColorTv;
    private TextView mDarkMutedColorTv;
    private TextView mLightMutedColorTv;

    private TextView mDominantColorTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPictureIv = (ImageView) findViewById(R.id.iv_picture);
        mVibrantColorTv = (TextView) findViewById(R.id.tv_color_vibrant);
        mDarkVibrantColorTv = (TextView) findViewById(R.id.tv_color_vibrant_dark);
        mLightVibrantColorTv = (TextView) findViewById(R.id.tv_color_vibrant_light);
        mMutedColorTv = (TextView) findViewById(R.id.tv_color_muted);
        mDarkMutedColorTv = (TextView) findViewById(R.id.tv_color_muted_dark);
        mLightMutedColorTv = (TextView) findViewById(R.id.tv_color_muted_light);
        mDominantColorTv = (TextView) findViewById(R.id.tv_color_dominant);

        mContainerLayout = (LinearLayout) findViewById(R.id.ll_container);

        initPalette();
    }

    private void setPaletteColor(TextView tv, int color) {
        tv.setBackgroundColor(color);
        tv.setText(ColorUtils.toRGBHexString(color));
        tv.setTextColor(ColorUtils.parseBackgroundColor(color));
    }

    private void initPalette() {
        Drawable drawable = mPictureIv.getDrawable();
        Bitmap bitmap = drawableToBitmap(drawable);

        // Synchronous
        // mPalette = Palette.from(bitmap).generate();

        // Asynchronous
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // Use generated instance
                int defaultColor = Color.parseColor("#b64242");

                int mVibrantColor = palette.getVibrantColor(defaultColor);
                int mDarkVibrantColor = palette.getDarkVibrantColor(defaultColor);
                int mLightVibrantColor = palette.getLightVibrantColor(defaultColor);
                int mMutedColor = palette.getMutedColor(defaultColor);
                int mDarkMutedColor = palette.getDarkMutedColor(defaultColor);
                int mLightMutedColor = palette.getLightMutedColor(defaultColor);

                setPaletteColor(mVibrantColorTv, mVibrantColor);
                setPaletteColor(mDarkVibrantColorTv, mDarkVibrantColor);
                setPaletteColor(mLightVibrantColorTv, mLightVibrantColor);
                setPaletteColor(mMutedColorTv, mMutedColor);
                setPaletteColor(mDarkMutedColorTv, mDarkMutedColor);
                setPaletteColor(mLightMutedColorTv, mLightMutedColor);

                // dominant color (主色)
                int mDominantColor = palette.getDominantColor(defaultColor);
                setPaletteColor(mDominantColorTv, mDominantColor);

                // Swatch - 色块 // 15种
                List<Palette.Swatch> mSwatchList = palette.getSwatches();
                Toast.makeText(MainActivity.this, "Swatch num: " + mSwatchList.size(), Toast.LENGTH_SHORT).show();
                int index = -1;
                LinearLayout mSwatchesContainer = null;
                LinearLayout.LayoutParams params;
                for (Palette.Swatch swatch : mSwatchList) {
                    int color = swatch.getRgb();
                    index++;

                    if (index % 3 == 0) {
                        mSwatchesContainer = new LinearLayout(getApplicationContext());
                        mSwatchesContainer.setOrientation(LinearLayout.HORIZONTAL);
                        params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.topMargin = (int) DisplayUtils.dp2px(getApplicationContext(), 10);
                        mContainerLayout.addView(mSwatchesContainer, params);       //
                    }

                    LinearLayout mSwatchContainer = new LinearLayout(getApplicationContext());
                    mSwatchContainer.setOrientation(LinearLayout.VERTICAL);
                    params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = 1;
                    params.gravity = Gravity.CENTER;
                    if (mSwatchesContainer != null) {
                        mSwatchesContainer.addView(mSwatchContainer, params);       //
                    }

                    TextView mColorTv = new TextView(getApplicationContext());
                    mColorTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    setPaletteColor(mColorTv, color);           //
                    mColorTv.setGravity(Gravity.CENTER);
                    params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            (int) DisplayUtils.dp2px(getApplicationContext(), 80)
                    );
                    params.gravity = Gravity.CENTER;
                    mSwatchContainer.addView(mColorTv, params);                 //

                    TextView mColorNameTv = new TextView(getApplicationContext());
                    mColorNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mColorNameTv.setText("Swatch " + index);
                    mColorNameTv.setGravity(Gravity.CENTER);
                    mColorNameTv.setTextColor(Color.parseColor("#333333"));
                    params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.gravity = Gravity.CENTER;
                    mSwatchContainer.addView(mColorNameTv, params);
                }
            }
        });
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap); // canvas -> bitmap
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);      // drawable -> canvas
        return bitmap;  // drawable -> canvas -> bitmap
    }

}
