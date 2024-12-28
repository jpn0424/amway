package com.amway.calculator;

import com.amway.model.Command;
import com.amway.model.type.Operation;

import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to the calculator!\n using commands: ?1: start/reset, ?2: undo, ?3: redo, ?4: exit, add, sub, mul, div");
        Scanner scanner = new Scanner(System.in);

        String input = "";

        boolean isStarted = false;
        boolean isFirstNumber = true;
        boolean protect = false;
        boolean afterUndo = false;
        double result = 0;
        double beUsedValue = 0;

        Stack<Command> undoCommands = new Stack<>();
        Stack<Command> redoCommands = new Stack<>();

        Command command = null;

        // 持續輸入直到停止
        while (!input.equals("?4")) {
            input = scanner.nextLine().trim();

            // 開始與重置
            if (input.equals("?1")) {
                isStarted = !isStarted;

                if (isStarted) {
                    // 清除資料
                    result = 0;
                    beUsedValue = 0;
                    isFirstNumber = true;
                    undoCommands.clear();
                    redoCommands.clear();
                    protect = false;
                    System.out.println("Calculator started. Current result = " + result);
                    System.out.println("\nPlease enter a initial number.");
                } else {
                    System.out.println("Calculator reset. Enter ?1 to start again.");
                }
                continue;
            }

            if (!isStarted) {
                System.out.println("Calculator is not started, enter ?1 to start.");
                continue;
            }

            // 第一次輸入
            if (isFirstNumber) {
                beUsedValue = Double.parseDouble(input);
                command = new Command(Operation.ADD, beUsedValue, 0, 0);
                result = command.execute(result);
                command.setResult(result);
                undoCommands.push(command);
                protect = true;
                isFirstNumber = false;
                System.out.println("Number received, please enter an operation (add/sub/mul/div) or ?2/?3/?4...");
                continue;
            }

            // undo
            if (input.equals("?2")) {
                if (undoCommands.isEmpty()) {
                    System.out.println("No operation to undo.");
                    continue;
                }
                Command undoCommand = undoCommands.pop();
                redoCommands.push(undoCommand);
                result = undoCommand.getLastResult();
                System.out.println("Undo operation: " + undoCommand.getOperation() + " " + undoCommand.getValue());
                System.out.println("Current result = " + result);
                System.out.println("\nPlease enter a command.");
                protect = true;
                afterUndo = true;
                continue;
            }

            // redo
            if (input.equals("?3")) {
                if (redoCommands.isEmpty()) {
                    System.out.println("No operation to redo.");
                    continue;
                }
                Command redoCommand = redoCommands.pop();
                undoCommands.push(redoCommand);
                result = redoCommand.execute(result);
                System.out.println("Redo operation: " + redoCommand.getOperation() + " " + redoCommand.getValue());
                System.out.println("Current result = " + result);
                System.out.println("\nPlease enter a command.");
                protect = true;
                continue;
            }

            // 如果 undo 後進行了新的操作, 則 redo 會被清空
            if (afterUndo) {
                redoCommands.clear();
                afterUndo = false;
            }

            // 輸入指令
            switch (input) {
                case "add":
                case "sub":
                case "mul":
                case "div":
                    // 確保操作前數字已經輸入，代表進入 command 模式
                    if (!protect) {
                        System.out.println("Operation is failed, please enter a command.");
                        continue;
                    }

                    // 輸入指令並儲存
                    command = new Command(Operation.valueOf(input.toUpperCase()), 0, 0, result);
                    protect = false;
                    System.out.println("\nPlease enter a number");
                    break;
                default:
                    try {
                        beUsedValue = Double.parseDouble(input);
                        command.setValue(beUsedValue);
                        result = command.execute(result);
                        command.setResult(result);
                        undoCommands.push(command);
                        protect = true;
                        System.out.println("input: " + beUsedValue);
                        System.out.println("\nNumber received, \nCurrent result = " + result);
                        System.out.println("\nPlease enter a command.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, please try again.");
                    }
                    break;
            }
        }
    }
}