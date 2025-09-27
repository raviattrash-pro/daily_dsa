import java.util.*;

class Sub_str{
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		Sub_str  obj = new Sub_str();
		System.out.println("Enter the String :");
		String str = input.next();
		obj.all_str(str);
		
	}
	public void all_str(String str){
		String [] str_arr = new String [str.length() * str.length()+1];
		int k=0;
		for(int i=0;i<=str.length();i++){
			for(int j=i;j<=str.length();j++){
				str_arr[k] = str.substring(i,j);
				k++;
			}
		}
		System.out.println("Array store strings are : ");
		for(String s : str_arr){
			System.out.println(s);
		}
	}
}