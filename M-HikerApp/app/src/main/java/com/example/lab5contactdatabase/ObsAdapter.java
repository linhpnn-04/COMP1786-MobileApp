package com.example.lab5contactdatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class ObsAdapter extends RecyclerView.Adapter<ObsAdapter.ObsViewHolder> {

    private List<Observation> obsList;
    private Context context;

    public ObsAdapter(List<Observation> obsList, Context context) {
        this.obsList = obsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ObsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.observation_view, parent, false);
        return new ObsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObsViewHolder holder, int position) {
        // Lấy đối tượng Hike tại vị trí ban đầu
        Observation currentObs = obsList.get(position);

        holder.obsTextView.setText(currentObs.getObservation());
        holder.TimeTextView.setText("Time: " + currentObs.getTime());
        holder.CommentTextView.setText("Comment: " + currentObs.getComments());

        String imageUriString = currentObs.getImages();
        if (imageUriString != null && !imageUriString.isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
            try {
                android.net.Uri imageUri = android.net.Uri.parse(imageUriString);

                java.io.InputStream inputStream = holder.itemView.getContext().getContentResolver().openInputStream(imageUri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                holder.imageView.setImageBitmap(bitmap);

                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (java.io.FileNotFoundException e) {
                // Nếu không tìm thấy file ảnh (ví dụ đã bị xóa), hide image
                android.util.Log.e("ObsAdapter", "Image file not found: " + imageUriString, e);
                holder.imageView.setVisibility(View.GONE);
            } catch (java.io.IOException e) {
                android.util.Log.e("ObsAdapter", "IO Exception while closing stream", e);
            }
        } else {
            // Nếu không có đường dẫn ảnh trong database, hide
            holder.imageView.setVisibility(View.GONE);
        }
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vị trí chính xác tại thời điểm nút được click
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Lấy lại obs hiện tại bằng vị trí chính xác
                    Observation obsToEdit = obsList.get(adapterPosition);
                    Intent intent = new Intent(context, Obs_MainActivity.class);

                    intent.putExtra("OBS_TO_EDIT", (Serializable) obsToEdit);
                    intent.putExtra("hike_id", obsToEdit.getHikeId());
                    context.startActivity(intent);
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vị trí chính xác tại thời điểm nút được click
                int adapterPosition = holder.getAdapterPosition(); // <--- DÒNG QUAN TRỌNG

                // Kiểm tra xem vị trí có hợp lệ không (-1 nghĩa là item đã bị xóa)
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return; // Item đã bị xóa hoặc không còn hợp lệ, không làm gì cả
                }

                // Lấy lại obs hiện tại bằng vị trí chính xác
                // RẤT QUAN TRỌNG: Lấy currentobs BẰNG VỊ TRÍ MỚI adapterPosition
                Observation obsToDelete = obsList.get(adapterPosition);

                new AlertDialog.Builder(context)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete '" + obsToDelete.getObservation() + "' ?") // Sử dụng hikeToDelete
                        .setPositiveButton("Delete", (dialog, which) -> {
                            DatabaseHelper dbHelper = new DatabaseHelper(context);
                            int rowsDeleted = dbHelper.deleteObs(obsToDelete.getId()); // Sử dụng ID của hikeToDelete

                            if (rowsDeleted > 0) {
                                Toast.makeText(context, "Delete successfully!" + obsToDelete.getObservation(), Toast.LENGTH_SHORT).show();
                                // Xóa item khỏi danh sách và cập nhật RecyclerView
                                obsList.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                notifyItemRangeChanged(adapterPosition, obsList.size()); // Cập nhật vị trí các item còn lại
                            } else {
                                Toast.makeText(context, "Error! Can not delete: " + obsToDelete.getObservation(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
    public void setFilteredList(List<Observation> filteredList) {
        this.obsList = filteredList;
        notifyDataSetChanged(); // Lệnh này bảo RecyclerView vẽ lại ngay lập tức
    }
    @Override
    public int getItemCount() {
        return obsList.size();
    }
    public static class ObsViewHolder extends RecyclerView.ViewHolder {
        TextView obsTextView;
        TextView TimeTextView;
        TextView CommentTextView;
        Button editButton;
        Button deleteButton;
        ImageView imageView;


        public ObsViewHolder(@NonNull View itemView) {
            super(itemView);
            obsTextView = itemView.findViewById(R.id.tvName);
            TimeTextView = itemView.findViewById(R.id.tvTime);
            CommentTextView = itemView.findViewById(R.id.tvcomments);
            editButton = itemView.findViewById(R.id.edit_btn);
            deleteButton = itemView.findViewById(R.id.del_btn);
            imageView = itemView.findViewById(R.id.obs_item_image);
        }
    }
}