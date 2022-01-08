package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p: productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                int bought_products = 1;
                int bought_products_ratio = quantityAsInt / bought_products;
                
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
                }
                else {
                    double offer_amount = 0;
                    double price_coefficient = 1;
                    if (offer.offerType == SpecialOfferType.ThreeForTwo){
                        bought_products  = 3;
                        offer_amount = 2;
                        price_coefficient = unitPrice;
                    }
                    else if (offer.offerType == SpecialOfferType.TwoForAmount) {
                        bought_products = 2;
                        offer_amount = offer.argument;
                    }
                    else if (offer.offerType == SpecialOfferType.FiveForAmount) {
                        bought_products  = 5;
                        offer_amount = offer.argument;
                    }
                    if (quantityAsInt >= bought_products) {
                        double discountTotal = unitPrice * quantity - (offer_amount * bought_products_ratio * price_coefficient + quantityAsInt % bought_products * unitPrice);
                        discount = new Discount(p, bought_products  + " for " + offer_amount, -discountTotal);
                    }
                }

                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }
}