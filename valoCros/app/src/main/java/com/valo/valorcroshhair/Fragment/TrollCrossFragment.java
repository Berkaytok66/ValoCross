package com.valo.valorcroshhair.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valo.valorcroshhair.Adapter.TrollAdapter;
import com.valo.valorcroshhair.Claslar.AdminVeriEkle;
import com.valo.valorcroshhair.R;

import java.util.ArrayList;

public class TrollCrossFragment extends Fragment {
    private View view;
    private FirebaseUser mUser;
    private AdminVeriEkle adminVeriEkle;
    private RecyclerView recyclerView;
    private ArrayList<AdminVeriEkle> mList;
    private TrollAdapter myAdapter;

    public void init(){

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.rv3);
        mList = new ArrayList<AdminVeriEkle>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_troll_cross, container, false);
        init();
        RecyclerViewSetting();
        FirebaseRecyclerViewVeriAktarimi();
        return view;
    }
    private void RecyclerViewSetting(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        myAdapter = new TrollAdapter(view.getContext(),mList,recyclerView);
        recyclerView.setAdapter(myAdapter);


    }
    private void FirebaseRecyclerViewVeriAktarimi() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Trol");
        databaseReference.addValueEventListener(new ValueEventListener() {
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
}