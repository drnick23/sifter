package com.codepath.drnick.sifter.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.drnick.sifter.R;
import com.codepath.drnick.sifter.models.Article;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nick on 10/21/16.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private ArrayList<Article> articles;
    private Context mContext;

    // Define listener member variable
    private OnArticlesAdapterListener aaListener;
    public void setOnArticlesAdapterListener(OnArticlesAdapterListener listener) {
        this.aaListener = listener;
    }

    // Define the listener interface
    public interface OnArticlesAdapterListener {
        void onArticleClick(Article article);
    }

    // Pass in the articles array into the constructor
    public ArticlesAdapter(Context context, ArrayList<Article> articleList) {
        articles = articleList;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (aaListener != null) {
                        int position = getAdapterPosition();
                        if (aaListener != null && position != RecyclerView.NO_POSITION) {
                            aaListener.onArticleClick(articles.get(position));
                        }
                    }
                }
            });
        }
    }

    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticlesAdapter.ViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.tvTitle.setText(article.getHeadline());
        if (!TextUtils.isEmpty(article.getThumbnail())) {
            Glide.with(getContext()).load(article.getThumbnail()).into(holder.ivThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
