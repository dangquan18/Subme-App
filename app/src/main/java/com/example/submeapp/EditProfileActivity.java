package com.example.submeapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnChangePhoto;
    private TextInputEditText editName;
    private TextInputEditText editEmail;
    private TextInputEditText editPhone;
    private TextInputEditText editAddress;
    private TextInputEditText editDateOfBirth;
    private Button btnSave;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnBack = findViewById(R.id.btnBack);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editDateOfBirth = findViewById(R.id.editDateOfBirth);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnBack.setOnClickListener(v -> finish());

        btnChangePhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            // TODO: Implement image picker
        });

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String email = editEmail.getText().toString();
            String phone = editPhone.getText().toString();
            String address = editAddress.getText().toString();
            String dob = editDateOfBirth.getText().toString();

            // TODO: Save profile data
            Toast.makeText(this, "Đã lưu thay đổi", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}

