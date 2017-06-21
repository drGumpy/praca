package Zapis;
 
import java.io.File;
 
class data{
    int number=0;
    File file=null;
    boolean RH= true;
    int num=0;
    String date="";
    String time="";
    String temp="";
    String hum="";
    //ustalanie rodzaju wzorcowania t/ t/Rh
    data(boolean RH){
        this.RH=RH;
    }
    //ustalenie wartości nr. przyrządu na liście i plik z danymi
    void set(int number, File file){
        this.number= number;
        this.file= file;
    }
    //podzielenie linii na dane
    public data divide(String nextLine) {
        return new data(RH);
    }
    // liczba linii bez danych liczbowych
    public int get_n() {
        return 0;
    }
    public String toString(){
        return temp+"\t"+hum;
    }
}
 
class onset extends data{
    onset(boolean RH) {
        super(RH);
    }
    
    public int get_n(){
        return 3;
    }
    
    public data divide(String line){
        String[] data = line.split(";");
        String[] when =data[1].split(" ");
        
        data d= new data(RH);
        d.num =Integer.parseInt(data[0]);
        
        String[] linedate = when[0].split("\\.");
        
        d.date = linedate[1]+"."+linedate[0]+".20"+linedate[2];
        
        d.temp = data[2];
        
        if(RH) d.hum = data[3];
        
        String[] linetime = when[1].split(":");
        d.time = linetime[0]+":"+linetime[1];
        return d;
    }
}
 
class testo extends data{
    testo(boolean RH) {
        super(RH);
    }
    
    public int get_n(){
        return 2;
    }
 
    public data divide(String line){
        String[] data = line.split(";");
        String[] when =data[1].split(" ");
        
        data d= new data(RH);
        d.num =Integer.parseInt(data[0]);
        
        String[] linedate = when[0].split("-");
        d.date = linedate[2]+"."+linedate[1]+"."+linedate[0];
        
        String[] linetime = when[1].split(":");
         d.time = linetime[0]+":"+linetime[1];
         String[] tempdata=data[2].split(",");
        if(tempdata.length>1)
            d.temp = data[2];
        else
            d.temp =tempdata[0]+",0";
        if(RH){
            String[] humdata=data[3].split(",");
            if(humdata.length>1)
                d.hum = data[3];
            else
                d.hum =humdata[0]+",0";        
        }
        return d;
    }
}
 
class CEM extends data{
    CEM(boolean RH) {
        super(RH);
    }
    
    public int get_n(){
        return 11;
    }
 
    public data divide(String line){
        String[] data = line.split("\t");
        String[] when =data[5].split("/");
        data d= new data(RH);
        d.num =Integer.parseInt(data[0]);
        
         String[] linedate = when[0].split("-");
         d.date = linedate[0]+"."+linedate[1]+".20"+linedate[2];
         
         String[] linetime = when[1].split(":");
         d.time = linetime[0]+":"+linetime[1];
         
         String[] tempdata = data[1].split(" ");
         String[] tempdatafin= tempdata[3].split("\\.");
         if(tempdatafin.length>1)
             d.temp =tempdatafin[0]+","+tempdatafin[1];
         else
             d.temp =tempdatafin[0]+",0";
         
         if(RH){
             String[] humdata = data[3].split(" ");
             String[] humdatafin = humdata[3].split("\\.");
             if(humdatafin.length>1)
                 d.hum = humdatafin[0]+","+humdatafin[1];
             else
                 d.hum = humdatafin[0]+",0";
         }
         
         return d;    
    }
}
 
class RC extends data{
    RC(boolean RH) {
        super(RH);
    }
    
    public int get_n(){
        return 23;
    }
 
    public data divide(String line){
        String[] data = line.split("\t");
        if(data.length<2)
            System.out.println("error:"+ line);
        String[] when =data[2].split(" ");
        data d= new data(RH);
        d.num =Integer.parseInt(data[0]);
        
         String[] linedate = when[0].split("-");
         d.date = linedate[2]+"."+linedate[1]+"."+linedate[0];
         
         String[] linetime = when[1].split(":");
         d.time = linetime[0]+":"+linetime[1];
         
         String[] tempdata=data[4].split("\\.");
        if(tempdata.length>1)
            d.temp = tempdata[0]+","+tempdata[1];
        else
            d.temp =tempdata[0]+",0";
         
         if(RH){
             String[] humdata=data[6].split("\\.");
            if(humdata.length>1)
                d.hum = humdata[0]+","+humdata[1];
            else
                d.hum =humdata[0]+",0";
         }
         
         return d;    
    }
}
 
class Lascar extends data{
    Lascar(boolean RH) {
        super(RH);
    }
    
    public int get_n(){
        return 1;
    }
 
    public data divide(String line){
        String[] data = line.split(",");
        if(data.length<2)
            System.out.println("error:"+ line);
        String[] when =data[1].split(" ");
        data d= new data(RH);
        d.num =Integer.parseInt(data[0]);
        
         String[] linedate = when[0].split("-");
         d.date = linedate[2]+"."+linedate[1]+"."+linedate[0];
         
         String[] linetime = when[1].split(":");
         d.time = linetime[0]+":"+linetime[1];
         
         d.temp= data[2].replace(".", ",");
         
         if(RH){
             d.hum= data[3].replace(".", ",");
         }
         
         return d;    
    }
}
 
class EBI extends data{
    EBI(boolean RH) {
        super(RH);
    }
    
    public int get_n(){
        return 10;
    }
 
    public data divide(String line){
        String[] data = line.split(",");
        String[] when =data[0].split(" ");
        
        data d= new data(RH);
        
        String[] linedate = when[0].split("-");
        if(linedate.length<2)
            System.out.println(linedate[0]);
        d.date = linedate[2]+"."+linedate[1]+"."+linedate[0];
        if(when.length<2)
            d.time="00:00";
        else{
            String[] linetime = when[1].split(":");
            d.time = linetime[0]+":"+linetime[1];
        }
         String[] tempdata;
         
         if(RH){
             String[] humdata=data[1].split("\\.");
            if(humdata.length>1)
                d.hum = humdata[0]+","+humdata[1];
            else
                d.hum =humdata[0]+",0";
            tempdata=data[2].split("\\.");
            if(tempdata.length>1)
                d.temp = tempdata[0]+","+tempdata[1];
            else
                d.temp =tempdata[0]+",0";
         }else{
             tempdata=data[1].split("\\.");
             if(tempdata.length>1)
                 d.temp = tempdata[0]+","+tempdata[1];
             else
                 d.temp =tempdata[0]+",0";
         }
         
         return d;    
    }
}
 
// dane na temat wzorcowania
class types{
    public File file;
    public String path;
    public int points;
    public boolean RH;
    String Sheetname;
    public String probe;
    int datacol;
    int timecol;
    int gaps;
    int startdata;
    int gap=1;
    int devicenum;
    // ustalenie arkusza docelowego
    void Filesset(File file){
        this.file=file;
    }
    // ustalenie liczby punktów pomiarowyc
    void pointset(int points){
        this.points=points;
    }
    void probeset(String probe){
        this.probe=probe;
    }
    // wprowadzenie danych na temat wzorcowania i arkusza
    void dataset(boolean a){
        this.RH=a;
        if(a){ //arkusz "Zapiska Temp & RH"
            datacol=34;
            timecol=3;
            gaps= 45;
            startdata =10;
            gap=2;
            Sheetname = "Zapiska Temp & RH";
            probe = "61602551";
            devicenum=21;
        }else{    //arkusz "Zapiska temp."
            datacol=33;
            timecol=2;
            gaps= 34;
            startdata=9;
            Sheetname = "Zapiska temp.";
            probe = "20098288";
            devicenum=31;
        }
    }
}
 
 
 
 
public class function {
    static File file;
    static data[] punkty;
    static String path;
}
