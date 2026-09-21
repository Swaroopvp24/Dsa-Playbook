class Solution {

    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] result = new int[nums1.length];

        // Monotonic decreasing stack.
        // Stores elements from nums2 that may be the next greater element
        // for elements appearing to their left.
        Deque<Integer> decreasingStack = new ArrayDeque<>();

        // Maps each number in nums2 to its next greater element.
        Map<Integer, Integer> nextGreaterMap = new HashMap<>();

        // The last element has no element to its right,
        // so its next greater element is -1.
        int lastElement = nums2[nums2.length - 1];
        decreasingStack.push(lastElement);
        nextGreaterMap.put(lastElement, -1);

        // Traverse nums2 from right to left.
        for (int index = nums2.length - 2; index >= 0; index--) {
            int currentElement = nums2[index];

            // Remove all elements that are smaller than or equal to
            // the current element because they cannot be its next greater element.
            while (!decreasingStack.isEmpty()
                    && decreasingStack.peek() <= currentElement) {
                decreasingStack.pop();
            }

            // If the stack is empty, there is no greater element to the right.
            // Otherwise, the top of the stack is the next greater element.
            int nextGreaterElement = decreasingStack.isEmpty()
                    ? -1
                    : decreasingStack.peek();

            nextGreaterMap.put(currentElement, nextGreaterElement);

            // The current element can be a next greater element
            // for elements further to its left.
            decreasingStack.push(currentElement);
        }

        // Look up the precomputed next greater element for every
        // element in nums1.
        for (int index = 0; index < nums1.length; index++) {
            result[index] = nextGreaterMap.get(nums1[index]);
        }

        return result;
    }
}
