package com.example.lacdpapel.Fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Activity.BasketActivity;
import com.example.lacdpapel.Adapters.AdapterProduct;
import com.example.lacdpapel.BasketManager;
import com.example.lacdpapel.Models.Basket;
import com.example.lacdpapel.Models.Product;
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

public class storeFRAG extends Fragment {
    RecyclerView recyclerView;
    CollectionReference productRef;

    private List<Product> productList;
    private AdapterProduct adapterProduct;
    ImageView basketActivity;
    public List<Basket> basketList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_store, container, false);
        recyclerView = root.findViewById(R.id.recyclerProducts);
        basketActivity = root.findViewById(R.id.go_basket_activity);

        basketActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BasketActivity.class);
                startActivity(intent);
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            productList = new ArrayList<>();
            productRef = FirebaseFirestore.getInstance().collection("Products");
            loadProducts();
            basketList = BasketManager.loadBasketList(getContext());
            adapterProduct = new AdapterProduct(getContext(), productList, basketList);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                    LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapterProduct);
        } else {
        }

        return root;

    }
    private void loadProducts() {
        productRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Ошибка загрузки продуктов", error);
                            return;
                        }
                        productList.clear();
                        for (QueryDocumentSnapshot document : snapshot) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }

                        adapterProduct.notifyDataSetChanged();
                    }
                });
    }
}
