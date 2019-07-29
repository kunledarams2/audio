package com.e.audio_sdk.View.UI.Fragment.Finddoctor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.e.audio_sdk.R;
import com.e.audio_sdk.View.Adapter.DocAdapter;
import com.e.audio_sdk.View.Callback.FragmentChanger;
import com.e.audio_sdk.View.UI.Fragment.FragmentTitled;
import com.e.audio_sdk.ViewModel.DoctorViewmodel;

//import android.support.v4.app.Fragment;


public class FindADoctor extends FragmentTitled implements FragmentChanger {

   private RecyclerView recyclerView;
   private ImageButton searchBtn ,closesearchBtn ;
    private RelativeLayout retrylayout;
    private Button retrybtn;
   private ProgressBar loader;
   private LinearLayoutManager llm;
   private SwipeRefreshLayout refreshLayout;
   private DoctorViewmodel viewmodel;
   private EditText searchField;
   private TextView errormessage;
   private ImageView emptyIcon;
   private int page= 1;
   private int specialtyId, doctorId;
   private DocAdapter docAdapter;
   private TextView title;


    public FindADoctor() {
        // Required empty public constructor
    }


    public static FindADoctor newInstance(int specailtyId) {
        FindADoctor fragment = new FindADoctor();
        fragment.specialtyId = specailtyId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_findadoctor, container, false);
        setupView(view);
        setupAdapter();
        return view;
    }


    private void setupView(View view){
        recyclerView=view.findViewById(R.id.recycler_view);
        loader = view.findViewById(R.id.progressBar);
        searchField = view.findViewById(R.id.search_field);
        searchBtn = view.findViewById(R.id.search_btn);
        retrylayout=view.findViewById(R.id.tryLayout);
        retrybtn= view.findViewById(R.id.retryBtn);
        errormessage=view.findViewById(R.id.placeholder_label);
        emptyIcon=view.findViewById(R.id.placeholder_icon);
        refreshLayout = view.findViewById(R.id.pulldownrefresh);
        title= view.findViewById(R.id.title);
        closesearchBtn=view.findViewById(R.id.closeSearch);


//        doctor =new Doctor();

        retrybtn.setOnClickListener(view1 -> retrySearch());

        searchBtn.setOnClickListener(btn->{
            title.setVisibility(View.GONE);
            searchField.setVisibility(View.VISIBLE);
            searchBtn.setVisibility(View.VISIBLE);
            closesearchBtn.setVisibility(View.VISIBLE);
//            viewmodel.fetchRadomDoctor();
            String query = searchField.getText().toString();

            if(!TextUtils.isEmpty(query)){
                retrylayout.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                viewmodel.fetchSearchDoctor(query,page);
            }

        });
        closesearchBtn.setOnClickListener(btn->{
            searchField.setShowSoftInputOnFocus(false);
            searchField.getText().clear();
            searchField.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            closesearchBtn.setVisibility(View.GONE);
            viewmodel.fetchSpecialyDoctor(specialtyId,page);
        });
        refreshLayout.setOnRefreshListener(() -> {
            viewmodel.fetchSpecialyDoctor(specialtyId,page);
            refreshLayout.setRefreshing(false);
        });

    }

    private void setupAdapter(){
        llm= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        docAdapter =new DocAdapter(getContext());
        recyclerView.setAdapter(docAdapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewmodel = ViewModelProviders.of(this).get(DoctorViewmodel.class);
        observe(viewmodel);
        viewmodel.fetchSpecialyDoctor(specialtyId,page);

    }

    private void observe(DoctorViewmodel viewmodel) {

        viewmodel.getMediatorLiveData().observe(this,doctorResult -> {
            loader.setVisibility(View.GONE);
            if(doctorResult.isSuccessful() && doctorResult.getDatalist().isEmpty()){
                recyclerView.setVisibility(View.GONE);
                errormessage.setText("No matching doctors found");
                retrylayout.setVisibility(View.VISIBLE);
            }
           else if(doctorResult.isSuccessful() && !doctorResult.getDatalist().isEmpty()){
               recyclerView.setVisibility(View.VISIBLE);
               retrylayout.setVisibility(View.GONE);
               docAdapter.setDoctors(doctorResult.getDatalist());


            } else if(!doctorResult.isSuccessful()){
               recyclerView.setVisibility(View.GONE);
                retrylayout.setVisibility(View.VISIBLE);
                errormessage.setText(doctorResult.getMessage());
                emptyIcon.setImageResource(R.drawable.placeholder_error);

            }
        });
    }

    private void retrySearch() {
        closesearchBtn.setVisibility(View.GONE);
        searchField.setVisibility(View.GONE);
        searchField.getText().clear();
        viewmodel.fetchSpecialyDoctor(specialtyId,page);
    }



    @Override
    public void ChangeFragment(FragmentTitled fragment) {

    }


}
