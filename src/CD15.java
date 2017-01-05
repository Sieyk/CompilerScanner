import java.util.Scanner;

//Matthew Muller 3184660

public class CD15 {
	public static void main(String[]args){
		
		System.out.println("Please input the file name for scanning");
		Scanner reader = new Scanner(System.in);
		COMPScanner scan = new COMPScanner(reader.next());
		reader.close();
        Token token = scan.scan();
        scan.printToken(token);
        while (!token.EOFChecker()) {
            token = scan.scan();
            scan.printToken(token);
        }
    }
}
