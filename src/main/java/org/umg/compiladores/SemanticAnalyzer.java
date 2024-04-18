package org.umg.compiladores;

import org.umg.compiladores.dto.SemanticAnalysisResult;

import java.util.Stack;

public class SemanticAnalyzer {

    public static SemanticAnalysisResult evaluateExpression(String expression) {
        char[] tokens = expression.toCharArray();

        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        try {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i] == ' ')
                    continue;

                if (tokens[i] >= '0' && tokens[i] <= '9') {
                    StringBuilder sb = new StringBuilder();
                    while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.')) {
                        sb.append(tokens[i++]);
                    }
                    values.push(Double.parseDouble(sb.toString()));
                    i--;
                } else if (tokens[i] == '(') {
                    operators.push(tokens[i]);
                } else if (tokens[i] == ')') {
                    while (operators.peek() != '(') {
                        values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.pop();
                } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                    while (!operators.empty() && hasPrecedence(tokens[i], operators.peek())) {
                        values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.push(tokens[i]);
                }
            }

            while (!operators.empty()) {
                values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
            }

            return new SemanticAnalysisResult(true, values.pop(), null);
        } catch (Exception e) {
            return new SemanticAnalysisResult(false, 0, e.getMessage());
        }
    }

    private static double applyOperation(char operator, double b, double a) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0)
                    throw new ArithmeticException("División por cero no permitida");
                yield a / b;
            }
            default -> 0;
        };
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }
}
