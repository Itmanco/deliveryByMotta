package jp.kenschool.delivery.controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;
import jp.kenschool.delivery.dao.CustomerDAO;
import jp.kenschool.delivery.model.CustomerModel;
import jp.kenschool.delivery.model.LoginInput;
import jp.kenschool.delivery.model.LoginModel;

/**
 * 顧客関連のWebリクエスト（ログイン、ログアウト、登録、更新、パスワード忘れ）を処理するコントローラクラスです。
 * Controller class for handling customer-related web requests (login, logout, registration, update, password reset).
 * * @author モッタハイメ
 */
@Controller
public class CustomerController {
    
    // ログイン中のユーザー情報を保持するセッションスコープのBean
    // (Session-scoped bean that holds the logged-in user's information)
    @Autowired
    LoginModel loginModel;
    
    // データベース操作を実行するためのJdbcTemplate
    // (JdbcTemplate for executing database operations)
    @Autowired
    JdbcTemplate template;
    
    /**
     * ログインページを表示します。
     * Displays the login page.
     * * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return ログインページのビュー名 (View name for the login page)
     */
    @GetMapping(value="/login")
    public String toLogin(Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());
        
        // 既にログインしている場合（セッションが有効な場合）、フォームにIDをセットする
        // (If already logged in (session is active), set the ID in the form)
        if (loginModel.getLogin_id() != null) {
            model.addAttribute("loginInput", loginModel);
        } else {
            // 新規ログインフォーム
            // (New login form)
            model.addAttribute("loginInput", new LoginInput());
        }
        
        return "login"; // "login.html" (or .jsp) を返す (Return "login.html")
    }
    
    /**
     * ログイン処理を実行します。
     * Executes the login process.
     * * @param loginInput2 フォームから送信されたログイン情報 (Login information from the form)
     * @param result バリデーション結果 (Validation result)
     * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return 成功時はデリバリーページへリダイレクト、失敗時はログインページ (Redirects to delivery page on success, or login page on failure)
     */
    @PostMapping(value="/login")
    public String login(@ModelAttribute @Validated LoginInput loginInput2, BindingResult result, Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());

        // バリデーションエラーがある場合
        // (If validation errors exist)
        if(result.hasErrors()) {
            model.addAttribute("message","login_idまたはpasswordを確認してください。");
            return "/login"; // ログインページに戻る (Return to login page)
        }else {
            // DBからユーザー情報を取得 (Get user info from DB)
            Map<String,Object> local = new CustomerDAO().getOneRecord(template,loginInput2);
            System.out.println("/login->local:"+local);
            
            // ユーザーが存在しない、またはパスワードが一致しない場合
            // (If user does not exist or password mismatch)
            if (local == null || local.isEmpty()) {
                model.addAttribute("message","login_idまたはpasswordを確認してください。");
                return "/login";
            }
            
            // 認証成功：セッションスコープのLoginModelに情報をセット
            // (Auth success: Set info to session-scoped LoginModel)
            System.out.println("before:loginModel:"+loginModel);
            loginModel.setLogin_id((String)local.get("login_id"));
            loginModel.setName((String)local.get("name"));
            loginModel.setPassword(loginInput2.getPassword());
            loginModel.setCustomer_id((Integer)local.get("customer_id"));
            System.out.println("after:loginModel:"+loginModel);
            
            return "redirect:/delivery"; // メインページへリダイレクト (Redirect to main page)
        }
    }
    
    /**
     * ログアウト処理を実行します。
     * Executes the logout process.
     * * @param session HTTPセッション (HTTP Session)
     * @param status セッションステータス (Session status)
     * @param model モデル (Model)
     * @return デリバリーページへのリダイレクト (Redirect to the delivery page)
     */
    @GetMapping(value="/logout")
    public String logout(HttpSession session, SessionStatus status, Model model) {
        
        // Springのセッション（@SessionAttributes）を完了としてマーク
        // (Mark Spring session (@SessionAttributes) as complete)
        status.setComplete();
        
        // HTTPセッションを無効化
        // (Invalidate HTTP session)
        session.invalidate();
        
        // セッションスコープBeanの内部状態をクリア
        // (Clear internal state of session-scoped bean)
        loginModel.clearFields();
        return "redirect:/delivery";
    }
    
    /**
     * パスワード忘れ（ID入力）ページを表示します。
     * Displays the "forgot password" (ID input) page.
     * * @return パスワード忘れページのビュー名 (View name for the "forgot password" page)
     */
    @GetMapping(value="/forget")
    public String forget() {
        return "fogetPassword";
    }
    
    /**
     * ログインIDを受け取り、秘密の質問を検索して表示します。
     * Receives the Login ID, searches for the secret question, and displays it.
     * * @param login_id フォームから送信されたログインID (Login ID submitted from the form)
     * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return パスワード忘れページのビュー名 (View name for the "forgot password" page)
     */
    @PostMapping(value="/forget")
    public String forget(@RequestParam String login_id, Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());
        
        // IDをキーにユーザー情報を取得 (Get user info by ID)
        Map<String, Object> local = new CustomerDAO().getOneRecord(template,login_id);
        
        System.out.println("/login->local:"+local);
        
        // ユーザーが存在しない場合
        // (If user does not exist)
        if (local == null || local.isEmpty()) {
            model.addAttribute("message","login_id"+login_id +"は登録されていません。");
            return "/fogetPassword";
        }
        
        // ユーザー情報をモデルに追加し、質問を表示させる
        // (Add user info to model to display the question)
        model.addAttribute("login_id",login_id);
        model.addAttribute("name",(String)local.get("name"));
        model.addAttribute("question",(String)local.get("question"));

        // 同じビューに戻すが、今度は質問が表示される
        // (Return to the same view, but now showing the question)
        return "fogetPassword";
    }
    
    /**
     * 秘密の質問の答えを検証し、新パスワードを発行します。
     * Verifies the answer to the secret question and issues a new password.
     * * @param login_id ログインID (Login ID)
     * @param answer 秘密の質問への答え (Answer to the secret question)
     * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return 答えが正しければ新パスワード表示ページ、間違っていればパスワード忘れページ (New password page if correct, or back to forgot password page if wrong)
     */
    @PostMapping(value="/newPassword")
    public String getPassword(@RequestParam String login_id, @RequestParam String answer, Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());
        Map<String, Object> local = new CustomerDAO().getOneRecord(template,login_id);
        
        // 答えが一致しない場合
        // (If the answer does not match)
        if (!local.get("answer").equals(answer)) {
            model.addAttribute("message",login_id+"様、答えが未入力か間違っています。");
            
            // 質問ページに戻すため、再度情報をセット
            // (Set info again to return to the question page)
            model.addAttribute("login_id",login_id);
            model.addAttribute("name",(String)local.get("name"));
            model.addAttribute("question",(String)local.get("question"));
            return "/fogetPassword";
        }
        
        // 答えが一致した場合 (If the answer matches)
        String newPassword = generatePassword(); // 新パスワードを生成 (Generate new password)
        new CustomerDAO().setNewPassword(template,login_id,newPassword); // DBを更新 (Update DB)
        
        model.addAttribute("newPassword",newPassword); // 新パスワードをビューに渡す (Pass new password to view)

        return "showNewPassword"; // 新パスワード表示ページへ (To "show new password" page)
    }
    
    /**
     * 12桁のランダムな新パスワードを生成します。
     * Generates a 12-character random new password.
     * * @return 生成された新しいパスワード文字列 (The newly generated password string)
     */
    private String generatePassword() {
        // パスワードに使用する文字セット
        // (Character set to use for the password)
        final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "012345678" + // 注意：元のコードでは'9'が抜けています (Note: '9' is missing in the original code)
            "_-!#$<>/";
        
        // パスワードの長さ (Password length)
        final int PASSWORD_LENGTH = 12;
        
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        
        // 安全な乱数ジェネレータ
        // (Secure random generator)
        SecureRandom random = new SecureRandom();
        
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            // CHARACTERSの長さからランダムなインデックスを取得
            // (Get a random index from CHARACTERS length)
            int randomIndex = random.nextInt(CHARACTERS.length());
            
            // パスワードを構築 (Build the password)
            password.append(CHARACTERS.charAt(randomIndex));
        }
        return password.toString();
    }
    
    /**
     * 新規顧客登録ページを表示します。
     * Displays the new customer registration page.
     * * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return 登録ページのビュー名 (View name for the registration page)
     */
    @GetMapping(value="/regist")
    public String toRegist(Model model) {
        System.out.println("/regist->");
        
        // フォームバッキングビーンとして空のCustomerModelをセット
        // (Set an empty CustomerModel as the form-backing bean)
        model.addAttribute("customerModel", new CustomerModel());
        
        return "regist";
    }
    
    /**
     * 新規顧客登録処理を実行します。
     * Executes the new customer registration process.
     * * @param customerModel フォームから送信された顧客情報 (Customer information from the form)
     * @param result バリデーション結果 (Validation result)
     * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return 成功時はデリバリーページへリダイレクト、失敗時は登録ページ (Redirects to delivery page on success, or registration page on failure)
     */
    @PostMapping(value="/regist")
    public String regist(@ModelAttribute @Validated CustomerModel customerModel, BindingResult result, Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());

        // バリデーションエラーがある場合
        // (If validation errors exist)
        if(result.hasErrors()) {
            return "regist"; // 登録ページに戻る (Return to registration page)
        }else {
            // DBに顧客情報を挿入
            // (Insert customer info into DB)
            int local = new CustomerDAO().insertCustomer(template,customerModel);
            System.out.println("/login->local:"+local);
            
            // 挿入失敗（例：login_idの重複など）
            // (Insert failed (e.g., duplicate login_id))
            if (local == 0) {
                model.addAttribute("message","login_id:"+customerModel.getLogin_id()+"は既に登録されています。");
                return "/regist";
            }
            
            // 登録成功後、そのままログイン状態にする
            // (After successful registration, automatically log them in)
            loginModel.setLogin_id(customerModel.getLogin_id());
            loginModel.setName(customerModel.getName());
            loginModel.setPassword(customerModel.getPassword());
            System.out.println("/regist:loginModel:"+loginModel);
            
            return "redirect:/delivery"; // メインページへリダイレクト (Redirect to main page)
        }
    }
    
    /**
     * 顧客情報更新ページを表示します。
     * Displays the customer information update page.
     * * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return 顧客情報更新ページのビュー名 (View name for the customer update page)
     */
    @GetMapping(value="/updateCustomer")
    public String toUpdateCustomer(Model model) {
        // 現在ログインしているユーザーの最新情報をDBから取得
        // (Get the latest info for the currently logged-in user from the DB)
        CustomerModel customerModel = new CustomerDAO().getOneRecord(template,loginModel.getLogin_id(), loginModel.getPassword());
        System.out.println("/updateCustomer->"+customerModel);
        
        // フォームバッキングビーンとして取得した情報をセット
        // (Set the retrieved info as the form-backing bean)
        model.addAttribute("customerModel", customerModel);
        model.addAttribute("loginInput", loginModel);
        
        return "updateCustomer";
    }
    
    /**
     * 顧客情報更新処理を実行します。
     * Executes the customer information update process.
     * * @param customerModel フォームから送信された更新後の顧客情報 (Updated customer information from the form)
     * @param result バリデーション結果 (Validation result)
     * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return 成功時はデリバリーページへリダイレクト、失敗時は更新ページ (Redirects to delivery page on success, or update page on failure)
     */
    @PostMapping(value="/updateCustomer")
    public String updateCustomer(@ModelAttribute @Validated CustomerModel customerModel, BindingResult result, Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());

        // バリデーションエラーがある場合
        // (If validation errors exist)
        if(result.hasErrors()) {
            return "updateCustomer"; // 更新ページに戻る (Return to update page)
        }else {
            CustomerDAO customerDAO = new CustomerDAO();
            int local = 0;
            
            // もしフォームでlogin_idがセッションのIDと異なる場合、IDの変更とみなす
            // (If the login_id in the form is different from the session ID, treat it as an ID change)
            if(!customerModel.getLogin_id().equals(loginModel.getLogin_id())) {
                local = customerDAO.updateLoginId(template, customerModel.getLogin_id(), customerModel.getCustomer_id());
                System.out.println("/login->updateLoginId->local:"+local);
            }
            
            // 顧客情報の本体を更新
            // (Update the main customer information)
            local = new CustomerDAO().updateCustomer(template,customerModel);
            System.out.println("/login->updateCustomer->local:"+local);
            
            // 更新に失敗した場合（例：変更後のIDが他者と重複）
            // (If update failed (e.g., changed ID conflicts with another user))
            if (local == 0) {
                model.addAttribute("message","login_id:"+customerModel.getLogin_id()+"は既に登録されています。");
                return "updateCustomer";
            }
            
            // セッションに保持している情報も更新する
            // (Update the information held in the session)
            loginModel.setLogin_id(customerModel.getLogin_id());
            loginModel.setName(customerModel.getName());
            loginModel.setPassword(customerModel.getPassword());
            System.out.println("/updateCustomer:loginModel:"+loginModel);
            
            return "redirect:/delivery"; // メインページへリダイレクト (Redirect to main page)
        }
    }
}