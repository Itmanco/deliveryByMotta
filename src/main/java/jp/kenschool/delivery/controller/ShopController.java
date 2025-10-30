package jp.kenschool.delivery.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.kenschool.delivery.dao.ShopDAO;
import jp.kenschool.delivery.model.LoginModel;
import jp.kenschool.delivery.model.ShopModel;

/**
 * 店舗（ショップ）情報に関連するWebリクエストを処理するコントローラクラスです。
 * Controller class for handling web requests related to shop information.
 * * @author モッタハイメ
 */
@Controller
public class ShopController {

    // ログイン中のユーザー情報を保持するセッションスコープのBean
    // (Session-scoped bean that holds the logged-in user's information)
    @Autowired
    LoginModel loginModel;

    // データベース操作を実行するためのJdbcTemplate
    // (JdbcTemplate for executing database operations)
    @Autowired
    JdbcTemplate template;
    
    /**
     * 店舗一覧ページを表示します。
     * Displays the shop list page.
     *
     * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return 店舗一覧ページのビュー名 (View name for the shop list page)
     */
    @GetMapping(value="/shop")
    public String toShops(Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());
        
        // 1. DBから全ての店舗のリストを取得
        // (1. Get the list of all shops from the DB)
        ArrayList<ShopModel> shopList = new ShopDAO().getShopList(template);
        System.out.println("/shop->"+shopList);
        
        // 2. 店舗リストをモデルに追加
        // (2. Add the shop list to the model)
        model.addAttribute("shopList", shopList);
        
        // 3. ヘッダー表示用にセッション情報をモデルに追加
        // (3. Pass session info for the header)
        if (loginModel.getLogin_id() != null) {
            model.addAttribute("loginInput", loginModel);
        }
        
        return "shop"; // "shop.html" を返す (Return "shop.html")
    }

}