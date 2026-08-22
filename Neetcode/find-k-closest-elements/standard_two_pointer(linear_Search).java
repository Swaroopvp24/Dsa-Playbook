public class Solution {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {

        int arrayLength = arr.length;
        int closestIndex = 0;

        // Find the element closest to x.
        for (int i = 1; i < arrayLength; i++) {
            if (Math.abs(x - arr[closestIndex]) > Math.abs(x - arr[i])) {
                closestIndex = i;
            }
        }

        List<Integer> closestElements = new ArrayList<>();
        closestElements.add(arr[closestIndex]);

        // Expand from the closest element in both directions.
        int leftIndex = closestIndex - 1;
        int rightIndex = closestIndex + 1;

        while (closestElements.size() < k) {

            if (leftIndex >= 0 && rightIndex < arrayLength) {

                // If both sides are available, choose the closer one.
                // In case of a tie, choose the smaller value (left side).
                if (Math.abs(x - arr[leftIndex]) <= Math.abs(x - arr[rightIndex])) {
                    closestElements.add(arr[leftIndex--]);
                } else {
                    closestElements.add(arr[rightIndex++]);
                }

            } else if (leftIndex >= 0) {
                // Right side is exhausted.
                closestElements.add(arr[leftIndex--]);

            } else if (rightIndex < arrayLength) {
                // Left side is exhausted.
                closestElements.add(arr[rightIndex++]);
            }
        }

        // Elements were added based on distance, so sort for final ascending order.
        Collections.sort(closestElements);

        return closestElements;
    }
}