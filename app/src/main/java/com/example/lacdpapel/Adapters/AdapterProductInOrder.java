package com.example.lacdpapel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Models.OrderUserModel;
import com.example.lacdpapel.Models.Product;
import com.example.lacdpapel.Models.ProductInOrder;
import com.example.lacdpapel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductInOrder extends RecyclerView.Adapter<AdapterProductInOrder.ViewHolder> {
    private ArrayList<ProductInOrder> productsList;

    public AdapterProductInOrder() {
        productsList = new ArrayList<>();
    }
    public void setProducts(ArrayList<ProductInOrder> products) {
        this.productsList = products;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdapterProductInOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_in_order, parent, false);
        return new AdapterProductInOrder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductInOrder.ViewHolder holder, int position) {
        holder.bind(productsList.get(position));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView info_product;
        ImageView image_product;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            info_product = itemView.findViewById(R.id.info_tv);
            image_product = itemView.findViewById(R.id.prod_in_ord_img);
        }
        public void bind(ProductInOrder product) {
            info_product.setText(product.getProduct().getPrice() + " × " + product.getQuantity());
            Picasso.with(itemView.getContext()).load(product.getProduct().getPhotoUrl()).into(image_product);
        }
    }
}