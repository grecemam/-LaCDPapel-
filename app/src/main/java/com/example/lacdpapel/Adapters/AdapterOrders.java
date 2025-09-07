package com.example.lacdpapel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Models.OrderUserModel;
import com.example.lacdpapel.R;

import java.util.ArrayList;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.ViewHolder> {
    private Context context;
    private ArrayList<OrderUserModel> orderList;

    public AdapterOrders(Context context, ArrayList<OrderUserModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView num_order, status_order, price_order;
        private RecyclerView detail_order;
        AdapterProductInOrder adapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            detail_order = itemView.findViewById(R.id.order_product_list);
            num_order = itemView.findViewById(R.id.num_orderTV);
            status_order = itemView.findViewById(R.id.tv_order_status);
            price_order = itemView.findViewById(R.id.tv_order_price);

            detail_order.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapter = new AdapterProductInOrder();
            detail_order.setHasFixedSize(true);
            detail_order.setAdapter(adapter);
        }
        public void bind(OrderUserModel order) {
            num_order.setText("Заказ №" + order.getOrder_number());
            status_order.setText("Статус: " + order.getStatus());
            price_order.setText(order.getPrice() + " ₽");
            adapter.setProducts(order.getProducts());
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_orders, parent, false);
        return new AdapterOrders.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderUserModel orderUserModel = orderList.get(position);
        holder.bind(orderUserModel);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
