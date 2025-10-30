package jp.kenschool.delivery.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

/**
 * ログインしているユーザーの情報をセッション全体で保持するためのモデルクラスです。
 * このBeanはセッションスコープで管理されます。
 * Model class for holding the logged-in user's information throughout the session.
 * This bean is managed in session scope.
 *
 * @author モッタハイメ
 */
@Data
@Component
@SessionScope
public class LoginModel {

    /**
     * ログイン中の顧客ID (customer_id)
     * The logged-in customer's ID.
     */
    private Integer customer_id;
    
    /**
     * ログイン中の顧客のログインID (login_id)
     * The logged-in customer's login ID.
     */
    private String login_id;
    
    /**
     * ログイン中の顧客名 (name)
     * The logged-in customer's name.
     */
    private String name;
    
    /**
     * ログイン中の顧客のパスワード (password)
     * (注意：セキュリティ上、平文のパスワードをセッションに長期間保持するのは非推奨です)
     * The logged-in customer's password.
     * (Note: Storing plaintext passwords in session for long periods is not recommended for security)
     */
    private String password;
    
    /**
     * ログアウト時にセッション情報をクリアするために、保持しているフィールドをすべてnullにします。
     * Clears all held fields (sets to null) to clear session information upon logout.
     */
    public void clearFields() {
        this.customer_id = null;
        this.login_id = null;
        this.name = null;
        this.password = null;
    }
}