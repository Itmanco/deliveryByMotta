SET FOREIGN_KEY_CHECKS = 0; 
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS shops;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS menu_subcategory;
DROP TABLE IF EXISTS menu_category;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS customer (
	`customer_id` INT(8) NOT NULL AUTO_INCREMENT,
	`login_id` VARCHAR(100) NOT NULL COLLATE 'utf8mb3_general_ci',
	`name` VARCHAR(30) NOT NULL COLLATE 'utf8mb3_general_ci',
	`password` VARCHAR(255) NOT NULL COLLATE 'utf8mb3_general_ci',
	`phone` VARCHAR(11) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
	`zip` VARCHAR(7) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
	`address1` VARCHAR(100) NOT NULL COLLATE 'utf8mb3_general_ci',
	`address2` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
	`question` VARCHAR(100) NOT NULL COLLATE 'utf8mb3_general_ci',
	`answer` VARCHAR(100) NOT NULL COLLATE 'utf8mb3_general_ci',
	`point` INT(8) NULL DEFAULT '0',
	PRIMARY KEY (`customer_id`) USING BTREE,
	UNIQUE INDEX `login_id` (`login_id`) USING BTREE
)
COLLATE='utf8mb3_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=4
;


INSERT INTO `customer` (`customer_id`, `login_id`, `name`, `password`, `phone`, `zip`, `address1`, `address2`, `question`, `answer`, `point`) VALUES (1, 'motta@motta.com', 'モッタハイメ', '*56D05B9BB8F389B14EC4F61D534F44429087E07D', '09012345678', '1000001', '東京都千代田区', '丸の内1-1-1', '好きな食べ物', 'ピザ', 100);
INSERT INTO `customer` (`customer_id`, `login_id`, `name`, `password`, `phone`, `zip`, `address1`, `address2`, `question`, `answer`, `point`) VALUES (2, 'user3@example.com', '田中 花子', '*4994A78AFED55B0F529C11C436F85458C1F8D4C2', '08098765432', '5500002', '大阪府大阪市', '北区梅田3-1-3', 'ペットの名前', 'ポチ', 50);
INSERT INTO `customer` (`customer_id`, `login_id`, `name`, `password`, `phone`, `zip`, `address1`, `address2`, `question`, `answer`, `point`) VALUES (3, 'newuser@test.com', '山田 次郎', '*DF216F57F1F2066124E1AA5491D995C3CB57E4C2', '0311223344', '6040000', '京都府京都市', '中京区河原町', '生まれた年', '1995', 0);
INSERT INTO `customer` (`customer_id`, `login_id`, `name`, `password`, `phone`, `zip`, `address1`, `address2`, `question`, `answer`, `point`) VALUES (6, 'motta2@motta.com', 'Alberto Motta', '*56D05B9BB8F389B14EC4F61D534F44429087E07D', '0123456638', '0050004', '豊平区水車町2丁目3－2', '', 'name of grandmother', 'leonord', 0);
INSERT INTO `customer` (`customer_id`, `login_id`, `name`, `password`, `phone`, `zip`, `address1`, `address2`, `question`, `answer`, `point`) VALUES (7, 'motta3@motta.com', 'Alberto Motta', '*56D05B9BB8F389B14EC4F61D534F44429087E07D', '0123456789', '0050004', '豊平区水車町2丁目3－2', '', 'name of grandmother', 'leonord', 0);
INSERT INTO `customer` (`customer_id`, `login_id`, `name`, `password`, `phone`, `zip`, `address1`, `address2`, `question`, `answer`, `point`) VALUES (8, 'jmottadev2@gmail.com', 'Alberto Motta', '*56D05B9BB8F389B14EC4F61D534F44429087E07D', '07083766399', '0050004', '豊平区水車町2丁目3－2', '', 'name of grandmother', 'leonord', 0);

CREATE TABLE IF NOT EXISTS shops (
    -- Primary Key: shop_id (assuming it's a unique identifier like a store code)
    shop_id VARCHAR(10) NOT NULL,

    shop_name VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NULL,
    zip VARCHAR(8) NULL,          -- Postal code field (can include hyphen)
    address VARCHAR(150) NOT NULL, -- Full street address
    explanation VARCHAR(255) NULL,
    map_url VARCHAR(255) NULL,     -- URL for Google Maps or similar

    PRIMARY KEY (shop_id)
);

INSERT INTO `shops` (`shop_id`, `shop_name`, `phone`, `zip`, `address`, `explanation`, `map_url`) VALUES ('S001', 'SAVOY EZO', '0112221111', '0600003', '札幌市中央区北３条西２丁目1−２７ アストリア札幌 地下1階', '札幌駅西口より徒歩1分。', '札幌市中央区北３条西２丁目1−２７ アストリア札幌 地下1階');
INSERT INTO `shops` (`shop_id`, `shop_name`, `phone`, `zip`, `address`, `explanation`, `map_url`) VALUES ('S002', 'PIZZA JOINT PIKE', '0115123456', '0640805', '札幌市中央区南4条西3丁目', '終電まで営業中。深夜の注文に。', '札幌市中央区南５条西９丁目１００８−１０ １階');
INSERT INTO `shops` (`shop_id`, `shop_name`, `phone`, `zip`, `address`, `explanation`, `map_url`) VALUES ('S003', 'PIZZA ＆ Cheese RITORNO', '0112318765', '0600063', '札幌市中央区南３条西３ プレイタウンふじ井ビル 1F', '大通公園に近く、ランチに最適。', '札幌市中央区南３条西３ プレイタウンふじ井ビル 1F');


-- 1. Create the Main Category Table (e.g., Pizza, Pasta, Drinks)
CREATE TABLE IF NOT EXISTS menu_category (
    category_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (category_id),
    UNIQUE KEY (name) 
);

-- 2. Create the Subcategory Table (e.g., Vegetarian, Traditional, Alcoholic)
CREATE TABLE IF NOT EXISTS menu_subcategory (
    subcategory_id INT NOT NULL AUTO_INCREMENT,
    category_id INT NOT NULL, -- Links to the parent category
    name VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (subcategory_id),
    
    -- Foreign Key Constraint: Links subcategory back to its main category
    FOREIGN KEY (category_id) REFERENCES menu_category(category_id),
    
    -- Ensures subcategory names are unique within a parent category
    UNIQUE KEY (category_id, name)
);
-- 1. INSERT MAIN CATEGORIES
INSERT INTO menu_category (category_id, name) VALUES
(1, 'ピザ'),            -- Pizza
(2, 'パスタ'),           -- Pasta
(3, 'ドリンク'),         -- Drinks
(4, 'サイドメニュー'),     -- Side Menus
(5, 'スープ');           -- Soups

-- 2. INSERT SUBCATEGORIES
-- Category ID 1: ピザ (Pizza)
INSERT INTO menu_subcategory (subcategory_id, category_id, name) VALUES
(1, 1, 'トラディショナル'),  -- Traditional
(2, 1, 'スペシャル'),        -- Specials
(3, 1, 'ベジタリアン'),     -- Vegetarian

-- Category ID 2: パスタ (Pasta)
(4, 2, 'トマトベース'),      -- Tomato Base
(5, 2, 'クリームベース'),     -- Cream Base

-- Category ID 3: ドリンク (Drinks)
(6, 3, 'ソフトドリンク'),     -- Soft Drinks
(7, 3, 'アルコール'),       -- Alcoholic

-- Category ID 4: サイドメニュー (Side Menu)
(8, 4, '定番'),            -- Standard

-- Category ID 5: スープ (Soups)
(9, 5, 'その他');           -- Other

-- 2. Create the new menu table with category and subcategory Foreign Keys
CREATE TABLE IF NOT EXISTS menu (
    item_id VARCHAR(10) NOT NULL,
    shop_id VARCHAR(10) NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    price INT(8) NOT NULL,
    description VARCHAR(255) NULL,
    image_url VARCHAR(100) NULL,
    
    -- NEW FOREIGN KEY COLUMNS
    category_id INT NULL,
    subcategory_id INT NULL,
    
    PRIMARY KEY (item_id),
    
    -- Foreign Key to the Shop table
    FOREIGN KEY (shop_id) REFERENCES shops(shop_id),
    
    -- Foreign Key to the Category table (must be created first!)
    FOREIGN KEY (category_id) REFERENCES menu_category(category_id),
    
    -- Foreign Key to the Subcategory table (must be created first!)
    FOREIGN KEY (subcategory_id) REFERENCES menu_subcategory(subcategory_id)
);

--  INSERT MENU ITEMS (Using the new Foreign Keys)
INSERT INTO menu (item_id, shop_id, item_name, price, description, image_url, category_id, subcategory_id) VALUES
-- PIZZA CATEGORY (Category ID 1)
('PZ001', 'S001', 'マルゲリータピザ', 1800, '特製トマトソースとフレッシュバジル', '/images/pizza_margherita.jpg', 1, 1), -- Traditional (ID 1)
('PZ002', 'S001', 'ジャーマンポテトピザ', 2200, '新じゃがとベーコンの風味豊かなピザ', '/images/german_potato_pizza.jpg', 1, 2), -- Special (ID 2)
('PZ003', 'S002', 'ベジタリアンピザ', 1900, '新鮮野菜と自家製ソースのヘルシーピザ', '/images/pizza_vegetarian.jpg', 1, 3), -- Vegetarian (ID 3)
('PZ004', 'S003', '4種のチーズピザ', 2500, '厳選された4種類のチーズのハーモニー', '/images/four_cheese_pizza.jpg', 1, 2), -- Special (ID 2)

-- PASTA CATEGORY (Category ID 2)
('PS001', 'S001', 'ボロネーゼパスタ', 1500, '自家製ミートソースの定番パスタ', '/images/pasta_bolognese.jpg', 2, 4), -- Tomato Base (ID 4)
('PS002', 'S002', 'カルボナーラ', 1650, '濃厚チーズソースと黒胡椒', '/images/pasta_carbonara.jpg', 2, 5), -- Cream Base (ID 5)

-- DRINKS CATEGORY (Category ID 3)
('DR001', 'S001', 'コーラ', 250, '定番のソフトドリンク', '/images/drink_coke.jpg', 3, 6), -- Soft Drink (ID 6)
('DR002', 'S003', 'ビール (一番搾り)', 550, 'キリン一番搾り', '/images/drink_beer.jpg', 3, 7), -- Alcoholic (ID 7)

-- SIDES/SOUPS CATEGORY (Category IDs 4, 5)
('SD001', 'S002', 'ポテトフライ', 450, 'サイドメニューの定番！', '/images/side_fries.jpg', 4, 8), -- Sides/Standard (ID 8)
('SP001', 'S003', 'ミネストローネ', 350, '野菜たっぷりヘルシーなスープ', '/images/soup.jpg', 5, 9); -- Soups/Other (ID 9);

INSERT INTO menu 
    (item_id, shop_id, item_name, price, description, image_url, category_id, subcategory_id) 
VALUES
-- === PIZZA CATEGORY (ID 1) - 10 Items Total ===
('PZ005', 'S001', 'クアトロフォルマッジ', 2400, '4種の濃厚チーズとハチミツの絶妙なバランス', '/images/pizza_quattro.jpg', 1, 2), -- スペシャル
('PZ006', 'S002', 'ハワイアンピザ', 2100, 'パイナップルとハムの定番、甘じょっぱい味わい', '/images/pizza_hawaii.jpg', 1, 1), -- トラディショナル
('PZ007', 'S003', 'ヴィーガンピザ', 2300, '動物性食材不使用。ソイチーズと新鮮野菜', '/images/pizza_vegan.jpg', 1, 3), -- ベジタリアン
('PZ008', 'S001', 'ミートラバーズピザ', 2600, '牛肉、ソーセージ、ベーコンの肉尽くし', '/images/pizza_meat.jpg', 1, 2), -- スペシャル
('PZ009', 'S002', 'ディープディッシュ', 2800, 'シカゴ風の分厚い生地とたっぷりの具材', '/images/pizza_deepdish.jpg', 1, 1), -- トラディショナル
('PZ010', 'S003', 'シーフードピザ', 2700, '新鮮なエビ、イカ、アサリをトッピング', '/images/pizza_seafood.jpg', 1, 2), -- スペシャル
('PZ011', 'S001', 'きのこトリュフピザ', 2900, 'キノコとトリュフオイルの芳醇な香り', '/images/pizza_truffle.jpg', 1, 3), -- ベジタリアン
('PZ012', 'S002', 'スパイシーペパロニ', 2150, '辛口サラミがアクセントの大人向けピザ', '/images/pizza_spicy.jpg', 1, 1), -- トラディショナル
('PZ013', 'S003', 'BBQチキンピザ', 2450, '香ばしいBBQソースと柔らかいチキンを使用', '/images/pizza_bbq_chicken.jpg', 1, 2), -- スペシャル (NEW)
('PZ014', 'S001', 'ズッキーニとハーブ', 2250, '爽やかなズッキーニとハーブを散らしたピザ', '/images/pizza_herb.jpg', 1, 3), -- ベジタリアン (NEW)

-- === PASTA CATEGORY (ID 2) - 8 Items Total ===
('PS003', 'S003', 'ペペロンチーノ', 1400, 'ニンニクと唐辛子のシンプルなオイルパスタ', '/images/pasta_peperoncino.jpg', 2, 4), -- トマトベース
('PS004', 'S001', 'エビのクリームパスタ', 1750, '濃厚ソースにエビの旨味が凝縮', '/images/pasta_shrimp_cream.jpg', 2, 5), -- クリームベース
('PS005', 'S002', 'ジェノベーゼパスタ', 1600, 'フレッシュバジルと松の実の香り高いソース', '/images/pasta_pesto.jpg', 2, 4), -- トマトベース
('PS006', 'S003', 'ラザニア', 1900, 'ミートソースとベシャメルソースの重ね焼き', '/images/pasta_lasagna.jpg', 2, 5), -- クリームベース
('PS007', 'S001', 'ナスとモッツァレラ', 1550, 'モッツァレラと揚げナスのトマトソース', '/images/pasta_eggplant.jpg', 2, 4), -- トマトベース
('PS008', 'S002', 'きのこのバター醤油', 1450, '和風ベースの隠れた人気メニュー', '/images/pasta_soysauce.jpg', 2, 5), -- クリームベース
('PS009', 'S003', '明太子クリームパスタ', 1700, '博多明太子を使用したクリーミーな和風パスタ', '/images/pasta_mentaiko.jpg', 2, 5), -- クリームベース (NEW)
('PS010', 'S002', 'アマトリチャーナ', 1650, 'パンチェッタの塩気が効いた定番トマトソース', '/images/pasta_amatriciana.jpg', 2, 4), -- トマトベース (NEW)

-- === DRINKS CATEGORY (ID 3) - 7 Items Total ===
('DR003', 'S003', 'ジンジャーエール', 250, '爽快な辛さが喉に心地よい', '/images/drink_ginger.jpg', 3, 6), -- ソフトドリンク
('DR004', 'S001', 'ウーロン茶', 200, 'お口直しにさっぱりと', '/images/drink_oolong.jpg', 3, 6), -- ソフトドリンク
('DR005', 'S002', 'イタリアンソーダ', 350, '日替わりフレーバーのイタリアンソーダ', '/images/drink_soda.jpg', 3, 6), -- ソフトドリンク
('DR006', 'S003', 'ハイボール', 480, '食事に合う爽快な飲み口', '/images/drink_highball.jpg', 3, 7), -- アルコール
('DR007', 'S001', 'ワイン (赤)', 600, 'グラスワイン赤', '/images/drink_wine_red.jpg', 3, 7), -- アルコール
('DR008', 'S002', 'オレンジジュース', 280, 'フレッシュなオレンジを絞ったジュース', '/images/drink_oj.jpg', 3, 6), -- ソフトドリンク (NEW)
('DR009', 'S003', 'クラフトビール', 750, '地元のクラフトビール', '/images/drink_craft_beer.jpg', 3, 7), -- アルコール (NEW)

-- === SIDE MENU CATEGORY (ID 4) - 9 Items Total ===
('SD002', 'S003', 'フライドチキン', 650, '特製スパイスで揚げたジューシーチキン', '/images/side_chicken.jpg', 4, 8), -- 定番
('SD003', 'S001', 'ガーリックブレッド', 300, 'バターとガーリックの風味豊かなパン', '/images/side_garlic.jpg', 4, 8), -- 定番
('SD004', 'S002', 'シーザーサラダ', 550, '新鮮レタスとシーザードレッシング', '/images/side_caesar.jpg', 4, 8), -- 定番
('SD005', 'S003', 'オニオンリング', 480, 'サクサクのオニオンリング', '/images/side_onion.jpg', 4, 8), -- 定番
('SD006', 'S001', 'ミニカルツォーネ', 700, 'チーズとミートのミニ包みピザ', '/images/side_calzone.jpg', 4, 8), -- 定番
('SD007', 'S002', 'チーズポテト', 520, 'とろけるチーズがたっぷり', '/images/side_cheesefries.jpg', 4, 8), -- 定番
('SD008', 'S003', 'グリーンサラダ', 400, 'シンプルでヘルシーなサラダ', '/images/side_greensalad.jpg', 4, 8), -- 定番
('SD009', 'S001', 'チキンナゲット', 450, 'お子様にも人気の一口ナゲット', '/images/side_nuggets.jpg', 4, 8), -- 定番 (NEW)
('SD010', 'S002', 'フォカッチャ', 350, 'ハーブとオリーブオイルのシンプルパン', '/images/side_focaccia.jpg', 4, 8), -- 定番 (NEW)

-- === SOUP CATEGORY (ID 5) - 6 Items Total ===
('SP002', 'S001', 'オニオンスープ', 450, '玉ねぎをじっくり炒めた深い味わい', '/images/soup_onion.jpg', 5, 9), -- その他
('SP003', 'S002', 'コーンポタージュ', 380, '濃厚な甘みの定番スープ', '/images/soup_corn.jpg', 5, 9), -- その他
('SP004', 'S003', 'クラムチャウダー', 500, '魚介の旨味たっぷりのクリーミーなスープ', '/images/soup_clam.jpg', 5, 9), -- その他
('SP005', 'S001', '本日のスープ', 400, '日替わりの特製スープ', '/images/soup_daily.jpg', 5, 9), -- その他
('SP006', 'S003', 'トマトクリームスープ', 420, 'トマトと生クリームの濃厚なハーモニー', '/images/soup_tomato.jpg', 5, 9), -- その他 (NEW)
('SP007', 'S001', '野菜のポタージュ', 380, '数種類の野菜を煮込んだ優しい味', '/images/soup_veg_potage.jpg', 5, 9); -- その他 (NEW)

