package com.ika.kof.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.ImageView;
import android.widget.TextView;

import com.ika.kof.Constant;
import com.ika.kof.R;

/**
 * Created by Alhudaghifari on 7/11/2017.
 */

public class RecyclerContact extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;

    public RecyclerContact(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0 :
                view = LayoutInflater.from(mContext).inflate(R.layout.list_phone_design, parent, false);
//                view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bluesky1));
                return new ViewHolderPhone(view);
            default :
                view = LayoutInflater.from(mContext).inflate(R.layout.list_email_design, parent, false);
//                view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_pollar));
                return new ViewHolderEmail(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int posisi = getItemViewType(position);
        switch (posisi) {
            case 0 :
                ViewHolderPhone viewHolderPhone = (ViewHolderPhone) holder;
                final int posisiAdapter = holder.getAdapterPosition();

                viewHolderPhone.mViewContainer.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (mOnArtikelClickListener != null) {

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mOnArtikelClickListener.onClick(posisiAdapter);
                                        }
                                    }, 250);
                                }
                            }
                        }
                );

                break;
            case 1 :
                ViewHolderEmail viewHolderEmail = (ViewHolderEmail) holder;
                final int posisiAdapter2 = holder.getAdapterPosition();

                viewHolderEmail.mViewContainer.setOnClickListener(
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

                break;
        }
    }

    @Override
    public int getItemCount() {
        return Constant.total_contact;
    }

    //JIKA CONTAINER DI KLIK
    public interface OnArtikelClickListener {

        void onClick(int posisi);
    }

    public void setOnArtikelClickListener(OnArtikelClickListener onArtikelClickListener) {
        mOnArtikelClickListener = onArtikelClickListener;
    }

    protected class ViewHolderPhone extends ViewHolder {
        public View mViewContainer;

        public ImageView imageView;
        public TextView textView;

        public ViewHolderPhone(View itemView) {
            super(itemView);

            mViewContainer = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.logo_phone);
            textView = (TextView) itemView.findViewById(R.id.text_phone);
        }
    }

    protected class ViewHolderEmail extends ViewHolder {
        public View mViewContainer;

        public ImageView imageView;
        public TextView textView;

        public ViewHolderEmail(View itemView) {
            super(itemView);

            mViewContainer = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.logo_phone);
            textView = (TextView) itemView.findViewById(R.id.text_phone);
        }
    }
}
