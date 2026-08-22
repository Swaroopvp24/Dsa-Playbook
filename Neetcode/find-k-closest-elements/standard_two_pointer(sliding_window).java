public class Solution {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {

        int left = 0;
        int right = arr.length - 1;

        /*
         * Keep removing one element from either end
         * until exactly k elements remain.
         */
        while (right - left >= k) {

            // Compare the two boundary elements.
            if (Math.abs(x - arr[left]) <= Math.abs(x - arr[right])) {
                // Right element is farther, so remove it.
                right--;
            } else {
                // Left element is farther, so remove it.
                left++;
            }
        }

        List<Integer> closestElements = new ArrayList<>();

        // The remaining window contains the k closest elements.
        for (int i = left; i <= right; i++) {
            closestElements.add(arr[i]);
        }

        return closestElements;
    }
}