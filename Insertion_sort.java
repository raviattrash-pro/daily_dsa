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