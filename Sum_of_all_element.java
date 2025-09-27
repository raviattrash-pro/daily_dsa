import java.util.*;

class Sum_of_all_element{
	public static void main(String args[]){
		
	   Scanner input = new Scanner(System.in);	
	   System.out.println("Enter the value size of array  : ");
	   int n = input.nextInt();
	   int [] arr = new int[n];
	   System.out.println("Enter the elelemnt of array : ");
	   
	   for(int i=0;i<n;i++){
		arr[i] = input.nextInt();
	   }
	   
	   
	   Sum_of_all_element obj = new Sum_of_all_element();
	   int totalsum = obj.sum(n-1,arr);
		System.out.println("Sum of  "+n+"  th number will be :   "+totalsum);
	}
	
	public int sum(int n , int [] arr){
		if(n==0){
			return arr[0];
		}
		return (arr[n]+sum(n-1, arr));
	}
}