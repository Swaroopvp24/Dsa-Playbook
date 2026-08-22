class Solution {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {

        // Convert the array into a List so we can sort it using a custom comparator.
        List<Integer> numbers = new ArrayList<>();

        for (int value : arr) {
            numbers.add(value);
        }

        /*
         * Sort by:
         * 1. Smaller distance from x first.
         * 2. If distances are equal, smaller number first.
         *
         * Example:
         * x = 5
         * numbers = [3, 4, 6, 7]
         *
         * Distances:
         * 3 -> 2
         * 4 -> 1
         * 6 -> 1
         * 7 -> 2
         *
         * So the order becomes: [4, 6, 3, 7]
         */
        numbers.sort((first, second) -> {

            int firstDistance = Math.abs(first - x);
            int secondDistance = Math.abs(second - x);

            // Primary condition: closer number comes first.
            if (firstDistance != secondDistance) {
                return firstDistance - secondDistance;
            }

            // Tie-breaker: if equally close, smaller number comes first.
            return first - second;
        });

        /*
         * The first k elements are the k closest elements.
         *
         * However, they are currently sorted by distance,
         * while the problem requires the final answer in ascending order.
         */
        List<Integer> closestElements =
                new ArrayList<>(numbers.subList(0, k));

        // Final result must be sorted in increasing order.
        Collections.sort(closestElements);

        return closestElements;
    }
}