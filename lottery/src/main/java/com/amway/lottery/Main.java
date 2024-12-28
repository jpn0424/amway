package com.amway.lottery;

import com.amway.model.type.PrizeDB;
import com.amway.model.type.UserDB;
import com.amway.utils.Snowflake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

        // simulation - initialize the data
        // PrizeDB 代表存在 Prize 表的資料
        PrizeDB appleWatch = new PrizeDB("1", "apple watch", 30, 30, 0.07);
        PrizeDB iphone = new PrizeDB("2", "iphone", 10, 10, 0.02);
        PrizeDB macbook = new PrizeDB("3", "macbook", 5, 5, 0.01);
        PrizeDB thankYou = new PrizeDB("4", "thank you", 0, 0, 0);

        // simulation - add 500 users
        // UserDB 代表存在 User 表的資料
        List<UserDB> users = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            // 隨機 1 ~ 5 次抽獎, attendCount 數字 1 ~ 5 之間
            int attendCount = ThreadLocalRandom.current().nextInt(1, 6);
            String userId = String.valueOf(Snowflake.INSTANCE.nextId());
            users.add(new UserDB(userId, "user" + i));
            UserDB.Lottery lottery = new UserDB.Lottery();
            String lotteryId = String.valueOf(Snowflake.INSTANCE.nextId());
            lottery.setLotteryId(lotteryId);
            lottery.setUserId(userId);
            lottery.setAttendedCount(attendCount);

            // 模擬抽獎票
            List<UserDB.LotteryTicket> lotteryTickets = new ArrayList<>();
            for (int j = 0; j < attendCount; j++) {
                UserDB.LotteryTicket lotteryTicket = new UserDB.LotteryTicket();
                lotteryTicket.setTicketId(String.valueOf(Snowflake.INSTANCE.nextId()));
                lotteryTicket.setLotteryId(lotteryId);
                lotteryTicket.setWeight(1);
                lotteryTickets.add(lotteryTicket);
            }
            users.get(i).setLottery(lottery);
            users.get(i).setLotteryTickets(lotteryTickets);
        }

        // simulation - 中獎清冊
        // Map<userId, Map<prizeId, List<ticketId>>>
        Map<String, Map<String, List<String>>> winners = new HashMap<>();

        // simulation - 中獎池
        // Map<prizeId, count> count 模擬 db 被執行 select count(*) 時的數量
        Map<String, Integer> prizePool = new HashMap<>();
        prizePool.put(appleWatch.getId(), 0);
        prizePool.put(iphone.getId(), 0);
        prizePool.put(macbook.getId(), 0);

        // simulation - count user from db;
        int attenders = users.size();

        System.out.println("Lottery starts!");
        System.out.println("Total users: " + attenders);
        System.out.println("Prizes: ");
        System.out.println("  First. macbook: " + macbook.getTotal() + " units");
        System.out.println("  Second. iphone: " + iphone.getTotal() + " units");
        System.out.println("  Third. apple watch: " + appleWatch.getTotal() + " units");

        // While 迴圈模擬每人抽獎
        for (UserDB user : users) {
            UserDB.Lottery lottery = user.getLottery();
            System.out.println("抽獎人: " + user.getName() + "抽獎剩餘次數: " + lottery.getAttendedCount());
            System.out.println("開始抽獎");

            List<UserDB.LotteryTicket> lotteryTickets = user.getLotteryTickets();

            // 先模擬連續按下抽獎按鈕, 不考慮其他因素, 正常是檢查次數
            for (UserDB.LotteryTicket lotteryTicket : lotteryTickets) {

                // 模擬抽獎, 簡單的演算法
                double random = ThreadLocalRandom.current().nextDouble();
                random = random * lotteryTicket.getWeight();

                if (random <= macbook.getWeight()) {
                    if (checkCountOnUpdatePrize(prizePool, macbook.getId()) &&
                            !winners.getOrDefault(user.getId(), new HashMap<>())
                                    .containsKey(macbook.getId())) {
                        System.out.println("抽獎人: " + user.getName() + " 中大獎: " + macbook.getName());
                        winners.putIfAbsent(user.getId(), new HashMap<>());
                        winners.get(user.getId()).putIfAbsent(macbook.getId(), new ArrayList<>());
                        winners.get(user.getId()).get(macbook.getId()).add(lotteryTicket.getTicketId());
                    } else {
                        System.out.println("抽獎人: " + user.getName() + " 中獎: " + thankYou.getName());
                        winners.putIfAbsent(user.getId(), new HashMap<>());
                        winners.get(user.getId()).putIfAbsent(thankYou.getId(), new ArrayList<>());
                        winners.get(user.getId()).get(thankYou.getId()).add(lotteryTicket.getTicketId());
                    }
                    continue;
                }

                if (random <= iphone.getWeight()) {
                    if (checkCountOnUpdatePrize(prizePool, iphone.getId()) &&
                            !winners.getOrDefault(user.getId(), new HashMap<>())
                                    .containsKey(iphone.getId())) {
                        System.out.println("抽獎人: " + user.getName() + " 中大獎: " + iphone.getName());
                        winners.putIfAbsent(user.getId(), new HashMap<>());
                        winners.get(user.getId()).putIfAbsent(iphone.getId(), new ArrayList<>());
                        winners.get(user.getId()).get(iphone.getId()).add(lotteryTicket.getTicketId());
                    } else {
                        System.out.println("抽獎人: " + user.getName() + " 中獎: " + thankYou.getName());
                        winners.putIfAbsent(user.getId(), new HashMap<>());
                        winners.get(user.getId()).putIfAbsent(thankYou.getId(), new ArrayList<>());
                        winners.get(user.getId()).get(thankYou.getId()).add(lotteryTicket.getTicketId());
                    }
                    continue;
                }

                if (random <= appleWatch.getWeight()) {
                    // 檢查還有沒有獎項, 以及這傢伙中過沒有，沒有中過才能中，中過的話給 thank you
                    // checkCountOnUpdatePrize 只會檢查還有沒有獎品，不會檢查這個人有沒有中過
                    if (checkCountOnUpdatePrize(prizePool, appleWatch.getId()) &&
                            !winners.getOrDefault(user.getId(), new HashMap<>())
                                    .containsKey(appleWatch.getId())) {
                        System.out.println("抽獎人: " + user.getName() + " 中大獎: " + appleWatch.getName());
                        winners.putIfAbsent(user.getId(), new HashMap<>());
                        winners.get(user.getId()).putIfAbsent(appleWatch.getId(), new ArrayList<>());
                        winners.get(user.getId()).get(appleWatch.getId()).add(lotteryTicket.getTicketId());
                    } else {
                        System.out.println("抽獎人: " + user.getName() + " 中獎: " + thankYou.getName());
                        winners.putIfAbsent(user.getId(), new HashMap<>());
                        winners.get(user.getId()).putIfAbsent(thankYou.getId(), new ArrayList<>());
                        winners.get(user.getId()).get(thankYou.getId()).add(lotteryTicket.getTicketId());
                    }
                    continue;
                }

                System.out.println("抽獎人: " + user.getName() + " 中獎: " + thankYou.getName());

                // 每次抽完更新抽獎次數
                lottery.setAttendedCount(lottery.getAttendedCount() - 1);
            }
        }
    }

    // 使用在當計算為中獎時，透過 DB 的 update 去防止超過獎項數量
    private static boolean checkCountOnUpdatePrize(Map<String, Integer> prizePool, String prizeId) {
        Integer count = prizePool.get(prizeId);

        // 假設都已經取到相關資料
        if (prizeId.equals("1") && count >= 30) {
            return false;
        } else if (prizeId.equals("2") && count >= 10) {
            return false;
        } else if (prizeId.equals("3") && count >= 5) {
            return false;
        }

        prizePool.put(prizeId, prizePool.get(prizeId) + 1);
        return true;
    }
}
