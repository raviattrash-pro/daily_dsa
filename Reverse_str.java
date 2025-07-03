import java.util.*;

public class Reverse_str{
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the number of string : ");
		int size = input.nextInt();
		String [] str = new String [size];
		System.out.println("Enter the string in array :");
		for(int i=0;i<size;i++){
			System.out.println("Enter the "+i+"  th string:");
			str[i] = input.next();
		}
		System.out.println("Entered string will be : ");
		for(String i :str){
			System.out.println(i);
		}
		Reverse_str obj = new Reverse_str();
		
		obj.reverse_str(str);
	}
	public void reverse_str(String [] str){
		String temp = null;
		int size = str.length;
		for(int i =0;i<(size/2) ;i++){
			temp = str[i];
			str[i] = str[size-1];
			str[size-1] = temp;
			size--;
		}
		System.out.println("Entered string after reverse will be : ");
		for(String i :str){
			System.out.println(i);
		}
		
	}
}