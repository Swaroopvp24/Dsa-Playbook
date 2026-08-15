class Solution {
    public boolean isVowel(char c) {
        return "aeiouAEIOU".indexOf(c) != -1;
    }

    public String reverseVowels(String s) {
        char[] chac = s.toCharArray();
        int st = 0, en = chac.length - 1;
        while (st < en) {
            while (st < chac.length && !isVowel(chac[st]))
                st++;
            while (en >=0 && !isVowel(chac[en]))
                en--;
            if (st < en) {
                char t = chac[st];
                chac[st++] = chac[en];
                chac[en--] = t;
            }
        }
        return new String(chac);
    }
}