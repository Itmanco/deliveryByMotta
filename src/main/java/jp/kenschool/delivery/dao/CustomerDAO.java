package jp.kenschool.delivery.dao;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import jp.kenschool.delivery.model.CustomerModel;
import jp.kenschool.delivery.model.LoginInput;

/**
 * customerテーブルに関連するデータベース操作（DAO）を行うクラスです。
 * Data Access Object (DAO) class for operations related to the customer table.
 * * @author モッタハイメ
 */
public class CustomerDAO {

    /**
     * ログインIDとパスワードを使用して顧客を認証し、1件のレコードを取得します。
     * Authenticates a customer using login_id and password, retrieving one record.
     *
     * @param template   JdbcTemplate
     * @param loginInput ログインIDとパスワードを含む入力 (Input containing login_id and password)
     * @return 検索結果のMap（見つからない場合はnull） (A Map of the result, or null if not found)
     */
    public Map<String, Object> getOneRecord(JdbcTemplate template, LoginInput loginInput) {
        // PASSWORD(?) はMySQLのハッシュ関数
        // (PASSWORD(?) is the MySQL hashing function)
        String sql = "SELECT * FROM customer WHERE login_id = ? AND password = PASSWORD(?)";
        Map<String, Object> result = null;
        try {
            // 該当レコードがない場合、queryForMapはEmptyResultDataAccessExceptionをスローします
            // (If no record exists, queryForMap throws EmptyResultDataAccessException)
            result = template.queryForMap(sql, loginInput.getLogin_id(), loginInput.getPassword());
        } catch (Exception e) {
            // ログイン失敗（ID/PW不一致）またはその他のDBエラー
            // (Login failure (ID/PW mismatch) or other DB error)
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ログインIDのみを使用して、1件の顧客レコードを取得します。（パスワードリセット用）
     * Retrieves one customer record using only the login_id. (For password reset)
     *
     * @param template JdbcTemplate
     * @param login_id 検索するログインID (The login_id to search for)
     * @return 検索結果のMap（見つからない場合はnull） (A Map of the result, or null if not found)
     */
    public Map<String, Object> getOneRecord(JdbcTemplate template, String login_id) {
        String sql = "SELECT * FROM customer WHERE login_id = ?";
        Map<String, Object> result = null;
        try {
            result = template.queryForMap(sql, login_id);
        } catch (Exception e) {
            // IDが見つからない場合
            // (If ID is not found)
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 指定されたログインIDのパスワードを更新します。
     * Updates the password for a specified login_id.
     *
     * @param template    JdbcTemplate
     * @param login_id    対象のログインID (The target login_id)
     * @param newPassword 新しいパスワード（平文） (The new password in plaintext)
     * @return 更新された行数 (Number of rows updated)
     */
    public int setNewPassword(JdbcTemplate template, String login_id, String newPassword) {
        String sql = "UPDATE customer SET password=PASSWORD(?) WHERE login_id = ?";
        
        try {
            return template.update(sql, newPassword, login_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // 失敗 (Failure)
    }

    /**
     * 新しい顧客情報をデータベースに挿入します。
     * Inserts a new customer record into the database.
     *
     * @param template      JdbcTemplate
     * @param customerModel 登録する顧客情報 (Customer information to register)
     * @return 挿入された行数（重複IDなどで失敗した場合は0） (Number of rows inserted, or 0 on failure e.g., duplicate ID)
     */
    public int insertCustomer(JdbcTemplate template, CustomerModel customerModel) {
        // (customer_id, login_id, name, password, phone, zip, address1, address2, question, answer, point)
        // pointはデフォルトで0を挿入
        // (Insert 0 as default for point)
        String sql = "INSERT INTO customer VALUES(DEFAULT, ?, ?, PASSWORD(?), ?, ?, ?, ?, ?, ?, 0)";
        
        int result = 0;
        try {
            result = template.update(sql,
                    customerModel.getLogin_id(),
                    customerModel.getName(),
                    customerModel.getPassword(),
                    customerModel.getPhone(),
                    customerModel.getZip(),
                    customerModel.getAddress1(),
                    customerModel.getAddress2(),
                    customerModel.getQuestion(),
                    customerModel.getAnswer()
            );
            
        } catch (Exception e) {
            // login_idのUNIQUE制約違反などで失敗する可能性
            // (Possible failure due to login_id UNIQUE constraint violation, etc.)
            e.printStackTrace();
        }

        return result;
    }
    
    /**
     * 顧客のログインIDを変更します。
     * Updates a customer's login_id.
     *
     * @param template   JdbcTemplate
     * @param newLoginId 新しいログインID (The new login_id)
     * @param customerId 対象の顧客ID (The target customer_id)
     * @return 更新された行数 (Number of rows updated)
     */
    public int updateLoginId(JdbcTemplate template, String newLoginId, Integer customerId) {
        String sql = "UPDATE customer SET login_id = ? WHERE customer_id = ?";
        
        int result = 0;
        try {
            result = template.update(sql, newLoginId, customerId);
        } catch (Exception e) {
            // newLoginIdのUNIQUE制約違反の可能性
            // (Possible violation of newLoginId UNIQUE constraint)
            e.printStackTrace();
            return 0;
        }
        return result;
    }
    
    /**
     * 顧客IDに基づき、顧客のプロフィール情報（ログインIDを除く）を更新します。
     * Updates a customer's profile information (excluding login_id) based on their customer_id.
     *
     * @param template      JdbcTemplate
     * @param customerModel 更新データを含む顧客モデル (Customer model containing update data)
     * @return 更新された行数 (Number of rows updated)
     */
    public int updateCustomer(JdbcTemplate template, CustomerModel customerModel) {
        String sql = "UPDATE customer SET "
                + "name = ?, "
                + "password = PASSWORD(?), "
                + "phone = ?, "
                + "zip = ?, "
                + "address1 = ?, "
                + "address2 = ?, "
                + "question = ?, "
                + "answer = ? "
                + "WHERE customer_id = ?";
        
        int result = 0;
        try {
            result = template.update(sql,
                    customerModel.getName(),
                    customerModel.getPassword(),
                    customerModel.getPhone(),
                    customerModel.getZip(),
                    customerModel.getAddress1(),
                    customerModel.getAddress2(),
                    customerModel.getQuestion(),
                    customerModel.getAnswer(),
                    customerModel.getCustomer_id()
            );
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * ログインIDを使用して顧客の全情報を取得し、CustomerModelとして返します。
     * （注意：パスワードはDBから取得せず、引数の値をそのままセットします）
     * Retrieves all customer information using login_id and returns it as a CustomerModel.
     * (Note: The password is not fetched from the DB; the parameter value is set directly)
     *
     * @param template JdbcTemplate
     * @param login_id 検索するログインID (The login_id to search for)
     * @param password モデルにセットするパスワード（通常はセッションからの値） (The password to set in the model (usually from the session))
     * @return 顧客情報モデル (CustomerModel)
     */
    public CustomerModel getOneRecord(JdbcTemplate template, String login_id, String password) {
        // このメソッドは認証を行わない (This method does not perform authentication)
        String sql = "SELECT * FROM customer WHERE login_id = ?";
        CustomerModel customerModel = null;
        Map<String, Object> result = null;
        try {
            result = template.queryForMap(sql, login_id);
            customerModel = new CustomerModel();
            
            // MapからModelへの手動マッピング (Manual mapping from Map to Model)
            customerModel.setCustomer_id((Integer) result.get("customer_id"));
            customerModel.setLogin_id((String) result.get("login_id"));
            customerModel.setName((String) result.get("name"));
            
            // DBからハッシュ化されたパスワードを取得する代わりに、引数で渡されたパスワード（セッション内の値）をセット
            // (Instead of getting the hashed password from DB, set the password passed as an argument (value from session))
            customerModel.setPassword(password);
            
            customerModel.setPhone((String) result.get("phone"));
            customerModel.setZip((String) result.get("zip"));
            customerModel.setAddress1((String) result.get("address1"));
            customerModel.setAddress2((String) result.get("address2"));
            customerModel.setQuestion((String) result.get("question"));
            customerModel.setAnswer((String) result.get("answer"));
            customerModel.setPoint((Integer) result.get("point"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerModel;
    }
}