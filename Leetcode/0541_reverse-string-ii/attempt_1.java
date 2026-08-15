class Solution {
    public void rev(char[] c, int s, int e) {
        while (s < e) {
            char temp = c[s];
            c[s] = c[e];
            c[e] = temp;
            s++;
            e--;
        }
    }

    public String reverseStr(String s, int k) {
        char[] chac = s.toCharArray();

        for (int st = 0; st < chac.length; st += 2 * k) {
            int e = Math.min(st + k - 1, chac.length - 1);
            rev(chac, st, e);
        }

        return new String(chac);
    }
}