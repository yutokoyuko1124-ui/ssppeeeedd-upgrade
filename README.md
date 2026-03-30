# ssppeeeedd-upgrade

Mekanism 1.20.1 (Forge) 向けの **過剰加速系アドオン** です。  
通常のバランス調整ではなく、機械処理速度を桁違いに引き上げることを目的にしています。

## コンセプト

> 電力ちょい増しで速度を盛りまくる。

- 精錬 / 粉砕 / 圧縮 / 各種加工を超高速化
- 省エネではなく、工場全体のスループット最大化を狙う設計
- 本家Mekanismの運用感をなるべく維持しつつ、性能だけを大幅に拡張

## 対応ターゲット

- Minecraft **1.20.1**
- **Forge**
- **Mekanism addon**

## 追加要素 (v1)

### SSPPEEEEDD Upgrade Card

Mekanism機械に追加できる独自アップグレードカード。

- 1枚ごとに **速度 +10%**
- 1枚ごとに **消費電力 +2%**
- 最大 **64枚**
- アップグレードスロットを持つMekanism機械が対象
- **Energy Upgrade と併用可**

## 実装方針

Mekanismに完全な新Upgrade種別を追加するのではなく、以下の現実路線を採用します。

1. 独自カードアイテム (`SSPPEEEEDD Upgrade Card`) を追加
2. 内部処理上は `Speed Upgrade` 系として機能させる
3. `Speed Upgrade` の実質上限を **8 → 64** 相当へ拡張

これにより、互換性・実装負荷・運用体験のバランスを取りつつ、明確な性能向上を実現します。

## v1 スコープ

- SSPPEEEEDD Upgrade Card
- 言語ファイル
- レシピ
- Forge + Mekanism addon 土台
- 機械への適用処理

## v2 候補

- UUUULLLLTTTTRRRAAAA Speed Card
- Energy側の64枚化
- GUI表示強化
- Configによる倍率調整
- 機械ごとの倍率制御

## 1文要約

**Mekanism 1.20.1 Forge 用の、Speed Upgrade を64枚級まで拡張して工場を異常加速させる addon。**

## どこから見える？（確認手順）

このmodがゲーム内で見える場所は次の通りです。

1. **インベントリ検索（JEI/クリエイティブ検索）**  
   `ssppeeeedd` または `upgrade card` で検索。
2. **クラフトレシピ**  
   `Redstone + Sugar + mekanism:upgrade_speed` から `SSPPEEEEDD Upgrade Card` を作成。
3. **Mekanism機械への右クリック使用**  
   機械ブロックにカードを使うと、適用に成功すればカードが1枚消費されます。
4. **適用失敗時のメッセージ**  
   対象外機械や適用不可のときは失敗メッセージが表示されます。

> 注意: 現在の実装はMekanism内部メソッドを反射で呼ぶ互換層です。  
> 環境やMekanismバージョン差異によっては、追加調整が必要になる場合があります。
