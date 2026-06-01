package com.cookandroid.password;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    private List<PasswordModel> passwordList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(PasswordModel model);
    }

    public PasswordAdapter(List<PasswordModel> passwordList, OnItemClickListener listener) {
        this.passwordList = passwordList;
        this.listener = listener;
    }

    public void updateData(List<PasswordModel> newList) {
        this.passwordList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        PasswordModel model = passwordList.get(position);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    static class PasswordViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSiteName;
        private TextView tvUserId;
        private TextView tvPassword;
        private ImageButton btnDelete;
        private ImageButton btnToggleVisibility;
        private boolean isPasswordVisible = false;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSiteName = itemView.findViewById(R.id.tv_item_site_name);
            tvUserId = itemView.findViewById(R.id.tv_item_user_id);
            tvPassword = itemView.findViewById(R.id.tv_item_password);
            btnDelete = itemView.findViewById(R.id.btn_item_delete);
            btnToggleVisibility = itemView.findViewById(R.id.btn_item_toggle_visibility);
        }

        public void bind(final PasswordModel model, final OnItemClickListener listener) {
            tvSiteName.setText(model.getSiteName());
            tvUserId.setText(model.getUserId());
            
            // 초기 상태는 항상 숨김
            isPasswordVisible = false;
            tvPassword.setText(getMaskedPassword(model.getPassword().length()));
            btnToggleVisibility.setColorFilter(itemView.getContext().getResources().getColor(android.R.color.darker_gray));

            // 비밀번호 표시/숨김 토글
            btnToggleVisibility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isPasswordVisible = !isPasswordVisible;
                    if (isPasswordVisible) {
                        tvPassword.setText(model.getPassword());
                        // 활성화 시 파란색 계열로 아이콘 tint 변경
                        btnToggleVisibility.setColorFilter(v.getContext().getResources().getColor(android.R.color.holo_blue_dark));
                    } else {
                        tvPassword.setText(getMaskedPassword(model.getPassword().length()));
                        // 비활성화 시 다시 회색
                        btnToggleVisibility.setColorFilter(v.getContext().getResources().getColor(android.R.color.darker_gray));
                    }
                }
            });

            // 비밀번호 영역 클릭 시 클립보드 복사 기능 제공
            tvPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Password", model.getPassword());
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(v.getContext(), model.getSiteName() + " 비밀번호가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 삭제 버튼 클릭 이벤트 위임
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeleteClick(model);
                    }
                }
            });
        }

        // 비밀번호 길이만큼 도트(•) 스트링 생성
        private String getMaskedPassword(int length) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(length, 16); i++) {
                sb.append("•");
            }
            return sb.toString();
        }
    }
}
