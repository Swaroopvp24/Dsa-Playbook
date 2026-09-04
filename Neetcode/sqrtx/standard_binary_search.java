class Solution {
    public int mySqrt(int x) {

        // The square root of 0 and 1 is the number itself.
        if (x < 2) {
            return x;
        }

        // The integer square root must be between 2 and x.
        int left = 2;
        int right = x;

        while (left <= right) {

            // Calculate the middle value without risking integer overflow.
            int mid = left + (right - left) / 2;

            /*
             * Instead of checking mid * mid <= x, use x / mid >= mid.
             *
             * This avoids overflow because mid * mid can exceed
             * the maximum value of an int.
             */
            if (mid <= x / mid) {

                // mid is a valid answer.
                // Try to find a larger value whose square is still <= x.
                left = mid + 1;

            } else {

                // mid² is greater than x, so mid is too large.
                // Search the left half.
                right = mid - 1;
            }
        }

        /*
         * When the loop ends, right is the largest integer
         * such that right² <= x.
         */
        return right;
    }
}
