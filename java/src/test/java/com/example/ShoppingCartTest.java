package com.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
class ShoppingCartTest {
    private ShoppingCart cart;
    private Product product;
    private Map<String, Product> catalog;
    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        product = new Product("prod_1", "Смартфон", 50000.0, 10);
        catalog = new HashMap<>();
        catalog.put("prod_1", product);
    }
    @Test
    void testAddItemSuccess() {
        cart.addItem(product, 2);
        assertEquals(2, cart.getItems().get("prod_1"));

        cart.addItem(product, 3);
        assertEquals(5, cart.getItems().get("prod_1"));
    }
    @Test
    void testAddItemInvalidQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cart.addItem(product, 0));
        assertEquals("Количество должно быть больше нуля", exception.getMessage());
    }
    @Test
    void testAddItemExceedsStock() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cart.addItem(product, 11));
        assertTrue(exception.getMessage().contains("Недостаточно товара на складе"));
    }
    @Test
    void testRemoveItemSuccess() {
        cart.addItem(product, 5);
        cart.removeItem("prod_1", 2);
        assertEquals(3, cart.getItems().get("prod_1"));

        cart.removeItem("prod_1", 3);
        assertFalse(cart.getItems().containsKey("prod_1"));
    }
    @Test
    void testRemoveItemErrors() {
        Exception ex1 = assertThrows(IllegalArgumentException.class, () -> cart.removeItem("prod_1", 0));
        assertEquals("Количество для удаления должно быть больше нуля", ex1.getMessage());

        Exception ex2 = assertThrows(IllegalArgumentException.class, () -> cart.removeItem("prod_2", 1));
        assertEquals("Товара нет в корзине", ex2.getMessage());

        cart.addItem(product, 2);
        Exception ex3 = assertThrows(IllegalArgumentException.class, () -> cart.removeItem("prod_1", 3));
        assertEquals("Нельзя удалить больше товаров, чем есть в корзине", ex3.getMessage());
    }
    @Test
    void testCalculateTotalNoPromo() {
        assertEquals(0.0, cart.calculateTotal(catalog, null));

        cart.addItem(product, 2);
        assertEquals(100000.0, cart.calculateTotal(catalog, null));

        // Товара нет в каталоге
        cart.addItem(new Product("prod_2", "Чехол", 1000.0, 5), 1);
        assertEquals(100000.0, cart.calculateTotal(catalog, null));
    }
    @Test
    void testCalculateTotalWithValidPromo() {
        cart.addItem(product, 2); // 100000
        assertEquals(90000.0, cart.calculateTotal(catalog, "SALE10"));
    }
    @Test
    void testCalculateTotalWithLowerCasePromo() {
        cart.addItem(product, 1); // 50000
        assertEquals(45000.0, cart.calculateTotal(catalog, "sale10"));
    }
    @Test
    void testCalculateTotalWithInvalidPromo() {
        cart.addItem(product, 1);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> cart.calculateTotal(catalog, "SALE20"));
        assertTrue(exception.getMessage().contains("Неверный промокод"));
    }
    @Test
    void testCalculateTotalEmptyCartWithPromo() {
        assertEquals(0.0, cart.calculateTotal(catalog, "SALE10"));
    }
}