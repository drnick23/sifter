package com.codepath.drnick.sifter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nick on 10/19/16.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articleList) {
        super(context, android.R.layout.simple_list_item_1, articleList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Article article = this.getItem(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.tvTitle.setText(article.getHeadline());
        if (!TextUtils.isEmpty(article.getThumbnail())) {
            Picasso.with(getContext()).load(article.getThumbnail()).into(holder.ivThumbnail);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivThumbnail) ImageView ivThumbnail;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
