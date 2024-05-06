import java.util.Scanner;

public class Main {
    // "D:\newcompilers\miniRC.in"
    public static void main(String[] args) {
        System.out.println("Input you code path:");
        Scanner sc = new Scanner(System.in);
        String inPath = sc.nextLine();
        sc.close();

        int index = inPath.lastIndexOf(".");
        String prefix = inPath.substring(0 , index);
        
        Lexer lexer = new Lexer();
        lexer.GetInput(inPath, prefix + ".out", prefix + ".sym", prefix + ".err");
    }
}