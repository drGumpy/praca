package Zapis;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
 
class data_probe{
    boolean question=true;
    int value_t;
    int value_Rh;
    double correction_t;
    double correction_Rh;
    double uncertainty_t;
    double uncertainty_Rh;
    double drift_t;
    double drift_Rh;
    
    public String toString(){
        String s;
        s=value_t+"\t\t";
        s+=correction_t+"\t";
        s+=uncertainty_t+"\t";
        s+=drift_t+"\t";
        try {
            s+="\n";
            s+=value_Rh+"\t\t";
            s+=correction_Rh+"\t";
            s+=uncertainty_Rh+"\t";
            s+=drift_Rh+"\t";
        }catch (NullPointerException e){
            
        }
        return s;
    }
}
 
abstract class patern_probe{
    protected int n;
    protected int range_num;
    protected data_probe[] data;
    protected double drift_t, drift_Rh;
    protected int[][] ranges;
    void print(){
        System.out.println("odnaleźione punkty:");
        for(int i=0; i<data.length; i++){
            System.out.println(data[i]);
        }
        System.out.println("zakresy pomiarowe");
        for(int i=0; i<ranges.length; i++){
            for(int j=0; j<ranges[i].length; j++)
                System.out.print(ranges[i][j]+" ");
            System.out.println("");
        }
    }
    data_probe get(int t, int Rh){
        return new data_probe();
    }
}
 
class Rh_probe extends patern_probe {
    //data_probe[] data;
    //zbieranie danych o wzorcu
    Rh_probe(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
        n=sc.nextInt();
        drift_t = sc.nextDouble();
        drift_Rh = sc.nextDouble();
        data = new data_probe[n];
        for(int i=0; i<n; i++){
            data[i] = new data_probe();
            data[i].value_t= Integer.parseInt(sc.next());
            data[i].value_Rh=sc.nextInt();
            data[i].correction_t=sc.nextDouble();
            data[i].correction_Rh=sc.nextDouble();
            data[i].uncertainty_t=sc.nextDouble();
            data[i].uncertainty_Rh=sc.nextDouble();
            data[i].drift_t=drift_t;
            data[i].drift_Rh=drift_Rh;
        }
        //zakresy pomiarowe
        range_num=sc.nextInt();
        ranges = new int[range_num][4];
        // t_min, t_max, Rh_min, Rh_max
        for(int i=0; i<range_num; i++){
            for(int j=0; j<4; j++){
                ranges[i][j]=sc.nextInt();
            }
        }
        sc.close();
    }
    
    //dane o sondzie dla konkretnego punktu t, Rh
    data_probe get(int t, int Rh){
        for(int i=0; i<n; i++){
            if(data[i].value_t ==t && data[i].value_Rh==Rh)
                return data[i];
        }
        for(int i=0; i<range_num; i++){
            if(ranges[i][0]<=t && ranges[i][1]>=t){
                if(ranges[i][2]<=Rh && ranges[i][3]>=Rh){
                    return find(ranges[i], t, Rh);
                }
            }
        }
        data_probe s=new data_probe();
        s.question=false;
        return s;
    }
    
    //Przeszukiwanie zakresów pomiarowych
    private data_probe find(int[] range, int t, int rh) {
        data_probe sol = new data_probe();
        //sprawdzenie zakresów
        if(t==range[0] || t==range[1]){
            sol=easy_calculate_t(t, rh, range[2], range[3]);
            sol.value_t=t;
            sol.value_Rh=rh;
            return sol;
        }
        if(rh==range[2] || rh==range[3]){
            sol=easy_calculate_rh(t, rh, range[0], range[1]);
            sol.value_t=t;
            sol.value_Rh=rh;
            return sol;
        }
        data_probe d1 = null, d2=null, d3=null, d4=null;
        int b=0;
        
        for(int i=0; i<n; i++){
            if(data[i].value_t ==range[0] && data[i].value_Rh==range[2]){
                d1=data[i];
                b++;
                continue;
            }
            if(data[i].value_t ==range[1] && data[i].value_Rh==range[2]){
                d2=data[i];
                b++;
                continue;
            }
            if(data[i].value_t ==range[0] && data[i].value_Rh==range[3]){
                d3=data[i];
                b++;
                continue;
            }
            if(data[i].value_t ==range[1] && data[i].value_Rh==range[3]){
                d4=data[i];
                b++;
                continue;
            }
            if(b==4) break;
        }
        if(b<4){
            sol.question=false;
            return sol;
        }
 
        // wyliczenia dla punktu pomiarowego
        double t_correction= (t-range[0])/(range[1]-range[0]);
        double Rh_correction = (rh-range[2])/(range[3]-range[2]);
        sol.value_Rh=rh;
        sol.value_t=t;
        sol.correction_Rh = metrologyMath.calculate(t_correction, Rh_correction, d1.correction_Rh, d2.correction_Rh,
                d3.correction_Rh, d4.correction_Rh);
        sol.correction_t = metrologyMath.calculate(t_correction, Rh_correction, d1.correction_t, d2.correction_t,
                d3.correction_t, d4.correction_t);
        sol.drift_Rh = drift_Rh;
        sol.drift_t = drift_t;
        sol.uncertainty_Rh= Math.max(Math.max(d1.uncertainty_Rh, d2.uncertainty_Rh),
                Math.max(d3.uncertainty_Rh, d3.uncertainty_Rh));
        sol.uncertainty_t = Math.max(Math.max(d1.uncertainty_t, d2.uncertainty_t),
                Math.max(d3.uncertainty_t, d3.uncertainty_t));
        return sol;
    }
 
    private data_probe easy_calculate_rh(int t, int rh, int t1, int t2){
        data_probe d1 = null, d2=null;
        int b=0;
        for(int i=0; i<n; i++){
            if(data[i].value_t ==t1 && data[i].value_Rh==rh){
                d1=data[i];
                b++;
                continue;
            }
            if(data[i].value_t ==t2 && data[i].value_Rh==rh){
                d2=data[i];
                b++;
                continue;
            }            
            if(b==2) break;
        }
        double cor= (t-t1)/(t2-t1);
        return metrologyMath.easy_calculate(cor, d1, d2);
    }
 
    private data_probe easy_calculate_t(int t, int rh, int rh1, int rh2) {
        data_probe d1 = null, d2=null;
        int b=0;
        for(int i=0; i<n; i++){
            if(data[i].value_t ==t && data[i].value_Rh==rh1){
                d1=data[i];
                b++;
                continue;
            }
            if(data[i].value_t ==t && data[i].value_Rh==rh2){
                d2=data[i];
                b++;
                continue;
            }            
            if(b==2) break;
        }
        double cor= (rh-rh1)/(rh2-rh1);
        return metrologyMath.easy_calculate(cor, d1, d2);
    }
}
 
class t_probe extends patern_probe {
    //zbieranie danych o wzorcu
    t_probe(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
        n=sc.nextInt();
        drift_t = sc.nextDouble();
        data = new data_probe[n];
        sc.nextLine();
        for(int i=0; i<n; i++){
            data[i] =new data_probe();
            data[i].value_t=sc.nextInt();
            data[i].correction_t=sc.nextDouble();
            data[i].uncertainty_t=sc.nextDouble();
            data[i].drift_t=drift_t;
        }
        //zakresy pomiarowe
        range_num=sc.nextInt();
        ranges = new int[range_num][2];
        for(int i=0; i<range_num; i++){
            for(int j=0; j<2; j++){
                ranges[i][j]=sc.nextInt();
            }
        }
        sc.close();
    }
    
    //dane o sondzie dla konkretnego punktu t, Rh
    data_probe get(int t, int Rh){
        for(int i=0; i<n; i++){
            if(data[i].value_t ==t)
                return data[i];
        }
        for(int i=0; i<range_num; i++){
            if(ranges[i][0]<=t && ranges[i][1]>=t){
                    return find(ranges[i], t);
            }
        }
        data_probe s=new data_probe();
        s.question=false;
        return s;
    }
    
    //Przeszukiwanie zakresów pomiarowych
    private data_probe find(int[] range, int t) {
        data_probe d1 = null, d2=null, sol = new data_probe();
        int b=0;
        
        for(int i=0; i<n; i++){
            if(data[i].value_t ==range[0]){
                d1=data[i];
                b++;
                continue;
            }
            if(data[i].value_t ==range[1]){
                d2=data[i];
                b++;
                continue;
            }
            if(b==2) break;
        }
        if(b<2){
            sol.question=false;
            return sol;
        }
        // wyliczenia dla punktu pomiarowego
        double t_correction= (t-range[0])/(range[1]-range[0]);
        sol.value_t=t;
        sol.correction_t = metrologyMath.calculate(t_correction, d1.correction_t, d2.correction_t);
        sol.drift_t = drift_t;
        sol.uncertainty_t = Math.max(d1.uncertainty_t, d2.uncertainty_t);
        return sol;
    }    
}
 
 
 
