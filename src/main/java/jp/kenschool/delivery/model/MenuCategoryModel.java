package jp.kenschool.delivery.model;

import lombok.Data;

/**
 * メニューカテゴリの情報を保持するモデルクラスです。
 * データベースのmenu_categoryテーブルのレコードに対応します。
 * Model class for holding menu category information.
 * Corresponds to records in the 'menu_category' table.
 *
 * @author モッタハイメ
 */
@Data
public class MenuCategoryModel {
    
    /**
     * カテゴリID (category_id) - 主キー
     * The category ID (Primary Key).
     */
    private Integer category_id;
    
    /**
     * カテゴリ名 (name) (例: "ピザ", "ドリンク")
     * The category name (e.g., "Pizza", "Drinks").
     */
    private String name;
}