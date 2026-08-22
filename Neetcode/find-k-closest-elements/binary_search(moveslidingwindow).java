public class Solution {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {

        // The answer is a window of size k.
        // So we binary search for the starting index of that window.
        int left = 0;
        int right = arr.length - k;

        while (left < right) {

            int mid = (left + right) / 2;

            /*
             * Compare the two elements that would be removed
             * if the window starts at mid:
             *
             * arr[mid]       -> left side candidate
             * arr[mid + k]   -> right side candidate
             *
             * If arr[mid] is farther from x, move the window right.
             */
            if (x - arr[mid] > arr[mid + k] - x) {
                left = mid + 1;
            } else {
                // mid can still be the answer, so keep it.
                right = mid;
            }
        }

        List<Integer> closestElements = new ArrayList<>();

        // 'left' is the starting index of the closest window.
        for (int i = left; i < left + k; i++) {
            closestElements.add(arr[i]);
        }

        return closestElements;
    }
}