package jp.kenschool.delivery.utils;

/**
 * ビュー（HTMLテンプレートなど）で使用するためのユーティリティメソッドを提供するクラスです。
 * This class provides utility methods for use in views (e.g., HTML templates).
 *
 * @author モッタハイメ
 */
public class TemplateUtilities {

    /**
     * 日本の電話番号（10桁または11桁）を、スペースで区切られた読みやすい形式に整形します。
     * (例: 09012345678 -> "090 1234 5678")
     * Formats a 10 or 11-digit Japanese phone number into a space-separated, readable format.
     * (e.g., 09012345678 -> "090 1234 5678")
     *
     * @param phone ハイフン（-）が含まれている可能性のある、未整形の電話番号文字列
     * (The raw phone number string, possibly containing hyphens)
     * @return 整形済みの文字列 (例: "090 1234 5678" または "011 234 5678")
     * (The formatted string (e.g., "090 1234 5678" or "011 234 5678"))
     */
    public String formatJapanesePhone(String phone) {
        // 入力がnullまたは空の場合は、空文字列を返す
        // (If the input is null or empty, return an empty string)
        if (phone == null || phone.isEmpty()) {
            return "";
        }

        // ハイフンやスペースなど、数字以外のすべての文字を削除
        // (Remove all non-digit characters, such as hyphens or spaces)
        String digits = phone.replaceAll("[^0-9]", "");

        // 携帯電話番号（11桁）の場合
        // (For mobile numbers (11 digits))
        if (digits.length() == 11) {
            // 3桁 / 4桁 / 4桁 (090 XXXX XXXX) の形式に分割
            // (Split into 3 / 4 / 4 format (090 XXXX XXXX))
            return digits.substring(0, 3) + " " + digits.substring(3, 7) + " " + digits.substring(7);
        }
        // 市外局番（例：札幌 011）を含む固定電話（10桁）の場合
        // (For fixed-line numbers (10 digits) including area code (e.g., Sapporo 011))
        else if (digits.length() == 10) {
            // 3桁 / 3桁 / 4桁 (011 XXX XXXX) の形式に分割
            // (Split into 3 / 3 / 4 format (011 XXX XXXX))
            // 注意：これは市外局番が3桁の地域（東京03、大阪06など）では正しく機能しない可能性があります
            // (Note: This might not work correctly for 2-digit area codes (Tokyo 03, Osaka 06))
            return digits.substring(0, 3) + " " + digits.substring(3, 6) + " " + digits.substring(6);
        }
        // 10桁でも11桁でもない、予期しない長さの場合
        // (For unexpected lengths that are not 10 or 11 digits)
        else {
            // 元の入力（整形しようとしたが失敗した）をそのまま返す
            // (Return the original input as a fallback)
            return phone;
        }
    }
}