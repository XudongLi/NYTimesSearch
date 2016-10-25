package com.lixudong.android.nytarticlesearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixudong.android.nytarticlesearch.R;
import com.lixudong.android.nytarticlesearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

    private OnItemClickListener mOnItemClickListener;
    private List<Article> articles;
    private Context mContext;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public ArticlesAdapter(Context context, List<Article> articles, OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        this.articles = articles;
        this.mContext = context;
    }

    private Context getContext() {
        return this.mContext;
    }

    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticlesAdapter.ViewHolder viewHolder, final int position) {
        Article article = articles.get(position);

        TextView tvTitle = viewHolder.tvTitle;
        tvTitle.setText(article.getHeadline());

        ImageView imageView = viewHolder.ivImage;
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
