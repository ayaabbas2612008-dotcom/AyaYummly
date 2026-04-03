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
