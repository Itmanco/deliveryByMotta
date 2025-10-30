package jp.kenschool.delivery.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.kenschool.delivery.dao.MenuDAO;
import jp.kenschool.delivery.dao.ShopDAO;
import jp.kenschool.delivery.model.LoginModel;
import jp.kenschool.delivery.model.MenuCategoryModel;
import jp.kenschool.delivery.model.MenuModel;
import jp.kenschool.delivery.model.ShopModel;

/**
 * 店舗のメニュー表示に関連するWebリクエストを処理するコントローラクラスです。
 * Controller class for handling web requests related to displaying shop menus.
 * * @author モッタハイメ
 */
@Controller
public class MenuController {
    
    // ログイン中のユーザー情報を保持するセッションスコープのBean
    // (Session-scoped bean that holds the logged-in user's information)
    @Autowired
    LoginModel loginModel;
    
    // データベース操作を実行するためのJdbcTemplate
    // (JdbcTemplate for executing database operations)
    @Autowired
    JdbcTemplate template;
    
    // DAOのインスタンス化 (Instantiate DAOs)
    // (本番環境では@AutowiredによるDIを推奨)
    // (In production, DI with @Autowired is recommended)
    private ShopDAO shopDAO = new ShopDAO();
    private MenuDAO menuDAO = new MenuDAO();
    
    /**
     * 指定された店舗IDまたはデフォルトの店舗に基づき、メニューページを表示します。
     * Displays the menu page based on the specified shop ID or a default shop.
     *
     * @param selectedShopId     リクエストパラメータから受け取った店舗ID (Shop ID from request parameter)
     * @param selectedCategoryId リクエストパラメータから受け取ったカテゴリID (Category ID from request parameter)
     * @param model              ビューに渡すためのモデル (Model for passing data to the view)
     * @return メニューページのビュー名 (View name for the menu page)
     */
    @GetMapping(value="/menu")
    public String showMenu(
            @RequestParam(value = "shopId", required = false) String selectedShopId,
            @RequestParam(value = "categoryId", required = false) String selectedCategoryId,
            Model model) {
        
        model.addAttribute("currentTime", LocalDateTime.now());
        
        // 1. （ドロップダウンリスト用に）全ての店舗情報を取得
        // (1. Get ALL Shops (for the dropdown list))
        ArrayList<ShopModel> shopList = shopDAO.getShopList(template);
        
        // 2. メニューとカテゴリのフィルタリングに使用する店舗IDを決定
        // (2. Determine the shop ID to use for filtering the menu and categories)
        String activeShopId;
        if (selectedShopId != null && !selectedShopId.isEmpty()) {
            // ユーザーが店舗を選択した場合
            // (If the user selected a shop)
            activeShopId = selectedShopId;
        } else if (!shopList.isEmpty()) {
            // デフォルトとして、リストの最初の店舗を使用
            // (Default to the first available shop if none is selected)
            activeShopId = shopList.get(0).getShop_id();
        } else {
            // 利用可能な店舗がない場合
            // (No shops available case)
            activeShopId = null;
        }

        // 3. フィルタリングされたメニュー項目とカテゴリを準備
        // (3. Prepare filtered menu items and categories)
        List<MenuModel> menuItems = new ArrayList<>();
        List<MenuCategoryModel> categoryItems = new ArrayList<>();
        ShopModel activeShop = null;
        
        if (activeShopId != null) {
            // 4. activeShopIdに基づいてメニュー項目を取得
            // (4. Fetch Menu Items based on activeShopId)
            
            // カテゴリIDが指定され、"ALL"でない場合、カテゴリで絞り込む
            // (If categoryId is specified and is not "ALL", filter by category)
            if(selectedCategoryId != null && !selectedCategoryId.isEmpty() && !selectedCategoryId.equals("ALL")) {
                menuItems = menuDAO.getMenuItemsByShopIdAndCategory(template, activeShopId, selectedCategoryId);
            } else {
                // 店舗の全メニューを取得
                // (Get all menus for the shop)
                menuItems = menuDAO.getMenuItemsByShopId(template, activeShopId);
            }
            
            // 5. その店舗で利用可能なカテゴリを取得
            // (5. Get available categories for that shop)
            categoryItems = menuDAO.getMenuCategories(template, activeShopId);
            
            // 6. 選択中の店舗のモデルオブジェクトを取得（店舗名表示用）
            // (6. Get the specific shop model to display its name)
            activeShop = shopDAO.getShopById(template, activeShopId);
            
        }
        
        // activeShopがnullだが店舗リストには存在する、という稀なケースへのフォールバック（最初の店舗をセット）
        // (Fallback for the rare case where activeShop is null but shopList is not empty - set to the first shop)
        // ※ original: activeShop = shopList.isEmpty() ? shopList.get(0) : activeShop; (This seems to be a bug, fixed logic below)
        if (activeShop == null && !shopList.isEmpty()) {
            activeShop = shopList.get(0);
        }

        // 7. 必要なすべてのデータをモデルに追加
        // (7. Add all necessary data to the Model)
        model.addAttribute("shopList", shopList);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("categoryItems", categoryItems);
        model.addAttribute("activeShop", activeShop);
        model.addAttribute("selectedCategoryId", selectedCategoryId); // 選択されたカテゴリをビューに戻す (Pass back the selected category)
        
        // 8. ヘッダー表示用にセッション情報をモデルに追加
        // (8. Pass session info for the header)
        if (loginModel.getLogin_id() != null) {
            model.addAttribute("loginInput", loginModel);
        }
        return "menu"; // "menu.html" を返す (Return "menu.html")
    }
}