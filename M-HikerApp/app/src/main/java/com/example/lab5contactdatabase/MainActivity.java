package com.example.lab5contactdatabase; // Thay thế bằng package của bạn nếu khác

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String HIKE_TO_EDIT_KEY = "HIKE_TO_EDIT";

    // Khai báo tất cả các thành phần UI từ layout
    private EditText editTextHikeName;
    private EditText editTextLocation;
    private TextView textSelectedDate;
    private RadioGroup radioGroupParking;
    private EditText editTextLength;
    private RadioGroup radioGroupDifficulty;
    private EditText editTextHikeType;
    private EditText editTextScenicPoints;
    private EditText editTextDescription;
    private Button buttonAddHike;
    private Button buttonCancel;
    private Button buttonPickDate;

    private Calendar myCalendar;

    private Hike currentHikeToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo tên file layout là đúng

        // Khởi tạo Calendar
        myCalendar = Calendar.getInstance();

        // Ánh xạ các thành phần UI với ID trong file XML
        editTextHikeName = findViewById(R.id.edit_text_hike_name);
        editTextLocation = findViewById(R.id.edit_text_location);
        buttonPickDate = findViewById(R.id.button_pick_date);
        textSelectedDate = findViewById(R.id.text_selected_date);
        radioGroupParking = findViewById(R.id.radio_group_parking);
        editTextLength = findViewById(R.id.edit_text_length);
        radioGroupDifficulty = findViewById(R.id.radio_group_difficulty);
        editTextHikeType = findViewById(R.id.edit_text_hike_type);
        editTextScenicPoints = findViewById(R.id.edit_text_scenic_points);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonAddHike = findViewById(R.id.add_btn);
        buttonCancel = findViewById(R.id.button_cancel);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(HIKE_TO_EDIT_KEY)) {
            // Nhận đối tượng Hike đã được truyền
            currentHikeToEdit = (Hike) intent.getSerializableExtra(HIKE_TO_EDIT_KEY);
            if (currentHikeToEdit != null) {
                // Đang ở chế độ chỉnh sửa, điền dữ liệu vào các trường
                populateFields(currentHikeToEdit);
                buttonAddHike.setText("Update hike");// Đổi text nút Add Hike thành Update Hike
            }
        }

        // Thiết lập sự kiện click cho nút "Select Date"
        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Thiết lập sự kiện click cho nút "Add Hike"
        buttonAddHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentHikeToEdit == null) {
                    // Nếu currentHikeToEdit là null, tức là đang ở chế độ thêm mới
                    addHike();
                } else {
                    // Nếu currentHikeToEdit KHÔNG null, tức là đang ở chế độ chỉnh sửa
                    updateHike();
                }
            }
        });

        // Thiết lập sự kiện click cho nút "Cancel"
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thoát khỏi Activity hiện tại
                finish();
            }
        });
    }

    // Phương thức hiển thị DatePickerDialog
    public void showDatePickerDialog() {
        new DatePickerDialog(MainActivity.this, dateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Listener để nhận ngày tháng sau khi người dùng chọn
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }
    };

    // Cập nhật TextView hiển thị ngày tháng đã chọn
    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy"; // Định dạng ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textSelectedDate.setText(sdf.format(myCalendar.getTime()));
    }

    // Phương thức điền dữ liệu vào các trường UI khi chỉnh sửa
    private void populateFields(Hike hike) {
        editTextHikeName.setText(hike.getName());
        editTextLocation.setText(hike.getLocation());
        textSelectedDate.setText(hike.getDateandtime());

        // Xử lý RadioGroup Parking: Bạn đã lưu 1=Yes, 0=No
        if (hike.getParkingAvailable() == 1) {
            radioGroupParking.check(R.id.radio_parking_yes);
        } else {
            radioGroupParking.check(R.id.radio_parking_no);
        }

        // Chuyển đổi double thành String cho EditText
        editTextLength.setText(String.valueOf(hike.getLengthofhike()));

        // Xử lý RadioGroup Difficulty
        if (hike.getHike_lv() == 1) {
            radioGroupDifficulty.check(R.id.radio_difficulty_easy);
        } else if (hike.getHike_lv() == 2) {
            radioGroupDifficulty.check(R.id.radio_difficulty_moderate);
        } else {
            radioGroupDifficulty.check(R.id.radio_difficulty_hard);
        }

        editTextHikeType.setText(hike.getHikeType());
        editTextScenicPoints.setText(hike.getScenicPoints());
        editTextDescription.setText(hike.getDescription());

        String dateString = hike.getDateandtime();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            myCalendar.setTime(sdf.parse(dateString));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi định dạng ngày khi tải dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức xử lý việc cập nhật Hike
    private void updateHike() {
        if (currentHikeToEdit == null) {
            Toast.makeText(this, "Error: No hike selected for update.", Toast.LENGTH_SHORT).show();
            return;
        }

        String hikeName = editTextHikeName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String hikeDate = textSelectedDate.getText().toString();

        if (hikeName.isEmpty() || location.isEmpty() || hikeDate.equals("No date selected")) {
            Toast.makeText(this, "Please fill in all required fields (Name, Location, Date)", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedParkingId = radioGroupParking.getCheckedRadioButtonId();
        if (selectedParkingId == -1) {
            Toast.makeText(this, "Please select Parking availability.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioParkingButton = findViewById(selectedParkingId);
        if (radioParkingButton == null) {
            Toast.makeText(this, "Parking RadioButton not found. Check layout IDs.", Toast.LENGTH_SHORT).show();
            return;
        }
        String isParkingAvailable = radioParkingButton.getText().toString();
        int parkingValue;
        if (isParkingAvailable.equals("Yes"))
            parkingValue = 1;
        else
            parkingValue = 0;

        String lengthText = editTextLength.getText().toString().trim();
        double lengthValue = 0.0; // <--- KHỞI TẠO BIẾN
        if (lengthText.isEmpty()) {
            Toast.makeText(this, "Please enter the length of the hike.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                lengthValue = Double.parseDouble(lengthText); // Gán giá trị vào lengthValue
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Length must be a valid number.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int selectedDifficultyId = radioGroupDifficulty.getCheckedRadioButtonId();
        if (selectedDifficultyId == -1) {
            Toast.makeText(this, "Please select Difficulty level.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioDifficultyButton = findViewById(selectedDifficultyId);
        if (radioDifficultyButton == null) {
            Toast.makeText(this, "Difficulty RadioButton not found. Check layout IDs.", Toast.LENGTH_SHORT).show();
            return;
        }
        String hike_lv_text = radioDifficultyButton.getText().toString(); // Đổi tên biến để tránh nhầm lẫn
        int difficultyValue;
        if (hike_lv_text.equals("Easy")) {
            difficultyValue = 1;
        } else if (hike_lv_text.equals("Moderate")) {
            difficultyValue = 2;
        } else { // Hard
            difficultyValue = 3;
        }

        String hikeType = editTextHikeType.getText().toString().trim();
        String scenicPoints = editTextScenicPoints.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        // Tạo đối tượng Hike mới với dữ liệu cập nhật
        Hike updatedHike = new Hike(
                currentHikeToEdit.getId(),
                hikeName,
                location,
                hikeDate,
                parkingValue,
                lengthValue, // <--- TRUYỀN lengthValue (double)
                difficultyValue,
                scenicPoints,
                description,
                hikeType
        );

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int rowsAffected = dbHelper.updateHike(updatedHike); // <--- SỬA LỖI GỌI PHƯƠNG THỨC updateHike

        if (rowsAffected > 0) {
            Toast.makeText(this, "Your hike has been updated successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MhikeApp.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error: Can not update your hike.", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức xử lý việc thêm Hike
    private void addHike() {
        // Lấy dữ liệu từ các trường nhập liệu
        String hikeName = editTextHikeName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String hikeDate = textSelectedDate.getText().toString();

        // Kiểm tra các trường bắt buộc
        if (hikeName.isEmpty() || location.isEmpty() || hikeDate.equals("No date selected")) {
            Toast.makeText(this, "Please fill in all required fields (Name, Location, Date)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy giá trị từ RadioGroup Parking
        int selectedParkingId = radioGroupParking.getCheckedRadioButtonId();
        if (selectedParkingId == -1) { // Kiểm tra nếu chưa chọn gì
            Toast.makeText(this, "Please select Parking availability.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioParkingButton = findViewById(selectedParkingId);
        String isParkingAvailable = radioParkingButton.getText().toString();
        int parkingValue ;
        if (isParkingAvailable.equals("Yes"))
            parkingValue = 1;
        else
            parkingValue = 0;

        // Lấy chiều dài chuyến đi
        String lengthText = editTextLength.getText().toString().trim();
        double lengthValue = 0.0; // <--- KHỞI TẠO BIẾN
        if (lengthText.isEmpty()) {
            Toast.makeText(this, "Please enter the length of the hike.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                lengthValue = Double.parseDouble(lengthText); // Gán giá trị vào lengthValue
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Length must be a valid number.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Lấy giá trị từ RadioGroup Difficulty
        int selectedDifficultyId = radioGroupDifficulty.getCheckedRadioButtonId();
        if (selectedDifficultyId == -1) { // Kiểm tra nếu chưa chọn gì
            Toast.makeText(this, "Please select Difficulty level.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioDifficultyButton = findViewById(selectedDifficultyId);
        String hike_lv_text = radioDifficultyButton.getText().toString(); // Đổi tên biến
        int difficultyValue;
        if (hike_lv_text.equals("Easy")) {
            difficultyValue = 1;
        } else if (hike_lv_text.equals("Moderate")) {
            difficultyValue = 2;
        } else { // Hard
            difficultyValue = 3;
        }

        String hikeType = editTextHikeType.getText().toString().trim();
        String scenicPoints = editTextScenicPoints.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();


        // --- PHẦN XỬ LÝ DATABASE ---
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        long result = dbHelper.insertDetails(
                hikeName,
                location,
                hikeDate,
                difficultyValue,
                description,
                lengthValue,
                hikeType,
                scenicPoints,
                parkingValue
        );

        if (result != -1) {
            Toast.makeText(this, "Hike added successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MhikeApp.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to add hike.", Toast.LENGTH_SHORT).show();
        }

        // Toast này sẽ không bao giờ được hiển thị nếu insert thành công và chuyển Activity
        // Hoặc nếu insert thất bại, nó vẫn sẽ hiển thị sau toast "Failed to add hike."
        // Hơn nữa, khối if-else về lengthValue cuối cùng là dư thừa và sai logic.
        // Tạm thời hiển thị Toast để xác nhận
        // Toast.makeText(this, "Hike '" + hikeName + "' is ready to be saved.", Toast.LENGTH_LONG).show();
    }
}