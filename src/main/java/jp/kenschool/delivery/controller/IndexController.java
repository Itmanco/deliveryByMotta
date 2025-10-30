package jp.kenschool.delivery.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.kenschool.delivery.model.LoginModel;

/**
 * アプリケーションのメインインデックス（トップページ）へのリクエストを処理するコントローラクラスです。
 * Controller class for handling requests to the application's main index (top page).
 * * @author モッタハイメ
 */
@Controller
public class IndexController {

    // ログイン中のユーザー情報を保持するセッションスコープのBean
    // (Session-scoped bean that holds the logged-in user's information)
    @Autowired
    LoginModel loginModel;

    /**
     * トップページ（インデックスページ）を表示します。
     * Displays the top page (index page).
     *
     * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return インデックスページのビュー名 (View name for the index page)
     */
    @GetMapping(value = {"/", "/delivery"})
    public String toTop(Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());
        
        System.out.println("/delivery->loginModel:" + loginModel);
        
        // ログイン状態かどうかを確認 (Check if logged in)
        if (loginModel.getLogin_id() != null) {
            // ログインしている場合、ユーザー情報をモデルに追加してビューで利用できるようにする
            // (If logged in, add user info to the model for use in the view)
            model.addAttribute("loginInput", loginModel);
        }
        return "index"; // "index.html" (or .jsp) を返す (Return "index.html")
    }
}