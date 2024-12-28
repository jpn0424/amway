package com.amway.model;

import com.amway.model.type.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Command {
    private Operation operation;
    private double value;
    private double result;
    private double lastResult;

    public double execute(double currentValue) {
        return switch (operation) {
            case ADD -> currentValue + value;
            case SUB -> currentValue - value;
            case MUL -> currentValue * value;
            case DIV -> {
                if (value == 0) {
                    System.out.println("Cannot divide by zero.");
                    yield currentValue;
                }
                yield currentValue / value;
            }
        };
    }
}
