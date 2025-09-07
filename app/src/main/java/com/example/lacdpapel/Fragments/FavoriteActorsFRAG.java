package com.example.lacdpapel.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Adapters.AdapterFavoriteActor;
import com.example.lacdpapel.Models.ActorsFavorite;
import com.example.lacdpapel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActorsFRAG extends Fragment {
    RecyclerView recyclerView;
    CollectionReference actorsRef;

    private List<ActorsFavorite> actorsList;
    private String userId;
    private AdapterFavoriteActor adapterFavoriteActor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_actors_favorite, container, false);
        recyclerView = root.findViewById(R.id.recyclerActorFavorite);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
            actorsList = new ArrayList<>();
            actorsRef = FirebaseFirestore.getInstance().collection("ActorsFavotite");
            loadActors();

            adapterFavoriteActor = new AdapterFavoriteActor(getContext(), actorsList);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapterFavoriteActor);
        } else {
        }

        return root;
    }

    private void loadActors() {
        actorsRef.whereEqualTo("userId", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        actorsList.clear();
                        for (QueryDocumentSnapshot document : snapshot) {
                            ActorsFavorite actor = document.toObject(ActorsFavorite.class);
                            actorsList.add(actor);
                        }

                        adapterFavoriteActor.notifyDataSetChanged();
                    }
                });
    }

}
