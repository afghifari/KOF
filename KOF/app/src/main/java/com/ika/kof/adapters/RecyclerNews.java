package com.ika.kof.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.ika.kof.Constant;
import com.ika.kof.R;

/**
 * Created by Alhudaghifari on 7/30/2017.
 */

public class RecyclerNews extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;
    private OnButtonShareClickListener mOnButtonShareClickListener;

    public RecyclerNews(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            default :
                view = LayoutInflater.from(mContext).inflate(R.layout.list_article_design, parent, false);
                return new ViewHolderArticle(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int posisi = getItemViewType(position);
        ViewHolderArticle viewHolderArticle1 = (ViewHolderArticle) holder;
        final int posisiAdapter2 = holder.getAdapterPosition();

        switch (posisi) {
            case 0:
                viewHolderArticle1.mImageViewGambarBerita.setImageResource(R.drawable.gambar1);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText(Constant.judul1);
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText(Constant.keterangan1);
                break;
            case 1:
                viewHolderArticle1.mImageViewGambarBerita.setImageResource(R.drawable.gambar2);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText(Constant.judul2);
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText(Constant.keterangan2);
                break;
            case 2:
                viewHolderArticle1.mImageViewGambarBerita.setImageResource(R.drawable.gambar3);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText(Constant.judul3);
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText(Constant.keterangan3);
                break;
            case 3:
                viewHolderArticle1.mImageViewGambarBerita.setImageResource(R.drawable.gambar4);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText(Constant.judul4);
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText(Constant.keterangan4);
                break;
            case 4:
                viewHolderArticle1.mImageViewGambarBerita.setImageResource(R.drawable.gambar5);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText(Constant.judul5);
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText(Constant.keterangan5);
                break;
            case 5:
                viewHolderArticle1.mImageViewGambarBerita.setImageResource(R.drawable.gambar6);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText(Constant.judul6);
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText(Constant.keterangan6);
                break;
            case 6:
                viewHolderArticle1.mImageViewGambarBerita.setImageResource(R.drawable.gambar7);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText(Constant.judul7);
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText(Constant.keterangan7);
                break;
            case 7:
                viewHolderArticle1.mImageViewGambarBerita.setImageResource(R.drawable.gambar8);
                viewHolderArticle1.mTextViewTeksJudulBerita.setText(Constant.judul8);
                viewHolderArticle1.mTextViewTeksKeteranganBerita.setText(Constant.keterangan8);
                break;
        }

        viewHolderArticle1.mViewContainer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mOnArtikelClickListener != null) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mOnArtikelClickListener != null)
                                        mOnArtikelClickListener.onClick(posisiAdapter2);
                                }
                            }, 250);
                        }
                    }
                }
        );

        viewHolderArticle1.mImageButtonBagikan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mOnButtonShareClickListener != null) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (mOnButtonShareClickListener != null) {
                                        mOnButtonShareClickListener.onClick(posisiAdapter2);
                                    }
                                }
                            }, 250);
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return Constant.total_article;
    }

    //JIKA CONTAINER DI KLIK
    public interface OnArtikelClickListener {

        void onClick(int posisi);
    }

    //INTERFACE JIKA TOMBOL SHARE DI KLIK
    public interface OnButtonShareClickListener {

        void onClick(int posisi);
    }

    public void setOnArtikelClickListener(OnArtikelClickListener onArtikelClickListener) {
        mOnArtikelClickListener = onArtikelClickListener;
    }

    public void setOnButtonShareClickListener(OnButtonShareClickListener onButtonShareClickListener) {
        mOnButtonShareClickListener = onButtonShareClickListener;
    }

    protected class ViewHolderArticle extends RecyclerView.ViewHolder {
        public View mViewContainer;
        public CardView mCardViewContainer;
        
        public ImageView mImageViewGambarBerita;
        
        public TextView mTextViewTeksJudulBerita;
        public TextView mTextViewTeksKeteranganBerita;

        public ImageButton mImageButtonBagikan;

        public ViewHolderArticle(View itemView) {
            super(itemView);

            mViewContainer = itemView;
            mCardViewContainer = (CardView) itemView.findViewById(R.id.cardview_container);
            
            mImageViewGambarBerita = (ImageView) itemView.findViewById(R.id.gambar_berita);
            
            mTextViewTeksJudulBerita = (TextView) itemView.findViewById(R.id.teks_judulberita);
            mTextViewTeksKeteranganBerita = (TextView) itemView.findViewById(R.id.teks_keteranganpanjang);
            
            mImageButtonBagikan = (ImageButton) itemView.findViewById(R.id.tombol_share_berita);
        }
    }
}
