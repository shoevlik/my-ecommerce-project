from src.product import Product

class ShoppingCart:
    def __init__(self):
        self.items = {}

    def add_item(self, product: Product, quantity: int):
        if quantity <= 0:
            raise ValueError("Количество должно быть больше нуля")
        
        current_quantity = self.items.get(product.id, 0)
        if current_quantity + quantity > product.stock:
            raise ValueError(f"Недостаточно товара на складе")

        self.items[product.id] = current_quantity + quantity

    def remove_item(self, product_id: str, quantity: int):
        if quantity <= 0:
            raise ValueError("Количество для удаления должно быть больше нуля")
        
        if product_id not in self.items:
            raise ValueError("Товара нет в корзине")
        
        current_quantity = self.items[product_id]
        if quantity > current_quantity:
            raise ValueError("Нельзя удалить больше товаров, чем есть в корзине")

        if quantity == current_quantity:
            del self.items[product_id]
        else:
            self.items[product_id] -= quantity

    def calculate_total(self, catalog: dict[str, Product]) -> float:
        total = 0.0
        for product_id, quantity in self.items.items():
            if product_id in catalog:
                total += catalog[product_id].price * quantity
        return total