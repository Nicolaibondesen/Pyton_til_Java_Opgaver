import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

public class klient{
    public static void main(String args[]) throws IOException{ // igen debugger vil have mig til at throw den her exception, ved ikke om den burde ligge som endnu et catch?

        Socket sock = null;

        try{
        sock = new Socket("localhost", 1068);

        Scanner netin = new Scanner(sock.getInputStream());
        PrintWriter netout = new PrintWriter(sock.getOutputStream());
        Scanner tast = new Scanner(System.in);
        
        

        while (netin.hasNextLine()){ // nextLine metoden igen 
            String txtline = netin.nextLine();
            System.out.println(txtline);

        if (txtline.startsWith("Skriv")){ // har et if statement, der leder efter skriv, herefter bruges netout
                String text = tast.nextLine();
                netout.println(text);
                netout.flush();
            }
        }
        sock.close(); // programmet lukkes efter et enekelt ord. forstår ikke helt om det er inde for opgavebeskrivelsen om at " Serveren skal herefter være klar til at modtage en ny forbindelse fra en klient" Den virker ihvertald hvis man java klient igen.
        


        }catch (Exception e){
            System.out.println("Fejl");
        }
        
    }
}

