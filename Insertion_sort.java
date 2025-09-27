import java.util.*;

class Insertion_sort{
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		Insertion_sort obj = new Insertion_sort();
		System.out.println("Enter size of array : ");
		int size = input.nextInt();
		int [] arr = new int [size];
		System.out.println("Enter the element in the array :");
		for(int i=0;i<size;i++){
			arr[i] = input.nextInt();
		}
		int [] ans = obj.ins_sort(arr);
		System.out.println("Sorted Array will be : ");
		for(int i:ans){
			System.out.println(i);
		}
	}
	public int []  ins_sort(int [] arr){
		int n = arr.length ;
		for(int i=1;i<arr.length;i++){
			int key = arr[i];
			int j = i-1;
			
			while(j>=0 && arr[j] > key){
				arr[j+1] = arr[j];
				j =j-1;
			}
			
			arr[j+1] = key ;
		}
		return arr;
	}
}


/*

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
/*class Solution {
    public ListNode insertionSortList(ListNode head) {
          if (head == null || head.next == null) return head;

        ListNode dummy = new ListNode(0); // Dummy node for the new sorted list
        ListNode curr = head;

        while (curr != null) {
            ListNode prev = dummy;
            // Find the right place to insert curr
            while (prev.next != null && prev.next.val < curr.val) {
                prev = prev.next;
            }

            ListNode nextTemp = curr.next; // Save next node
            // Insert curr between prev and prev.next
            curr.next = prev.next;
            prev.next = curr;

            curr = nextTemp; // Move to the next node
        }

        return dummy.next;
    }
}


*/