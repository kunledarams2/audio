package com.e.audio_sdk.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.audio_sdk.R;
import com.e.audio_sdk.Model.Doctor;
import com.e.audio_sdk.View.Callback.CallListenerBtn;
import com.e.audio_sdk.View.UI.Activity.DirectRouting;
import com.e.audio_sdk.View.UI.UUitil.IO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_AVATAR;
import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_NAME;
import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_AVATAR;
import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_NAME;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.DoctorViewHolder> {

    Context context;
    List<Doctor> doctors=new ArrayList<>();
    private CallListenerBtn callListenerBtn;
    private List<String> getDoctordetails= new ArrayList<>();

    public DocAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public DocAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.holder_doctor,parent,false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocAdapter.DoctorViewHolder holder, int position) {

        holder.doctorName.setText("Dr."+ doctors.get(position).getFullname());
        holder.hospitalName.setText(doctors.get(position).getHospital());

        Picasso.get().load(doctors.get(position).getAvatar())
                .into(holder.doctorAvatar);

        holder.status.setBackgroundResource(doctors.get(position).isAvailable() ? R.drawable.circle_green : R.drawable.circle_red);

        if(callListenerBtn!=null){
            holder.voiceCall.setOnClickListener(btn->{
                IO.setData(context, DOCTOR_NAME, doctors.get(position).getFullname());
                IO.setData(context,DOCTOR_AVATAR,doctors.get(position).getAvatar());
                callListenerBtn.callBtn(doctors.get(position).getId());
            });
        }


        holder.voiceCall.setOnClickListener(btn->{

            IO.setData(context, DOCTOR_NAME,doctors.get(position).getFullname());
            IO.setData(context,DOCTOR_AVATAR,doctors.get(position).getAvatar());
//            String doctorId= String.valueOf(doctors.get(position).getId());
            getDoctordetails.add(String.valueOf(doctors.get(position).getId()));
            getDoctordetails.add(doctors.get(position).getSpecailtyId());
            getDoctordetails.add(doctors.get(position).getFullname());

            Intent intent= new Intent(context, DirectRouting.class);
            intent.putExtra("doctorDetails", (ArrayList<String>) getDoctordetails);
            context.startActivity(intent);
            ((Activity)context).finish();

        });

        holder.setRating(holder.rating,doctors.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public void setBtnclicklistener(CallListenerBtn  listenerBtn){
        this.callListenerBtn=listenerBtn;

    }

    public void setDoctors(List<Doctor> doctors){
        this.doctors=doctors;
        notifyDataSetChanged();
    }


    class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorAvatar;
        TextView doctorName, hospitalName;
        ImageButton voiceCall;
        View status;
        LinearLayout rating;


        DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorAvatar =itemView.findViewById(R.id.avatar);
            hospitalName =itemView.findViewById(R.id.hospital);
            doctorName =itemView.findViewById(R.id.name);

            voiceCall=itemView.findViewById(R.id.btn_voice_call);
            status=itemView.findViewById(R.id.available);
            rating =itemView.findViewById(R.id.rating);

        }


        private void setRating(LinearLayout view, double rating) {
            view.removeAllViews();
            int intVal = (int) rating;
            int absentVal = 5 - intVal;
            double rem = rating - intVal;

            for (int i = 0; i < intVal; i++) {
                ImageView img = new ImageView(view.getContext());
                img.setImageResource(R.drawable.ic_star);
                view.addView(img);
            }
            if (rem > 0) {
                ImageView img = new ImageView(view.getContext());
                img.setImageResource(R.drawable.ic_star_half);
                view.addView(img);

                absentVal--; //very important;
            }

            for (int i = 0; i < absentVal; i++) {
                ImageView img = new ImageView(view.getContext());
                img.setImageResource(R.drawable.ic_star_empty);
                view.addView(img);
            }
        }
    }
}
