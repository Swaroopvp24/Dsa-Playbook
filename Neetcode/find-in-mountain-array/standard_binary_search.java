/**
 * // This is MountainArray's API interface.
 * // You should not implement it, or speculate about its implementation.
 * interface MountainArray {
 *     public int get(int index) {}
 *     public int length() {}
 * }
 */

class Solution {

    public int findInMountainArray(int target, MountainArray mountainArray) {
        int arrayLength = mountainArray.length();

        // Step 1: Find the index of the peak element.
        // The mountain array increases up to the peak and decreases after it.
        int peakIndex = findPeakIndex(mountainArray, arrayLength);

        // Step 2: Search the increasing (ascending) portion first.
        // This ensures we return the smallest index if the target exists on both sides.
        int ascendingIndex = binarySearch(
            mountainArray,
            0,
            peakIndex,
            target,
            true
        );

        if (ascendingIndex != -1) {
            return ascendingIndex;
        }

        // Step 3: If the target wasn't found on the ascending side,
        // search the decreasing (descending) portion.
        return binarySearch(
            mountainArray,
            peakIndex + 1,
            arrayLength - 1,
            target,
            false
        );
    }

    /**
     * Finds the index of the peak element in the mountain array.
     *
     * At any index:
     * - If arr[mid] < arr[mid + 1], we are on the ascending side,
     *   so the peak must be to the right.
     * - Otherwise, we are on the descending side (or at the peak),
     *   so the peak is at mid or somewhere to the left.
     */
    private int findPeakIndex(MountainArray mountainArray, int arrayLength) {
        int left = 0;
        int right = arrayLength - 1;

        while (left < right) {
            int middle = left + (right - left) / 2;

            if (mountainArray.get(middle) < mountainArray.get(middle + 1)) {
                // We are still climbing, so the peak is to the right.
                left = middle + 1;
            } else {
                // We are descending, or middle is the peak.
                // Keep middle as a possible peak.
                right = middle;
            }
        }

        // When left == right, we have found the peak index.
        return left;
    }

    /**
     * Performs binary search on either the ascending or descending portion
     * of the mountain array.
     *
     * @param leftIndex  Start index of the search range.
     * @param rightIndex End index of the search range.
     * @param target     Value we are looking for.
     * @param ascending  True if the search range is increasing,
     *                   false if it is decreasing.
     *
     * @return Index of the target if found, otherwise -1.
     */
    private int binarySearch(
        MountainArray mountainArray,
        int leftIndex,
        int rightIndex,
        int target,
        boolean ascending
    ) {
        int left = leftIndex;
        int right = rightIndex;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            int middleValue = mountainArray.get(middle);

            if (middleValue == target) {
                return middle;
            }

            if (ascending) {
                // Ascending order:
                // If middleValue is smaller, target must be on the right.
                if (middleValue < target) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            } else {
                // Descending order:
                // If middleValue is smaller, target must be on the left.
                if (middleValue < target) {
                    right = middle - 1;
                } else {
                    left = middle + 1;
                }
            }
        }

        // Target does not exist in this portion of the mountain.
        return -1;
    }
}
