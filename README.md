# ゲームタイマー

## 概要

ゲームのプレイ時間を管理するためのandroidアプリ。

* 一分を1ポイントとして扱う
* ゲームをプレイした時間 (分) だけポイントが減少する
* 一定期間ごとポイントが加算される。
* 使われなかったポイントは次の期間に繰り越せ、超過した分は借金になる。 

## 仕様
### データクラス
#### 現在
SharedPreferencesとそれを扱うクラスを作っている。
* point
* 
#### SQLiteに移行したとき用の設計

##### マスタテーブル

| period_length                       | point_per_period                 |      |
| ----------------------------------- | -------------------------------- | ---- |
| 時間の基本単位。デフォルトでは1日。 | 一定期間ごともらえるポイントの量 |      |



##### メインテーブル

| current_point            |
| ------------------------ |
| 現在保有しているポイント |

### 検討中の仕様
#### ポイントの追加の仕方

1. 一日経過した時点で追加する方法
2. ミリ秒ごとに追加する方法

1はjava.util.Calendarを使おうとするとつらい。SQLiteのユリユス日にやらせればできそう。2は簡単に実装できるのでこれでとりあえず書いてみる。