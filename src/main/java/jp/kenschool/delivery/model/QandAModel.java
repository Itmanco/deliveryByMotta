package jp.kenschool.delivery.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 秘密の質問と回答（Q&A）に関連するデータを保持するモデルクラスです。
 * 主にパスワード忘れ機能などで使用されることを想定しています。
 * Model class for holding data related to secret questions and answers (Q&A).
 * Primarily intended for use in features like "forgot password".
 *
 * @author モッタハイメ
 */
@Data
public class QandAModel {

    /**
     * ログインID (login_id)
     * The login ID.
     */
    @NotBlank
    private String login_id;
    
    /**
     * 顧客名 (name)
     * Customer's name.
     */
    private String name;
    
    /**
     * パスワード (password)
     * Password.
     */
    private String password;
    
    /**
     * 秘密の質問 (question)
     * The secret question.
     */
    private String question;
    
    /**
     * 秘密の質問の答え (answer)
     * The answer to the secret question.
     */
    private String answer;
}