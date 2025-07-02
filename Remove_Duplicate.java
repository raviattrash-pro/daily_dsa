import java.util.*;

public class Remove_Duplicate{
	public static void main(String args[]){
	   Scanner input = new Scanner(System.in);	System.out.println("Enter the array size : ");
	   int size = input.nextInt();
	   int [] sorted_arr = new int [size];
	   System.out.println("Enter the array element : ");
	   for(int i=0;i<size;i++){
		System.out.println("Enter"+i+"th element ");
		sorted_arr[i] = input.nextInt();
	   }
	   System.out.println("Entered  the array elements are  : ");
	   for(int i=0;i<size;i++){
		System.out.println(sorted_arr[i]);
	   }
	   Remove_Duplicate obj = new Remove_Duplicate();
	   int unique_element = obj.dup_remove(sorted_arr);
	   System.out.println("unique  elements are : "+unique_element);
	}
	public int dup_remove(int [] nums){
		/*HashSet <Integer> set  = new HashSet<>();
        for(int i=0;i<nums.length;i++){
            set.add(nums[i]);
        }
		System.out.println("Hashset Element : ");
		for(int i :set){
			System.out.println(i);
		}
      return set.size();  */
	  
	  int leftpointer = 1;
	  for(int rightpointer =1; rightpointer < nums.length; rightpointer++){
		  if(nums[rightpointer] != nums[rightpointer -1] ){
			nums[leftpointer]   = nums[rightpointer];
			leftpointer++;
		  }
	  }
	  return leftpointer;
	}
}
//check util package 