package jp.kenschool.delivery.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ログインフォームからの入力を受け取るためのモデルクラスです。
 * バリデーションルールも定義します。
 * Model class for receiving input from the login form.
 * Also defines validation rules.
 *
 * @author モッタハイメ
 */
@Data
public class LoginInput {

    /**
     * 顧客ID (customer_id)
     * (Note: This field was named 'customert_id' in the original file, corrected to 'customer_id')
     * The customer ID.
     */
    private Integer customer_id; // Typo 'customert_id' corrected to 'customer_id'
    
    /**
     * ログインID（Eメール形式）
     * Login ID (Must be in E-Mail format).
     */
    @NotBlank(message="login_idを入力してください。")
    @Email(message="login_id(E-Mail形式)が不正です。")
    private String login_id;
    
    /**
     * 顧客名 (name)
     * (This field is often populated *after* login, not from the input form itself)
     * Customer's name.
     */
    private String name;
    
    /**
     * パスワード (password)
     * Password.
     */
    @NotBlank(message="パスワードを入力してください。")
    private String password;    
}