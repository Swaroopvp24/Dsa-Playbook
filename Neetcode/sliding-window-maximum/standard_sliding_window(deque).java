class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        // The deque maintains values in decreasing order from front to back.

        int left = 0;
        int resultIndex = 0;

        int[] result = new int[nums.length - k + 1];

        for (int right = 0; right < nums.length; right++) {

            // Remove indices outside the window
            while (!deque.isEmpty() && deque.peekFirst() < left) {
                deque.pollFirst();
            }

            //Remove to Maintain decreasing order of values
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[right]) {
                deque.pollLast();
            }

            deque.addLast(right);

            // Once the window reaches size k, record its maximum
            if (right - left + 1 >= k) {
                result[resultIndex++] = nums[deque.peekFirst()];
                left++;
            }
        }

        return result;
    }
}