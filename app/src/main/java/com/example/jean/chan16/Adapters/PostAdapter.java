package com.example.jean.chan16.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jean.chan16.Models.Post;
import com.example.jean.chan16.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements View.OnClickListener {

    private final List<Post> items;
    private final Context context;

    public PostAdapter(Context context, @NonNull List<Post> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = items.get(position);

        holder.mText.setText(post.getText());
        holder.mDetails.setText("Posted by " + post.getAuthor().getDisplayName() +
                                " from " + post.getLocation() +
                                " on " + post.getDate());
        if(post.getImageUri() != null) {
            Glide.with(context).load(post.getImageUri()).into(holder.mImage);
        } else {
            holder.mImage.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(post.getAuthor().getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mText;
        ImageView userImage;
        TextView mDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.postImage);
            mText = itemView.findViewById(R.id.postText);
            userImage = itemView.findViewById(R.id.userImage);
            mDetails = itemView.findViewById(R.id.details);
        }
    }
}
