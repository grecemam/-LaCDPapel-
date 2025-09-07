package com.example.lacdpapel.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.google.firebase.auth.FirebaseAuth.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Adapters.AdapterOrders;
import com.example.lacdpapel.Models.OrderUserModel;
import com.example.lacdpapel.Models.Product;
import com.example.lacdpapel.Models.ProductInOrder;
import com.example.lacdpapel.R;
import com.example.lacdpapel.databinding.AllOrdersActivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AllOrders extends AppCompatActivity {
    AllOrdersActivityBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**
     * Метод onCreate инициализирует активность при ее первом создании.
     * Он извлекает идентификатор пользователя из намерения, получает заказы, принадлежащие этому пользователю, из Firestore
     * и заполняет RecyclerView полученными заказами.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AllOrdersActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String userId = intent.getStringExtra("USER_ID");
        RecyclerView recyclerView = binding.orderInfoRecyclerView;

        db.collection("Orders")
                .whereEqualTo("id_user", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<OrderUserModel> orderList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        OrderUserModel order = document.toObject(OrderUserModel.class);


                        db.collection("Detail_Orders")
                                .whereEqualTo("order_id", order.getOrder_id())
                                .get()
                                .addOnSuccessListener(detailDocumentSnapshots -> {
                                    ArrayList<ProductInOrder> productList = new ArrayList<>();
                                    for (DocumentSnapshot detailDocument : detailDocumentSnapshots) {
                                        String productId = detailDocument.getString("id_product");
                                        int quantity = detailDocument.getLong("quantity_product").intValue();

                                        db.collection("Products")
                                                .document(productId)
                                                .get()
                                                .addOnSuccessListener(productDocument -> {
                                                    if (productDocument.exists()) {

                                                        Product product = productDocument.toObject(Product.class);

                                                        ProductInOrder productInOrder = new ProductInOrder();
                                                        productInOrder.setProduct(product);
                                                        productInOrder.setQuantity_product(quantity);

                                                        productList.add(productInOrder);

                                                        if (productList.size() == detailDocumentSnapshots.size()) {

                                                            order.setProducts(productList);
                                                            orderList.add(order);
                                                            recyclerView.setLayoutManager(new LinearLayoutManager(this));


                                                            AdapterOrders adapter = new AdapterOrders(this, orderList);
                                                            recyclerView.setAdapter(adapter);
                                                        }
                                                    }
                                                });
                                    }
                                });
                    }

                });
    }

}