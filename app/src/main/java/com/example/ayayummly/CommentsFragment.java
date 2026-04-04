/*package com.example.ayayummly;

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


 */

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
import android.widget.Toast;

import com.example.ayayummly.classes.Comment;
import com.example.ayayummly.classes.CommentAdapter;
import com.example.ayayummly.classes.FirebaseServices;
import com.example.ayayummly.classes.Recipe;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

public class CommentsFragment extends Fragment {

    private FirebaseServices fbs;
    private RecyclerView rvComments;
    private CommentAdapter adapter;
    private ArrayList<Comment> commentList;
    private EditText etComment;
    private MaterialButton btnSend;

    private String recipeId;
    private String currentUserId;

    public CommentsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // استلام البيانات اللي بعتيها من RecipeDetailsFragment
        if (getArguments() != null) {
            recipeId = getArguments().getString("recipeId");
            currentUserId = getArguments().getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        fbs = FirebaseServices.getInstance();

        // الربط مع الـ IDs الموجودة في الـ XML تبعك
        rvComments = view.findViewById(R.id.rvCommentsPage);
        etComment = view.findViewById(R.id.etCommentPage);
        btnSend = view.findViewById(R.id.btnPostCommentPage);

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(getContext(), commentList);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComments.setAdapter(adapter);

        // إذا المستخدم مش مسجل دخول، بنخفي شريط التعليق
        if (fbs.getAuth().getCurrentUser() == null) {
            if (view.findViewById(R.id.commentInputCard) != null) {
                view.findViewById(R.id.commentInputCard).setVisibility(View.GONE);
            }
        }

        loadComments();

        btnSend.setOnClickListener(v -> {
            String text = etComment.getText().toString().trim();
            if (!text.isEmpty()) {
                addComment(text);
            }
        });
    }

    private void loadComments() {
        if (recipeId == null) return;

        fbs.getFire().collection("recipes").document(recipeId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Error loading comments", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null && value.exists()) {
                        commentList.clear();
                        // تحويل الوثيقة لكائن Recipe وجلب قائمة التعليقات منه
                        Recipe recipe = value.toObject(Recipe.class);
                        if (recipe != null && recipe.getComments() != null) {
                            commentList.addAll(recipe.getComments());
                        }
                        adapter.notifyDataSetChanged();

                        // التمرير لآخر تعليق
                        if (commentList.size() > 0) {
                            rvComments.smoothScrollToPosition(commentList.size() - 1);
                        }
                    }
                });
    }

    private void addComment(String text) {
        if (fbs.getAuth().getCurrentUser() == null) return;

        String name = fbs.getAuth().getCurrentUser().getDisplayName();
        if (name == null || name.isEmpty()) {
            name = fbs.getAuth().getCurrentUser().getEmail().split("@")[0];
        }

        // System.currentTimeMillis() بتعطينا الوقت الحالي بالملي ثانية
        Comment newComment = new Comment(text, name, currentUserId, recipeId, System.currentTimeMillis());

        fbs.getFire().collection("recipes").document(recipeId)
                .update("comments", FieldValue.arrayUnion(newComment))
                .addOnSuccessListener(aVoid -> etComment.setText(""))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }
}
