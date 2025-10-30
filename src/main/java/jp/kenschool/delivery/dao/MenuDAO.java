package jp.kenschool.delivery.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import jp.kenschool.delivery.model.MenuCategoryModel;
import jp.kenschool.delivery.model.MenuModel;

/**
 * menuテーブルおよびmenu_categoryテーブルに関連するデータベース操作（DAO）を行うクラスです。
 * Data Access Object (DAO) class for operations related to the `menu` and `menu_category` tables.
 * * @author モッタハイメ
 */
public class MenuDAO {
    
    /**
     * 指定された店舗IDに紐づくメニューアイテムを取得する。
     * Gets menu items associated with the specified shop ID.
     * * @param template JdbcTemplate
     * @param shopId 取得したいメニューの店舗ID (Shop ID for the desired menu)
     * @return MenuModelのリスト (List of MenuModel)
     */
    public List<MenuModel> getMenuItemsByShopId(JdbcTemplate template, String shopId) {
        // メニューテーブルから指定されたshop_idを持つアイテムを全て取得
        // (Get all items from the menu table with the specified shop_id)
        String sql = "SELECT * FROM menu WHERE shop_id = ?";
        
        List<MenuModel> menuList = new ArrayList<>();
        
        try {
            // queryForListを使うとMapのリストが返却される
            // (Using queryForList returns a List of Maps)
            List<Map<String, Object>> rows = template.queryForList(sql, shopId);
            
            // 取得した各行をMenuModelオブジェクトにマッピング
            // (Map each retrieved row to a MenuModel object)
            for (Map<String, Object> row : rows) {
                MenuModel menu = new MenuModel();
                
                // MapのキーとMenuModelのプロパティをマッピング
                // (Map the Map keys to the MenuModel properties)
                menu.setItem_id((String) row.get("item_id"));
                menu.setShop_id((String) row.get("shop_id"));
                menu.setItem_name((String) row.get("item_name"));
                
                // priceはデータベースではINT(8)のため、JavaではIntegerにキャスト
                // (Price is INT(8) in the DB, so cast to Integer in Java)
                menu.setPrice((Integer) row.get("price"));
                
                menu.setDescription((String) row.get("description"));
                menu.setImage_url((String) row.get("image_url"));
                
                menu.setCategory((Integer) row.get("category_id"));
                menu.setSubcategory((Integer) row.get("subcategory_id"));
                
                menuList.add(menu);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving menu for shopId: " + shopId);
            e.printStackTrace();
            // エラー時も空のリストを返す
            // (Return an empty list on error)
            return new ArrayList<>();
        }
        
        return menuList;
    }
    
    /**
     * 指定された店舗IDとカテゴリIDに紐づくメニューアイテムを取得する。
     * Gets menu items associated with the specified shop ID and category ID.
     * * @param template   JdbcTemplate
     * @param shopId     取得したいメニューの店舗ID (Shop ID for the desired menu)
     * @param categoryId 絞り込みたいカテゴリID (Category ID to filter by)
     * @return MenuModelのリスト (List of MenuModel)
     */
    public List<MenuModel> getMenuItemsByShopIdAndCategory(JdbcTemplate template, String shopId, String categoryId) {
        // メニューテーブルから指定されたshop_idとcategory_idを持つアイテムを全て取得
        // (Get all items from the menu table with the specified shop_id and category_id)
        String sql = "SELECT * FROM menu WHERE shop_id = ? AND category_id = ?";
        
        List<MenuModel> menuList = new ArrayList<>();
        
        try {
            // queryForListを使うとMapのリストが返却される
            // (Using queryForList returns a List of Maps)
            List<Map<String, Object>> rows = template.queryForList(sql, shopId, categoryId);
            
            for (Map<String, Object> row : rows) {
                MenuModel menu = new MenuModel();
                
                // MapのキーとMenuModelのプロパティをマッピング
                // (Map the Map keys to the MenuModel properties)
                menu.setItem_id((String) row.get("item_id"));
                menu.setShop_id((String) row.get("shop_id"));
                menu.setItem_name((String) row.get("item_name"));
                
                // priceはデータベースではINT(8)のため、JavaではIntegerにキャスト
                // (Price is INT(8) in the DB, so cast to Integer in Java)
                menu.setPrice((Integer) row.get("price"));
                
                menu.setDescription((String) row.get("description"));
                menu.setImage_url((String) row.get("image_url"));
                
                menu.setCategory((Integer) row.get("category_id"));
                menu.setSubcategory((Integer) row.get("subcategory_id"));
                
                menuList.add(menu);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving menu for shopId: " + shopId + " and categoryId: " + categoryId);
            e.printStackTrace();
            // エラー時も空のリストを返す
            // (Return an empty list on error)
            return new ArrayList<>();
        }
        
        return menuList;
    }
    
    /**
     * 単一のメニューアイテムをIDで取得する。 (OrderControllerで使用)
     * Gets a single menu item by its ID. (Used by OrderController)
     * * @param template JdbcTemplate
     * @param itemId 取得したいメニューアイテムのID (ID of the menu item to get)
     * @return MenuModelオブジェクト、見つからない場合はnull (MenuModel object, or null if not found)
     */
    public MenuModel getMenuItemById(JdbcTemplate template, String itemId) {
        String sql = "SELECT * FROM menu WHERE item_id = ?";
        System.out.println("MenuDAO.getMenuItemById-1>"+template+", "+itemId);
        MenuModel menu = null;
        try {
            // queryForMapは単一の行をMapとして取得
            // (queryForMap retrieves a single row as a Map)
            Map<String, Object> row = template.queryForMap(sql, itemId);
            
            System.out.println("MenuDAO.getMenuItemById-2>"+row);
            menu = new MenuModel();
            
            // MapのキーとMenuModelのプロパティをマッピング
            // (Map the Map keys to the MenuModel properties)
            menu.setItem_id((String) row.get("item_id"));
            menu.setShop_id((String) row.get("shop_id"));
            menu.setItem_name((String) row.get("item_name"));
            menu.setPrice((Integer) row.get("price"));
            menu.setDescription((String) row.get("description"));
            menu.setImage_url((String) row.get("image_url"));
            menu.setCategory((Integer) row.get("category_id"));
            menu.setSubcategory((Integer) row.get("subcategory_id"));
            
        } catch (EmptyResultDataAccessException e) {
            System.out.println("MenuDAO.getMenuItemById-3>EmptyResultDataAccessException");
            e.printStackTrace();
            // データが見つからなかった場合はnullを返す
            // (Return null if data is not found)
            return null;
        } catch (Exception e) {
            System.err.println("Error retrieving menu item by ID: " + itemId);
            e.printStackTrace();
            return null;
        }
        
        return menu;
    }

    /**
     * 指定された店舗が提供しているメニューカテゴリのリストを取得する（重複なし）。
     * Gets a distinct list of menu categories offered by the specified shop.
     * * @param template JdbcTemplate
     * @param shopId   店舗ID (Shop ID)
     * @return MenuCategoryModelのリスト (List of MenuCategoryModel)
     */
    public List<MenuCategoryModel> getMenuCategories(JdbcTemplate template, String shopId) {
         // menuテーブルに存在するカテゴリのみをmenu_categoryテーブルから取得
         // (Get only categories from menu_category that exist in the menu table for that shop)
         String sql = "SELECT mc.category_id, mc.name"
                + " FROM menu_category mc"
                + " INNER JOIN menu m ON mc.category_id = m.category_id"
                + " WHERE m.shop_id = ?"
                + " GROUP BY mc.category_id, mc.name"; // SQL標準に準拠するためGROUP BYに両方の列を指定 (Specify both columns in GROUP BY for SQL standard compliance)
        
        List<MenuCategoryModel> categoriesList = new ArrayList<>();
        
        try {
            List<Map<String, Object>> rows = template.queryForList(sql, shopId);
            
            for (Map<String, Object> row : rows) {
                MenuCategoryModel category = new MenuCategoryModel();
                
                // MapのキーとMenuCategoryModelのプロパティをマッピング
                // (Map the Map keys to the MenuCategoryModel properties)
                category.setCategory_id((Integer) row.get("category_id"));
                category.setName((String) row.get("name"));
                
                categoriesList.add(category);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving menu categories for shopId: " + shopId);
            e.printStackTrace();
            // エラー時も空のリストを返す
            // (Return an empty list on error)
            return new ArrayList<>();
        }
        
        return categoriesList;
    }
}