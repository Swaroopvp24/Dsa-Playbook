/**
 * // This is MountainArray's API interface.
 * // You should not implement it, or speculate about its implementation.
 * interface MountainArray {
 *     public int get(int index) {}
 *     public int length() {}
 * }
 */

class Solution {
    // MountainArray.get() has a limited number of allowed calls.
    // Cache values so the same index is never fetched more than once.
    private Map<Integer, Integer> valueCache = new HashMap<>();

    /**
     * Returns the value at the given index.
     *
     * If the value has already been fetched, return it from the cache
     * instead of making another MountainArray.get() call.
     */
    private int getValue(int index, MountainArray mountainArray) {
        if (!valueCache.containsKey(index)) {
            valueCache.put(index, mountainArray.get(index));
        }

        return valueCache.get(index);
    }

    /**
     * Performs binary search on a sorted portion of the mountain array.
     *
     * @param leftIndex  Start index of the search range.
     * @param rightIndex End index of the search range.
     * @param ascending  True if the range is ascending,
     *                   false if the range is descending.
     * @param target     Value we are searching for.
     *
     * @return Index of target if found, otherwise -1.
     */
    private int binarySearch(
        int leftIndex, int rightIndex, boolean ascending, MountainArray mountainArray, int target) {
        int left = leftIndex;
        int right = rightIndex;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            int middleValue = getValue(middle, mountainArray);

            if (middleValue == target) {
                return middle;
            }

            if (ascending) {
                // Ascending order:
                // Smaller value -> target must be on the right.
                if (middleValue < target) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            } else {
                // Descending order:
                // Smaller value -> target must be on the left.
                if (middleValue < target) {
                    right = middle - 1;
                } else {
                    left = middle + 1;
                }
            }
        }
        /*
         * The above can be written more compactly as:
         *
         * if (ascending == (value < target)) {
         *     l = mid + 1;
         * } else {
         *     r = mid - 1;
         * }
         *
         * Same logic, just more condensed.
         */

        return -1;
    }

    public int findInMountainArray(int target, MountainArray mountainArray) {
        int arrayLength = mountainArray.length();

        // --------------------------------------------------
        // Step 1: Find the peak element
        // --------------------------------------------------

        // The peak cannot be at index 0 or the last index
        // because this is a valid mountain array.
        int left = 1;
        int right = arrayLength - 2;
        int peakIndex = 0;

        while (left <= right) {
            int middle = left + (right - left) / 2;

            int leftValue = getValue(middle - 1, mountainArray);
            int middleValue = getValue(middle, mountainArray);
            int rightValue = getValue(middle + 1, mountainArray);

            // We are still climbing:
            // leftValue < middleValue < rightValue
            //
            // Therefore, the peak must be somewhere to the right.
            if (leftValue < middleValue && middleValue < rightValue) {
                left = middle + 1;
            }

            // We are already descending:
            // leftValue > middleValue > rightValue
            //
            // Therefore, the peak must be somewhere to the left.
            else if (leftValue > middleValue && middleValue > rightValue) {
                right = middle - 1;
            }

            // Neither increasing nor decreasing:
            // middle is the peak.
            else {
                peakIndex = middle;
                break;
            }
        }

        // --------------------------------------------------
        // Step 2: Search the ascending portion
        // --------------------------------------------------

        // Search from index 0 through the peak.
        // We search this side first because the problem asks
        // for the smallest index when the target occurs multiple times.
        int ascendingIndex = binarySearch(0, peakIndex, true, mountainArray, target);

        if (ascendingIndex != -1) {
            return ascendingIndex;
        }

        // --------------------------------------------------
        // Step 3: Search the descending portion
        // --------------------------------------------------

        // If the target was not found on the ascending side,
        // search from the element after the peak to the end.
        return binarySearch(peakIndex + 1, arrayLength - 1, false, mountainArray, target);
    }
}
