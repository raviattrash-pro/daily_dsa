import java.util.*;

class First_n_Sum{
	public static void main(String args[]){
		
	   Scanner input = new Scanner(System.in);	
	   System.out.println("Enter the value n for which you find sum : ");
	   
	   int n = input.nextInt();
	   First_n_Sum obj = new First_n_Sum();
	   int totalsum = obj.sum(n);
		System.out.println("Sum of  "+n+"  th number will be :   "+totalsum);
	}
	
	public int sum(int n){
		if(n==0){
			return 0;
		}
		return (n+sum(n-1));
	}
}