package com.valo.valorcroshhair.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.valo.valorcroshhair.Claslar.ShareClass;
import com.valo.valorcroshhair.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareHolder> {
    private Context mContext;
    private ArrayList<ShareClass> mList;
    private ShareClass mClas;
    private View view;

    public ShareAdapter(Context mContext, ArrayList<ShareClass> mList, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ShareHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.share_item,parent,false);
        return new ShareHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareHolder holder, int position) {
    mClas = mList.get(position);
        Picasso.get().load(mClas.getCrosProfImage()).resize(100,100).into(holder.imageViewAvatar);
        Picasso.get().load(mClas.getCrosURL()).resize(250,250).into(holder.imageViewPaylasim);
        holder.textViewTitle.setText(mClas.getCrosName());
        holder.textViewAciklama.setText(mClas.getCrosCode());
        holder.userName.setText(mClas.getUserName());

        holder.textViewAciklama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Code",mClas.getCrosCode());
                clipboardManager.setPrimaryClip(clipData);
                if (clipboardManager !=null){
                    Toast.makeText(mContext, "Code Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ShareHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPaylasim;
        CircleImageView imageViewAvatar;
        TextView textViewTitle,textViewAciklama,userName;
        public ShareHolder(@NonNull View itemView) {
            super(itemView);

            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            imageViewPaylasim = itemView.findViewById(R.id.imageViewPaylasim);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAciklama = itemView.findViewById(R.id.textViewAciklama);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}
