package com.example.lacdpapel.Adapters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Activity.BasketActivity;
import com.example.lacdpapel.BasketManager;
import com.example.lacdpapel.Models.Basket;

import com.example.lacdpapel.Models.Product;
import com.example.lacdpapel.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;


public class AdapterBasket extends RecyclerView.Adapter<AdapterBasket.ViewHolder> {
    private Context context;
    private List<Basket> basketList;
    private BasketActivity basketActivity;

    public AdapterBasket(Context context, List<Basket> basketList, BasketActivity basketActivity) {
        this.context = context;
        this.basketList = basketList;
        this.basketActivity = basketActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_in_basket, parent, false);
        return new ViewHolder(view, 0);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Basket basketItem = basketList.get(position);
        holder.bind(basketItem);
    }

    @Override
    public int getItemCount() {
        return basketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        private int position;
        TextView productPriceTextView, productDescriptionTextView;
        TextView productQuantityTextView;
        ImageView productImage;
        TextView addQuantityTextView, removeQuantityTextView;

        public ViewHolder(@NonNull View itemView, int position) {
            super(itemView);
            this.position = position;
            productNameTextView = itemView.findViewById(R.id.nameProduct);
            productPriceTextView = itemView.findViewById(R.id.priceProduct);
            productDescriptionTextView = itemView.findViewById(R.id.descriptionProduct);
            productQuantityTextView = itemView.findViewById(R.id.quantityTextView);
            productImage = itemView.findViewById(R.id.imageProduct);
            addQuantityTextView = itemView.findViewById(R.id.addquantity);
            removeQuantityTextView = itemView.findViewById(R.id.removequantity);

            addQuantityTextView.setOnClickListener(v -> {
                int quantity = Integer.parseInt(productQuantityTextView.getText().toString());
                quantity++;
                productQuantityTextView.setText(String.valueOf(quantity));
                basketList.get(position).setQuantity(quantity);
                BasketManager.saveBasketList(context, basketList);
                basketActivity.changeAmount();
            });

            removeQuantityTextView.setOnClickListener(v -> {
                int quantity = Integer.parseInt(productQuantityTextView.getText().toString());
                Basket basketToRemove = basketList.get(getAdapterPosition());
                String productId = basketToRemove.getProductId();
                BasketManager.removeBasketItem(context, productId);
                if (quantity > 0) {
                    quantity--;
                    if (quantity == 0) {
                        basketList.remove(basketToRemove);
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), basketList.size());
                        if (basketList.size() == 0) {
                            BasketManager.removeALL(context);
                            basketActivity.updateTotalAmount(0);
                        }
                        basketActivity.changeAmount();

                    } else {
                        productQuantityTextView.setText(String.valueOf(quantity));
                        basketList.get(position).setQuantity(quantity);
                        BasketManager.saveBasketList(context, basketList);
                        basketActivity.changeAmount();
                    }
                }
            });
        }

        public void bind(Basket basketItem) {
            getProductFromFirestore(basketItem.getProductId(), new ProductCallback() {
                @Override
                public void onProductLoaded(Product product) {
                    if (product != null) {
                        String productName = product.getName();
                        Integer productPrice = product.getPrice();
                        String productQuantity = String.valueOf(basketItem.getQuantity());
                        String description = product.getDescription();

                        productNameTextView.setText(productName);
                        productPriceTextView.setText(String.valueOf(productPrice) + " ₽");
                        productQuantityTextView.setText(productQuantity);
                        productDescriptionTextView.setText(description);

                        Picasso.with(itemView.getContext()).load(product.getPhotoUrl()).into(productImage);
                    } else {
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    }

    interface ProductCallback {
        void onProductLoaded(Product product);
        void onFailure(Exception e);
    }

    private void getProductFromFirestore(String productId, ProductCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");

        productsRef.document(productId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Product product = documentSnapshot.toObject(Product.class);
                        callback.onProductLoaded(product);
                    } else {
                        Log.d(TAG, "Документ с ID " + productId + " не найден.");
                        callback.onProductLoaded(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Ошибка при получении продукта из Firestore: ", e);
                    callback.onFailure(e);
                });
    }
}

