class Solution {
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int flowersPlanted = 0;

        for (int i = 0; i < flowerbed.length; i++) {
            // Check only empty spots
            if (flowerbed[i] == 0) {
                int left = (i == 0) ? 0 : flowerbed[i - 1];
                int right = (i == flowerbed.length - 1) ? 0 : flowerbed[i + 1];

                // Plant a flower if both adjacent spots are empty
                if (left == 0 && right == 0) {
                    flowerbed[i] = 1;
                    flowersPlanted++;

                    // No need to continue once we have planted enough
                    if (flowersPlanted >= n) {
                        return true;
                    }
                }
            }
        }

        return flowersPlanted >= n;
    }
}
