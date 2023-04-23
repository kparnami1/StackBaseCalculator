import java.util.Scanner;
import java.util.Stack;

public class StackCalculator {
    // method to evaluate the postfix expression
    public static int evaluatePostfix(String postfix) {
        Stack<Integer> stack = new Stack<Integer>();
        boolean prevNeg = false;
        String temp = "";
        for (int i = 0; i < postfix.length(); i++) {
            char ch = postfix.charAt(i);

            if (Character.isDigit(ch)) {
                if (prevNeg) {
                    //stack.push(-ch + '0');
                    temp = "-"+ch;
                    prevNeg = false;
                } else {
                    temp = temp + ch;
                }
                    //stack.push(ch + '0');
            } else if (ch == '^') { // treating ^ as negative sign
                //stack.push(-stack.pop());
                prevNeg = !prevNeg;
            } else if (ch == ',')  {
                if (!temp.equals(""))
                    stack.push(Integer.parseInt(temp));
                temp = "";
            } else {
                if (!temp.equals("")) {
                    stack.push(Integer.parseInt(temp));
                    temp = "";
                }
                int num2 = stack.pop();
                int num1 = stack.pop();
                switch (ch) {
                    case '+':
                        byte sum = (byte) (num1 + num2);
                        if ((num1 > 0 && num2 > 0 && sum < 0) || (num1 < 0 && num2 < 0 && sum > 0)) {
                            System.out.println("Overflow occurred during addition");
                        }
                        stack.push((int)sum);
                        break;
                    case '-':
                        byte difference = (byte) (num1 - num2);
                        if ((num1 > 0 && num2 < 0 && difference < 0) || (num1 < 0 && num2 > 0 && difference > 0)) {
                            System.out.println("Overflow occurred during subtraction");
                        }
                        stack.push((int)difference);
                        break;
                    case '*':
                        int product = (int)(num1 * num2);
                        if (product > Byte.MAX_VALUE || product < Byte.MIN_VALUE)
                            System.out.println("Overflow occurred during subtraction");
                        byte byProduct = (byte) product;
                        stack.push((int)(byProduct));
                        break;
                    case '/':
                        int quotient = (int)Math.round((double) num1 / (double) num2);
                        byte byQuotient = (byte) quotient;
                        stack.push((int) byQuotient);
                        break;
                }
            }
        }
        if (stack.isEmpty() && !temp.equals(""))
            stack.push(Integer.parseInt(temp));
        return stack.pop();
    }
    public static String infixToPostfix(String infix) {
        Stack<Character> stack = new Stack<Character>();
        Stack<Integer> stackInt = new Stack<Integer>();
        StringBuilder postfix = new StringBuilder();
        char prev = 0;
        for (int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop();
            } else if (Character.isDigit(ch)) {
                postfix.append(ch);
            } else if (ch == '-' && (prev == 0 || prev == '(')) {
                postfix.append('^');
            } else {
                while (!stack.isEmpty() && getPrecedence(ch) <= getPrecedence(stack.peek())) {
                    postfix.append(stack.pop());
                }
                stack.push(ch);
                postfix.append(',');
            }
            prev = ch;
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }
    public static int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^': // treating ^ as negative sign
                return 3;
            default:
                return -1;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an arithmetic expression in infix notation: ");
        String infix = scanner.nextLine();
        String postfix = infixToPostfix(infix);

        int result = evaluatePostfix(postfix);
        System.out.println("Postfix expression: " + postfix.replaceAll(",",""));
       System.out.println("Result: " + result);

    }
}
