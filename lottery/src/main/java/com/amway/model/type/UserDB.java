package com.amway.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class UserDB {

    // .. 用戶資訊
    private String id;
    private String name;

    public UserDB(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // 模擬抽獎表
    Lottery lottery;

    // 模擬抽獎票表
    List<LotteryTicket> lotteryTickets;

    @Data
    public static class Lottery {
        private String lotteryId;
        private String userId;
        private int attendedCount;
        // ... some other data
    }


    @Data
    public static class LotteryTicket {
        private String ticketId;
        private String lotteryId;
        // 預設 1, 預留當人數過多時，可以依比例降低權重, 避免太早被抽光
        // 舉例：原本有三個獎項分別是以 500% 的機率去預估，但活動到一半卻還有 800 多人參加，此時獎項機率應該要依比例降低。
        // 這邊可以依照剩餘獎項數量去計算權重，這邊為了簡化，直接設定為 1。
        // 假設變成 800 則權重應該變成 0.625
        // 留在這也是為了當有更強大的演算法時，每次抽獎時可以即時計算權重。
        private double weight;
        private String prizeId;
    }
}
