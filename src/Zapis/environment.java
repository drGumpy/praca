package Zapis;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
 
public class environment {
    private data_probe[] certificate_data= new data_probe[4];
    
    double[] new_data = new double[4];
    
    int[] range = new int[4];
    
    private File doc = new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\w-srod.txt");
    
    private void find(){
        try {
            Scanner sc = new Scanner(doc);
            for(int i=0; i<4; i++){
                certificate_data[i]= new data_probe();
                certificate_data[i].value_t=sc.nextInt();
                certificate_data[i].value_Rh=sc.nextInt();
                certificate_data[i].correction_t=sc.nextDouble();
                certificate_data[i].correction_Rh=sc.nextDouble();
                certificate_data[i].uncertainty_t=sc.nextDouble();
                certificate_data[i].uncertainty_Rh=sc.nextDouble();                
            }
            sc.close();
            range[0] =certificate_data[0].value_t;
            range[1] =certificate_data[3].value_t;
            range[2] =certificate_data[0].value_Rh;
            range[3] =certificate_data[3].value_Rh;
        }catch (FileNotFoundException e){}    
    }
    
    private void find_data(double a, double b, int i){
        double t_correction = (a-range[0])/(range[1]-range[0]);
        double Rh_correction = (b-range[2])/(range[3]-range[2]);
 //      System.out.println("wsp. korekcji t: "+t_correction+" Rh:"+Rh_correction);
        double     c_t= metrologyMath.calculate(t_correction, Rh_correction,
                        certificate_data[0].correction_t, certificate_data[2].correction_t,
                        certificate_data[1].correction_t, certificate_data[3].correction_t),
                c_Rh=metrologyMath.calculate(t_correction, Rh_correction,
                        certificate_data[0].correction_Rh, certificate_data[2].correction_Rh,
                        certificate_data[1].correction_Rh, certificate_data[3].correction_Rh);
//        System.out.println("korekcja t: "+c_t+ " Rh:"+c_Rh);
        
        new_data[0+i]= a + c_t;
        new_data[2+i]= b + c_Rh;
    }
    
    private void corection() {
        double a= Math.max(Math.max(certificate_data[0].uncertainty_t, certificate_data[1].uncertainty_t),
                Math.max(certificate_data[2].uncertainty_t, certificate_data[3].uncertainty_t)),
               b= Math.max(Math.max(certificate_data[0].uncertainty_Rh, certificate_data[1].uncertainty_Rh),
                        Math.max(certificate_data[2].uncertainty_Rh, certificate_data[3].uncertainty_Rh));
        new_data[0]-=a;
        new_data[1]+=a;
        new_data[2]-=b;
        new_data[3]+=b;
        new_data[0]=metrologyMath.round_d(new_data[0], 0.1);
        new_data[1]=metrologyMath.round_d(new_data[1], 0.1);
        new_data[2]=metrologyMath.round_d(new_data[2], 0.1);
        new_data[3]=metrologyMath.round_d(new_data[3], 0.1);
    }
    
    void print(){
        for(int i=0; i<certificate_data.length; i++)
            System.out.println(certificate_data[i]+"\n");
    }
    
    String[] calculate_data(double[] data){
        find();
        //print();
        find_data(data[0], data[2], 0);
        find_data(data[1], data[3], 1);
        corection();
        String[] solution = new String[2];
        solution[0]= "("+new_data[0]+ " ÷ "+new_data[1]+")°C";
        solution[0]= solution[0].replace(".", ",");
        solution[1]= "("+new_data[2]+ " ÷ "+new_data[3]+")%Rh";
        solution[1]= solution[1].replace(".", ",");
        return solution;
    }    
}