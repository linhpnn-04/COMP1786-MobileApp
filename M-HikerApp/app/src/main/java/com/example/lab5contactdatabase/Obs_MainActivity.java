package com.example.lab5contactdatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Obs_MainActivity extends AppCompatActivity {
    public static final String OBS_TO_EDIT_KEY = "OBS_TO_EDIT";
    private static final int PICK_IMAGE_REQUEST = 1;

    // Khai báo tất cả các thành phần UI
    private EditText editTextObsName;
    private EditText editTextComment;
    private EditText editTextTime;
    private Button buttonAddObs;
    private Button buttonCancel;
    private Button buttonChooseImage;
    private ImageView ivPreview;

    private Observation currentObsToEdit = null;
    private String selectedImageUri = null; // Biến để lưu đường dẫn ảnh
    private int currentHikeId = -1; // Biến để lưu hikeId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obervation);

        // Ánh xạ các thành phần UI
        editTextObsName = findViewById(R.id.etObservationName);
        buttonChooseImage = findViewById(R.id.btnPickImage);
        ivPreview = findViewById(R.id.ivPreview);
        editTextComment = findViewById(R.id.etObservationComments);
        buttonAddObs = findViewById(R.id.Save_btn);
        buttonCancel = findViewById(R.id.cancel_btn);
        editTextTime = findViewById(R.id.etObservationTime);

        // Đặt ngày giờ hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(Calendar.getInstance().getTime());
        editTextTime.setText(currentTime);
        editTextTime.setEnabled(true); // cho người dùng sửa

        buttonChooseImage.setOnClickListener(v -> openImageChooser());

        Intent intent = getIntent();
        if (intent != null) {
            // Nhận hikeId để biết observation này thuộc về hike nào
            currentHikeId = intent.getIntExtra("hike_id", -1);

            if (intent.hasExtra(OBS_TO_EDIT_KEY)) {
                // Chế độ chỉnh sửa
                currentObsToEdit = (Observation) intent.getSerializableExtra(OBS_TO_EDIT_KEY);
                if (currentObsToEdit != null) {
                    populateFields(currentObsToEdit);
                    buttonAddObs.setText("Update Observation");
                }
            }
        }

        buttonAddObs.setOnClickListener(v -> {
            if (currentObsToEdit == null) {
                addObs(); // Chế độ thêm mới
            } else {
                updateObs(); // Chế độ cập nhật
            }
        });

        buttonCancel.setOnClickListener(v -> finish());
    }

    private void populateFields(Observation obs) {
        editTextObsName.setText(obs.getObservation());
        editTextComment.setText(obs.getComments());
        editTextTime.setText(obs.getTime());
        selectedImageUri = obs.getImages(); // Lưu lại đường dẫn ảnh cũ

        if (selectedImageUri != null && !selectedImageUri.isEmpty()) {
            // SỬA LỖI 4: Chuyển String thành Uri
            ivPreview.setImageURI(Uri.parse(selectedImageUri));
        }
    }

    private void updateObs() {
        if (currentObsToEdit == null) return;
        String obsName = editTextObsName.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String comment = editTextComment.getText().toString();

        if (obsName.isEmpty()) {
            Toast.makeText(this, "Observation name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        Observation updatedObs = new Observation(
                currentObsToEdit.getId(),
                currentObsToEdit.getHikeId(), // Giữ lại hike_id cũ
                obsName,
                time,
                comment,
                selectedImageUri // Sử dụng ảnh mới hoặc ảnh cũ
        );

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int rowsAffected = dbHelper.updateObs(updatedObs);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Observation updated successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình trước đó
        } else {
            Toast.makeText(this, "Failed to update observation.", Toast.LENGTH_SHORT).show();
        }
    }
    private void addObs() {
        if (currentHikeId == -1) {
            Toast.makeText(this, "Error: Could not find associated hike.", Toast.LENGTH_SHORT).show();
            return;
        }

        String obsName = editTextObsName.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String comment = editTextComment.getText().toString();

        if (obsName.isEmpty()) {
            Toast.makeText(this, "Observation name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        // Khi thêm mới, chỉ cần truyền các giá trị từ form và currentHikeId
        long result = dbHelper.addObservation(
                currentHikeId,
                obsName,
                time,
                comment,
                selectedImageUri

        );

        if (result != -1) {
            Toast.makeText(this, "Observation added successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình trước đó
        } else {
            Toast.makeText(this, "Failed to add observation.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Lưu URI và hiển thị ảnh chỉ sau khi đã lấy được quyền thành công
                selectedImageUri = imageUri.toString(); // Lưu URI dưới dạng String
                ivPreview.setImageURI(imageUri); // Hiển thị ảnh xem trước

            } catch (SecurityException e) {
                // Xử lý trường hợp không lấy được quyền (hiếm khi xảy ra với ACTION_OPEN_DOCUMENT)
                e.printStackTrace();
                Toast.makeText(this, "Failed to get permission for the image.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

