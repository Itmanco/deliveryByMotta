package jp.kenschool.delivery.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 顧客情報を保持するモデルクラスです。
 * データベースのcustomerテーブルのレコードに対応し、フォームのバリデーションルールも定義します。
 * Model class for holding customer information.
 * Corresponds to records in the 'customer' table and defines form validation rules.
 *
 * @author モッタハイメ
 */
@Data
public class CustomerModel {

    /**
     * 顧客ID (customer_id) - 主キー
     * The customer ID (Primary Key).
     */
    private Integer customer_id;
    
    /**
     * ログインID（Eメール形式）
     * Login ID (Must be in E-Mail format).
     */
    @NotBlank(message="login_idを入力してください。")
    @Email(message="login_id(E-Mail形式)が不正です。")
    private String login_id;
    
    /**
     * 顧客名 (name)
     * Customer's name.
     */
    @NotBlank(message="お名前を入力してください。")
    private String name;
    
    /**
     * パスワード (password) - 平文
     * Password (in plaintext).
     */
    @NotBlank(message="パスワードを入力してください。")
    private String password;
    
    /**
     * 電話番号 (phone) - 0から始まる10桁または11桁
     * Phone number (10 or 11 digits, starting with 0).
     */
    @Pattern(regexp = "^[0][0-9]{9,10}$", message = "電話番号はハイフン（-）なしで、0から始まる10桁または11桁の数字で入力してください。")
    private String phone;
    
    /**
     * 郵便番号 (zip) - 7桁の数字
     * ZIP code (7 digits).
     */
    @Pattern(regexp = "^[0-9]{7}$", message = "郵便番号はハイフン（-）なしの7桁の数字で入力してください。")
    private String zip;
    
    /**
     * 住所1 (都道府県、市区町村、番地)
     * Address line 1 (Prefecture, City, Street number).
     */
    @NotBlank(message="住所を入力してください。")
    private String address1;
    
    /**
     * 住所2 (建物名、部屋番号など) - 任意入力
     * Address line 2 (Building name, room number, etc.) - Optional.
     */
    private String address2;
    
    /**
     * パスワード忘れ用の秘密の質問
     * Secret question for password reset.
     */
    @NotBlank(message="秘密の質問を入力してください。")
    private String question;
    
    /**
     * 秘密の質問の答え
     * Answer to the secret question.
     */
    @NotBlank(message="秘密の質問の答えを入力してください。")
    private String answer;
    
    /**
     * 保有ポイント (point)
     * Customer's points.
     */
    private Integer point;

}