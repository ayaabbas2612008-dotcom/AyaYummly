/*
package com.example.ayayummly.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ayayummly.R;
import java.util.ArrayList;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.tvUser.setText(comment.getUserName());
        holder.tvText.setText(comment.getCommentText());
        holder.tvDate.setText("Just now"); // تقدري تطوريها لاحقاً

        // سحر الدائرة: بناخد أول حرف من الاسم ونعرضه
        if (comment.getUserName() != null && !comment.getUserName().isEmpty()) {
            String initial = comment.getUserName().substring(0, 1).toUpperCase();
            holder.tvUserInitial.setText(initial);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvText, tvUserInitial, tvDate;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvCommentUser);
            tvText = itemView.findViewById(R.id.tvCommentText);
            tvUserInitial = itemView.findViewById(R.id.tvUserInitial);
            tvDate = itemView.findViewById(R.id.tvCommentDate);
        }
    }
}

 */
package com.example.ayayummly.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ayayummly.R;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.tvUser.setText(comment.getUserName());
        holder.tvText.setText(comment.getCommentText());

        // استخدام الدالة الجديدة لتحويل الوقت
        holder.tvDate.setText(getTimeAgo(comment.getDate()));

        // سحر الدائرة: أول حرف من الاسم
        if (comment.getUserName() != null && !comment.getUserName().isEmpty()) {
            String initial = comment.getUserName().substring(0, 1).toUpperCase();
            holder.tvUserInitial.setText(initial);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    /**
     * دالة سحرية لتحويل الوقت من رقم (long) إلى نص (Time Ago)
     */
    private String getTimeAgo(long time) {
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) return "Just now";

        final long diff = now - time;
        if (diff < 60000) { // أقل من دقيقة
            return "Just now";
        } else if (diff < 3600000) { // أقل من ساعة
            return (diff / 60000) + "m ago";
        } else if (diff < 86400000) { // أقل من يوم
            return (diff / 3600000) + "h ago";
        } else if (diff < 604800000) { // أقل من أسبوع
            return (diff / 86400000) + "d ago";
        } else {
            // إذا مر أكثر من أسبوع، اعرض التاريخ العادي
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(new Date(time));
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvText, tvUserInitial, tvDate;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvCommentUser);
            tvText = itemView.findViewById(R.id.tvCommentText);
            tvUserInitial = itemView.findViewById(R.id.tvUserInitial);
            tvDate = itemView.findViewById(R.id.tvCommentDate);
        }
    }
}
