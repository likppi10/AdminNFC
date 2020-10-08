package com.example.sns_project_nfc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sns_project_nfc.FirebaseHelper;
import com.example.sns_project_nfc.R;
import com.example.sns_project_nfc.UserInfo;
import com.example.sns_project_nfc.listener.OnPostListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.MainViewHolder> {
    private ArrayList<UserInfo> mDataset;
    private Activity activity;
    private Button authbtn;
    private FirebaseHelper firebaseHelper;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public UserInfoAdapter(Activity activity, ArrayList<UserInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
        firebaseHelper = new FirebaseHelper(activity);
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }
    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    @NonNull
    @Override
    public UserInfoAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);        // part22 : 프로필 사진 불러오기 (57'10")
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        cardView.findViewById(R.id.authbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           // part15: 점3개 메뉴(수정, 삭제) (20'30")
                showPopup(v, mainViewHolder.getAdapterPosition());
            }
        });
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView photoImageVIew = cardView.findViewById(R.id.photoImageVIew);
        TextView nameTextView = cardView.findViewById(R.id.nameTextView);
        TextView addressTextView = cardView.findViewById(R.id.addressTextView);
        //authbtn = cardView.findViewById(R.id.authbtn);

        UserInfo userInfo = mDataset.get(position);
        if(mDataset.get(position).getPhotoUrl() != null){
            Glide.with(activity).load(mDataset.get(position).getPhotoUrl()).centerCrop().override(500).into(photoImageVIew);
        }
        else{
            Glide.with(activity).load(R.drawable.user).into(photoImageVIew);
        }
        nameTextView.setText(userInfo.getName());
        addressTextView.setText(userInfo.getAddress());

    }
    private void showPopup(View v, final int position) {                                                // part15 : 오른쪽 상단의 점3개 (수정 삭제) (23'25")
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.auth:
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        UserInfo userInfo = mDataset.get(position);
                        final DocumentReference documentReference =firebaseFirestore.collection("users").document(userInfo.getUserUID());

                        userInfo.setAuthState("O");

                        documentReference.set(userInfo.getUserInfo())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) { }})
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) { }
                                });

                        return true;
                    case R.id.delete:
                        firebaseHelper.userstorageDelete(mDataset.get(position));
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.userinfo, popup.getMenu());
        popup.show();
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}