package com.example.lab5contactdatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import androidx.annotation.NonNull;
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> contactList;

    private String avatarUri;
    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.tvName.setText("Name: " + contact.getName());
        holder.tvDob.setText("Dob: " + contact.getDob());
        holder.tvEmail.setText("Email: " + contact.getEmail());

        String imageUriString = contact.getAvatarUri();
        if (imageUriString != null && !imageUriString.isEmpty()) {
            try {
                android.net.Uri imageUri = android.net.Uri.parse(imageUriString);

                // Sửa lỗi: Sử dụng ContentResolver để lấy dữ liệu ảnh từ Uri
                java.io.InputStream inputStream = holder.itemView.getContext().getContentResolver().openInputStream(imageUri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                holder.imgAvatar.setImageBitmap(bitmap);

                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (java.io.FileNotFoundException e) {
                // Nếu không tìm thấy file ảnh (ví dụ đã bị xóa), đặt ảnh mặc định
                android.util.Log.e("ContactAdapter", "Image file not found: " + imageUriString, e);
                holder.imgAvatar.setImageResource(R.drawable.df);
            } catch (java.io.IOException e) {
                android.util.Log.e("ContactAdapter", "IO Exception while closing stream", e);
            }
        } else {
            // Nếu không có đường dẫn ảnh trong database, đặt ảnh mặc định
            holder.imgAvatar.setImageResource(R.drawable.pp);
        }}

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvDob;
        ImageView imgAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDob = itemView.findViewById(R.id.tvDob);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }

}
