package jp.kenschool.delivery.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ユーザーのセッション間でショッピングカートの状態を管理するセッションスコープのコンポーネントです。
 * A session-scoped component that manages the state of the shopping cart for a user's session.
 *
 * @author モッタハイメ
 */
@Component
@SessionScope
@Data
public class ShoppingCart {

    /**
     * カートに入っている商品（CartItemModel）のリスト。
     * The list of items (CartItemModel) currently in the cart.
     */
    private List<CartItemModel> items = new ArrayList<>();

    /**
     * カートに商品を追加します。
     * もし商品が既にカート内に存在する場合、数量を加算します。存在しない場合は、新しいアイテムとしてリストに追加します。
     * Adds an item to the cart.
     * If the item already exists in the cart, it increases the quantity. If not, it adds it as a new item to the list.
     *
     * @param newItem 追加する商品 (The new item to add)
     */
    public void addItem(CartItemModel newItem) {
        // 商品IDでカート内に既に存在するか検索
        // (Find if the item already exists in the cart by its ID)
        Optional<CartItemModel> existingItem = findItemById(newItem.getItem_id());
        
        if (existingItem.isPresent()) {
            // 商品が既に存在する場合：既存の数量に新しい数量を加算
            // (Item already in cart: update quantity by adding the new quantity)
            CartItemModel item = existingItem.get();
            item.setQuantity(item.getQuantity() + newItem.getQuantity());
        } else {
            // 商品が新規の場合：リストに新しい商品を追加
            // (New item: add to list)
            items.add(newItem);
        }
    }

    /**
     * カート内の特定の商品の数量を更新します。
     * 数量が1未満の場合は、商品をカートから削除します。
     * Updates the quantity of a specific item in the cart.
     * If the quantity is less than 1, the item is removed.
     *
     * @param itemId   数量を変更する商品のID (The ID of the item to update)
     * @param quantity 新しい数量 (The new quantity)
     */
    public void updateItemQuantity(String itemId, int quantity) {
        if (quantity < 1) {
            // 数量が1未満（0以下）の場合は商品を削除
            // (Remove if quantity is 0 or less)
            removeItem(itemId);
            return;
        }
        
        // 該当の商品が存在すれば、その数量をセット
        // (If the item exists, set its quantity)
        Optional<CartItemModel> existingItem = findItemById(itemId);
        existingItem.ifPresent(item -> item.setQuantity(quantity));
    }

    /**
     * 指定された商品IDのアイテムをカートから削除します。
     * Removes an item from the cart based on its item ID.
     *
     * @param itemId 削除する商品のID (The ID of the item to remove)
     */
    public void removeItem(String itemId) {
        // 商品IDが一致するアイテムをリストから削除
        // (Remove the item from the list if the item ID matches)
        items.removeIf(item -> item.getItem_id().equals(itemId));
    }
    
    /**
     * カート内のすべての商品の合計金額を計算します。
     * Calculates the total price of all items in the cart.
     *
     * @return カートの合計金額 (The total price of the cart)
     */
    public int getTotalPrice() {
        return items.stream()
                // 各アイテムの小計（getSubtotal）を取得して合計する
                // (Get the subtotal for each item and sum them up)
                .mapToInt(CartItemModel::getSubtotal)
                .sum();
    }
    
    /**
     * 商品IDを使用してカート内のアイテムを検索するヘルパーメソッドです。
     * Helper method to find an item in the cart using its item ID.
     *
     * @param itemId 検索する商品ID (The item ID to find)
     * @return 見つかった場合はCartItemModelを含むOptional、見つからない場合はOptional.empty()
     * (An Optional containing the CartItemModel if found, or Optional.empty() if not)
     */
    private Optional<CartItemModel> findItemById(String itemId) {
        return items.stream()
                .filter(item -> item.getItem_id().equals(itemId))
                .findFirst();
    }
}