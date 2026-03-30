# GitHubに表示されないとき

この環境でのコミットは**ローカルブランチ**に作成されています。  
GitHubに表示するには、あなたの環境でリモートへ push が必要です。

## 現在の想定状態

- ローカル: `work` ブランチに最新コミットあり
- GitHub: `main` が初期コミットのまま

## あなたのリポジトリURLでの反映手順

対象リポジトリ:

- `https://github.com/yutokoyuko1124-ui/ssppeeeedd-upgrade`

```bash
git remote add origin https://github.com/yutokoyuko1124-ui/ssppeeeedd-upgrade.git
# すでにoriginがある場合
# git remote set-url origin https://github.com/yutokoyuko1124-ui/ssppeeeedd-upgrade.git

git push -u origin work
```

## main に反映する方法

### A. Pull Request で取り込む（おすすめ）

1. `work` を push
2. GitHub 上で `work -> main` の PR を作成
3. Merge すると `main` に表示されます

### B. 直接 main に反映

```bash
git checkout main
git merge work
git push origin main
```

## 注意

この実行環境ではネットワーク制限により `git push` が失敗することがあります。  
その場合は、手元PC（GitHubへ通常接続できる環境）で上記コマンドを実行してください。

## iPad の場合

GitHub公式アプリだけでは `git push` はできません。  
iPadでは次のどちらかを使ってください。

### 1) Working Copy（おすすめ）

1. App Storeで **Working Copy** を入れる
2. リポジトリを clone（`yutokoyuko1124-ui/ssppeeeedd-upgrade`）
3. `work` ブランチを checkout
4. Commit / Push を実行
5. GitHubアプリまたはブラウザで `work -> main` のPRを作成

### 2) a-Shell / ish + git

ターミナル系アプリで `git push -u origin work` を実行。
（SSHキー or PAT の設定が必要）
