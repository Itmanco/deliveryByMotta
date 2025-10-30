package jp.kenschool.delivery.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import jp.kenschool.delivery.model.ShopModel;

/**
 * shopsテーブルに関連するデータベース操作（DAO）を行うクラスです。
 * Data Access Object (DAO) class for operations related to the `shops` table.
 * * @author モッタハイメ
 */
public class ShopDAO {
    
    /**
     * データベースから全ての店舗のリストを取得します。
     * Retrieves a list of all shops from the database.
     *
     * @param template JdbcTemplate
     * @return 店舗のShopModelオブジェクトを含むArrayList（エラーの場合はnull）
     * (An ArrayList of ShopModel objects, or null on error)
     */
    public ArrayList<ShopModel> getShopList(JdbcTemplate template) {
        // 全ての店舗を選択 (Select all shops)
        String sql = "SELECT * FROM shops";
        List<Map<String,Object>> list = null;
        ArrayList<ShopModel> al = null;
        try {
            // SQLを実行し、結果をMapのListとして取得
            // (Execute SQL and get results as a List of Maps)
            list = template.queryForList(sql);
            
            // 結果が存在する場合
            // (If results exist)
            if(list != null && !list.isEmpty()) {
                al = new ArrayList<>();
                
                // 各行（Map）をShopModelオブジェクトにマッピング
                // (Map each row (Map) to a ShopModel object)
                for(Map<String,Object> shop: list) {
                    ShopModel s = new ShopModel();
                    s.setShop_id((String)shop.get("shop_id"));
                    s.setShop_name((String)shop.get("shop_name"));
                    s.setPhone((String)shop.get("phone"));
                    s.setZip((String)shop.get("zip"));
                    s.setAddress((String)shop.get("address"));
                    s.setExplanation((String)shop.get("explanation"));
                    s.setMap_url((String)shop.get("map_url"));
                    al.add(s);
                }
            }
            return al;
        } catch(Exception e) {
            // DBエラー (DB error)
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 指定された店舗IDを使用して、1件の店舗情報を取得します。
     * Retrieves a single shop's information using the specified shop ID.
     *
     * @param template JdbcTemplate
     * @param shopId   検索する店舗ID (The shop ID to search for)
     * @return 該当するShopModelオブジェクト（見つからないかエラーの場合はnull）
     * (The corresponding ShopModel object, or null if not found or on error)
     */
    public ShopModel getShopById(JdbcTemplate template, String shopId) {
        String sql = "SELECT shop_id, shop_name, phone, zip, address, explanation, map_url FROM shops WHERE shop_id = ?";
        try {
            // queryForMapは単一の行をMapとして取得
            // (queryForMap retrieves a single row as a Map)
            Map<String, Object> row = template.queryForMap(sql, shopId);
            
            // 新しいShopModelにデータをマッピング
            // (Map data to a new ShopModel)
            ShopModel shop = new ShopModel();
            shop.setShop_id((String) row.get("shop_id"));
            shop.setShop_name((String) row.get("shop_name"));
            shop.setPhone((String) row.get("phone"));
            shop.setZip((String) row.get("zip"));
            shop.setAddress((String) row.get("address"));
            shop.setExplanation((String) row.get("explanation"));
            shop.setMap_url((String) row.get("map_url"));
            return shop;
        } catch (Exception e) {
            // EmptyResultDataAccessException（見つからない）または他のDBエラー
            // (EmptyResultDataAccessException (not found) or other DB error)
            e.printStackTrace();
            return null;
        }
    }
}