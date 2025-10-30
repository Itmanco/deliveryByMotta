package jp.kenschool.delivery.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.kenschool.delivery.dao.MenuDAO;
import jp.kenschool.delivery.model.CartItemModel;
import jp.kenschool.delivery.model.LoginModel;
import jp.kenschool.delivery.model.MenuModel;
import jp.kenschool.delivery.model.ShoppingCart;

/**
 * 注文（ショッピングカート）に関連するWebリクエストを処理するコントローラクラスです。
 * Controller class for handling web requests related to the shopping cart.
 * * @author モッタハイメ
 */
@Controller
public class OrderController {

    // データベース操作を実行するためのJdbcTemplate
    // (JdbcTemplate for executing database operations)
    @Autowired
    JdbcTemplate template;
    
    // ログイン中のユーザー情報を保持するセッションスコープのBean
    // (Session-scoped bean that holds the logged-in user's information)
    @Autowired
    LoginModel loginModel;

    // セッションスコープのショッピングカートBean
    // (Session-scoped ShoppingCart bean)
    @Autowired
    ShoppingCart shoppingCart;
    
    // メニューDAOのインスタンス
    // (Instance of MenuDAO)
    private MenuDAO menuDAO = new MenuDAO();

    /**
     * メニューページから送信された商品をショッピングカートに追加します。
     * Adds an item sent from the menu page to the shopping cart.
     *
     * @param itemId             追加する商品のID (ID of the item to add)
     * @param selectedShopId     メニューページに戻るために使用する店舗ID (Shop ID to use when returning to the menu)
     * @param selectedCategoryId メニューページに戻るために使用するカテゴリID (Category ID to use when returning to the menu)
     * @param quantity           追加する商品の数量 (Quantity of the item to add)
     * @param redirectAttributes リダイレクト先にメッセージを渡すための属性 (Attributes for passing messages to the redirect destination)
     * @param model              モデル (Model)
     * @return メニューページへのリダイレクト文字列 (Redirect string to the menu page)
     */
    @PostMapping(value = "/order/add")
    public String addToCart(
            @RequestParam("itemId") String itemId,
            @RequestParam(value = "shopId", required = false) String selectedShopId,
            @RequestParam(value = "categoryId", required = false) String selectedCategoryId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        // リクエストパラメータから余分な引用符を削除
        // (Remove extra quotes from request parameter)
        itemId = itemId.replace("\"", "");
        
        // 1. DBから商品情報を取得
        // (1. Get item info from DB)
        MenuModel menuItem = menuDAO.getMenuItemById(template, itemId);
        
        // 商品が見つからない場合
        // (If item is not found)
        if (menuItem == null) {
            redirectAttributes.addFlashAttribute("message", "商品が見つかりませんでした。");
            return "redirect:/menu";
        }

        // 2. カートアイテムオブジェクトを作成
        // (2. Create a CartItemModel object)
        CartItemModel cartItem = new CartItemModel();
        cartItem.setItem_id(itemId);
        cartItem.setItem_name(menuItem.getItem_name());
        cartItem.setPrice(menuItem.getPrice());
        cartItem.setQuantity(quantity);
        cartItem.setShop_id(menuItem.getShop_id()); // どの店の商品か追跡 (Track which shop the item belongs to)

        // 3. セッションスコープのカートに商品を追加
        // (3. Add the item to the session-scoped cart)
        shoppingCart.addItem(cartItem);
        
        // 4. リダイレクト先で表示するメッセージとパラメータを設定
        // (4. Set messages and parameters for the redirect destination)
        redirectAttributes.addFlashAttribute("message", menuItem.getItem_name() + "をカートに追加しました。");
        redirectAttributes.addAttribute("shopId", selectedShopId);
        redirectAttributes.addAttribute("categoryId", selectedCategoryId);
        
        // 5. メニューページに戻る
        // (5. Return to the menu page)
        return "redirect:/menu";
    }
    
    /**
     * 現在のショッピングカートの内容を表示するページ（注文ページ）に遷移します。
     * Navigates to the page displaying the current shopping cart contents (order page).
     *
     * @param model ビューに渡すためのモデル (Model for passing data to the view)
     * @return 注文ページのビュー名 (View name for the order page)
     */
    @GetMapping(value="/order")
    public String showOrder(Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());
        
        // ビュー（HTML）がカートの内容にアクセスできるように、カートをモデルに追加
        // (Add the shopping cart to the model so the view (HTML) can access its contents)
        model.addAttribute("shoppingCart", shoppingCart);
        
        // ヘッダー表示用にセッション情報をモデルに追加
        // (Pass session info for the header)
        if (loginModel.getLogin_id() != null) {
            model.addAttribute("loginInput", loginModel);
        }
        return "order"; // "order.html" を返す (Return "order.html")
    }
    
    /**
     * カート内の商品の数量を変更します。
     * Updates the quantity of an item in the cart.
     *
     * @param itemId             数量を変更する商品のID (ID of the item to update)
     * @param quantity           新しい数量 (The new quantity)
     * @param redirectAttributes リダイレクト先にメッセージを渡すための属性 (Attributes for passing messages)
     * @return 注文ページへのリダイレクト文字列 (Redirect string to the order page)
     */
    @PostMapping(value="/order/update")
    public String updateCartItem(
            @RequestParam("itemId") String itemId,
            @RequestParam("quantity") int quantity,
            RedirectAttributes redirectAttributes) {
        
        // カート内の商品数量を更新
        // (Update the item quantity in the cart)
        shoppingCart.updateItemQuantity(itemId, quantity);
        
        redirectAttributes.addFlashAttribute("orderMessage", "数量を変更しました。");
        return "redirect:/order";
    }

    /**
     * カートから商品を削除します。
     * Removes an item from the cart.
     *
     * @param itemId             削除する商品のID (ID of the item to remove)
     * @param redirectAttributes リダイレクト先にメッセージを渡すための属性 (Attributes for passing messages)
     * @return 
     */
    @PostMapping(value="/order/remove")
    public String removeCartItem(
            @RequestParam("itemId") String itemId,
            RedirectAttributes redirectAttributes) {
        
        // カートから商品を削除
        // (Remove the item from the cart)
        shoppingCart.removeItem(itemId);
        
        redirectAttributes.addFlashAttribute("orderMessage", "商品を削除しました。");
        return "redirect:/order";
    }
}