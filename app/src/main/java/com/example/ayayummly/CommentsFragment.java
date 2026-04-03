package com.example.ayayummly;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ayayummly.classes.Comment;
import com.example.ayayummly.classes.CommentAdapter;
import com.example.ayayummly.classes.FirebaseServices;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;

public class CommentsFragment extends Fragment {

    private FirebaseServices fbs;
    private RecyclerView rvComments;
    private EditText etComment;
    private ImageView btnPostComment;
    private ArrayList<Comment> commentList;
    private CommentAdapter commentAdapter;
    private String recipeId;

    public CommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. استقبال الـ ID أولاً
        if (getArguments() != null) {
            recipeId = getArguments().getString("recipeId");
        }

        init(view);
    }

    private void init(View view) {
        fbs = FirebaseServices.getInstance();
        rvComments = view.findViewById(R.id.rvCommentsPage);
        etComment = view.findViewById(R.id.etCommentPage);
        btnPostComment = view.findViewById(R.id.btnPostCommentPage);

        commentList = new ArrayList<>();

        // 2. إعداد الـ RecyclerView والـ Adapter (مرة واحدة فقط هنا)
        commentAdapter = new CommentAdapter(getContext(), commentList);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComments.setAdapter(commentAdapter);

        btnPostComment.setOnClickListener(v -> postComment());

        // 3. تحميل التعليقات
        loadComments();
    }

    private void postComment() {
        String text = etComment.getText().toString().trim();
        if (text.isEmpty()) return;

        if (recipeId == null) {
            Toast.makeText(getContext(), "Error: Recipe ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = fbs.getAuth().getUid();
        // أخذ الجزء الأول من الإيميل كاسم مستخدم (اختياري للجمالية)
        String fullEmail = fbs.getAuth().getCurrentUser().getEmail();
        String userName = fullEmail != null ? fullEmail.split("@")[0] : "User";

        Comment newComment = new Comment(text, userName, userId, recipeId);

        fbs.getFire().collection("comments").add(newComment)
                .addOnSuccessListener(doc -> {
                    etComment.setText("");
                    Toast.makeText(getContext(), "Commented! ✅", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadComments() {
        if (recipeId == null) return;

        fbs.getFire().collection("comments")
                .whereEqualTo("recipeId", recipeId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (value != null) {
                        commentList.clear(); // تنظيف القائمة القديمة
                        for (DocumentSnapshot doc : value) {
                            Comment comment = doc.toObject(Comment.class);
                            if (comment != null) {
                                commentList.add(comment);
                            }
                        }

                        // تحديث الـ Adapter فقط بدون إعادة تعريفه (هذا يمنع التعليق)
                        commentAdapter.notifyDataSetChanged();

                        // النزول لآخر تعليق تلقائياً عند وصول تعليق جديد
                        if (commentList.size() > 0) {
                            rvComments.smoothScrollToPosition(commentList.size() - 1);
                        }
                    }
                });
    }
}