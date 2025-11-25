package com.example.lab5contactdatabase; // Đổi tên package cho đúng với của bạn

import android.net.Uri;
import java.io.Serializable;

public class Observation implements Serializable { // <--- THÊM implements Serializable VÀO ĐÂY    private int id;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;  // URI của ảnh user chọn
    private int hikeId; // KHÓA NGOẠI: Để biết quan sát này thuộc chuyến đi nào
    private String observation;
    private String time;
    private String comments;
    private String images;
    private int id;


    // Constructor
    public Observation(int id, int hikeId, String observation, String time, String comments, String images) {
        this.id = id;
        this.hikeId = hikeId;
        this.observation = observation;
        this.time = time;
        this.comments = comments;
        this.images= images;
    }

    // Getter & Setter
    public int getId() { return id; }
    public int getHikeId() { return hikeId; }
    public String getObservation() { return observation; }
    public String getTime() { return time; }
    public String getComments() { return comments; }

    public String getImages() {
        return images;
    }
}