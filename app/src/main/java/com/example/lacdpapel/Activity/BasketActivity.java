package com.example.lacdpapel.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Adapters.AdapterBasket;
import com.example.lacdpapel.BasketManager;
import com.example.lacdpapel.Fragments.storeFRAG;
import com.example.lacdpapel.Models.Basket;
import com.example.lacdpapel.Models.Product;
import com.example.lacdpapel.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;


public class BasketActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterBasket adapter;
    private Button oformlenie;
    private TextView orderNumberTextView, totalAmountText;
    private List<Basket> basketList;
    /**
     * Метод onCreate инициализирует активность при ее первом создании.
     * Он находит и присваивает ресурсы виджетов, устанавливает макет для активности,
     * загружает список товаров в корзине, генерирует номер заказа, вычисляет общую сумму заказа,
     * создает адаптер для RecyclerView и устанавливает обработчик нажатия кнопки "Оформление".
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_activity);
        totalAmountText = findViewById(R.id.totalAmountTV);
        recyclerView = findViewById(R.id.recyclerOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        basketList = BasketManager.loadBasketList(this);
        oformlenie = findViewById(R.id.next_btn);
        String orderNumber = generateOrderNumber();
        StringBuilder stringBuilder = new StringBuilder();
        for (Basket basket : basketList) {
            stringBuilder.append(basket.toString()).append("\n");
        }
        calculateTotalAmount();
        adapter = new AdapterBasket(this, basketList, this);
        recyclerView.setAdapter(adapter);
        oformlenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalAmount = totalAmountText.getText().toString();
                Intent intent = new Intent(BasketActivity.this, Decoration.class);
                intent.putExtra("TOTAL_AMOUNT", totalAmount);
                startActivity(intent);
                finish();
            }
        });

    }
    /**
     * Метод changeAmount обновляет список товаров в корзине и пересчитывает общую сумму.
     */
    public void changeAmount()
    {
        basketList = BasketManager.loadBasketList(getApplicationContext());
        calculateTotalAmount();

    }
    /**
     * Метод calculateTotalAmount вычисляет общую сумму заказа на основе товаров в корзине.
     * Он также обновляет отображение общей суммы.
     */
    public void calculateTotalAmount() {
        if (basketList != null) {
            AtomicInteger totalAmount = new AtomicInteger(0);
            AtomicInteger counter = new AtomicInteger(basketList.size());

            for (Basket basketItem : basketList) {
                int quantity = basketItem.getQuantity();
                String productId = basketItem.getProductId();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference productsRef = db.collection("Products");

                productsRef.document(productId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Product product = documentSnapshot.toObject(Product.class);
                                int price = product.getPrice();
                                totalAmount.addAndGet(quantity * price);
                            } else {
                                Log.d(TAG, "Документ с ID " + productId + " не найден.");
                            }
                            if (counter.decrementAndGet() == 0) {
                                updateTotalAmount(totalAmount.get());
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Ошибка при получении цены товара из Firestore: ", e);
                            if (counter.decrementAndGet() == 0) {
                                updateTotalAmount(totalAmount.get());
                            }
                        });
            }
        }
        else {
            Log.e(TAG, "Список basketList равен null!");
        }
    }
    /**
     * Метод updateTotalAmount обновляет отображение общей суммы заказа.
     * Если общая сумма равна 0, метод также удаляет все товары из корзины.
     * @param totalAmount Общая сумма заказа.
     */
    public void updateTotalAmount(int totalAmount) {
        runOnUiThread(() -> totalAmountText.setText("Итог: " + String.valueOf(totalAmount)+ " ₽"));
        if (totalAmount == 0){
            BasketManager.removeALL(getApplicationContext());
        }
    }

    /**
     * Метод generateOrderNumber генерирует случайный номер заказа.
     * @return Сгенерированный номер заказа.
     */
    private String generateOrderNumber() {
        int randomNumber = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(randomNumber);
    }

}
