package com.valo.valorcroshhair.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valo.valorcroshhair.Adapter.AnaMenuAdapter;
import com.valo.valorcroshhair.Claslar.AdminVeriEkle;
import com.valo.valorcroshhair.MainActivity;
import com.valo.valorcroshhair.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


public class HomeFragment extends Fragment {
    private View view;
    private Button adminBtn;
    private FirebaseUser mUser;
    private AdminVeriEkle adminVeriEkle;
    private Dialog adminDialog;

    private RecyclerView recyclerView;
    private ArrayList<Object> mList;
    private AnaMenuAdapter myAdapter;


    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;


    private AdView mAdView;
    private ImageView image;
    private Animation animation;

    public static final int ITEMS_PER_AD = 9;

    public void init(){

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        adminBtn = view.findViewById(R.id.adminBtnID);
        recyclerView = view.findViewById(R.id.rv3);
        mList = new ArrayList<Object>();
        mAdView = view.findViewById(R.id.adView);
        image = view.findViewById(R.id.qweqwe);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.network_yok);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();



        adminSorgu("gbMTEh2dMkdUI5l2ypBiCwirQdo2");

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            adminDialogOpen();
            }
        });

        MobileAds.initialize(view.getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Toast.makeText(view.getContext(), "AdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){


          RecyclerViewSetting();
          FirebaseRecyclerViewVeriAktarimi();

        }else {
            image.setVisibility(View.VISIBLE);
            image.startAnimation(animation);
            Toast.makeText(getContext(),"internet yok", Toast.LENGTH_SHORT).show();
        }

        return view;
    }




    public void adminSorgu(String adminMUID){
        if (mUser != null){
            if (Objects.equals(mUser.getUid(),adminMUID)){
                adminBtn.setVisibility(View.VISIBLE);
            }
        }else
            adminBtn.setVisibility(View.INVISIBLE);
    }

    private void adminDialogOpen(){

        EditText codeEditText,urlEditText,nameEditText;
        Button ekleBtn,trolEkleBtn,proEkleBtn;

        adminDialog = new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        adminDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adminDialog.setContentView(R.layout.admin_veri_ekle_dialog);
        Window window = adminDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        adminDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        adminDialog.show();

        codeEditText = adminDialog.findViewById(R.id.textInputCodeEditText);
        urlEditText = adminDialog.findViewById(R.id.textInputUrlEditText);
        nameEditText = adminDialog.findViewById(R.id.textInputCrosNameEditText);

        ekleBtn = adminDialog.findViewById(R.id.AnaSayfaEkle);
        trolEkleBtn = adminDialog.findViewById(R.id.TrolSayfaEkle);
        proEkleBtn = adminDialog.findViewById(R.id.ProSayfaEkle);

      

        ekleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String  codeTutucu = codeEditText.getText().toString();
                String nameTutucu = nameEditText.getText().toString();
                String urlTutucu = urlEditText.getText().toString();
                String RandomUuid = UUID.randomUUID().toString();
               if (!TextUtils.isEmpty(codeTutucu)){
                   if (!TextUtils.isEmpty(nameTutucu)){
                       if (!TextUtils.isEmpty(urlTutucu)){
                            adminVeriEkle = new AdminVeriEkle(urlTutucu,nameTutucu,codeTutucu);
                           mDatabase.child("users").child(RandomUuid).setValue(adminVeriEkle);
                           Toast.makeText(view.getContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                       }else
                           Toast.makeText(getContext(), "url boş", Toast.LENGTH_SHORT).show();
                   }else
                       Toast.makeText(getContext(), "name boş", Toast.LENGTH_SHORT).show();
               }else
                   Toast.makeText(getContext(), "code boş", Toast.LENGTH_SHORT).show();

            }
        });
        trolEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  codeTutucu = codeEditText.getText().toString();
                String nameTutucu = nameEditText.getText().toString();
                String urlTutucu = urlEditText.getText().toString();
                String RandomUuid = UUID.randomUUID().toString();
                if (!TextUtils.isEmpty(codeTutucu)){
                    if (!TextUtils.isEmpty(nameTutucu)){
                        if (!TextUtils.isEmpty(urlTutucu)){
                            adminVeriEkle = new AdminVeriEkle(urlTutucu,nameTutucu,codeTutucu);
                            mDatabase.child("Trol").child(RandomUuid).setValue(adminVeriEkle);
                            Toast.makeText(view.getContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getContext(), "url boş", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getContext(), "name boş", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getContext(), "code boş", Toast.LENGTH_SHORT).show();
            }
        });
        proEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  codeTutucu = codeEditText.getText().toString();
                String nameTutucu = nameEditText.getText().toString();
                String urlTutucu = urlEditText.getText().toString();
                String RandomUuid = UUID.randomUUID().toString();
                if (!TextUtils.isEmpty(codeTutucu)){
                    if (!TextUtils.isEmpty(nameTutucu)){
                        if (!TextUtils.isEmpty(urlTutucu)){
                            adminVeriEkle = new AdminVeriEkle(urlTutucu,nameTutucu,codeTutucu);
                            mDatabase.child("Pro").child(RandomUuid).setValue(adminVeriEkle);
                            Toast.makeText(view.getContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getContext(), "url boş", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getContext(), "name boş", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getContext(), "code boş", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void RecyclerViewSetting(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        myAdapter = new AnaMenuAdapter(view.getContext(),mList,recyclerView);
        recyclerView.setAdapter(myAdapter);


    }
    private void FirebaseRecyclerViewVeriAktarimi() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mList != null){
                    mList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AdminVeriEkle veriEkle = dataSnapshot.getValue(AdminVeriEkle.class);
                    mList.add(veriEkle);

                }



                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



    }



    public void procressDialogShow(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Veri Alınırkem Lütfen Bekleyin...");
        progressDialog.show();
    }


}