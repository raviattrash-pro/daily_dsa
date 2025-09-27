public class MergeSort {

    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return; // Already sorted
        }

        mergeSortHelper(arr, 0, arr.length - 1);
    }

    private static void mergeSortHelper(int[] arr, int left, int right) {
        if (left >= right) {
            return; // Base case: single element
        }

        int mid = left + (right - left) / 2;

        // Sort left half
        mergeSortHelper(arr, left, mid);

        // Sort right half
        mergeSortHelper(arr, mid + 1, right);

        // Merge two halves
        merge(arr, left, mid, right);
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;      // Pointer for left half
        int j = mid + 1;   // Pointer for right half
        int k = 0;         // Pointer for temp array

        // Merge both halves into temp[]
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        // Copy remaining elements of left half
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // Copy remaining elements of right half
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // Copy temp[] back to arr[]
        for (int p = 0; p < temp.length; p++) {
            arr[left + p] = temp[p];
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, 2, 9, 1, 5, 6};

        System.out.println("Original Array:");
        for (int num : arr) System.out.print(num + " ");

        mergeSort(arr);

        System.out.println("\n\nSorted Array:");
        for (int num : arr) System.out.print(num + " ");
    }
}
