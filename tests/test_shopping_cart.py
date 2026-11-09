import pytest
from src.product import Product
from src.shopping_cart import ShoppingCart

@pytest.fixture
def sample_product():
    return Product(product_id="prod_1", name="Смартфон", price=50000.0, stock=10)

@pytest.fixture
def sample_cart():
    return ShoppingCart()

def test_add_item_success(sample_cart, sample_product):
    sample_cart.add_item(sample_product, 2)
    assert sample_cart.items["prod_1"] == 2
    sample_cart.add_item(sample_product, 3)
    assert sample_cart.items["prod_1"] == 5

def test_add_item_invalid_quantity(sample_cart, sample_product):
    with pytest.raises(ValueError, match="Количество должно быть больше нуля"):
        sample_cart.add_item(sample_product, 0)
    with pytest.raises(ValueError, match="Количество должно быть больше нуля"):
        sample_cart.add_item(sample_product, -1)

def test_add_item_exceeds_stock(sample_cart, sample_product):
    with pytest.raises(ValueError, match="Недостаточно товара на складе"):
        sample_cart.add_item(sample_product, 11)
    sample_cart.add_item(sample_product, 5)
    with pytest.raises(ValueError, match="Недостаточно товара на складе"):
        sample_cart.add_item(sample_product, 6)

def test_remove_item_success(sample_cart, sample_product):
    sample_cart.add_item(sample_product, 5)
    sample_cart.remove_item("prod_1", 2)
    assert sample_cart.items["prod_1"] == 3
    sample_cart.remove_item("prod_1", 3)
    assert "prod_1" not in sample_cart.items

def test_remove_item_errors(sample_cart, sample_product):
    with pytest.raises(ValueError, match="Количество для удаления должно быть больше нуля"):
        sample_cart.remove_item("prod_1", 0)
    with pytest.raises(ValueError, match="Товара нет в корзине"):
        sample_cart.remove_item("prod_2", 1)
    sample_cart.add_item(sample_product, 2)
    with pytest.raises(ValueError, match="Нельзя удалить больше товаров, чем есть в корзине"):
        sample_cart.remove_item("prod_1", 3)

def test_calculate_total(sample_cart, sample_product):
    assert sample_cart.calculate_total({"prod_1": sample_product}) == 0.0
    sample_cart.add_item(sample_product, 2)
    catalog = {"prod_1": sample_product}
    assert sample_cart.calculate_total(catalog) == 100000.0
    assert sample_cart.calculate_total({}) == 0.0