class Solution {
    public String decodeString(String s) {
        Stack<String> previousStrings = new Stack<>();
        Stack<Integer> repeatCounts = new Stack<>();

        StringBuilder currentString = new StringBuilder();
        int currentNumber = 0;

        for (char ch : s.toCharArray()) {

            // Build the repeat count, e.g. "123" -> 123
            if (Character.isDigit(ch)) {
                currentNumber = currentNumber * 10 + (ch - '0');

            } 
            // Start of a new encoded substring: "3[abc]"
            else if (ch == '[') {

                // Save the string built before '['
                previousStrings.push(currentString.toString());

                // Save how many times this substring should repeat
                repeatCounts.push(currentNumber);

                // Start building the string inside the brackets
                currentString = new StringBuilder();
                currentNumber = 0;

            } 
            // End of the current encoded substring
            else if (ch == ']') {

                String decodedPart = currentString.toString();

                // Restore the string that existed before '['
                currentString = new StringBuilder(previousStrings.pop());

                int repeatCount = repeatCounts.pop();

                // Append the decoded part the required number of times
                for (int i = 0; i < repeatCount; i++) {
                    currentString.append(decodedPart);
                }

            } 
            // Normal character: simply add it to the current string
            else {
                currentString.append(ch);
            }
        }

        return currentString.toString();
    }
}