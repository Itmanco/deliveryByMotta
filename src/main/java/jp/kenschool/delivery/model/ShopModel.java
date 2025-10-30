package jp.kenschool.delivery.model;

import lombok.Data;

/**
 * 店舗（ショップ）の情報を保持するモデルクラスです。
 * データベースのshopsテーブルのレコードに対応します。
 * Model class for holding shop information.
 * Corresponds to records in the 'shops' table.
 *
 * @author モッタハイメ
 */
@Data
public class ShopModel {
    
    /**
     * 店舗ID (shop_id) - 主キー
     * The shop ID (Primary Key).
     */
    private String shop_id;
    
    /**
     * 店舗名 (shop_name)
     * The name of the shop.
     */
    private String shop_name;
    
    /**
     * 店舗の電話番号 (phone)
     * The shop's phone number.
     */
    private String phone;
    
    /**
     * 店舗の郵便番号 (zip)
     * The shop's ZIP code.
     */
    private String zip;
    
    /**
     * 店舗の住所 (address)
     * The shop's address.
     */
    private String address;
    
    /**
     * 店舗の説明文 (explanation)
     * A description of the shop.
     */
    private String explanation;
    
    /**
     * Googleマップなどの地図URL (map_url)
     * The URL for a map (e.g., Google Maps).
     */
    private String map_url;
}