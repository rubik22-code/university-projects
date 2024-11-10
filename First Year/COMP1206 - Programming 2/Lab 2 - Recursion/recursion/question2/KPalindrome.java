import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.recursion.PalindromeChecker;

public class KPalindrome implements PalindromeChecker {

  public static void main(String[] args) {
    KPalindrome newPalindrome = new KPalindrome();
    System.out.println(newPalindrome.isKPalindrome("ABBA", 0));
  }

  public boolean isKPalindrome(String s, int i) {

    if (s.length() == 0 || s.length() == 1) {
      return true;
    }

    if (i >= s.length()) {
      return true;
    }

    if (s.charAt(0) == s.charAt(s.length() - 1)) {
      return isKPalindrome(s.substring(1, s.length() - 1), i);
    }

    if (i > 0) {
      return isKPalindrome(s.substring(0, s.length() - 1), i - 1) || isKPalindrome(s.substring(1, s.length()), i - 1);
    }

    return false;
  }
}
