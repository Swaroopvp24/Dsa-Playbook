class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        // The maximum possible eating speed is the largest pile.
        int maxPileSize = 0;

        for (int pile : piles) {
            maxPileSize = Math.max(maxPileSize, pile);
        }

        // Binary search for the minimum valid eating speed.
        int left = 1;
        int right = maxPileSize;
        int minimumSpeed = maxPileSize;

        while (left <= right) {
            // Calculate the middle eating speed.
            int eatingSpeed = left + (right - left) / 2;

            // Calculate total hours needed at this eating speed.
            long totalHours = 0;

            for (int pile : piles) {
                // Hours needed for this pile = ceil(pile / eatingSpeed)
                totalHours += (pile + eatingSpeed - 1) / eatingSpeed;
            }

            if (totalHours <= h) {
                // This speed is sufficient, so try a smaller speed.
                minimumSpeed = eatingSpeed;
                right = eatingSpeed - 1;
            } else {
                // This speed is too slow, so increase the speed.
                left = eatingSpeed + 1;
            }
        }

        return minimumSpeed;
    }
}
