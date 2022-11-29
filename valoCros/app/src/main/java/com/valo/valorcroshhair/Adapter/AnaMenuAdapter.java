package com.valo.valorcroshhair.Adapter;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.valo.valorcroshhair.Claslar.AdminVeriEkle;
import com.valo.valorcroshhair.Fragment.HomeFragment;
import com.valo.valorcroshhair.MainActivity;
import com.valo.valorcroshhair.OnLoadMoreListener;
import com.valo.valorcroshhair.R;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class AnaMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mContext;
    ArrayList<Object> adminVeriEkleArrayList;
    private AdminVeriEkle adminVeriEkle;
    private boolean isLoading = false;
    private int visibleThreshold = 1;
    private OnLoadMoreListener onLoadMoreListener;



    public AnaMenuAdapter(Context mContext, ArrayList<Object> adminVeriEkleArrayList, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.adminVeriEkleArrayList = adminVeriEkleArrayList;


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int total = getTotal(recyclerView);
                int lastVisible = getLastVisiblePosition(recyclerView);
                if (!getLoading() && total <= lastVisible + visibleThreshold)
                {
                    if (onLoadMoreListener != null)
                        onLoadMoreListener.onLoadMore();
                    setLoading(true);
                }
            }
        });
    }
    private int getTotal(RecyclerView recyclerView)
    {
        if (recyclerView != null)
        {
            final RecyclerView.LayoutManager layoutManager = recyclerView
                    .getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager)
            {
                return layoutManager.getItemCount();
            }
        }
        return 0;
    }
    public int getLastVisiblePosition(RecyclerView recyclerView)
    {
        if (recyclerView != null)
        {
            final RecyclerView.LayoutManager layoutManager = recyclerView
                    .getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager)
            {
                return ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
            }
        }
        return 0;
    }
    public boolean getLoading()
    {
        return isLoading;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
    {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public void setLoading(boolean loading)
    {
        isLoading = loading;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ana_sayfa_croslar, parent, false);
            final MyMenuHolder myMenuHolder = new MyMenuHolder(view);
            return myMenuHolder;
        }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof MyMenuHolder){
            final MyMenuHolder myMenuHolder = (MyMenuHolder) holder;
            adminVeriEkle =  (AdminVeriEkle) adminVeriEkleArrayList.get(position);
            myMenuHolder.crosText.setText(adminVeriEkle.getCrosName());

            Picasso.get().load(adminVeriEkle.getUrl()).into(myMenuHolder.crosImage);
            Picasso.get().load(adminVeriEkle.getUrl()).into(myMenuHolder.crosImageGenis);

            myMenuHolder.btnCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s;
                    ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Code",adminVeriEkle.getCrosCode());
                    clipboardManager.setPrimaryClip(clipData);
                    if (clipboardManager !=null){
                        Toast.makeText(mContext, "Code Copied", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            
            boolean isExpanded = ((AdminVeriEkle) adminVeriEkleArrayList.get(position)).isExpanded();
            myMenuHolder.constraintLayoutChild.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        } else if (holder instanceof ViewHolderLoading){
            ViewHolderLoading viewHolderLoading = (ViewHolderLoading) holder;
            viewHolderLoading.progressBar.setIndeterminate(true);
        }
    }



    @Override
    public int getItemCount() {
        return adminVeriEkleArrayList.size();
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public class MyMenuHolder extends RecyclerView.ViewHolder{
        ImageView crosImage,crosImageGenis;
        TextView crosText;
        CardView cardView;
        ConstraintLayout constraintLayoutChild;
        ConstraintLayout constraintLayoutParent;
        Button btnCopy;
        public MyMenuHolder(@NonNull View itemView) {
            super(itemView);


            constraintLayoutParent = itemView.findViewById(R.id.constraintLayoutParent);
            constraintLayoutChild = itemView.findViewById(R.id.constraintLayoutChild);
            crosImage = itemView.findViewById(R.id.imageViewAvatar);
            crosImageGenis = itemView.findViewById(R.id.imageViewDescription);
            btnCopy = itemView.findViewById(R.id.copyBTN);
            crosText = itemView.findViewById(R.id.textViewTitle);

            cardView = itemView.findViewById(R.id.cardView);
            constraintLayoutParent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AdminVeriEkle city =(AdminVeriEkle) adminVeriEkleArrayList.get(getBindingAdapterPosition());
                    city.setExpanded(!city.isExpanded());
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });

        }
    }

    public class ViewHolderLoading extends RecyclerView.ViewHolder {

        MaterialProgressBar progressBar;

        public ViewHolderLoading(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.meZhanghai);
        }
    }



}
