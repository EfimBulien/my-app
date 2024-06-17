package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int PHOTO_ID = 123; // Идентификатор для запроса фото
    private final ArrayList<Uri> images = new ArrayList<>(); // Список для хранения URI изображений
    private ArrayAdapter<Uri> adapter; // Адаптер для ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Установка полного экрана
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        // Инициализация элементов интерфейса
        Button cameraOpen = findViewById(R.id.camera_button);
        ListView imageList = findViewById(R.id.image_list);
        TextView nameTextView = findViewById(R.id.name_text);

        // Установка фамилии и инициалов
        nameTextView.setText("Бульен Е.М.");

        // Настройка адаптера для ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, images);
        imageList.setAdapter(adapter);

        // Обработчик нажатия кнопки для открытия камеры
        cameraOpen.setOnClickListener(v ->
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), PHOTO_ID));

        // Обработчик нажатия на элемент списка для просмотра изображения в полном размере
        imageList.setOnItemClickListener((parent, view, position, id) -> {
            // Получение URI выбранного изображения
            Uri imageUri = (Uri) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, FullImageView.class);
            intent.putExtra("image", imageUri.toString()); // Передача URI в новое активити
            startActivity(intent);
        });
    }

    // Метод для сохранения изображения в кеш
    private Uri saveImageStorage(Bitmap bitmapImage) {
        // Получение кеш директории
        File outputDirectory = getApplicationContext().getCacheDir();
        // Создание файла для изображения
        File imageFile = new File(outputDirectory, System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fileOutputStream = new FileOutputStream(imageFile)) {
            // Сжатие и сохранение изображения
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            return Uri.fromFile(imageFile); // Возвращение URI сохраненного файла
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_ID && resultCode == RESULT_OK) { // Проверка запроса и результата
            // Получение изображения из результата
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            Uri imageUri = saveImageStorage(photo); // Сохранение изображения в кеш
            if (imageUri != null) {
                images.add(imageUri); // Добавление URI изображения в список
                adapter.notifyDataSetChanged(); // Обновление адаптера
            }
        }
    }
}
