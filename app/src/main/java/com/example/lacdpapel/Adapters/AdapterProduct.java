package com.example.lacdpapel.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lacdpapel.BasketManager;
import com.example.lacdpapel.Models.Basket;
import com.example.lacdpapel.Models.Product;
import com.example.lacdpapel.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {
    private Context context;
    private List<Product> productList = new ArrayList<>();
    private List<Basket> basketList = new ArrayList<>();

    public AdapterProduct(Context context, List<Product> productList, List<Basket> basketList) {
        this.context = context;
        this.productList = productList;
        this.basketList = basketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText(product.getPrice() + " ₽");
        holder.descriptionProduct.setText(product.getDescription());
        Picasso.with(context).load(product.getPhotoUrl()).into(holder.imageProduct);

        holder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Product clickedProduct = productList.get(clickedPosition);
                    Basket basketItem = new Basket(clickedProduct.getUid(), 1);
                    BasketManager.addProductToBasket(context, basketItem);
                    Toast.makeText(context, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
                    Log.d("BasketContent", "Содержимое корзины: " + basketList.toString());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct, addProduct;
        TextView nameProduct, priceProduct, descriptionProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            addProduct = itemView.findViewById(R.id.addProductIcon);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            descriptionProduct = itemView.findViewById(R.id.descriptionProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
        }
    }
}