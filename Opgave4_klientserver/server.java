import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

public class server{
    public static void main(String args[]) throws IOException{ // Her vælger min debugger at ligge en IOException hver eneste gang. jeg har bare ladt den af dovenskab. Hvis dette ikke godtages i den endelige opgave undgår jeg det selvfølgelig.
    Boolean ServerAktiv = true; // fra undervisninging
        try (ServerSocket serversock = new ServerSocket(1068)){ // man kan skrive try(..) {}, åbenbart, så gør jeg herfra. Lytter på port 1068 efter klient
                System.out.println("Lytter på port 1068");

                while(ServerAktiv){ // når serveren er aktiv
                    Socket sock = serversock.accept();
                    System.out.println("Connected"); // skriver den ud så man er sikke rpå den virker

                    Scanner netin = new Scanner(sock.getInputStream());
                    PrintWriter netout = new PrintWriter(sock.getOutputStream());

                    netout.println("Skriv noget"); // det der sendes til klienten HUSK AT FLUSH!
                    netout.flush();

                    
                    if (netin.hasNextLine()){ // Tager string, gør den til char array for at kunne arbejde med den. Herfter gåes den igennem, vendes og sendes tilbage
                        String txtline = netin.nextLine();
                        char[] tempArray = txtline.toCharArray();
                        for (int i = 0, j = tempArray.length -1; i < j; i++, j--){ // Delvis taget fra nettet. Logikken her er noget helt andet end jeg er vant til.
                            char reversedArray = tempArray[i];
                            tempArray[i]=tempArray[j];
                            tempArray[j]=reversedArray;

                        /* Logikken til overstående er dog således:
                        i starter forrest i arrayet mens j går bagud i længden af vores temparray, de går derfor "ind mod midten" af arrayet 
                        når de mødes på midten er hele arrayet vendt rundt. herefter gøres vores temparray til det reversed. */
                    

                        }
                        String reversed = new String(tempArray);
                        netout.println(reversed);// sender vores nu reversed array tilbage til klienten
                        netout.flush();
                    }
                
                sock.close();
            }
        }catch (Exception e){
            System.out.println("Fejl");
        }
    }
}
              