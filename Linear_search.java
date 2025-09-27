import java.util.*;

class Linear_search{
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the size of array :");
		int size = input.nextInt();
		int [] arr = new int [size];
		System.out.println("Enter the element in the array :  ");
		for(int i=0;i<size;i++){
			arr[i] = input.nextInt();
		}
		System.out.println("Enter the element to find :  ");
		int find  = input.nextInt();
		Linear_search obj = new Linear_search();
		int index = obj.search(arr,find);
		 if(index >= 0){
			System.out.println("Enter number found at  index : "+index);
		 }
		 else{
			System.out.println("Enter number Not found in the array ");
		 }


	}
	
	public int search(int [] arr ,int find){
		for(int i=0;i<arr.length;i++){
			if(arr[i] == find ){
				return i;
			}
		}
		return -1;
	}
}