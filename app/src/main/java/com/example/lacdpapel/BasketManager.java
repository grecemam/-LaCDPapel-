package com.example.lacdpapel;

import android.content.Context;

import com.example.lacdpapel.Models.Basket;

import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;

public class BasketManager {

    private static final String BASKET_KEY = "basket";

    public static void saveBasketList(Context context, List<Basket> basketList) {
        Paper.init(context);
        Paper.book().write(BASKET_KEY, basketList);
    }

    public static List<Basket> loadBasketList(Context context) {
        Paper.init(context);
        return Paper.book().read(BASKET_KEY, new ArrayList<>());
    }


    public static void addProductToBasket(Context context, Basket basket) {
        Paper.init(context);
        List<Basket> baskets = loadBasketList(context);
        boolean productFound = false;

        if (baskets.size() > 0) {
            for (Basket b : baskets) {
                if (b.getProductId().equals(basket.getProductId())) {
                    productFound = true;
                    b.setQuantity(b.getQuantity() + basket.getQuantity());
                }
            }
            if (!productFound) {
                baskets.add(basket);
            }
        } else {
            baskets.add(basket);
        }
        saveBasketList(context, baskets);
    }

    public static void removeBasketItem(Context context, String productId) {
        List<Basket> basket = loadBasketList(context);
        for(Basket b : basket){
            if (b.getProductId().equals(productId)){
                if(b.getQuantity() > 1){
                    b.setQuantity(b.getQuantity()-1);
                }
                else {
                    basket.remove(b);
                }
                saveBasketList(context,basket);
                break;
            }
        }
    }

    public static void removeALL(Context context) {
        Paper.init(context);
        Paper.book().delete(BASKET_KEY);
    }
}
