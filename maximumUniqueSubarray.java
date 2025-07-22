class Solution {
    public int maximumUniqueSubarray(int[] nums) {
       Set<Integer> set = new HashSet<>();
        int left = 0, sum = 0, maxSum = 0;

        for (int right = 0; right < nums.length; right++) {
            // Remove from left until nums[right] is unique
            while (set.contains(nums[right])) {
                set.remove(nums[left]);
                sum -= nums[left];
                left++;
            }
            // Add nums[right] to window
            set.add(nums[right]);
            sum += nums[right];

            // Update maxSum
            maxSum = Math.max(maxSum, sum);
        }

        return maxSum; 
    }
}

/*class Solution {
    public int maximumUniqueSubarray(int[] nums) {

        int[] lastIndex = new int[10001];
        for (int i = 0; i < lastIndex.length; i++) {
            lastIndex[i] = -1;
        }
        int l = -1, sum = 0;
        int[] prefixSum = new int[nums.length+1];

        for (int r = 0; r < nums.length; r++) {
            prefixSum[r+1] = nums[r] + prefixSum[r];
            if (lastIndex[nums[r]] >= 0) {
                l = Math.max(l, lastIndex[nums[r]]);
            }
            sum = Math.max(sum, prefixSum[r+1] - prefixSum[l+1]);
            lastIndex[nums[r]] = r;
        }

        return sum;
        
    }
}*/