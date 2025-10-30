package jp.kenschool.delivery.model;

import lombok.Data;

/**
 * 個々のメニュー商品の情報を保持するモデルクラスです。
 * データベースのmenuテーブルのレコードに対応します。
 * Model class for holding individual menu item information.
 * Corresponds to records in the 'menu' table.
 *
 * @author モッタハイメ
 */
@Data
public class MenuModel {
    
    /**
     * 商品ID (item_id) - 主キー
     * The item ID (Primary Key).
     */
    private String item_id;
    
    /**
     * 商品名 (item_name)
     * The name of the item.
     */
    private String item_name;
    
    /**
     * この商品が属する店舗ID (shop_id) - 外部キー
     * The shop ID this item belongs to (Foreign Key).
     */
    private String shop_id;
    
    /**
     * 価格 (price)
     * The price of the item.
     */
    private Integer price;
    
    /**
     * 商品説明 (description)
     * The description of the item.
     */
    private String description;
    
    /**
     * 商品画像のURL (image_url)
     * The URL for the item's image.
     */
    private String image_url;
    
    /**
     * カテゴリID (category_id) - 外部キー
     * The category ID (Foreign Key).
     * (Note: Field name is 'category' but corresponds to 'category_id' in the DB)
     */
    private Integer category;
    
    /**
     * サブカテゴリID (subcategory_id) - 外部キー
     * The subcategory ID (Foreign Key).
     * (Note: Field name is 'subcategory' but corresponds to 'subcategory_id' in the DB)
     */
    private Integer subcategory;
}