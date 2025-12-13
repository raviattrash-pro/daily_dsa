import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class Solution {
    
    // Time Complexity: O(N * L + N log N), where N is the number of coupons and L is the max length of a code string.
    // O(N * L) for validation and categorization. O(N log N) for sorting the final list of valid codes.
    // Space Complexity: O(N) to store the intermediate categorized lists and the final result.
    public List<String> couponCodeValidator(String[] code, String[] businessLine, boolean[] isActive) {
        int n = code.length;
        
        // Define the required order for business lines
        final List<String> REQUIRED_ORDER = Arrays.asList(
            "electronics", "grocery", "pharmacy", "restaurant"
        );
        
        // Map business line to an index for easy sorting later
        Map<String, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < REQUIRED_ORDER.size(); i++) {
            orderMap.put(REQUIRED_ORDER.get(i), i);
        }
        
        // Regex pattern for condition 1: alphanumeric characters and underscores
        // Pattern: ^[a-zA-Z0-9_]+$
        // The '+' ensures it's non-empty, which covers condition 1 requirement.
        Pattern codePattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        
        // Structure to hold valid codes grouped by their business line index
        // Map: BusinessLineIndex -> List<Code>
        Map<Integer, List<String>> categorizedCodes = new HashMap<>();
        for (int i = 0; i < REQUIRED_ORDER.size(); i++) {
            categorizedCodes.put(i, new ArrayList<>());
        }
        
        for (int i = 0; i < n; i++) {
            String currentCode = code[i];
            String currentBusinessLine = businessLine[i];
            boolean currentIsActive = isActive[i];
            
            // 1. Check isActive condition
            if (!currentIsActive) {
                continue;
            }
            
            // 2. Check businessLine condition
            if (!orderMap.containsKey(currentBusinessLine)) {
                continue;
            }
            int businessLineIndex = orderMap.get(currentBusinessLine);
            
            // 3. Check code condition (non-empty and alphanumeric/underscore)
            // Note: codePattern handles both non-empty and allowed characters because it uses '+'.
            if (currentCode.isEmpty() || !codePattern.matcher(currentCode).matches()) {
                continue;
            }
            
            // If all conditions pass, add the code to the corresponding category
            categorizedCodes.get(businessLineIndex).add(currentCode);
        }
        
        List<String> result = new ArrayList<>();
        
        // Iterate through the business lines in the required order (0 to 3)
        for (int index : REQUIRED_ORDER.stream().map(orderMap::get).sorted().toList()) {
            List<String> codesInCategory = categorizedCodes.get(index);
            
            // Sort codes lexicographically within the category
            Collections.sort(codesInCategory);
            
            // Add sorted codes to the final result
            result.addAll(codesInCategory);
        }
        
        return result;
    }
}