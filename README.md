# Amway - Java Test

### Q1: Calculator

**Desc**  
設計一個計算機（Calculator），可以執行兩個數的加、减、乘、除計算，可以進行undo和redo操作，在實現功能的基礎上進行最佳化設計。  
`備註：undo和redo 就是 復原和取消(上次)復原的操作`

**設計**  
情境:
- 像是傳統計算機那樣，每次輸入都會是一個數字或者一個指令而已.
- undo 後若進行新的操作, 則 redo 會被清空.
- 不特別實現數字大小檢查, 不嚴格實現小數計算.

### Q2: Lottery
**Desc**  
設計一個電商轉盤抽獎功能，3種獎品各有N種數量且每個獎品的中獎機率不同，與銘謝惠顧合起來機率為100%，可有同時多次抽獎的機會並包含防止重複抽獎與獎品超抽的情況。

**設計**  
情境:
 - 模擬使用者達成條件後可以獲得抽獎票，並且使用抽獎票參加轉盤抽獎，獎項可能被抽光。
 - 每個使用者最多可以抽獎 5 次。
 - 獎品: 有三種獎品，分別是 macbook, iphone, apple watch。
 - 獎品數量: macbook 5 台, iphone 10 台, apple watch 30 台。
 - 獎品抽中機率: 模擬 500 人內可以抽完獎的機率，若大於 500 人以此機率為準。
               因此預估 macbook 1%, iphone 2% , apple watch 機率是 7%。
 - 以 OO 的方式設計，使用物件模擬 DB 資料，使用 Map 作為 中獎清冊 及 中獎計算。
 - 使用 checkCountOnUpdatePrize() 模擬抽到時，將資料 update 到 DB 時，某個 where 檢查的最後防線。

### 專案初始化
這是一個 maven 專案，可使用 IDE 內建的 maven 功能執行專案，或是使用命令列執行專案。

** 檔案結構 **
```
.
├── calculator
│   ├── pom.xml
│   ├── src
│   └── target
├── lottery
│   ├── pom.xml
│   ├── src
│   └── target
└── pom.xml
```

1. 確認環境 
    - Java 17
    - Maven 3.+ 『 [下載](https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip) 』
2. 執行方式
   - **intelliJ IDEA**
     - 開啟專案後，點選右上角的 maven 按鈕，選擇對應的 maven 命令執行即可。
   - **命令列**
     - 進入專案目錄後，執行 `mvn clean install` 進行編譯。
     - 在父層執行 `mvn exec:java -pl calculator` 執行 Q1 的程式。
     - 在父層執行 `mvn exec:java -pl lottery` 執行 Q2 的程式。
     - 或在各子層執行 `mvn exec:java` 執行對應的程式。
3. 結果檢查
   - lottery 可以使用 `mvn exec:java | grep 中大獎` 進行快速檢查。