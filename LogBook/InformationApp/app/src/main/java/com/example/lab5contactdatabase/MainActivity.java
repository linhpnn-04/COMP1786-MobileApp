package com.example.lab5contactdatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgAvatar;
    private Uri imageUri;  // URI của ảnh user chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgAvatar = findViewById(R.id.imgAvatar);
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        Button btnSave = findViewById(R.id.save_btn); // nút lưu thông tin

        // Chọn ảnh
        btnChooseImage.setOnClickListener(v -> openImageChooser());

        // Lưu thông tin
        btnSave.setOnClickListener(v -> saveDetails());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        // Thêm cờ để yêu cầu quyền đọc
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Nhận ảnh user chọn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgAvatar.setImageURI(imageUri); // hiển thị ảnh đã chọn

            final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION );
            try {
                // Truyền trực tiếp cờ quyền đọc vào phương thức
                getContentResolver().takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            } catch (SecurityException e) {
                // Import android.util.Log nếu chưa có
                android.util.Log.e("MainActivity", "Failed to take permission: ", e);
            }
        }
    }

    private void saveDetails() {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        EditText nameTxt = findViewById(R.id.editTextText);
        EditText dobTxt = findViewById(R.id.editTextText2);
        EditText emailTxt = findViewById(R.id.editTextText3);

        String name = nameTxt.getText().toString();
        String dob = dobTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String image = imageUri != null ? imageUri.toString() : null;

        // Bạn có thể thêm dòng này nếu muốn lưu luôn ảnh
         if (imageUri != null) {

         }
        // long personId = dbHelper.insertDetails(name, dob, email, imageUri != null ? imageUri.toString() : null);
        long personId = dbHelper.insertDetails(name, dob, email, image);

        Toast.makeText(this, "Person has been created with id: " + personId,
                Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, DetailsActivity.class);
        startActivity(i);
    }
}
