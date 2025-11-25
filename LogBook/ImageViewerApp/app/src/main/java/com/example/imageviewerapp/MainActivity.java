package com.example.imageviewerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Có thể cần cho các tính năng nâng cao hơn
import android.graphics.drawable.Drawable; // Có thể cần nếu làm việc với Drawable

import android.os.Bundle;
import android.view.View; // Quan trọng cho OnClickListener
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.Animation; // Quan trọng cho Animation
import android.view.animation.AnimationUtils; //
public class MainActivity extends AppCompatActivity {

    int[] images = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5, R.drawable.pic6};

    String[] captions = {
            "Hey you!\n My Cute Pink Bear 🐻",
            "Wait..come here.\nWho allowed you to be this cute?",
            "One little heart,\nfor one special person ❤️",
            "And I will be so sad\n if you don’t understand my mind",
            "Don’t worry. I’ll help you...\nBut if you mess up…\nWe talk 😐🔪",
            "Ehehe… it’s me,\n the one who loves you more than anything, even snacks 🍬🧁"
    };

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        TextView tvDescription = findViewById(R.id.tvDescription);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnPrev = findViewById(R.id.btnPrev);

        Animation fade = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        // Initial load
        imageView.setImageResource(images[index]);
        tvDescription.setText(captions[index]);

        btnNext.setOnClickListener(v -> {
            index = (index + 1) % images.length;
            imageView.startAnimation(fade);
            tvDescription.startAnimation(fade);
            imageView.setImageResource(images[index]);
            tvDescription.setText(captions[index]);
        });

        btnPrev.setOnClickListener(v -> {
            index = (index - 1 + images.length) % images.length;
            imageView.startAnimation(fade);
            tvDescription.startAnimation(fade);
            imageView.setImageResource(images[index]);
            tvDescription.setText(captions[index]);
        });
    }
}
