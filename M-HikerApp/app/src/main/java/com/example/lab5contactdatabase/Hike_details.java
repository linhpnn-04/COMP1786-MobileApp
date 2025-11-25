// C:/Users/Admin/AndroidStudioProjects/M-HikerApp/app/src/main/java/com/example/lab5contactdatabase/Hike_details.java
package com.example.lab5contactdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Thêm  Log để debug
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Hike_details extends AppCompatActivity {

    private TextView tvHikeName, tvLocation, tvDate, tvParking, tvLength,
            tvDifficulty, tvType, tvScenicPoints, tvDescription;
    private Button btnViewObservations;
    private int currentHikeId = -1;
    private String currentHikeName = "";
    private DatabaseHelper dbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hike_detail); // Đảm bảo layout này tồn tại và đúng tên

        dbHelper = new DatabaseHelper(this);

        // Ánh xạ các Views
        tvHikeName = findViewById(R.id.tv_detail_hike_name);
        tvLocation = findViewById(R.id.tv_detail_location);
        tvDate = findViewById(R.id.tv_detail_date);
        tvParking = findViewById(R.id.tv_detail_parking); // Ánh xạ TextView cho Parking
        tvLength = findViewById(R.id.tv_detail_length);
        tvDifficulty = findViewById(R.id.tv_detail_difficulty); // Ánh xạ TextView cho Difficulty
        tvType = findViewById(R.id.tv_detail_type);
        tvScenicPoints = findViewById(R.id.tv_detail_scenic_points);
        tvDescription = findViewById(R.id.tv_detail_description);
        btnViewObservations = findViewById(R.id.btn_view_observations);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            currentHikeId = intent.getIntExtra("hike_id", -1);
            currentHikeName = intent.getStringExtra("hike_name");
        }

        if (currentHikeId != -1) {
            loadHikeDetails(currentHikeId); // Gọi hàm tải chi tiết Hike
        } else {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_SHORT).show();
            Log.e("Hike_details", "Hike ID was -1. Finishing activity."); // Ghi log lỗi
            finish(); // Đóng Activity nếu không có ID
        }

        btnViewObservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obsIntent = new Intent(Hike_details.this, ObservationActivity.class);
                obsIntent.putExtra("hike_id", currentHikeId);
                obsIntent.putExtra("hike_name", currentHikeName); // Truyền tên Hike
                startActivity(obsIntent);
            }
        });
    }

    private void loadHikeDetails(int hikeId) {
        Hike hike = dbHelper.getHikeById(hikeId); // Giả định bạn có hàm này trong DatabaseHelper
        if (hike != null) {
            tvHikeName.setText(hike.getName());
            tvLocation.setText("Location: " + hike.getLocation());
            tvDate.setText("Date: " + hike.getDateandtime());


            tvParking.setText("Parking: " + (hike.getParkingAvailable() == 1 ? "Yes" : "No"));

            tvLength.setText("Length: " + hike.getLengthofhike() + " km");
            // --- Xử lý Difficulty bằng TextView ---
            String difficultyText;
            if (hike.getHike_lv() == 1) {
                difficultyText = "Easy";
            } else if (hike.getHike_lv() == 2) {
                difficultyText = "Moderate";
            } else { // getHike_lv() == 3 hoặc các giá trị khác
                difficultyText = "Hard";
            }
            tvDifficulty.setText("Difficulty: " + difficultyText);

            tvType.setText("Type: " + hike.getHikeType());
            tvScenicPoints.setText("Scenic Points: " + hike.getScenicPoints());
            tvDescription.setText("Description: " + hike.getDescription());

            // Cập nhật currentHikeName từ đối tượng Hike để đảm bảo chính xác nhất
            currentHikeName = hike.getName();
            // Đặt tiêu đề Activity
            setTitle(currentHikeName + " Details");
        } else {
            Toast.makeText(this, "Hike details not found.", Toast.LENGTH_SHORT).show();
            Log.e("Hike_details", "Hike object was null for ID: " + hikeId + ". Finishing activity.");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentHikeId != -1) {
            loadHikeDetails(currentHikeId);
        }
    }
}