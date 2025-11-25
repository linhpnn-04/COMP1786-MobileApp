package com.example.lab5contactdatabase;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ObservationActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private ImageButton btnDeleteAll;
    private RecyclerView recyclerView;
    private TextView tvHikeTitle;
    private ObsAdapter obsAdapter;
    private List<Observation> obsList;
    private Button buttonAddObs;
    private SearchView searchView;

    private int currentHikeId = -1; // Biến để lưu ID của hike hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obervation_view);

        dbHelper = new DatabaseHelper(this);

        // Ánh xạ View
        recyclerView = findViewById(R.id.recyclerContacts);
        buttonAddObs = findViewById(R.id.add_obs);
        btnDeleteAll = findViewById(R.id.btn_delete_all);
        tvHikeTitle = findViewById(R.id.text_view_your_observation_title);
        searchView = findViewById(R.id.search_view);


        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Lấy hikeId và hikeName từ Intent
            currentHikeId = intent.getIntExtra("hike_id", -1); // Lấy hikeId
            String hikeName = intent.getStringExtra("hike_name");

            if (hikeName != null && !hikeName.isEmpty()) {
                tvHikeTitle.setText(hikeName);
            } else {
                tvHikeTitle.setText("Observations");
            }
        } else {
            tvHikeTitle.setText("Observations");
        }

        if (currentHikeId != -1) {
            obsList = dbHelper.getObservationsByHikeId(currentHikeId); // Truyền hikeId vào đây
        } else {
            obsList = new ArrayList<>(); // Nếu không có id, tạo danh sách rỗng
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_SHORT).show();
        }

        if (obsList == null) {
            obsList = new ArrayList<>();
        }

        // Cài đặt RecyclerView
        obsAdapter = new ObsAdapter(obsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(obsAdapter);

        // Cài đặt Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        buttonAddObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ObservationActivity.this, Obs_MainActivity.class);
                intent.putExtra("hike_id", currentHikeId);
                startActivity(intent);
            }
        });
        btnDeleteAll.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void filterList(String text) {
        if (obsList == null) return;
        List<Observation> filteredList = new ArrayList<>();
        for (Observation item : obsList) {
            if (item.getObservation().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        obsAdapter.setFilteredList(filteredList);
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Observations?")
                .setMessage("Are you sure you want to delete all observations for this hike?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete All", (dialog, which) -> {
                    if (currentHikeId != -1) {
                        // Gọi hàm xóa tất cả observation CỦA HIKE NÀY
                        dbHelper.deleteAllObservationsForHike(currentHikeId);

                        // Cập nhật giao diện
                        obsList.clear();
                        obsAdapter.notifyDataSetChanged(); // Gọi trên adapter
                        Toast.makeText(ObservationActivity.this, "All observations deleted.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại dữ liệu khi quay lại màn hình này
        if(dbHelper != null && obsAdapter != null && currentHikeId != -1) {
            // Truyền biến hikeId đã lưu vào đây
            List<Observation> newList = dbHelper.getObservationsByHikeId(currentHikeId);
            if (newList != null) {
                obsList.clear();
                obsList.addAll(newList);
                obsAdapter.notifyDataSetChanged();
            }
        }
    }
}
