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

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Article> articles;
    private Context mContext;

    public static int ARTICLE_TYPE_STANDARD = 0;
    public static int ARTICLE_TYPE_HEADLINE = 1;
    public static int ARTICLE_TYPE_FEATURE = 2;

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

    // base class for our different article views that setups up the click listeners
    // and also provides a configure function
    public abstract class BaseArticleViewHolder extends RecyclerView.ViewHolder {

        public BaseArticleViewHolder(View itemView) {
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

        public abstract void configure(Article article);

    }

    public class StandardArticleViewHolder extends BaseArticleViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnail;

        public StandardArticleViewHolder(View itemView) {
            super(itemView);
        }

        public void configure(Article article) {
            tvTitle.setText(article.getHeadline());
            if (!TextUtils.isEmpty(article.getThumbnail())) {
                Glide.with(getContext()).load(article.getThumbnail()).into(ivThumbnail);
            }
        }
    }

    public class HeadlineArticleViewHolder extends BaseArticleViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        public HeadlineArticleViewHolder(View itemView) {
            super(itemView);
        }

        public void configure(Article article) {
            tvTitle.setText(article.getHeadline());
        }
    }

    public class FeatureArticleViewHolder extends BaseArticleViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnail;

        public FeatureArticleViewHolder(View itemView) {
            super(itemView);
        }

        public void configure(Article article) {
            tvTitle.setText(article.getHeadline());
            if (!TextUtils.isEmpty(article.getThumbnail())) {
                Glide.with(getContext()).load(article.getThumbnail()).into(ivThumbnail);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;

        // Inflate the custom layout
        if (viewType == ARTICLE_TYPE_FEATURE) {
            View featureArticleResult = inflater.inflate(R.layout.item_article_feature, parent, false);
            viewHolder = new FeatureArticleViewHolder(featureArticleResult);
        }
        else if (viewType == ARTICLE_TYPE_HEADLINE) {
            View headlineArticleResult = inflater.inflate(R.layout.item_article_headline, parent, false);
            viewHolder = new HeadlineArticleViewHolder(headlineArticleResult);
        }
        else { // ARTICLE_TYPE_STANDARD
            View standardArticleResult = inflater.inflate(R.layout.item_article_result, parent, false);
            viewHolder = new StandardArticleViewHolder(standardArticleResult);
        }

        // Return a new holder instance
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // simply get the article and call the base class configure for said article
        Article article = articles.get(position);
        ((BaseArticleViewHolder) holder).configure(article);
    }

    //private void configureStandardArticleViewHolder()

    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);
        if (article.getThumbnail() == null || article.getThumbnail()=="") {
            return ARTICLE_TYPE_HEADLINE;
        }
        // feature every 3rd article for fun and profit.
        int alternate = position % 3;
        if (alternate == 0) {
            return ARTICLE_TYPE_FEATURE;
        }
        return ARTICLE_TYPE_STANDARD;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
