import java.io.File;
import java.util.Scanner;

public class wordcounter{
    public static void main(String args[]){
    
        try{
    
        File myFile = new File("C:\\Users\\n" + //
                        "icol\\OneDrive\\Skrivebord\\DTU\\Fra Python til Java\\Øvelse_1_2\\lorem.txt");// indlæser pathen ( har efterfølgende brugt myFile.exists() for at sikre den findes)
        Scanner fil_in = new Scanner(myFile); 
        int count=0; 
        while (fil_in.hasNextLine()){
            String txtline = fil_in.nextLine();// gemmer nextline i stringen txtline
            
            String[] sætning = txtline.split("\\.");// Laver et array, hvorefter jeg splitter ved "\\.", den omvendte backslash er apparently pga noget der hedder Regex (gpt). Den printer et tomt array, hvis jeg bare bruge "."
            for (int i = 0; i<sætning.length;i++) { // for-løkke der går ignnem sætningen
                String trim= sætning[i].trim();// trim er fundet på nettet for at undgå at empty-spaces tæller med i count
                String[] words = trim.split(" "); //splitter sætningen i ord, og sætter i array
                
                count += words.length; // tager længden af array og tilføjer til min count.
                }
            }   
        
        fil_in.close();
        System.out.println(count);//printer 1234
        
        
        }catch (Exception e){
            System.out.println("fejl");
        }
    }

}