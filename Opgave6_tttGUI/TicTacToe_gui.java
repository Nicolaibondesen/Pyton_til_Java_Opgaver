import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;



public class TicTacToe_gui extends JFrame implements ActionListener{

    public JButton btn[];//fra opgavebeskrivelsen. btn array

    private Socket sock; // lidt overkill med private, men havde egentlig startet med at have en client class for sig, så nu beholder jeg det bare. også for at få det brugt
    private Scanner netin;
    private PrintWriter netout;



    public TicTacToe_gui(){
        getContentPane().setLayout(new GridLayout(3,3));
        //button instans her kopieret direkte fra undervisningsnoten.
        btn = new JButton[9]; // Opretter arrayet men ikke JButton instanserne endnu
        for (int i = 0; i < 9; i++) {
            btn[i] = new JButton(""); // Opretter instanserne af JButton men uden tekst
            btn[i].addActionListener(this);
            getContentPane().add(btn[i]);
        }
        connect();//connect og thread metoden kaldes fra konstruktøren
        thread();

    }
    public void actionPerformed(ActionEvent e){// Går igennem knaperne, bliver de trykket på sendes værdien til server.
        for (int i = 0; i<9; i++){
            if(e.getSource()==btn[i]){
                netout.println(i+1);// serven forventer 1-9
                netout.flush();
            }
        }

    }
    private void connect(){// connecter til server. Jeg bemærker først senere jeg aldrig lukker sock, men den virker uden, så tør ikke røre mere ved den
        try{
            sock = new Socket ("cyberteknologi.dk",1060);
            netin = new Scanner(sock.getInputStream());
            netout = new PrintWriter(sock.getOutputStream());
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Kunne ikke forbinde til server");
        }
    }

    private void thread(){ 
        // Her er jeg endt med at bruge thread. Meget af koden er genbrug fra de andre opgaver, men kunne jeg ikke få server og GUI til at køre sammentidigt.
        // Jeg har derfor brugt Chat GPT til at løse problemet.
        // jeg ved ikke om det kan lade sig gøre uden, da jeg aldrig fik det til at virke, men noterer det alligevel.
        Thread t = new Thread(() ->{ // starter thread for GUI kører sammen med netværksdelen. Ellers er resten lånt fra de tidliger opgaver, og kommenterer derofr ikke yderligere.
            try{
                while (netin.hasNextLine()){
                    String txtline = netin.nextLine();
                    System.out.println(txtline); // Da vi har fået opgivet 3x3 løsningen med btn ovenover, og det i sig selv ville være en pain at lave om, så må man følge med i terminalen hehe.
                    // En løsning kunne have været et andet grid, eller popupvinduer med beskeder. Jeg tager den smådovne rute her.
                    if (txtline.startsWith("BOARD")){// kommentering i tidligere opgave
                        String[] piece = txtline.split(" ");//split som i den anden opgave
                        String board = piece[piece.length -1];// tager den sidste i arrayet, altså boardet.
                        updateBoard(board);// kalder metoden nedenunder
                    }
                } 
            } catch (Exception ex){
                System.out.println("fejl");
            }
        });
        t.start();
    }


    private void updateBoard(String board){// her gennegåes knapper i et loop og sættes til enten "værdien" eller en tom brik. stort set det samme som i tidligere Ttt opgave
        for (int i= 0; i < 9 ; i++){
            char c= board.charAt(i);
            if (c == '.'){
                btn[i].setText(""); // men i stedet for at printe boardet, sætter vi knappen fra GUI til værdi eller tom brik

            }else{
                btn[i].setText(String.valueOf(c)); 
            }
        }
    }

    public static void main(String[] args){// samme som TempConv gui
        TicTacToe_gui window = new TicTacToe_gui();

        window.setTitle("TicTacToe_gui");
        window.setSize(500, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        
        }
}