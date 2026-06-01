package com.cookandroid.password;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PasswordListActivity extends AppCompatActivity {

    private RecyclerView rvPasswordList;
    private LinearLayout layoutEmptyState;
    private ImageButton btnBack;

    private DatabaseHelper dbHelper;
    private PasswordAdapter adapter;
    private List<PasswordModel> passwordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 캡처 및 화면 녹화 방지 보안 플래그 설정
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        
        setContentView(R.layout.activity_password_list);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        loadPasswords();

        // 뒤로가기 버튼 이벤트
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        rvPasswordList = findViewById(R.id.rv_password_list);
        layoutEmptyState = findViewById(R.id.layout_empty_state);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupRecyclerView() {
        rvPasswordList.setLayoutManager(new LinearLayoutManager(this));
        
        // 어댑터 초기화 (삭제 클릭 시 다이얼로그 호출)
        adapter = new PasswordAdapter(passwordList, new PasswordAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(final PasswordModel model) {
                showDeleteConfirmDialog(model);
            }
        });
        rvPasswordList.setAdapter(adapter);
    }

    // DB에서 데이터 로드 및 UI 갱신
    private void loadPasswords() {
        passwordList = dbHelper.getAllPasswords();
        
        if (passwordList.isEmpty()) {
            rvPasswordList.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvPasswordList.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
            adapter.updateData(passwordList);
        }
    }

    // 삭제 확인 팝업창
    private void showDeleteConfirmDialog(final PasswordModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 삭제");
        builder.setMessage(model.getSiteName() + " (" + model.getUserId() + ") 정보를 정말 삭제하시겠습니까?");
        
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DB에서 삭제
                dbHelper.deletePassword(model.getId());
                // 다시 목록 로드
                loadPasswords();
            }
        });
        
        builder.setNegativeButton("취소", null);
        builder.show();
    }
}
