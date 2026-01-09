import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;


public class tictactoe{
    static void PrintBoard(String board){ 
        try{
        String[] moves = board.split(" "); // jeg splitter min string ved mellemrummene
        
        
        String b = moves[moves.length-1]; // Jeg henter det sidste element af arrayet, som er spillebrættet i og med Board is ........
        System.out.println("+---+---+---+");// starter med den visuelle repræsatnering

        for (int i = 0; i < 9; i++){// loop der giver alle pladser nummer og erstatter hvis char har været i board strengen
            char c = b.charAt(i);
            if (c == '.') {
                System.out.print("| " + (i+1) + " ");
            }
            else {
                System.out.print("| " + c + " ");
            }     

        if ( i % 3 ==2 ){
            System.out.println("|");
            System.out.println("+---+---+---+");// enden af spillebrættet
            }
        }
    } catch (Exception e){
        System.out.println("Fejl");
    }

    }  


    public static void main(String args[]){

        Socket sock = null;

        try{
        sock = new Socket("34302.cyberteknologi.dk",1060);

            Scanner netin = new Scanner(sock.getInputStream());
            PrintWriter netout = new PrintWriter(sock.getOutputStream());
            Scanner tast = new Scanner(System.in);

            // alt sammen direkte fra undervisningen
            
            while (netin.hasNextLine()){
                String txtline = netin.nextLine(); // tagger nextLine, også fra undervisning. Læser næste linje fra serveren


                if (txtline.startsWith("BOARD")) {
                    PrintBoard(txtline); // printer mit board, hvis servern sender "Board"
                    
                    continue; // springer resten af loopet over hvis board optræder. Min løsning på boardstrengen der elleres også ville optræde samt andre småfejl
                }
                System.out.println(txtline);
                
                
                if (txtline.startsWith("YOUR")){// her bruges min net.out til at skrive til serveren. HUSK AT FLUSH!
                    String move = tast.nextLine();
                    netout.println(move);
                    netout.flush();
                } else if
                    (txtline.startsWith("IllEGAL")){
                    
                    // print board igen
                    String move = tast.nextLine();
                    netout.println(move);
                    netout.flush();

                }
            }
                
            
            
        sock.close();    // lukker socket og min exception
        
        }catch(Exception e){
            System.out.println("no server  found");
        }
    
    }
}


   



