import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TempConv extends JFrame implements ActionListener{
    public JButton button1;
    public JButton button2;
    public JButton button3;
    public JButton button4;
    public JTextField text1;
    public JTextField text2;
    public JTextField text3;
    public JLabel label1;
    public JLabel label2;
    public JLabel label3;
    // alle mine knapper
    

    public TempConv(){


        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(4,3));// bruger toolbar for kontrol over grid

        // i hindsight, ville jeg nok hellere have brugt et andet grid, men nu er der en række der er lidt tom udover en clear knap

        button1 = new JButton("Fra Celcius");
        text1 = new JTextField(" ");
        button2 = new JButton("Fra Fahrenheit ");
        text2 = new JTextField("");
        button3 = new JButton("Fra Kelvin");
        text3 = new JTextField(" ");
        button4 = new JButton("Clear");

        label1 = new JLabel("((F, K) <-- C), i Celcious -> ");
        label2= new JLabel("((C, K) <-- F), i Fahrenheit->");
        label3 = new JLabel("((C, F) <-- K), i Kelvin -> ");
        
        
       
    
        toolbar.add(button1);
        button1.addActionListener(this);// bruger (this), metoden
        toolbar.add(label1);
        toolbar.add(text1);
        
        toolbar.add(button2);
        button2.addActionListener(this);
        toolbar.add(label2);
        toolbar.add(text2);
        toolbar.add(button3);
        button3.addActionListener(this);
        toolbar.add(label3);
        toolbar.add(text3);
        toolbar.add(button4);
        button4.addActionListener(this);


        


        getContentPane().add(toolbar); // sætter det hele i mit grid gennem toolbar

       
        //konstruktør her. de grafiske elementer
    }
    public void actionPerformed(ActionEvent e){
        //Kode der håndterer events 
        // når knap "1", trykkes med udregninger fra cel til fahrenheit og k. Herunder fra string til double og tilbag igen. Lidt tosset, gælder for alle knapper.
        if (e.getSource()==button1){
            try{
              String txt = text1.getText(); 
              Double d_txt = Double.valueOf(txt);
              Double fahr = (d_txt*(1.8)+32.0);
              Double kel = (d_txt+273.15);
              String s_fahr = Double.toString(fahr);
              String s_kel = Double.toString(kel);
              text2.setText(s_fahr);
              text3.setText(s_kel);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Forkert knap, prøv igen"); // dialog pop-up, hvis forker knap bruges ( mest for ikke at gøre det alt for indviklet)

            }
        } if (e.getSource()==button2){
            try{
                String txt = text2.getText(); 
                Double d_txt = Double.valueOf(txt);
                Double cel = ((d_txt-32)/1.8);
                Double kel = (((d_txt-32)/1.8)+273.15);
                String s_cel = Double.toString(cel);
                String s_kel = Double.toString(kel);
                text1.setText(s_cel);
                text3.setText(s_kel);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Forkert knap, prøv igen");
            }

        }
        if (e.getSource()==button3){
            try{
                String txt = text3.getText(); 
                Double d_txt = Double.valueOf(txt);
                Double cel = (d_txt-273.15);
                Double fahr= ((d_txt-273.15)*1.8+32);
                String s_cel = Double.toString(cel);
                String s_fahr = Double.toString(fahr);
                text1.setText(s_cel);
                text2.setText(s_fahr);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Forkert knap, prøv igen");
            }
        }
        if (e.getSource()==button4){//min clear knap, så man ikke selv skal slette
            text1.setText("");
            text2.setText("");
            text3.setText("");
        }
    }
    public static void main (String[] args){// min main, med windows setup.
        TempConv window = new TempConv();

        window.setTitle("Temperatur Converter");
        window.setSize(500, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

    }

}