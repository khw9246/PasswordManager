package com.cookandroid.password;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.view.WindowManager; // 추가됨
import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    private EditText etSiteName;
    private EditText etUserId;
    private EditText etPassword;
    private TextView tvStrengthStatus;
    private ProgressBar pbPasswordStrength;
    private Button btnGeneratePassword;
    private Button btnSave;
    private Button btnViewList; // 추가됨

    private ImageButton btnToggleVisibility;
    private boolean isPasswordVisible = false;

    private DatabaseHelper dbHelper; // 추가됨

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 캡처 및 화면 녹화 방지 보안 플래그 설정
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this); // DB Helper 초기화

        initViews();
        setupListeners();
    }

    private void initViews() {
        etSiteName = findViewById(R.id.et_site_name);
        etUserId = findViewById(R.id.et_user_id);
        etPassword = findViewById(R.id.et_password);
        tvStrengthStatus = findViewById(R.id.tv_strength_status);
        pbPasswordStrength = findViewById(R.id.pb_password_strength);
        btnGeneratePassword = findViewById(R.id.btn_generate_password);
        btnSave = findViewById(R.id.btn_save);
        btnViewList = findViewById(R.id.btn_view_list); // 뷰 연결

        btnToggleVisibility = findViewById(R.id.btn_toggle_visibility);
    }

    private void setupListeners() {
        // 비밀번호 표시/숨김 토글 버튼 클릭 이벤트
        btnToggleVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;

                if (isPasswordVisible) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                etPassword.setTypeface(etUserId.getTypeface());
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        // 비밀번호 입력 실시간 감지 및 강도 측정
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 자동 비밀번호 생성 버튼 클릭 이벤트
        btnGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String securePassword = generateSecurePassword(12);
                etPassword.setText(securePassword);
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        // 저장 버튼 클릭 이벤트
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        // 목록 보기 버튼 클릭 이벤트 (추가됨)
        btnViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PasswordListActivity.class);
                startActivity(intent);
            }
        });
    }

    // 비밀번호 강도 측정 로직
    private void updatePasswordStrength(String password) {
        int score = 0;

        if (password.length() >= 8) score++;
        if (password.matches(".*[a-zA-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) score++;

        pbPasswordStrength.setProgress(score);

        switch (score) {
            case 0:
                tvStrengthStatus.setText("비밀번호");
                tvStrengthStatus.setTextColor(Color.DKGRAY);
                break;
            case 1:
                tvStrengthStatus.setText("비밀번호 강도: 위험");
                tvStrengthStatus.setTextColor(Color.RED);
                break;
            case 2:
                tvStrengthStatus.setText("비밀번호 강도: 약함");
                tvStrengthStatus.setTextColor(Color.parseColor("#FF9800"));
                break;
            case 3:
                tvStrengthStatus.setText("비밀번호 강도: 보통");
                tvStrengthStatus.setTextColor(Color.parseColor("#8BC34A"));
                break;
            case 4:
                tvStrengthStatus.setText("비밀번호 강도: 안전");
                tvStrengthStatus.setTextColor(Color.parseColor("#4CAF50"));
                break;
        }
    }

    // 안전한 무작위 비밀번호 생성 로직 (무조건 '안전' 등급 조건 충족하도록 루핑)
    private String generateSecurePassword(int length) {
        final String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String lowercase = "abcdefghijklmnopqrstuvwxyz";
        final String digits = "0123456789";
        final String specialChars = "!@#$%^&*";
        final String allChars = uppercase + lowercase + digits + specialChars;
        
        SecureRandom random = new SecureRandom();
        String password;

        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(allChars.length());
                sb.append(allChars.charAt(randomIndex));
            }
            password = sb.toString();

            // '안전' 등급 조건(4점) 충족 여부 검사
            boolean hasLength = password.length() >= 8;
            boolean hasLetter = password.matches(".*[a-zA-Z].*");
            boolean hasDigit = password.matches(".*[0-9].*");
            boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

            // 모든 조건 충족 시 최종 반환
            if (hasLength && hasLetter && hasDigit && hasSpecial) {
                break;
            }
        }
        return password;
    }

    // 데이터 검증 및 저장 처리 (DB 저장 방식으로 수정됨)
    private void saveData() {
        String siteName = etSiteName.getText().toString().trim();
        String userId = etUserId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (siteName.isEmpty() || userId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // DB 저장 데이터 객체 생성
        PasswordModel model = new PasswordModel(siteName, userId, password);
        boolean isSuccess = dbHelper.addPassword(model);

        if (isSuccess) {
            Toast.makeText(this, siteName + " 정보가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
            
            // 입력창 초기화
            etSiteName.setText("");
            etUserId.setText("");
            etPassword.setText("");
            
            // 비밀번호 상태 및 UI 초기화
            isPasswordVisible = false;
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            pbPasswordStrength.setProgress(0);
            tvStrengthStatus.setText("비밀번호");
            tvStrengthStatus.setTextColor(Color.DKGRAY);
            
            // 포커스를 사이트명 입력칸으로 이동
            etSiteName.requestFocus();
        } else {
            Toast.makeText(this, "데이터베이스 저장에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
