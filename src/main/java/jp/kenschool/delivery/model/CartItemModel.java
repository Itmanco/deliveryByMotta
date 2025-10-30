package jp.kenschool.delivery.model;

import lombok.Data;

/**
 * ショッピングカート内の個々の商品アイテムを表すモデルクラスです。
 * Model class representing an individual item within the shopping cart.
 *
 * @author モッタハイメ
 */
@Data
public class CartItemModel {
    
    /**
     * 商品ID (item_id)
     * The item ID.
     */
    private String item_id;
    
    /**
     * 商品名 (item_name)
     * The item name.
     */
    private String item_name;
    
    /**
     * 商品の単価 (price)
     * The unit price of the item.
     */
    private Integer price;
    
    /**
     * カート内の数量 (quantity)
     * The quantity of this item in the cart.
     */
    private Integer quantity;
    
    /**
     * この商品が属する店舗ID (shop_id)
     * The shop ID this item belongs to.
     */
    private String shop_id; // To track where the order comes from

    /**
     * このカートアイテムの小計（単価 * 数量）を計算します。
     * Calculates the subtotal (price * quantity) for this cart item.
     *
     * @return 計算された小計 (The calculated subtotal)
     */
    public int getSubtotal() {
        if (price != null && quantity != null) {
            return price * quantity;
        }
        return 0; // priceまたはquantityがnullの場合は0を返す (Return 0 if price or quantity is null)
    }
}