class Solution {
    // Custom class to hold coupon data for sorting.
    // Implements Comparable for natural ordering based on problem requirements.
    private static class ValidCoupon implements Comparable<ValidCoupon> {
        String code;
        String businessLine;

        // A static map to define the custom order for business lines.
        // Initialized once when the class is loaded.
        private static final Map<String, Integer> BUSINESS_LINE_ORDER = new HashMap<>();
        static {
            BUSINESS_LINE_ORDER.put("electronics", 0);
            BUSINESS_LINE_ORDER.put("grocery", 1);
            BUSINESS_LINE_ORDER.put("pharmacy", 2);
            BUSINESS_LINE_ORDER.put("restaurant", 3);
        }

        public ValidCoupon(String code, String businessLine) {
            this.code = code;
            this.businessLine = businessLine;
        }

        @Override
        public int compareTo(ValidCoupon other) {
            // Primary sort by businessLine order.
            // We can directly use .get() because businessLine is validated against
            // the keys of BUSINESS_LINE_ORDER before a ValidCoupon object is created.
            int order1 = BUSINESS_LINE_ORDER.get(this.businessLine);
            int order2 = BUSINESS_LINE_ORDER.get(other.businessLine);

            if (order1 != order2) {
                return Integer.compare(order1, order2);
            }

            // Secondary sort by code lexicographically (ascending).
            return this.code.compareTo(other.code);
        }
    }

    public List<String> validateCoupons(String[] code, String[] businessLine, boolean[] isActive) {
        // List to store validated coupons along with their business lines for sorting.
        List<ValidCoupon> validCoupons = new ArrayList<>();

        // Define valid business lines for quick lookup using a HashSet.
        Set<String> validBusinessLines = new HashSet<>();
        validBusinessLines.add("electronics");
        validBusinessLines.add("grocery");
        validBusinessLines.add("pharmacy");
        validBusinessLines.add("restaurant");

        // Iterate through all coupons to perform validation.
        int n = code.length;
        for (int i = 0; i < n; i++) {
            String currentCode = code[i];
            String currentBusinessLine = businessLine[i];
            boolean currentIsActive = isActive[i];

            // Condition 1: code[i] is non-empty and consists only of alphanumeric characters and underscores.
            // The regex "^[a-zA-Z0-9_]+$" correctly enforces both non-empty (due to '+' quantifier)
            // and allowed characters (due to '[...]').
            boolean isCodeValid = currentCode.matches("^[a-zA-Z0-9_]+$");

            // Condition 2: businessLine[i] is one of the four specified categories.
            boolean isBusinessLineValid = validBusinessLines.contains(currentBusinessLine);

            // Condition 3: isActive[i] is true.
            boolean isCouponActive = currentIsActive;

            // If all three conditions hold, the coupon is valid.
            if (isCodeValid && isBusinessLineValid && isCouponActive) {
                validCoupons.add(new ValidCoupon(currentCode, currentBusinessLine));
            }
        }

        // Sort the list of valid coupons using the natural ordering defined in the ValidCoupon class.
        // This sorts first by businessLine (custom order) and then by code (lexicographical).
        Collections.sort(validCoupons);

        // Extract only the coupon codes from the sorted list to form the final result.
        List<String> resultCodes = new ArrayList<>();
        for (ValidCoupon coupon : validCoupons) {
            resultCodes.add(coupon.code);
        }

        return resultCodes;
    }
}

// Time Complexity: O(N * L_code + K * logK * L_code)
// N is the number of coupons.
// L_code is the maximum length of a coupon code.
// K is the number of valid coupons (K <= N).
//
// 1. Initialization of validBusinessLines Set: O(1) (fixed number of categories).
// 2. Iteration through N coupons: O(N) iterations.
//    - For each coupon:
//      - `currentCode.matches()` takes O(L_code) in the worst case (checks each character of the code).
//      - `validBusinessLines.contains()` takes O(1) on average for a HashSet.
//      - Other checks are O(1).
//    - Total for validation loop: O(N * L_code).
// 3. Adding valid coupons to `validCoupons` list: O(1) amortized for each addition. Total O(K).
// 4. Sorting `validCoupons` list: O(K log K) comparisons.
//    - Each comparison (`ValidCoupon.compareTo`) involves:
//      - Map lookups: O(1) on average.
//      - `String.compareTo()`: O(L_code) in the worst case.
//    - Total for sorting: O(K log K * L_code).
// 5. Extracting results: O(K) to iterate through the sorted list and add codes to the result list.
//
// Overall, the dominant factor is typically the sorting step, so O(N * L_code + K log K * L_code),
// which can be simplified to O(N log N * L_code) in the worst case where all coupons are valid (K=N).
// Given N <= 100 and L_code <= 100, this is very efficient.

// Space Complexity: O(N * (L_code + L_businessLine))
// 1. `validCoupons` list: In the worst case, all N coupons are valid. Each `ValidCoupon` object stores
//    a code (length up to L_code) and a business line (length up to L_businessLine).
//    Total space: O(N * (L_code + L_businessLine)).
// 2. `validBusinessLines` Set: O(1) (fixed number of categories).
// 3. `BUSINESS_LINE_ORDER` Map (static in ValidCoupon class): O(1) (fixed number of categories).
// 4. `resultCodes` list: Stores up to K (max N) coupon codes. Total space: O(N * L_code).
//
// Overall, the space complexity is dominated by storing the valid coupons and their codes.