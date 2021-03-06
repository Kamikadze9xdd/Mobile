package com.nazar.kulyk_lab.fragments.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nazar.kulyk_lab.R;
import com.nazar.kulyk_lab.activities.MainActivity;
import com.nazar.kulyk_lab.localstorage.ItemsStorage;
import com.nazar.kulyk_lab.models.artObjects.ArtObject;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsItemFragment extends Fragment {
    @BindView(R.id.image_detail)
    protected ImageView imageDetail;
    @BindView(R.id.title_details)
    protected TextView titleDetail;
    @BindView(R.id.long_title_details)
    protected TextView longTitleDetail;
    @BindView(R.id.fav_button)
    protected ImageButton favButton;
    @BindView(R.id.author_details)
    protected TextView author_detail;

    private ItemsStorage itemsStorage = new ItemsStorage();

    public DetailsItemFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.item_details, container, false);

        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        ArtObject artObject = (ArtObject) Objects.requireNonNull(bundle).getSerializable("current_item");
        if(artObject != null){
            visibleItem(artObject, view);

            imageDetail.setOnClickListener(v -> {
                FullScreenImageFragment fullScreenImageFragment = new FullScreenImageFragment();
                bundle.putSerializable("link", artObject.getWebImage().getUrl());
                fullScreenImageFragment.setArguments(bundle);
                ((MainActivity) view.getContext()).setCurrentFragment(fullScreenImageFragment);
            });
        }

        favButton.setOnClickListener(v -> {
            // if result == true object save, if result == false object delete
            Boolean result = itemsStorage.saveOrRemove(artObject, view);
            if(result){
                favButton.setImageResource(R.drawable.ic_star_black_full_30dp);
            } else{
                favButton.setImageResource(R.drawable.ic_star_border_black_empty_30dp);
            }
        });

        return view;
    }

    private void visibleItem(ArtObject artObject, View view){
        Picasso.get().load(artObject.getWebImage().getUrl()).into(imageDetail);
        titleDetail.setText(artObject.getTitle());
        longTitleDetail.setText(artObject.getLongTitle());
        author_detail.setText(artObject.getPrincipalOrFirstMaker());

        if(itemsStorage.checkThatObjectAlreadySaved(artObject, view)){
            favButton.setImageResource(R.drawable.ic_star_black_full_30dp);
        } else{
            favButton.setImageResource(R.drawable.ic_star_border_black_empty_30dp);
        }
    }
}
