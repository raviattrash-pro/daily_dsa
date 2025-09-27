import java.util.*;

class Group_Anagram{
	public void print_array(String [] strs){
		System.out.println("Entered Strings : ");
		for(String i :strs){
			System.out.println(i);
		}
		Arrays.sort(strs);
		System.out.println("Sorted array Entered Strings : ");
		for(String i :strs){
			System.out.println(i);
		}
		List <List <String>> str_list = new ArrayList<>();
		List<String> innerlist = new ArrayList<>();
		for(int i=0;i<strs.length-1;i++){
			innerlist.add(strs[i]);
			if(strs[i].length() == strs[i+1].length()){
				innerlist.add(strs[i]);
			}
			str_list.add(innerlist);
		}
		System.out.println("after seperate array Entered Strings : ");
		for(String i :strs){
			System.out.println(i);
		}
	}


    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            String key = new String(arr);

            // Group by sorted string
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());  // Collect all grouped anagrams
    }


	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		Group_Anagram obj = new Group_Anagram();
		System.out.println("enter the size of array :  ");
		int size = input.nextInt();
		String [] strs = new String [size];
		System.out.println("Enter the strings in array : ");
		for(int i =0;i<size;i++){
			strs[i] = input.next();
		}
		obj.print_array(strs);	
		List <List<String >> ans = obj.groupAnagrams(strs);
		System.out.println("List will be  :  "+ans);
	}
}