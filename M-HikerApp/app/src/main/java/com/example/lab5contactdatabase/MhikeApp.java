package com.example.lab5contactdatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MhikeApp extends AppCompatActivity {

    // Khai báo biến toàn cục
    private RecyclerView recyclerView;
    private HikerAdapter hikerAdapter;
    private List<Hike> hikeList;
    private Button buttonAddHike;
    private ImageButton btnDeleteAll;
    private DatabaseHelper dbHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiker_view);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerContacts);
        buttonAddHike = findViewById(R.id.add_hike);
        btnDeleteAll = findViewById(R.id.btn_delete_all);
        searchView = findViewById(R.id.search_view);

        hikeList = dbHelper.getAllHike();
        if (hikeList == null) {
            hikeList = new ArrayList<>();
        }
        loadHikes();

        // 4. Cài đặt RecyclerView
        hikerAdapter = new HikerAdapter(hikeList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(hikerAdapter);

        // 5. Cài đặt Search (Làm sau khi đã có dữ liệu)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Đây là nơi xử lý khi người dùng nhấn nút Search hoặc Enter
                Log.d("MhikeApp", "onQueryTextSubmit: " + query); // Ghi log để kiểm tra
                if (query != null && !query.trim().isEmpty()) {
                    List<Hike> results = dbHelper.searchHikes(query.trim()); // Gọi DB để tìm kiếm

                    Log.d("MhikeApp", "Search results size: " + results.size());
                    Toast.makeText(MhikeApp.this, "Tìm thấy " + results.size() + " chuyến đi.", Toast.LENGTH_SHORT).show();

                    if (results.size() == 1) {
                        // Tìm thấy chính xác một chuyến đi, mở HikeDetailActivity
                        Hike singleHike = results.get(0);
                        Intent intent = new Intent(MhikeApp.this, Hike_details.class);
                        intent.putExtra("hike_id", singleHike.getId());
                        intent.putExtra("hike_name", singleHike.getName());
                        startActivity(intent);

                        // Tùy chọn: Xóa text trên SearchView và ẩn bàn phím sau khi mở chi tiết
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                    } else if (results.size() > 1) {
                        // Tìm thấy nhiều hơn một chuyến đi, hiển thị danh sách lọc
                        hikerAdapter.setFilteredList(results);
                        searchView.clearFocus(); // Ẩn bàn phím
                    } else {
                        // Không tìm thấy chuyến đi nào
                        Toast.makeText(MhikeApp.this, "Không tìm thấy chuyến đi nào khớp với '" + query + "'", Toast.LENGTH_SHORT).show();
                        hikerAdapter.setFilteredList(new ArrayList<>()); // Hiển thị danh sách rỗng
                        searchView.clearFocus(); // Ẩn bàn phím
                    }
                } else {
                    // Nếu query rỗng, hiển thị lại toàn bộ danh sách
                    loadHikes();
                    searchView.clearFocus(); // Ẩn bàn phím
                }
                return true; // Đánh dấu rằng chúng ta đã xử lý sự kiện

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return true;
            }
        });

        // 6. Sự kiện nút Thêm (Add)
        buttonAddHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MhikeApp.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 7. Sự kiện nút Xóa hết
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

    } // <--- KẾT THÚC HÀM onCreate Ở ĐÂY

    // CÁC HÀM DƯỚI ĐÂY PHẢI VIẾT NGOÀI onCreate

    // hàm load lại data
    private void loadHikes() {
        hikeList = dbHelper.getAllHike(); // Lấy tất cả Hike từ DatabaseHelper
        if (hikeList == null) {
            hikeList = new ArrayList<>(); // Nếu rỗng thì tạo danh sách rỗng
        }
        // Khởi tạo hoặc cập nhật adapter với danh sách mới
        if (hikerAdapter == null) { // Nếu adapter chưa được tạo (lần đầu)
            hikerAdapter = new HikerAdapter(hikeList, this);
            recyclerView.setAdapter(hikerAdapter);
        } else { // Nếu adapter đã có, chỉ cần cập nhật danh sách và thông báo thay đổi
            hikerAdapter.setFilteredList(hikeList); // Hoặc một phương thức tương tự để cập nhật data
        }
    }
    private void filterList(String text) {
        // Kiểm tra nếu hikeList chưa có dữ liệu thì không làm gì cả
        if (hikeList == null) return;

        List<Hike> filteredList = new ArrayList<>();

        for (Hike item : hikeList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            // Nếu không tìm thấy thì truyền list rỗng để ẩn hết
            if (hikerAdapter != null) {
                hikerAdapter.setFilteredList(filteredList);
            }
        } else {
            if (hikerAdapter != null) {
                hikerAdapter.setFilteredList(filteredList);
            }
        }
    }

    // Hàm hiển thị hộp thoại xóa
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Do you want to delete all?")
                .setMessage("This action will delete all your hikes. ARE YOU SURE?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete NOW!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gọi hàm xóa trong Database (Kiểm tra tên hàm trong DatabaseHelper của bạn)
                        dbHelper.deleteAllhikes();

                        if (hikeList != null) {
                            hikeList.clear();
                        }
                        if (hikerAdapter != null) {
                            hikerAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(MhikeApp.this, "Delete successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại dữ liệu khi quay lại màn hình này
        if(dbHelper != null && hikerAdapter != null) {
            List<Hike> newList = dbHelper.getAllHike();
            if (newList != null) {
                hikeList.clear();
                hikeList.addAll(newList);
                hikerAdapter.notifyDataSetChanged();
            }
        }
    }
}