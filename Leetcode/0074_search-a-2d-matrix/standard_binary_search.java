class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {

        // Binary search over the rows.
        // We try to find the only row that can possibly contain the target.
        int topRow = 0;
        int bottomRow = matrix.length - 1;

        // Last column index of the matrix.
        int lastColumn = matrix[0].length - 1;

        while (topRow <= bottomRow) {

            // Find the middle row.
            int middleRow = topRow + (bottomRow - topRow) / 2;

            /*
             * Each row is sorted, so:
             *
             * - If target is greater than the last element of this row,
             *   the target must be in a row below.
             *
             * - If target is smaller than the first element of this row,
             *   the target must be in a row above.
             *
             * - Otherwise, target falls within this row's range,
             *   so this is the only row we need to search.
             */
            if (target > matrix[middleRow][lastColumn]) {
                topRow = middleRow + 1;

            } else if (target < matrix[middleRow][0]) {
                bottomRow = middleRow - 1;

            } else {
                // Target falls within the range of this row.
                // Now perform a standard binary search on the row.
                int leftColumn = 0;
                int rightColumn = lastColumn;

                while (leftColumn <= rightColumn) {

                    int middleColumn =
                            leftColumn + (rightColumn - leftColumn) / 2;

                    if (matrix[middleRow][middleColumn] == target) {
                        return true;
                    }

                    if (matrix[middleRow][middleColumn] < target) {
                        // Target is larger, so search the right half.
                        leftColumn = middleColumn + 1;
                    } else {
                        // Target is smaller, so search the left half.
                        rightColumn = middleColumn - 1;
                    }
                }

                // We found the correct row, but target isn't in it.
                return false;
            }
        }

        // No row can contain the target.
        return false;
    }
}
