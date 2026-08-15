class Solution {
    public String mergeAlternately(String w1, String w2) {
        StringBuilder ne = new StringBuilder();

        int n1 = w1.length(), n2 = w2.length();
        int i = 0, j = 0;

        while (i < n1 && j < n2) {
            ne.append(w1.charAt(i++));
            ne.append(w2.charAt(j++));
        }

        while (i < n1) {
            ne.append(w1.charAt(i++));
        }

        while (j < n2) {
            ne.append(w2.charAt(j++));
        }

        return ne.toString();
    }
}