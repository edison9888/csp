
public class Test2 {

  /**
   * @param args
   */
  public static void main(String[] args) {
    int[] array = new int[]{9,18,15,21};
    System.out.println(isRight(array));
    
    array = new int[]{6,15,12,21};
    System.out.println(isRight(array));
    
    array = new int[]{3,6,9,12};
    System.out.println(isRight(array));
  }
  
  public static boolean isRight(int[] array) {
	  if(array == null || array.length <=0)
		  return false;
	  
	  int n = array.length;
	  byte[] flagByte = new byte[n];
	  
	  for(int i=0; i<array.length - 1 ; i++) {
		  if(array[i + 1] > array[i] && ((array[i+1] - array[i]) == 3*(i+1)))
			  continue;
		  else if(array[i] > array[i+1] && ((array[i] - array[i+1]) == 3*(i+1)))
			  continue;
		  else 
			  return false;
	  }
	  return true;
  }
}
