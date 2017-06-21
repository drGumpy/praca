package Zapis;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
 
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
 
class dev{
    int num;
    boolean[] q;
    String[] time;
    double[][] data_t;
    double[][] data_Rh;
    double[] standard_t;
    double[] standard_Rh;
    double[] average_t;
    double[] average_Rh;
    String name;
    public String toString(){
        String s="-------- \n";
        s=name+"\n";
        for(int i=0; i<data_t.length;i++){
            for(int j=0; j<data_t[i].length; j++)
                s+=data_t[i][j]+" ";
            s+="\t"+average_t[i]+"\t"+standard_t[i]+"\n";}
        try {
            for(int i=0; i<data_Rh.length;i++){
                for(int j=0; j<data_t[i].length; j++)
                    s+=data_Rh[i][j]+" ";
                s+="\t"+average_Rh[i]+"\t"+standard_Rh[i]+"\n";}
        }catch (NullPointerException e) {
            s+="brak danych dla wilgotności\n";
        }
        s+="-------- \n";
        return s;
    }
}
 
public class getData {
    static private ArrayList<dev> devices = new ArrayList<dev>();
    static private ArrayList<data> point = new ArrayList<data>();
    
    static private dev patern = new dev();
    
    static private types typ = new types();
    
    static void set_data(boolean Rh){
        typ.dataset(Rh);    
    }
    
    static void set_file(File  add_file){
        typ.Filesset(add_file);
    }
    
    private static void find_probe_data(int points) throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(typ.file).getSheet(typ.Sheetname);
        patern.time = new String[points];
        patern.data_t= new double[points][10];
        patern.standard_t= new double[points];
        patern.average_t= new double[points];
        if(typ.RH){
            patern.data_Rh= new double[points][10];
            patern.standard_Rh= new double[points];
            patern.average_Rh= new double[points];
        }
        patern.name="wzorzec";
        int line = typ.startdata-typ.gap;
        for(int j=0; j<points; j++){
            int col=typ.timecol;
            for(int k=0; k<10; k++){
                patern.time[j] =metrologyMath.parseTime(
                        sheet.getValueAt(typ.timecol, line-2).toString());
                String a, b;
                a=sheet.getValueAt(col,line).toString();
                b=sheet.getValueAt(col+2,line).toString();
                patern.data_t[j][k]=Double.parseDouble(a+"."+b);
                if(typ.RH){
                    a=sheet.getValueAt(col,line+1).toString();
                    b=sheet.getValueAt(col+2,line+1).toString();
                    patern.data_Rh[j][k]=Double.parseDouble(a+"."+b);
                }
                col+=3;
            }
            patern.standard_t[j] = metrologyMath.standard_deviation(patern.data_t[j]);
            patern.average_t[j] = metrologyMath.average(patern.data_t[j]);
            if(typ.RH){
                patern.standard_Rh[j] = metrologyMath.standard_deviation(patern.data_Rh[j]);
                patern.average_Rh[j] = metrologyMath.average(patern.data_Rh[j]);
            }
            line+=typ.gaps;
        }
        //System.out.println(patern);
    }
    
    static dev get_patern() throws IOException{
        find_probe_data(point.size());
        return patern;
    }
    
    static ArrayList<data> get_point(){
        return point;
    }
    
    static ArrayList<dev> find_data(int points) throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(typ.file).getSheet(typ.Sheetname);
        for(int i=0; i<points; i++){
            data add = new data(typ.RH);
            add.time = sheet.getValueAt(typ.timecol,6+typ.gaps*i).toString();
            if(typ.RH){
                add.temp =sheet.getValueAt(0,6+typ.gaps*i).toString();
                add.hum =sheet.getValueAt(1,6+typ.gaps*i).toString();
            }else
                add.temp =sheet.getValueAt(1,6+typ.gaps*i).toString();
            point.add(add);
        }
        for(int i=0; i<typ.devicenum; i++){
            dev device = new dev();
            
            int line = typ.startdata+typ.gap*i;
            device.name= sheet.getValueAt(1,line).toString();
            if(device.name.equals(""))
                continue;
            int col;
            device.num=i;
            device.q= new boolean[points];
            device.data_t= new double[points][10];
            device.standard_t= new double[points];
            device.average_t= new double[points];
            if(typ.RH){
                device.data_Rh= new double[points][10];
                device.standard_Rh= new double[points];
                device.average_Rh= new double[points];
            }
            for(int j=0; j<points; j++){
                col=typ.timecol;
                try{
                    for(int k=0; k<10; k++){
                        String a, b;
                        a=sheet.getValueAt(col,line).toString();
                        b=sheet.getValueAt(col+2,line).toString();
                    //    if(a.equals("")) break;
                        device.data_t[j][k]=Double.parseDouble(a+"."+b);
                        if(typ.RH){
                            a=sheet.getValueAt(col,line+1).toString();
                            if(a.equals("-"))
                                device.data_Rh[j][k]=-1;    
                            else{
                                b=sheet.getValueAt(col+2,line+1).toString();
                                device.data_Rh[j][k]=Double.parseDouble(a+"."+b);
                            }
                        }
                        col+=3;
                    }
                    device.standard_t[j] = metrologyMath.standard_deviation(device.data_t[j]);
                    device.average_t[j] = metrologyMath.average(device.data_t[j]);
                    if(typ.RH){
                        device.standard_Rh[j] = metrologyMath.standard_deviation(device.data_Rh[j]);
                        device.average_Rh[j] = metrologyMath.average(device.data_Rh[j]);
                    }
                }catch(NumberFormatException e){
                    device.q[j]=true;
                }
                line+=typ.gaps;
            }            
            devices.add(device);
        }
        return devices;
    }
    
    static void print(){
        System.out.println("punkty pomiarowe");
        for(int i=0; i<point.size(); i++){
            System.out.println(point.get(i));
        }
        System.out.println("dane z wzorca");
        try{
            System.out.println(patern);
            }catch (NullPointerException e) {
                System.out.println("brak");
            }
        
        System.out.println("urządzenia");
        for(int i=0; i<devices.size(); i++){
             try{
                System.out.println(devices.get(i));
             }catch (NullPointerException e) {
                 System.out.println("brak");
             }
        }
        
    }
}
 
