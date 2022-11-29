package com.valo.valorcroshhair.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.valo.valorcroshhair.Adapter.NewCrossAdapter;
import com.valo.valorcroshhair.Adapter.ShareAdapter;
import com.valo.valorcroshhair.Claslar.ShareClass;
import com.valo.valorcroshhair.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ShareFragment extends Fragment {

    private View view;
    private ImageButton toGalery;
    private Button pylsBTN,menuBTn;
    private TextInputLayout textInputCrosNameLayout,textInputCoderLayout;
    private TextInputEditText textInputCrosNameEditText,textInputCodeEditText;



    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private Uri imageData;
    //private  Bitmap selectdImages;

    private FirebaseStorage firebaseStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseUser mUser;


    private RecyclerView recyclerView;
    private ShareAdapter myAdapter;
    private ArrayList<ShareClass> shareClasArrayList;

    public void init(){
    toGalery = view.findViewById(R.id.toGaleryBTN);
    firebaseStorage = FirebaseStorage.getInstance();
    mAuth = FirebaseAuth.getInstance();
    mUser = mAuth.getCurrentUser();
    firebaseFirestore = FirebaseFirestore.getInstance();
    storageReference = firebaseStorage.getReference();
    pylsBTN = view.findViewById(R.id.pylsBTN);


    textInputCrosNameLayout= view.findViewById(R.id.textInputCrosNameLayout);
    textInputCoderLayout= view.findViewById(R.id.textInputCoderLayout);
    textInputCrosNameEditText= view.findViewById(R.id.textInputCrosNameEditText);
    textInputCodeEditText= view.findViewById(R.id.textInputCodeEditText);
    shareClasArrayList = new ArrayList<>();

    recyclerView = view.findViewById(R.id.recyclerView4);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share, container, false);
        init();

        registerLancher();
        pylsBTN();
        rcViewSettings();
        getData();


        return view;

    }
    private void registerLancher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                     if (result.getResultCode() == RESULT_OK){
                         Intent intentFromResult = result.getData();
                         if (intentFromResult != null){
                             imageData =  intentFromResult.getData();
                             toGalery.setImageURI(imageData);

                             /* Bitmap e cevirmek icin kullanılır yukardakı işlem yeterli oluyor
                             try {
                                 if (Build.VERSION.SDK_INT >= 28){
                                     ImageDecoder.Source source = ImageDecoder.createSource(view.getContext().getContentResolver(),imageData);
                                     selectdImages = ImageDecoder.decodeBitmap(source);
                                     imageViewfotoEktarilacak.setImageBitmap(selectdImages);
                                 }else {
                                     selectdImages = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(),imageData);
                                     imageViewfotoEktarilacak.setImageBitmap(selectdImages);
                                 }
                             }catch (Exception e){
                                 e.printStackTrace();
                             }*/
                         }
                     }
            }
        });
        
        permissionLauncher =  registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    Intent goToGaleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(goToGaleryIntent);

                }else {
                    Toast.makeText(view.getContext(), "Bu İzine İhtiyacımız Var", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void pylsBTN(){
        toGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                        //sebep gostermek zorundaysak sebebp belirterek izin istiğicez
                        Snackbar.make(view,"Media erişimine izin vermeniz gerekmektedir...",Snackbar.LENGTH_INDEFINITE).setAction("İZİN VER", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);


                            }
                        }).show();
                    }else {
                        //  sebep göstermek zorunda değil isek izni direk istiğicez
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }else {
                    Intent goToGaleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(goToGaleryIntent);
                }
            }
        });
        pylsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String crosName = textInputCrosNameEditText.getText().toString();
                String crosCode = textInputCodeEditText.getText().toString();

                if (!TextUtils.isEmpty(crosName)){
                    if (!TextUtils.isEmpty(crosCode)){
                        if (imageData != null){

                            UUID uuid = UUID.randomUUID();
                            String imageName = "ShareCrosFile/" + uuid + ".jpg";
                            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    //dowland url
                                    StorageReference newRefarance = firebaseStorage.getReference(imageName);
                                    newRefarance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String dowlandUrl = uri.toString();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String name = user.getDisplayName();

                                            HashMap<String,Object> posData = new HashMap<>();
                                            posData.put("Url",dowlandUrl);
                                            posData.put("CrosName",crosName);
                                            posData.put("CrosCode",crosCode);
                                            posData.put("KullaniciName",name);
                                            posData.put("Date", FieldValue.serverTimestamp());
                                            posData.put("ProfilImageURL",mUser.getPhotoUrl());

                                            firebaseFirestore.collection("Posts").add(posData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(view.getContext(), "Succes", Toast.LENGTH_SHORT).show();
                                                    toGalery.setImageResource(R.drawable.galery_icon);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(view.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(view.getContext(),e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                                }
                            });


                        }else
                            Toast.makeText(view.getContext(), "Galerinize gidip cros seçin", Toast.LENGTH_SHORT).show();


                    }else
                        textInputCoderLayout.setError("Bol Olamaz !!!");

                }else
                    textInputCrosNameLayout.setError("Boş Olamaz !!!");

            }
        });


    }

    public void getData() {
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(view.getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if (value != null){
                   for (DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String,Object> data = snapshot.getData();
                        String CrosCode = (String) data.get("CrosCode");
                        String CrosName = (String) data.get("CrosName");
                        String KullaniciName = (String) data.get("KullaniciName");
                        String Url = (String) data.get("Url");
                        String CrosKullaniciPhoto = (String) data.get("ProfilImageURL");


                        ShareClass shareClass = new ShareClass(CrosCode,CrosName,Url,CrosKullaniciPhoto,KullaniciName);
                        shareClasArrayList.add(shareClass);
                   }
                    myAdapter.notifyDataSetChanged();
                }

            }
        });
    }
    public void rcViewSettings(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        myAdapter = new ShareAdapter(view.getContext(),shareClasArrayList,recyclerView);
        recyclerView.setAdapter(myAdapter);
    }
}