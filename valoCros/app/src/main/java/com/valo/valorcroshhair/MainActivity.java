package com.valo.valorcroshhair;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;
import com.valo.valorcroshhair.Fragment.HomeFragment;
import com.valo.valorcroshhair.Fragment.KlavuzFragment;
import com.valo.valorcroshhair.Fragment.NewCrossFragment;
import com.valo.valorcroshhair.Fragment.ShareFragment;
import com.valo.valorcroshhair.Fragment.TrollCrossFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private CircleImageView imageViewUserAvatar;
    private ConstraintLayout constraintLayoutNavBar;
    private TextView textViewUserName,textViewUserEmail,textViewHome,textViewYeniCros
            ,textViewTroll,textViewPaylasText,textViewAboutus,textViewLogout;
    private ImageView imageViewIconHome,imageViewIconYeniCros,imageViewIconTroll,imageViewIconPaylas;
    private Toolbar toolbar;
    Animation animation,animation2;

    private InterstitialAd mInterstitialAd;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 90*1000;
    int i = 0;

    public void init(){

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        constraintLayoutNavBar= findViewById(R.id.constraintLayoutNavBar);
        imageViewUserAvatar = findViewById(R.id.imageViewUserAvatar);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewHome=findViewById(R.id.textViewHome);
        imageViewIconYeniCros=findViewById(R.id.imageViewIconBooking);
        imageViewIconHome = findViewById(R.id.imageViewIconHome);
        textViewYeniCros = findViewById(R.id.textViewBooking);
        imageViewIconTroll = findViewById(R.id.imageViewIconPayment);
        textViewTroll = findViewById(R.id.textViewPayment);
        textViewLogout = findViewById(R.id.textViewLogout);
        imageViewIconPaylas = findViewById(R.id.imageViewIconContactus);
        textViewPaylasText = findViewById(R.id.textViewContactus);
        textViewAboutus = findViewById(R.id.textViewAboutus);


        toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("   HOME");
        toolbar.setLogo(R.drawable.icon_home);

        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.left_to_text);
        animation2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.boncue_anim);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.postAyar:
                Toast.makeText(this, "xxx", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


        if (mUser != null){
            i = 0;
            Picasso.get().load(mUser.getPhotoUrl()).resize(250,200).into(imageViewUserAvatar);
            textViewUserName.setText(mUser.getDisplayName());
            textViewUserEmail.setText(mUser.getEmail());

        }else {
            imageViewUserAvatar.setImageResource(R.mipmap.ic_launcher);
        }

        imageViewIconHome.setOnClickListener(this);
        textViewHome.setOnClickListener(this);
        imageViewIconYeniCros.setOnClickListener(this);
        textViewYeniCros.setOnClickListener(this);
        imageViewIconTroll.setOnClickListener(this);
        textViewTroll.setOnClickListener(this);
        imageViewIconPaylas.setOnClickListener(this);
        textViewPaylasText.setOnClickListener(this);
        textViewAboutus.setOnClickListener(this);
        textViewLogout.setOnClickListener(this);


        replace(new HomeFragment(),"home");


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewIconHome:
            case R.id.textViewHome:
                //Home txt
                //Home btn
                replace(new HomeFragment(),"Kullanılan Croslar Fragment");
                toolbar.setTitle("   HOME");
                toolbar.startAnimation(animation2);
                toolbar.setLogo(R.drawable.icon_home);

                break;
            case R.id.imageViewIconBooking:
            case R.id.textViewBooking:
                //new Cross Txt
                //new Cross
                replace(new NewCrossFragment(),"New Croslar");
                toolbar.setTitle("   YENİ CROSLAR");
                toolbar.startAnimation(animation2);
                toolbar.setLogo(R.drawable.ic_baseline_fiber_new_24);
                break;
            case R.id.imageViewIconPayment:
            case R.id.textViewPayment:
                //Trol txt
                //Trol Fragment
                replace(new TrollCrossFragment(),"Troll fragment");
                toolbar.setTitle("   TROLL CROSLAR");
                toolbar.startAnimation(animation2);
                toolbar.setLogo(R.drawable.troll_icons);
                break;
            case R.id.imageViewIconContactus:
            case R.id.textViewContactus:
                //paylas txt
                //paylas btn
                replace(new ShareFragment(),"Paylas Fragment");
                toolbar.setTitle("   CROSUNU PAYLAŞ");
                toolbar.startAnimation(animation2);
                toolbar.setLogo(R.drawable.ic_baseline_share_24);
                break;
            case R.id.textViewAboutus:
                //nasıl kullanılır text
                toolbar.setTitle("   NASIL KULLANILIR ?");
                toolbar.startAnimation(animation2);
                replace(new KlavuzFragment(),"Paylas Fragment");
                break;
            case R.id.textViewLogout:

                Snackbar.make(constraintLayoutNavBar,"Hesabınızdan çıkış yapıyorsunuz, Devam etmek istiyormusunuz ?",Snackbar.ANIMATION_MODE_SLIDE).setAction("DEVAM", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAuth.signOut();

                        Intent i = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).show();







                break;
         }
    }
    private void replace(Fragment fragment, String s){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.RealitiveLayout,fragment);
        transaction.addToBackStack(s);
        transaction.commit();
    }

    public void reklamEntegrasyon(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(MainActivity.this);

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Toast.makeText(MainActivity.this, loadAdError.toString(), Toast.LENGTH_SHORT).show();
                        mInterstitialAd = null;
                    }
                });



    }

    @Override
    protected void onResume() {

        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                reklamEntegrasyon();
            }
        },delay);
        super.onResume();
    }


}