import java.util.Scanner;
public class trekant{
   public static void main(String args[]) {
    
        Scanner sc = new Scanner(System.in);// bruger scanner util
        
    
        System.out.println("Velkommen til hypotenuse-udregneren"); // prompt til terminal
        System.out.println("Indsæt a-værdi");
        double a = sc.nextDouble(); // tager både double on ints.
        System.out.println("Indsæt b-værdi");
        double b = sc.nextDouble();
        double c = (double) Math.sqrt(a*a+b*b); // kunne ikke få den til at lavve **2, så det her blev løsningen. (kommentar er lavet efterfølgende)
        System.out.println("Hypotenusen i din trekant = " + c); // printer hypotenusen 
        
        
        sc.close();
    }
}