package com.example.lacdpapel.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.media.effect.Effect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Adapters.AdapterBasket;
import com.example.lacdpapel.BasketManager;
import com.example.lacdpapel.Models.Basket;
import com.example.lacdpapel.Models.Order;
import com.example.lacdpapel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Decoration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView totalAmountTV;
    private Button nextButton;
    private EditText addressET, number_card;
    private String paymentMethod, totalAmount;

    /**
     * Метод onCreate инициализирует активность при ее первом создании.
     * Он находит и присваивает ресурсы виджетов, устанавливает макет для активности,
     * получает общую сумму заказа из предыдущей активности, устанавливает обработчик для радиогруппы выбора способа оплаты,
     * и устанавливает обработчик нажатия кнопки "Далее" для сохранения заказа.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decoration_activity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        totalAmountTV = findViewById(R.id.totalAmountTV);
        nextButton = findViewById(R.id.next_btn);
        addressET = findViewById(R.id.address_et);
        number_card = findViewById(R.id.card_et);
        totalAmount = getIntent().getStringExtra("TOTAL_AMOUNT");
        totalAmountTV.setText(totalAmount);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.Writeoffimmediately_rb) {
                paymentMethod = "Списать сразу";
            } else if (checkedId == R.id.Uponreceipt_rb) {
                paymentMethod = "При получении";
            }
        });

        nextButton.setOnClickListener(v -> saveOrderToFirestore());
    }

    /**
     * Метод saveOrderToFirestore сохраняет заказ в Firestore, если все введенные данные корректны.
     * Он также переводит пользователя на другую активность после успешного сохранения заказа.
     */
    private void saveOrderToFirestore() {
        String userId = mAuth.getCurrentUser().getUid();
        int priceString = Integer.parseInt(totalAmountTV.getText().toString().replaceAll("[^\\d.]", ""));
        String cardNumber = number_card.getText().toString();
        String ad = addressET.getText().toString();
        if (!isValidCardNumber(cardNumber)) {
            number_card.setError("Номер карты должен содержать только цифры и быть длиной не менее 16 символов");
            return;
        }
        if (!isValidAddress(ad)) {
            addressET.setError("Введите корректный адрес");
            return;
        }
        if (!isValidPaymentMethod()) {
            Toast.makeText(Decoration.this, "Выберите способ оплаты", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAuth.getCurrentUser() != null) {
            Order order = new Order(null, userId, generateOrderNumber(), addressET.getText().toString(), number_card.getText().toString(), formatDate(new Date()), "Оформлен", priceString, paymentMethod);


            db.collection("Orders")
                    .add(order)
                    .addOnSuccessListener(documentReference -> {
                        String orderId = documentReference.getId();

                        order.setOrder_id(orderId);

                        db.collection("Orders").document(orderId).set(order)
                                .addOnSuccessListener(aVoid -> {

                                    List<Basket> baskets = BasketManager.loadBasketList(Decoration.this);
                                    for (Basket basket : baskets) {
                                        Map<String, Object> detailOrder = new HashMap<>();
                                        detailOrder.put("order_id", orderId);
                                        detailOrder.put("id_product", basket.getProductId());
                                        detailOrder.put("quantity_product", basket.getQuantity());

                                        db.collection("Detail_Orders")
                                                .add(detailOrder)
                                                .addOnSuccessListener(documentReference1 -> {
                                                    Log.d(TAG, "Запись в таблице Detail_Orders успешно добавлена");
                                                    Toast.makeText(Decoration.this, "Заказ успешно добавлен", Toast.LENGTH_SHORT).show();
                                                    BasketManager.removeALL(Decoration.this);
                                                })
                                                .addOnFailureListener(e -> Log.w(TAG, "Ошибка при добавлении записи в таблицу Detail_Orders", e));
                                    }
                                })
                                .addOnFailureListener(e -> Toast.makeText(Decoration.this, "Ошибка при сохранении заказа", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(Decoration.this, "Ошибка при добавлении заказа", Toast.LENGTH_SHORT).show());

            Intent intent = new Intent(Decoration.this, glavnaya.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Метод generateOrderNumber генерирует случайный номер заказа.
     * @return Сгенерированный номер заказа.
     */
    public static String generateOrderNumber() {
        int randomNumber = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(randomNumber);
    }

    /**
     * Метод formatDate форматирует дату в заданный формат.
     * @param date Дата, которую нужно отформатировать.
     * @return Отформатированная строка даты.
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        return sdf.format(date);
    }

    /**
     * Метод isValidCardNumber проверяет, является ли номер карты допустимым.
     * @param cardNumber Номер карты для проверки.
     * @return true, если номер карты допустимый, в противном случае - false.
     */
    public static boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("[0-9]+") && cardNumber.length() >= 16;
    }

    /**
     * Метод isValidAddress проверяет, является ли адрес доставки допустимым.
     * @param address Адрес для проверки.
     * @return true, если адрес доставки допустимый, в противном случае - false.
     */
    public static boolean isValidAddress(String address) {
        return !TextUtils.isEmpty(address) && address.length() > 10;
    }

    /**
     * Метод isValidPaymentMethod проверяет, выбран ли способ оплаты.
     * @return true, если выбран способ оплаты, в противном случае - false.
     */
    private boolean isValidPaymentMethod() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        return selectedId != -1;
    }
}
