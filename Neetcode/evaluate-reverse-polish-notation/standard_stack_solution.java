class Solution {

    private int calculate(int leftOperand, int rightOperand, String operator) {
        switch (operator) {
            case "+":
                return leftOperand + rightOperand;
            case "-":
                return leftOperand - rightOperand;
            case "*":
                return leftOperand * rightOperand;
            case "/":
                return leftOperand / rightOperand;
            default:
                return 0;
        }
    }

    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String token : tokens) {
            // Operators use the top two values from the stack.
            if ("+/*-".contains(token)) {
                int rightOperand = stack.pop();
                int leftOperand = stack.pop();

                int result = calculate(leftOperand, rightOperand, token);
                stack.push(result);
            } else {
                // Numbers are pushed directly onto the stack.
                stack.push(Integer.parseInt(token));
            }
        }

        return stack.pop();
    }
}
