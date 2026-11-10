package com.example;
import java.util.HashMap;
import java.util.Map;
public class ShoppingCart {
    public static final String promo_code = "SALE10";
    public static final double discount = 0.10;
    private final Map<String, Integer> items = new HashMap<>();
    public Map<String, Integer> getItems() {
        return items;
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }

        int currentQuantity = items.getOrDefault(product.getId(), 0);
        if (currentQuantity + quantity > product.getStock()) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        items.put(product.getId(), currentQuantity + quantity);
    }

    public void removeItem(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество для удаления должно быть больше нуля");
        }

        if (!items.containsKey(productId)) {
            throw new IllegalArgumentException("Товара нет в корзине");
        }

        int currentQuantity = items.get(productId);
        if (quantity > currentQuantity) {
            throw new IllegalArgumentException("Нельзя удалить больше товаров, чем есть в корзине");
        }
        if (quantity == currentQuantity) {
            items.remove(productId);
        } else {
            items.put(productId, currentQuantity - quantity);
        }
    }
    public double calculateTotal(Map<String, Product> catalog, String promoCode) {
        double total = 0.0;

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = catalog.get(entry.getKey());
            if (product != null) {
                total += product.getPrice() * entry.getValue();
            }
        }
        if (promoCode != null && !promoCode.isEmpty()) {
            if (!promoCode.toUpperCase().equals(promo_code)) {
                throw new IllegalArgumentException("Неверный промокод");
            }
            total = total * (1 - discount);
        }
        return total;
    }
}