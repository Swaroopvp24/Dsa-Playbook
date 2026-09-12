class Solution {

    // Returns the number of days required to ship all packages
    // when the ship's capacity is 'capacity'.
    private int calculateRequiredDays(int[] weights, int capacity) {
        int requiredDays = 1;
        int currentLoad = 0;

        for (int weight : weights) {
            // Add the package to the current day's load
            // if it does not exceed the capacity.
            if (currentLoad + weight <= capacity) {
                currentLoad += weight;
            } else {
                // Otherwise, start shipping on a new day.
                requiredDays++;
                currentLoad = weight;
            }
        }

        return requiredDays;
    }

    public int shipWithinDays(int[] weights, int days) {
        int totalWeight = 0;
        int maxWeight = 0;

        // Minimum possible capacity = heaviest package.
        // Maximum possible capacity = total weight of all packages.
        for (int weight : weights) {
            totalWeight += weight;
            maxWeight = Math.max(maxWeight, weight);
        }

        int left = maxWeight;
        int right = totalWeight;

        // Binary search for the minimum capacity
        // that allows all packages to be shipped within 'days'.
        while (left < right) {
            int capacity = left + (right - left) / 2;

            int requiredDays = calculateRequiredDays(weights, capacity);

            if (requiredDays <= days) {
                // This capacity is sufficient.
                // Try to find a smaller capacity.
                right = capacity;
            } else {
                // Capacity is too small, so increase it.
                left = capacity + 1;
            }
        }

        // 'left' is the minimum required shipping capacity.
        return left;
    }
}
