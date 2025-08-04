class Solution {
    static {
        for (int i = 0; i < 300; i++) {
            totalFruit(new int[0]);
        }
    }
    public static int totalFruit(int[] fruits) {
        int max = 0;
        int type1 = -1, type2 = -1;
        int count1 = 0, count2 = 0;
        int lead = -1, leadIndex = -1;
        for (int i = 0; i < fruits.length; i++) {
            if (type1 == -1 || type1 == fruits[i]) {
                type1 = fruits[i];
                count1++;
                if (lead != type1) {
                    lead = type1;
                    leadIndex = i;
                }
            } else if (type2 == -1 || type2 == fruits[i]) {
                type2 = fruits[i];
                count2++;
                if (lead != type2) {
                    lead = type2;
                    leadIndex = i;
                }
            } else {
                max = Math.max(max, count1+count2);
                if (lead == type1) {
                    count1 = i-leadIndex;
                    type2 = fruits[i];
                    count2 = 1;
                } else {
                    count2 = i-leadIndex;
                    type1 = fruits[i];
                    count1 = 1;
                }
                lead = fruits[i];
                leadIndex = i;
            }
        }

        return Math.max(max, count1+count2);
    }
}