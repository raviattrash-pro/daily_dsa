import java.util.*;

class Selection_sort{
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		Selection_sort obj = new Selection_sort();
		System.out.println("Enter size of array : ");
		int size = input.nextInt();
		int [] arr = new int [size];
		System.out.println("Enter the element in the array :");
		for(int i=0;i<size;i++){
			arr[i] = input.nextInt();
		}
		int [] ans = obj.sel_sort(arr);
		System.out.println("Sorted Array will be : ");
		for(int i:ans){
			System.out.println(i);
		}
	}
	public int []  sel_sort(int [] arr){
	
		for(int i=0;i<arr.length;i++){
			int min_index = i;//change to i
			
			for(int j=i+1;j<arr.length;j++){
				if(arr[min_index] > arr[j]){
					min_index = j;
				}
			}
			
			int temp = arr[min_index];
			arr[min_index] = arr[i];
			arr[i] = temp;
		}
		return arr;
	}
}