package Zapis;
 
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
 
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
 
@SuppressWarnings("serial")
public class console extends JFrame {
    
    static ArrayList<certificate> data = new ArrayList<certificate>();
    static ArrayList<data> cal_point = new ArrayList<data>();
    
    private data_probe[] data_probe;
    
    private ArrayList<dev> devices = new ArrayList<dev>();
    private ArrayList<data> point = new ArrayList<data>();    
    private dev patern = new dev();
    
    private chamber_data[] chamber_data;
    
    private static boolean check = true;
    private static boolean Rh = false;
    private static boolean Ir = false;
    private static int points=3;
    
    private static JTextField
     sheet = new JTextField(50),
     certificate = new JTextField(50),
     notes = new JTextField(50);
    
    private static AbstractButton t, rh, ir;
    
    private static JComboBox<Integer> pointsbox = new JComboBox<Integer>();
    
    private static JButton generation, clientdata;
    
    private static JTextField[] environment = new JTextField[4];
    
    private static double enviroment_condition[] = {22.0, 22.0, 45.0, 45.0};
    
    private static JPanel environ(){
        JPanel jp = new JPanel();
        jp.setPreferredSize(new Dimension(400, 80));
        jp.setBorder(new TitledBorder("Warunki środowiskowe"));
        String[] title = {"t min    ","t max   ","Rh min ","Rh max"};
        for(int i=0; i<4;i++){
            environment[i] = new JTextField(10);
            environment[i].setName(title[i]);
            if(i<2)
                environment[i].setText("22.000");
            else
                environment[i].setText("45.000");
            jp.add(environment[i]);
            JLabel label= new JLabel();
            label.setText(title[i]);
            jp.add(label);
        }
        
        environment[0].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String data = environment[0].getText().replace(",", ".");
                if(!metrologyMath.validate(data))
                    environment[0].setText("22.000");
                else{
                    String data2 = environment[1].getText();
                    if(Double.parseDouble(data)>Double.parseDouble(data2))
                        environment[0].setText("22.000");
                    else{
                        enviroment_condition[0]= Double.parseDouble(data);
                        environment[0].setText(data);
                        }
                }
            }
            
        });
        
        environment[1].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String data = environment[1].getText().replace(",", ".");
                if(!metrologyMath.validate(data))
                    environment[1].setText("22.000");
                else{
                    String data2 = environment[0].getText();
                    if(Double.parseDouble(data)<Double.parseDouble(data2))
                        environment[1].setText("22.000");
                    else{
                        enviroment_condition[1]= Double.parseDouble(data);
                        environment[1].setText(data);
                    }
                }
            }
            
        });
        
        environment[2].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String data = environment[2].getText().replace(",", ".");
                if(!metrologyMath.validate(data))
                    environment[2].setText("45.000");
                else{
                    String data2 = environment[3].getText();
                    if(Double.parseDouble(data)>Double.parseDouble(data2))
                        environment[2].setText("45.000");
                    else{
                        enviroment_condition[2]= Double.parseDouble(data);
                        environment[2].setText(data);
                    }
                }
            }
            
        });
        
        environment[3].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String data = environment[3].getText().replace(",", ".");
                if(!metrologyMath.validate(data))
                    environment[3].setText("45.000");
                else{
                    String data2 = environment[2].getText();
                    if(Double.parseDouble(data)<Double.parseDouble(data2))
                        environment[3].setText("45.000");
                    else{
                        enviroment_condition[3]= Double.parseDouble(data);
                        environment[3].setText(data);
                    }
                }
            }            
        });
        return jp;
    }
    
    //wybór pliku  roboczego
    private static JPanel sheet(){
        JButton b= new JButton("zmień");
        JPanel jp = new JPanel();
        jp.setBorder(new TitledBorder("Arkusz z danymi"));
        jp.add(b);
        sheet.setText("C:\\Users\\Laboratorium\\Desktop\\Laboratorium.ods");
        sheet.setEditable(false);
        jp.add(sheet);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.showOpenDialog(c);
                sheet.setText(c.getSelectedFile().toString());    
            }
        });
        return jp;    
    }
    
    //wybór miejsca zapisu zapisek z wzorcowania
    private static JPanel notes(){
        JButton b= new JButton("zmień");
        JPanel jp = new JPanel();
        jp.setBorder(new TitledBorder("Folder zapisu zapisek z wzorcowania"));
        jp.add(b);
        //notes.setText("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\Wyniki wzorcowań\\Zapiski\\");
        notes.setText("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\Nowy folder\\zapiski\\");
        notes.setEditable(false);
        jp.add(notes);
        b.addActionListener(new ActionListener(){    
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.showOpenDialog(c);
                notes.setText(c.getCurrentDirectory().toString());        
            }
        });
        return jp;    
    }
    
    //wybór miejsca zapisu świadectw wzorcowania
    private static JPanel certificate(){
        JButton b= new JButton("zmień");
        JPanel jp = new JPanel();
        jp.setBorder(new TitledBorder("Folder zapisu świadectw wzorcowania"));
        jp.add(b);
        //certificate.setText("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\Wyniki wzorcowań\\Świadectwa wzorcowania\\");
        certificate.setText("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\Nowy folder\\świadectwa\\");
        certificate.setEditable(false);
        jp.add(certificate);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.showOpenDialog(c);
                certificate.setText(c.getCurrentDirectory().toString());        
            }
        });
        return jp;    
    }
    
    //rodzaj wykonywanego wzorcowania
    private static JPanel calibrationtype() {
         //akcja bl = new akcja();
        ButtonGroup bg = new ButtonGroup();
        JPanel jp = new JPanel();
        String title = "ilośc punktów pomiarowych i rodzaj wzorcowania";
        title = title.substring(title.lastIndexOf('.') + 1);
        jp.setBorder(new TitledBorder(title));
        
        t  = new JRadioButton("temperatura");
        rh = new JRadioButton("temperatura i wilgotność");
        ir = new JRadioButton("pirometry");
 
        for(int i = 1; i < 7; i++)
               pointsbox.addItem(i);
        
        bg.add(t);
        t.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(check){
                    Rh= false;
                    Ir=false;
                    points=3;
                    pointsbox.setSelectedIndex(2);
                }
            }
        });
        t.setSelected(true);
        bg.add(rh);
        rh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(check){
                    Rh= true;
                    Ir=false;
                    points=5;
                    pointsbox.setSelectedIndex(4);
                }
            }
        });
        bg.add(ir);
        ir.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(check){
                    Rh= false;
                    Ir=true;
                    points=3;
                    pointsbox.setSelectedIndex(2);
                }
            }
        });
        
        jp.add(pointsbox);
        pointsbox.setSelectedIndex(2);
        pointsbox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            points= (int) pointsbox.getSelectedItem();
            }
        });
        jp.add(t);
        jp.add(rh);
        jp.add(ir);
        return jp;
    }
    
    //elementy gui
    public console() {
        JButton dattalogger= new JButton("dane o wzorcowaniu");
        clientdata= new JButton("wybierz zlecenia");
        generation= new JButton("generuj świadetwa");
        dattalogger.addActionListener(new ActionListener(){    
            public void actionPerformed(ActionEvent e) {
                long startTime = System.currentTimeMillis();
                if(!Ir){
                    File file = new File(sheet.getText());
                    putData.set(Rh, file ,points);
                    try {
                        cal_point=putData.get_points();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    putData.run();
                    putData.clean();
                    check=false;
                    pointsbox.setEnabled(false);
                    t.setEnabled(false);
                    rh.setEnabled(false);
                    ir.setEnabled(false);
                    clientdata.setEnabled(true);
                    long endTime   = System.currentTimeMillis();
                    System.out.println(" w czasie: " +(endTime - startTime)/1000.0+" s");
                }else{
                    System.out.println("brak danych do wprowadzenia");
                }
            }
        });
        setLayout(new FlowLayout());
    //    clientdata.setEnabled(false);
        clientdata.addActionListener(new ActionListener(){    
            public void actionPerformed(ActionEvent e) {
                long startTime = System.currentTimeMillis();
                File file = new File(sheet.getText());
                certificateData.set_file(file);
                try {
                    certificateData.run();
                    data=certificateData.get_data();
                    if(Ir){
                        
                    }
                } catch (IOException e1) {}
                try {
                    getData.set_data(Rh);
                    getData.set_file(file);
                    devices=getData.find_data(points);
                    patern=getData.get_patern();
                    point=getData.get_point();
                } catch (IOException e1) {}
                try {
                    data_probe = new data_probe[point.size()];
                    patern_probe probe;
                    if(Rh)
                        probe= new Rh_probe(new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\61602551.txt"));
                    else
                        probe= new t_probe(new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\13.026.txt"));
                    for(int i=0; i<point.size(); i++){
                        int t=Integer.parseInt(point.get(i).temp);
                        int rh=0;
                        if(Rh)
                            rh=Integer.parseInt(point.get(i).hum);
                        data_probe[i]=probe.get(t, rh);
                    }
                } catch (IOException e1) {}
                
                chamber cham= new chamber();
                cham.start(Rh);
                cham.get_points(point);
                chamber_data=cham.get();
                generation.setEnabled(true);
                long endTime   = System.currentTimeMillis();
                System.out.println("czas: " +(endTime - startTime)/1000.0 + " s");
                
            }
        });
        generation.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                generate make = new generate();
                make.put_chamber(chamber_data);
                make.put_data_probe(data_probe);
                make.put_device(devices);
                make.put_patern(patern);
                make.put_paths(notes.getText(), certificate.getText());
                environment d= new environment();
                make.put_environment(d.calculate_data(enviroment_condition));
                make.run(data);
                try {
                    File file = new File(sheet.getText());
                    putDate.put_file(file);
                    putDate.date(make.get_done());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
 
                System.out.println("koniec wprowadzania");
            }
        });
        generation.setEnabled(false);
        add(calibrationtype());
        add(environ());
        add(sheet());
        add(certificate());
        add(notes());
        add(dattalogger);
        add(clientdata);
        add(generation);
    }
    
    public void run(){
        SwingUtilities.invokeLater(new Runnable(){
            console f = new console();
            public void run(){
                f.setTitle("wydawanie świadectw dla biedaków");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(700,350);
                f.setVisible(true);
            }
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            console f = new console();
            public void run(){
                f.setTitle("wydawanie świadectw dla biedaków");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(700,500);
                f.setVisible(true);
            }
        });
        
        
    }
}