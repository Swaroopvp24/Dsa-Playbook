class Solution {
    public String decodeString(String s) {
        Deque<String> stack = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);

            if (currentChar == ']') {

                // Build the substring inside the brackets
                StringBuilder decodedPart = new StringBuilder();

                while (!stack.peek().equals("[")) {
                    // Stack stores characters in reverse order,
                    // so insert at the beginning to restore the original order.
                    decodedPart.insert(0, stack.pop());
                }

                // Remove the opening '['
                stack.pop();

                // Build the number before '[' (e.g. "12" from "12[abc]")
                StringBuilder repeatCountString = new StringBuilder();

                while (!stack.isEmpty()
                        && Character.isDigit(stack.peek().charAt(0))) {

                    // Digits are also stored in reverse order,
                    // so insert them at the beginning.
                    repeatCountString.insert(0, stack.pop());
                }

                int repeatCount = Integer.parseInt(repeatCountString.toString());

                // Repeat the decoded substring and push it back onto the stack
                String repeatedPart = decodedPart.toString().repeat(repeatCount);
                stack.push(repeatedPart);

            } else {
                // Store every character until we encounter ']'
                stack.push(String.valueOf(currentChar));
            }
        }

        // Combine everything remaining in the stack.
        // Since the stack is LIFO, build the result from the front.
        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            result.insert(0, stack.pop());
        }

        return result.toString();
    }
}