package Zapis;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class chamber_data{
    int value_Rh;
    int value_t;
    double t1;
    double t2;
    double Rh1;
    double Rh2;
    
    public String toString(){
        return value_t+"\t"+t1+"\t"+t2+"\t"+value_Rh+"\t"+Rh1+"\t"+Rh2;
    }
}
 
public class chamber {
    private boolean Rh;
    private int[][] ranges;
    private int range_num;
    private int n;
    private ArrayList<chamber_data> standard_point= new ArrayList<chamber_data>();
    private chamber_data[] data;
    
    private void find(File file) throws FileNotFoundException{
        Scanner in= new Scanner(file);
        n= in.nextInt();
        for(int i=0; i<n; i++){
            chamber_data d = new chamber_data();
            d.value_t = in.nextInt();
            if(Rh)
                d.value_Rh= in.nextInt();
            d.t1= in.nextDouble();
            d.t2= in.nextDouble();
            if(Rh){
                d.Rh1= in.nextDouble();
                d.Rh2= in.nextDouble();
            }
            standard_point.add(d);
        }
        n= in.nextInt();
        range_num=n;
        int m;
        if(Rh){
            ranges= new int[n][4];
            m=4;
        }else{
            ranges= new int[n][2];
            m=2;
        }
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                ranges[i][j]= in.nextInt();
            }
        }
        in.close();
    }
 
    private chamber_data get_max(chamber_data d1, chamber_data d2){
        chamber_data d= new chamber_data();
        d.t1 = Math.max(d1.t1, d2.t1);
        d.t2 = Math.max(d1.t2, d2.t2);
        if(Rh){
            d.Rh1 = Math.max(d1.Rh1, d2.Rh1);
            d.Rh2 = Math.max(d1.Rh2, d2.Rh2);
        }        
        return d;
    }
    
    private chamber_data calculate_rh(int t, int rh, int t1, int t2) {
        System.out.println(t+"\t"+rh+"\t"+t1+"\t"+t2);
        chamber_data d1 = null, d2=null, d=null;
        int b=0;
        for(int i=0; i<standard_point.size(); i++){
            System.out.println(i);
            if(standard_point.get(i).value_t ==t1 && standard_point.get(i).value_Rh==rh){
                d1=standard_point.get(i);
                b++;
                continue;
            }
            if(standard_point.get(i).value_t ==t2 && standard_point.get(i).value_Rh==rh){
                d2=standard_point.get(i);
                b++;
                continue;
            }            
            if(b==2) break;
        }
        if(b<2) return new chamber_data();
       // System.out.println(d1);
       // System.out.println(d2);
        d=get_max(d1, d2);
        d.value_t=t;
        d.value_Rh=rh;
        return d;
    }
 
    private chamber_data calculate_t(int t, int rh, int rh1, int rh2) {
        chamber_data d1 = null, d2=null, d=null;
        int b=0;
        for(int i=0; i<n; i++){
            if(standard_point.get(i).value_t ==t && standard_point.get(i).value_Rh==rh1){
                d1=standard_point.get(i);
                b++;
                continue;
            }
            if(standard_point.get(i).value_t ==t && standard_point.get(i).value_Rh==rh2){
                d2=standard_point.get(i);
                b++;
                continue;
            }            
            if(b==2) break;
        }
        if(b<2) return d;
        d=get_max(d1, d2);
        d.value_t=t;
        d.value_Rh=rh;
        return d;
    }
    
    private chamber_data find_point(int[] range, int t, int rh) {
        //sprawdzenie zakresów
        if(t==range[0] || t==range[1]){
            return calculate_t(t, rh, range[2], range[3]);
        }
        if(rh==range[2] || rh==range[3]){
            return calculate_rh(t, rh, range[0], range[1]);
        }
        chamber_data d1 = null, d2=null, d3=null, d4=null, d = new chamber_data();
        int b=0;
        for(int i=0; i<standard_point.size(); i++){
            if(standard_point.get(i).value_t ==range[0] &&
                    standard_point.get(i).value_Rh==range[2]){
                d1=standard_point.get(i);
                b++;
                continue;
            }
            if(standard_point.get(i).value_t ==range[1] &&
                    standard_point.get(i).value_Rh==range[2]){
                d2=standard_point.get(i);
                b++;
                continue;
            }
            if(standard_point.get(i).value_t ==range[0] &&
                    standard_point.get(i).value_Rh==range[3]){
                d3=standard_point.get(i);
                b++;
                continue;
            }
            if(standard_point.get(i).value_t ==range[1] &&
                    standard_point.get(i).value_Rh==range[3]){
                d4=standard_point.get(i);
                b++;
                continue;
            }
            if(b==4) break;
        }
        if(b<4) return d;
        d=get_max(get_max(d1,d2), get_max(d3,d4));
        d.value_t=t;
        d.value_Rh=rh;
        return d;
    }
 
    private chamber_data find_point(int[] range, int t){
        chamber_data d1 = null, d2=null, d=new chamber_data();
        int b=0;
        for(int i=0; i<standard_point.size(); i++){
            if(standard_point.get(i).value_t ==range[0]){
                d1=standard_point.get(i);
                b++;
                continue;
            }
            if(standard_point.get(i).value_t ==range[1]){
                d2=standard_point.get(i);
                b++;
                continue;
            }            
            if(b==2) break;
        }
        if(b<2) return d;
        d=get_max(d1, d2);
        d.value_t=t;
        return d;
    }
    
    private chamber_data find_point_Rh(data point){
        int t = Integer.parseInt(point.temp);
        int Rh = Integer.parseInt(point.hum);
 
        for(int i=0; i<standard_point.size(); i++){
            if(t==standard_point.get(i).value_t
                && Rh == standard_point.get(i).value_Rh){
                    return standard_point.get(i);
                    }
        }
        for(int i=0; i<range_num; i++){
            if(ranges[i][0]<=t && ranges[i][1]>=t){
                if(ranges[i][2]<=Rh && ranges[i][3]>=Rh){
                    return find_point(ranges[i], t, Rh);
                }
            }
        }
        chamber_data d = new chamber_data();
        System.out.println("nic");
        return d;
    }
    
    private chamber_data find_point_t(data point){
        int t = Integer.parseInt(point.temp);
        for(int i=0; i<standard_point.size(); i++){
            if(t==standard_point.get(i).value_t){
                    return standard_point.get(i);
            }
        }
        for(int i=0; i<range_num; i++){
            if(ranges[i][0]<t && ranges[i][1]>t){
                    return find_point(ranges[i], t);
            }
        }
        chamber_data d = new chamber_data();
        return d;
    }    
    
    void get_points(ArrayList<data> points){
        int num= points.size();
        data= new chamber_data[num];
        for(int i=0; i<num; i++){
            if(Rh)
                data[i]=find_point_Rh(points.get(i));
            else
                data[i]=find_point_t(points.get(i));
        }
    }
    
    void start(boolean Rh){
        this.Rh=Rh;
        File file;
        if(Rh)
            file= new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\12-03914 Rh.txt");
        else
            file= new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\12-03914 t.txt");
        try {
            find(file);
        } catch (FileNotFoundException e) {
            System.out.println("zły plik");
            e.printStackTrace();
        }
    }
    
    void print(){
        System.out.println("Punty do ze świadectwa:");
        for(int i=0; i<standard_point.size();i++){
            System.out.println(i+"\t"+standard_point.get(i));
        }
        System.out.println("zakresy:");
        for(int i=0; i<range_num;i++){
            System.out.print(i);
            for(int j=0; j<ranges[i].length; j++)
                System.out.print("\t"+ranges[i][j]);
            System.out.println("");
        }
        try {
            System.out.println("wyniki punktów:");
            for(int i=0; i<data.length;i++){
                System.out.println(i+"\t"+data[i]);
            }
        }catch (NullPointerException e) {
                System.out.println("brak wprowadzonych punktów pomiarowych");
        }
    }
    
    chamber_data[] get(){
        return data;
    }
    
}
