import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class FirePaaStribe{
    
    static char[] board = new char[42];
    static int[] boardPreference = {3,4,2,1,5,0,6}; // intialisering af mine boardpreferences
    static char playercolor;
    static char servercolor;
    static int col;
    static boolean ServerAktiv = true; // endt med at lave den static for at stoppe serveren
    
//#####################################################################################################################################

// Jeg har lavet opgaven selv, Nicolai Søgaard, s234146

// Starten på spillet, Game-loop, og Servertaktik

    private static void StartGame(Scanner netin, PrintWriter netout){
        resetboard();// resetboard findes i bunden af dokumentet i Boardsektionen. Sætter alle brikker til '.'
        
        netout.println("WELCOME TO FOUR-IN-A-ROW"); 
        netout.println("SELECT BLACK OR WHITE");
        
        netout.flush();

        if (netin.hasNextLine()){
            String txtline = netin.nextLine().trim();
                        
            if (txtline.endsWith("BLACK")){// giver farver ud fra valget klienten laver
                playercolor = ('B');
                servercolor =('W');
                Gameloop(netin,netout,false);// sætter klientfirst til false/true efter hvad kleinten vælger. Bruges til at styre hvor gameloopet starter 
            }

                
            else if (txtline.endsWith("WHITE")){
                //System.out.println(txtline);
                playercolor = ('W');
                servercolor = ('B');
                Gameloop(netin,netout,true);
            }
            else{
                System.out.println("Systemerror");
            }
         }
    }

    private static void Gameloop(Scanner netin, PrintWriter netout, boolean ClientFirst){

        boolean turn = ClientFirst;

        
        while (true){
        if (turn){
            boolean ClientOK =(ClientReader(netin,netout));// tjekker om boardet er okay, klientens svar er okay. i funktionen ses der at der tjekkes for at valget er inde for parametrende. skal gå igennem steppet igen hvis der er fejl. derfor continue
            if (!ClientOK){
                continue;
            }
            boolean BoardOK=(Board(netout,col,playercolor));// her er tjekket for at colonnen er fuld. derfor samme fremgangsmåde. Oprindeligt tiltænkt til at ha sammenhæng med en draw også men endte i noget andet
            if(!BoardOK){
                continue;
            }
            
            klientBoard(netin, netout);// sender boardet tilbage i string som klienten forventer.
            if (GameWon(playercolor)){ // 
                
                netout.println("PLAYER WINS");
                netout.flush();
                servarClose();// kalder luk serveren der gør min serveraktiv falsk
                return;
            }
            if (draw()){// draw funktion og samme fremgang 
                
                netout.println("NOBODY WINS");
                netout.flush();
                servarClose();
                return;
            }
            
            
            
        } else {

            int serverCol = GameTactic(); // tager colonnen som min gametaktik vælger
            Board(netout,serverCol, servercolor); // herfra det samme stortset. bortset fra den ikke tjekker board. serveren burde vælge rigtigt
            klientBoard(netin, netout);
            if (GameWon(servercolor)){
                
                netout.println("SERVER WINS");
                netout.flush();
                servarClose();
                return;
            }
            if (draw()){
                
                netout.println("NOBODY WINS");
                netout.flush();
                servarClose();
                return;
            }
            
        }
        turn=!turn;// sørger for at turen bliver vendt og at den uanset hvad skifter hver runde og ikke går 1-2-2-1-1-2-2-1
        }
       }

       // herfra kaldes funktionerne ud fra prioritering. Den er faktisk endt med at være så god at jeg ikke selv kan vinde længere lol.
       // den tjekker først om den selv kan få 4 på stribe, ellers blokerer den 4 på stribe, så 3 på stribe- block osvosv. indtil earlygame som sidste udvej
    private static int GameTactic(){
        int Four = FourInARow(servercolor);
        if (Four != -1){
           return Four;
        }
        int Fourblock = FourInARow(playercolor);
        if (Fourblock != -1){
           return Fourblock;
        }

        int Three = ThreeInARow(servercolor);
        if (Three != -1){
            return Three;    
        }
        int Threeblock = ThreeInARow(playercolor);
        if (Threeblock != -1){
            return Threeblock;
        }
        int Two = TwoInARow();
        if (Two != -1){
            return Two;
        }


        // den her og earlygame var det jeg brugte hele første dag på stort set.
        // i hindsight, brugte jeg alt for lang tid på det her. dette er i virkeligheden første træk lavet ud fra om midten er ledig. Endte medblive måden je gkunne få den til at printe noget på 
        for (int i=0;i<boardPreference.length;i++){ 
            int preference = boardPreference[i];
            if (EarlyGame(preference) != -1){ // kigger min boardets preferencer kronologisk igennem, er de fri returneres den preference
                return preference;
                
            }
        }return -1;
        
    }

    //#####################################################################################################################################

    // Metoder: Earlygame, To, tre, og fire på stribe. Dertil mit gamewon - check





     private static int EarlyGame(int col){
        for (int i = 5; i >= 5;i--){ // kun til beslutningstagen i starten af spillet. Tager kun den nedereste række.
            int index = i * 7+ col;
            if (board[index]=='.'){
                return col;
                //System.out.println("det her er mit print index: " + index);
            }else{
                return -1;
            }
        } 
        return -1;
    }



// herfra ligner funktionerne hianden med udvidede boundaries. kommenterer derfor bare two in a row
    public static int TwoInARow(){


        for ( int col = 0; col <7;col++)// alle 7 kolonner skal gåes igennem 
        {

        int landingSpot = -1;// egentlig tiltænkt til at virke som en draw til at starte med, men endte med at lave en funktion til dette i et søvnigt moment. derfor stadig sådan her
        int savedRow = -1; // Den row der kigges på 
        
        for (int i = 5; i >= 0; i--){ // laver boardet
            int index = i * 7 + col;

            if (board[index]=='.'){// hvis index er frit
                landingSpot = index;
                savedRow = i;
                break;
                }
        } 
        if (landingSpot == -1){// igen bruges ikke rigtigt, havde store planer
            continue;
        }

        boolean match =// boundaries og parameter for et match lavet som bool. horisontalt, vertikalt og diagonal. Mest afgrænsningen der var en hovedpine her. især i FourInARow
            (savedRow < 5 && board[landingSpot + 7] == servercolor) || 
            (col > 0 && board[landingSpot - 1] == servercolor) || 
            (col < 6 && board[landingSpot + 1] == servercolor) ||
            (col < 6 && savedRow < 5 && board[landingSpot + 8]  == servercolor)|| 
            (col > 0 && savedRow > 5 && board[landingSpot + 6] == servercolor);   

            if (match){// hvis der er mulighed for 2 på stribe, return kolonnen
                return col;
            }
        }return -1;
    }


    public static int ThreeInARow(char color){

        for ( int col = 0; col <7;col++)// kigger alle kolonner igennem
        {

        int landingSpot = -1;
        int savedRow = -1;
        
        for (int row = 5; row >= 0; row--){
            int index = row * 7+ col;

            if (board[index]=='.'){
                landingSpot = index;
                savedRow = row;
                break;
                }
        } 
        if (landingSpot == -1){
            continue;
        }

        boolean match =

            // horisontal til venstre
            (col > 1 && board[landingSpot - 1] == color && board[landingSpot - 2] == color) || 
            
            // horisontal til højre
            (col < 5 && board[landingSpot + 1] == color && board[landingSpot + 2] == color) ||

            //split
            (col > 0 && col < 6 && board[landingSpot + 1] == color && board[landingSpot -1] == color) || 

            // vertikalt 
            (savedRow < 4 && board[landingSpot + 7] == color && board[landingSpot + 14] == color)|| 

            // diagonal til venstre
            (col > 1 && savedRow < 4 && board[landingSpot + 6]  == color && board[landingSpot + 12] == color) || 

            // diagonal til højre
            (col < 5 && savedRow < 4 && board[landingSpot + 8]  == color && board[landingSpot + 16]== color);   

            if (match){
                return col;
            }
        }return -1;
    }
    



    public static int FourInARow(char color){

        for ( int col = 0; col <7;col++){   // kigger alle kolonner igennem
        

        int landingSpot = -1;
        int savedRow = -1;
        
        
        for (int row = 5; row >= 0; row--){
            int index = row * 7+ col;

            if (board[index]=='.'){
                landingSpot = index;
                savedRow = row;

                break;
                }
        } 
        if (landingSpot == -1){
            continue;
        }

        boolean match =
            (savedRow < 3 && board[landingSpot + 7] == color && board[landingSpot + 14] == color && board[landingSpot + 21] == color)|| 
            (col > 2 && board[landingSpot - 1] == color && board[landingSpot - 2] == color && board[landingSpot - 3] == color ) || 
            (col < 4 && board[landingSpot + 1] == color && board[landingSpot + 2] == color && board[landingSpot + 3] == color) || 
            (col < 5 && col > 1 && board[landingSpot + 1] == color && board[landingSpot -1] == color && (board[landingSpot + 2] == color ||board[landingSpot -2] == color ))|| 
            (col > 2 && savedRow < 3 && savedRow < 5 && board[landingSpot + 6]  == color && board[landingSpot + 12] == color && board[landingSpot + 18] == color)|| 
            (col < 4 && savedRow < 3 && board[landingSpot + 8]  == color && board[landingSpot + 16]== color&& board[landingSpot + 24]== color);  // kigger videre ned i gennem arrayet, til venstre 6 tabellen til højre 8 tabellen 

            if (match){
                return col;
                
            }
        }return -1;
    }
        
    
    private static boolean GameWon(char color){  // samme ide som min two/threeInARow funktioner

        
        for ( int col = 0; col <7;col++){ 
            for (int row = 0; row < 6; row++){// her nøjes jeg med at tage spillepladen ovenfra, logisk det rigtige
                int i = row * 7+ col;
                if (board[i]!=color){// gjort så den kan bruges både med server og playercolor 
                    continue;
                }

                // det her er den horisontale, lidt hardcodet, men ved faktisk ikke helt hvad man ellers kan gøre
                if (col < 4 && board [i]==color && board [i+1]== color && board [i+2]== color && board [i+3]== color){
                    return true;
                }
                // den vertikale, row 
                if (row < 3 && board[i]== color && board[i+7]==color && board[i+14]== color && board[i+21]== color){
                    
                    return true;
                    
                }//de to diagonale retninger
                if ( row < 3 && col < 4 && board[i]==color && board[i+8]==color && board[i+16]==color && board[i+24]==color ){
                    
                    return true;
                }
                if ( row < 3 && col > 2 && board[i]==color && board[i+6]==color && board[i+12]==color && board[i+18]==color ){
                    
                    return true;
                }

            }
        } return false;


    }






//#####################################################################################################################################


// CLientreader, Server og Board-funktioner.
    
    
    private  static boolean ClientReader(Scanner netin, PrintWriter netout){
        
            netout.println("YOUR TURN"); // loopet der trigger klientens promt for tal
            netout.flush();
            if (netin.hasNextLine()){
                String ServerInt =netin.nextLine().trim();

                try{
                    col = (Integer.parseInt(ServerInt)-1); // parseIn fundet på nettet, ligeså med exception, men tror det er den rigtige.
                }catch(NumberFormatException e){    
                    netout.print("ILLEGAL INPUT: SEND SINGLE DIGIT (1-7) FOR MOVE"); // Den her tager klienten selv tror jeg?
                    netout.flush();
                    return false;

                }
                if (col < 0  ||  col > 6){// skal være mellem 0-6
                    netout.println("ILLEGAL INPUT: COLLUMN DOES NOT EXIST");// samme her, tror ikke serveren har den her trigger nogen steder? 
                    netout.flush();
                    return false;
                    
                } 

            }return true;
        }
            
    private static void resetboard(){// sætter det til et tomt '.'-board
        for (int i = 0; i < board.length; i++){
            board[i]='.';
        }
    }

    private static boolean Board(PrintWriter netout, int col, char color){ 
            
        for (int i= 0; i<1; i++){// tjekker kun øverste række
            int fullindex = i*7 + col;
                if (board[fullindex]!='.'){
                    netout.println("ILLEGAL MOVE: COLLUMN IS FULL");
                    netout.flush();
                    return false;
                }
        
        }
        // tager den nedefra og tjekker om laveste er fri først. Havde egentlig gået igennem fra toppen, men blev for udpenslet og grimt, løsningen her sikrer den nedereste i kolonnen tages først
        
        for (int i = 5; i >= 0;i--){ // mine 6 rækker
            int index = i * 7+ col; // mine 7 kolonner + spilerens valg af kolonne i rækken
            if (board[index]=='.'){ // hvis den er leding
                board[index]= color; // sætter den til farven på spiller/server
                return true; // returnerer true
            

            }
        }return false;
    
    }

    private static boolean draw(){//egentlig bare en invers af min collumn full, dog gjort som bool
        for (int row = 0; row < 1; col++){
            if (board[row]=='.'){
                return false;
            }
        }return true;
    }


    private static void klientBoard(Scanner netin,PrintWriter netout){ // nødvendigt onde for at serveren får den som string

        String stringBoard = "BOARD IS ";
        for (int i = 0; i < 42 ; i++){
            stringBoard += board[i];
        }
        netout.println(stringBoard);
        
        netout.flush();
        

    }
    private static void servarClose(){// ja...
        ServerAktiv = false;
    }



    
    private static void ServerAktiv(){// serversocket taget fra de andre opgaver. igen virker uden sock.close()?

            try (ServerSocket serversock = new ServerSocket(1069)){ 
                    System.out.println("Lytter på port 1069");

                while(ServerAktiv){ 
                        Socket sock = serversock.accept();
                        System.out.println("Connected"); 

                        Scanner netin = new Scanner(sock.getInputStream());
                        PrintWriter netout = new PrintWriter(sock.getOutputStream());

                        StartGame(netin, netout);
                }
            }catch (Exception e){
                    System.out.println("Kunne ikke oprette forbindelse");
                }
            }

//#####################################################################################################################################


        public static void main(String args[]){
            ServerAktiv();
            
        }
}

//#############################################################

// herunder er min pseudokode jeg startede med at lave. synes den var sjov at have med. kom ret godt i mål

// 1 klient sender color, server skal være modsat

// 2 hvid starter - derfor IF-statement. Herfra Board is - Your turn. Det som klienten og robotten forventer

// Ha en reader på input fra klient, sammensæt til board

/*Den hårde del her:
    BOARDET:
    Skal have 7 collumns 6 rækker.
        
    prompt til klient 1-7 ( hvis de starter, en ting af gangen)
        vælges de ikke "illeagal"
    vælges 1, skal brik i første collumn: 
        Logik (måske?):
            loop af if i != null ( eller . eller whatever)
                 illegal move

                else [i+7] != null
                    i = color of player
                else if [i+14] != null
                    [i+7] = color of player osv

                alternativt start nedefra?



    Dertil den helt hårde: (tror jeg starter med at lave en hvor jeg selv skriver fra server hvis muligt) // Robotten laver et random move, ved ikke helt om det skal være så indviklet. måske lave den intelligent til et punkt og så ikke mere.
        logikken for serverens "taktik". Mål at slå robotten!
        Forstiller mig noget 7 tabellen i den lodrette
            if i += 7 = hvid/sort
                priotriter
        horisontale:
            if i+1 = hvid/sort
                prioriter
        skå
            if i i+8 or i+6 or i-6 or i-8 = sort/hvid
                prioriter
            // kan godt være den bliver lige lovlig grov her
        dertil til alle
            hvis 3 på stribe
                kig efter 4. det er en masse if statements i krydsretningen ved den skrå, så i begge ender af den horistontale og bare i collumn i den vertikale.

        tænker måske et if statement til hvis der ikke er mulighed for 2 sammenhængende mindst:
            så random choice
        
        Dertil hvis pladen er fyldt
            Nobody wins
            luk forbindelsen


        Hvis der er 4 på stribe
            Skal tjekkes efter 8* runde
                i kryds/vertikal/horisontal
                    Logik der er småsvær i sig selv

            player/klient wins
            luk forbindelsen
        

tænker gameplan:

    kommunikation mellem klient og server
        bare start med 1-7
    logik på boardet numerisk 
        Herefter server-taktik
    Sidst visualisering af board i ascii // sernere funde ud af det ikke er nødvendit


    
                


*/


