package com.example.lab5contactdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import java.util.List;

public class HikerAdapter extends RecyclerView.Adapter<HikerAdapter.HikeViewHolder> {

    private List<Hike> hikeList;
    private Context context;

    public HikerAdapter(List<Hike> hikeList, Context context) {
        this.hikeList = hikeList;
        this.context = context;
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hike_view, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        // Lấy đối tượng Hike tại vị trí ban đầu
        Hike currentHike = hikeList.get(position);

        holder.hikeNameTextView.setText(currentHike.getName());
        holder.locationTextView.setText("Location: " + currentHike.getLocation());
        holder.dateTextView.setText("Date: " + currentHike.getDateandtime());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vị trí chính xác tại thời điểm nút được click
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Lấy lại Hike hiện tại bằng vị trí chính xác
                    Hike hikeToEdit = hikeList.get(adapterPosition);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("HIKE_TO_EDIT", (Serializable) hikeToEdit);
                    context.startActivity(intent);
                }
            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vị trí chính xác tại thời điểm nút được click
                int adapterPosition = holder.getAdapterPosition();

                // Kiểm tra xem vị trí có hợp lệ không (-1 nghĩa là item đã bị xóa)
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return; // Item đã bị xóa hoặc không còn hợp lệ, không làm gì cả
                }

                // Lấy lại Hike hiện tại bằng vị trí chính xác
                Hike hikeToDelete = hikeList.get(adapterPosition);

                new AlertDialog.Builder(context)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete '" + hikeToDelete.getName() + "' ?") // Sử dụng hikeToDelete
                        .setPositiveButton("Delete", (dialog, which) -> {
                            DatabaseHelper dbHelper = new DatabaseHelper(context);
                            int rowsDeleted = dbHelper.deleteHike(hikeToDelete.getId()); // Sử dụng ID của hikeToDelete

                            if (rowsDeleted > 0) {
                                Toast.makeText(context, "Delete successfully!" + hikeToDelete.getName(), Toast.LENGTH_SHORT).show(); // Sử dụng hikeToDelete
                                // Xóa item khỏi danh sách và cập nhật RecyclerView
                                hikeList.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                notifyItemRangeChanged(adapterPosition, hikeList.size()); // Cập nhật vị trí các item còn lại
                            } else {
                                Toast.makeText(context, "Error! Can not delete: " + hikeToDelete.getName(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        holder.obsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Lấy vị trí chính xác của item tại thời điểm click
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                // Lấy lại đối tượng Hike tại vị trí đó để đảm bảo dữ liệu chính xác
                Hike selectedHike = hikeList.get(adapterPosition);

                // đến màn hình danh sách các quan sát (ObservationActivity)
                Intent intent = new Intent(context, ObservationActivity.class);

                // Đính kèm dữ liệu quan trọng vào Intent để màn hình tiếp theo có thể nhận được:
                // 1. ID của chuyến đi: để biết cần truy vấn observation của chuyến đi nào.
                intent.putExtra("hike_id", selectedHike.getId());
                // 2. Tên của chuyến đi: để hiển thị làm tiêu đề.
                intent.putExtra("hike_name", selectedHike.getName());

                context.startActivity(intent);

            }
        });
}
    public void setFilteredList(List<Hike> filteredList) {
        this.hikeList = filteredList;
        notifyDataSetChanged(); // Lệnh này bảo RecyclerView vẽ lại ngay lập tức
    }
    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public static class HikeViewHolder extends RecyclerView.ViewHolder {
        TextView hikeNameTextView;
        TextView locationTextView;
        TextView dateTextView;
        Button editButton;
        Button deleteButton;
        Button obsButton;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeNameTextView = itemView.findViewById(R.id.tvName);
            locationTextView = itemView.findViewById(R.id.tvLocation);
            dateTextView = itemView.findViewById(R.id.tvDate);

            editButton = itemView.findViewById(R.id.edit_btn);
            deleteButton = itemView.findViewById(R.id.del_btn);
            obsButton = itemView.findViewById(R.id.obs_btn);
        }
    }
}