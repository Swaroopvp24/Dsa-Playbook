public class Solution {

    public boolean searchMatrix(int[][] matrix, int target) {

        // Get the dimensions of the matrix.
        int rowCount = matrix.length;
        int columnCount = matrix[0].length;

        /*
         * The matrix is sorted both:
         * 1. From left to right within each row.
         * 2. From top to bottom between rows.
         *
         * Because of this, we can treat the matrix like one sorted 1D array.
         *
         * Example:
         *
         *   1   3   5
         *   7   9  11
         *  13  15  17
         *
         * Imagine it as:
         *
         *   1, 3, 5, 7, 9, 11, 13, 15, 17
         *
         * Instead of creating this 1D array, we simply calculate
         * which matrix position a 1D index corresponds to.
         */

        // Binary search range over the "virtual" 1D array.
        int left = 0;
        int right = rowCount * columnCount - 1;

        while (left <= right) {

            // Find the middle index of our virtual 1D array.
            int middle = left + (right - left) / 2;

            /*
             * Convert the 1D index back into a 2D matrix position.
             *
             * For a matrix with 3 columns:
             *
             * 1D index:   0   1   2   3   4   5
             * Matrix:   [0,0][0,1][0,2][1,0][1,1][1,2]
             *
             * Integer division tells us the row.
             * Modulo tells us the column.
             *
             * row = middle / columnCount
             * col = middle % columnCount
             */
            int row = middle / columnCount;
            int column = middle % columnCount;

            int currentValue = matrix[row][column];

            // Target is greater, so eliminate the left half.
            if (target > currentValue) {
                left = middle + 1;

            // Target is smaller, so eliminate the right half.
            } else if (target < currentValue) {
                right = middle - 1;

            // The current value is the target.
            } else {
                return true;
            }
        }

        // Binary search exhausted the search space.
        // Therefore, the target does not exist in the matrix.
        return false;
    }
}
