import java.util.*;

class Factorial_of_num{
	public static void main(String args[]){
		
	   Scanner input = new Scanner(System.in);	
	   System.out.println("Enter the value to find factorial  : ");
	   int n = input.nextInt();
	   
	   Factorial_of_num obj = new Factorial_of_num();
	   int total = obj.fact(n);
		System.out.println("factorial of  "+n+"  th number will be :   "+total);
	}
	
	public int fact(int n){
		if(n==0){
			return 1;
		}
		return (n*fact(n-1));
	}
}