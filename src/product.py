class Product:
    def __init__(self, product_id: str, name: str, price: float, stock: int):
        self.id = product_id
        self.name = name
        self.price = price
        self.stock = stock