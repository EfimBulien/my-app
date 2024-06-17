package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import java.io.InputStream;

public class FullImageView extends AppCompatActivity {
    ImageView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Установка полного экрана
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_full_image_view);
        fullImage = findViewById(R.id.fullImage); // Инициализация ImageView

        // Получение URI изображения из Intent
        String imageUriSrc = getIntent().getStringExtra("image");
        Uri imageUri = Uri.parse(imageUriSrc);
        try {
            // Открытие потока для чтения изображения
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            // Декодирование изображения
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            fullImage.setImageBitmap(selectedImage); // Установка изображения в ImageView
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
