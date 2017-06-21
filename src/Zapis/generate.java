package Zapis;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
 
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
 
class certificate_value{
    String probe_t;
    String probe_Rh;
    String device_t;
    String device_Rh;
    String error_t;
    String error_Rh;
    String uncertainty_t;
    String uncertainty_Rh;
}
 
public class generate {
    private int point;
    private dev patern;
    private ArrayList<dev> devices;
    private boolean Rh;
    private File note, cal;
    private  String note_path, cal_path;
    private  chamber_data[] data;
    private  data_probe[] data_probe;
    
    private ArrayList<String> done = new ArrayList<String>();
    
    private String[] environment;
    
    private void generate_cal(ArrayList<certificate_value> data,certificate type) throws IOException{
    //    System.out.println("genetrowanie...");
        boolean sw2 = false;
        File file =cal;
        if(!Rh){
            if(type.calibration_code.equals("SW2")){
                sw2=true;
                file= new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\sw_tx2.ods");
                }
            if(type.declarant.name.equals("Quintiles Poland Sp. z o.o.") && type.device.model.equals("810-210")){
                sw2=true;
                file= new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\sw_tx2.ods");
            }
        }
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Świadectwo wzorcowania");
        int col;
        if(Rh){
            sheet.setValueAt(new Date( ), 9 , 13);
            sheet.setValueAt(new Date( ), 9 , 70);
            sheet.setValueAt(type.num, 24 , 13);
            sheet.setValueAt(type.num, 24 , 70);
            col=13;
        }else{
            sheet.setValueAt(new Date( ), 8 , 13);
            sheet.setValueAt(new Date( ), 8 , 70);
            sheet.setValueAt(type.num, 22 , 13);
            sheet.setValueAt(type.num, 22 , 70);
            col=12;
        }
        String name =String.format("%s, model: %s, producent: %s, nr seryjny: %s",
                type.device.type, type.device.model, type.device.producent,type.device_serial);
        if(!type.probe_serial[0].equals("")){
            if(type.probe.type.equals(""))
                name+=String.format(", z %s, nr seryjny: %s.",
                        type.probe.model, type.probe_ser);
            else
                name+=String.format(", z %s model %s, producent: %s, nr seryjny: %s.",
                    type.probe.type,type.probe.model , type.probe.producent, type.probe_ser);
            }
        else
            name+=".";
        sheet.setValueAt(name, col , 16);
        sheet.setValueAt(type.declarant.name, col , 20);
        sheet.setValueAt(type.declarant, col , 21);
        sheet.setValueAt(type.user.name, col , 23);
        sheet.setValueAt(type.user, col , 24);
        sheet.setValueAt("Temperatura: "+environment[0], col , 30);
        sheet.setValueAt("Wilgotność: "+environment[1], col , 31);
        sheet.setValueAt(type.calibration_date, col , 33);
        
        if(Rh){
            int line=84;
            int lenght= data.size();
            if(point>5)
                lenght=5;
            for(int i=0; i<lenght; i++){
                sheet.setValueAt(data.get(i).probe_t, 3, line);
                sheet.setValueAt(data.get(i).probe_Rh, 8, line);
                sheet.setValueAt(data.get(i).device_t, 13, line);
                sheet.setValueAt(data.get(i).device_Rh, 18, line);
                sheet.setValueAt(data.get(i).error_t, 23, line);
                sheet.setValueAt(data.get(i).error_Rh, 28, line);
                sheet.setValueAt(data.get(i).uncertainty_t, 33, line);
                sheet.setValueAt(data.get(i).uncertainty_Rh, 38, line);
                line+=2;
            }
            if(point<5){
                for(int i=0; i<5-lenght; i++){
                    sheet.setValueAt("", 3, line);
                    sheet.setValueAt("", 8, line);
                    sheet.setValueAt("", 13, line);
                    sheet.setValueAt("", 18, line);
                    sheet.setValueAt("", 23, line);
                    sheet.setValueAt("", 28, line);
                    sheet.setValueAt("", 33, line);
                    sheet.setValueAt("", 38, line);
                    line+=2;
                }
            }
        }else{
            int max =1;
            if(sw2){
                max=2;
            }
            int line=76;
            int points=data.size();
            int lenght;
            String channel;
            for(int j=0; j<max; j++){
                channel="";
                if(type.probe_serial.length>j)
                    channel = find(type.probe_serial[j],type.device.channel[j]);
                else
                    channel = find("",type.device.channel[j]);
                if(!channel.equals(""))
                    sheet.setValueAt(channel, 3, line);
                line+=8;
                lenght = points-3*j;
                if(lenght>3)
                    lenght=3;
                for(int i=3*j; i<3*j+lenght; i++){
                    sheet.setValueAt(data.get(i).probe_t, 3, line);
                    sheet.setValueAt(data.get(i).device_t, 12, line);
                    sheet.setValueAt(data.get(i).error_t, 21, line);
                    sheet.setValueAt(data.get(i).uncertainty_t, 30, line);
                    line+=2;
                }
                if(lenght<3){
                    for(int i=0; i<3*j-lenght; i++){
                        sheet.setValueAt("", 3, line);
                        sheet.setValueAt("", 12, line);
                        sheet.setValueAt("", 21, line);
                        sheet.setValueAt("", 30, line);
                        line+=2;
                    }
                }
                line+=1;
            }
        }
        name = cal_path+type.num+"_"+type.declarant.name + ".ods";
        sheet.getSpreadSheet().saveAs(new File(name));       
    }
 
    private String find(String probe_serial, String chanel) {
        String val ="";
        
        if(chanel.equals(""))
            return "";
        else{
            val="Czujnik temperatury: ";
            if(!probe_serial.equals("")){
                val +=probe_serial+" ";
            }
            val+= "(nazwa kanału: "+chanel+")";
        }
        
        return val;
    }
 
    private void generate_doc(dev device, certificate type){
        
        point = device.average_t.length;
        ArrayList<certificate_value> cdata = new ArrayList<certificate_value>();
        try {
            final Sheet sheet = SpreadSheet.createFromFile(note).getSheet("Wyniki wzorcowania");
            for(int i=0; i<point; i++){
                if(device.q[i] || !data_probe[i].question)
                    continue;
                certificate_value val= new certificate_value();
                int line = i*32+3;
                sheet.setValueAt(type.num, 3 , line);
                sheet.setValueAt(type.calibration_code, 8 , line);
                sheet.setValueAt(type.calibration_date, 13 , line);
                sheet.setValueAt(environment[0], 3 , line+1);
                sheet.setValueAt(environment[1], 7 , line+1);
                sheet.setValueAt(type.device.type, 3 , line+3);
                sheet.setValueAt(type.device.model, 3 , line+6);
                sheet.setValueAt(type.probe.model, 4 , line+6);
                sheet.setValueAt(type.device.channel[0], 3 , line+8);
                sheet.setValueAt(type.device_serial, 3 , line+9);
                if(i<3)
                    sheet.setValueAt(type.probe_serial[0], 4 , line+9);
                else if(type.probe_serial.length>1)
                    sheet.setValueAt(type.probe_serial[1], 4 , line+9);
                sheet.setValueAt(type.device.producent, 3 , line+11);
                sheet.setValueAt(type.probe.producent, 4 , line+11);
                sheet.setValueAt(type.device.resolution_t, 3 , line+13);
                if(Rh)
                    sheet.setValueAt(type.device.resolution_Rh, 4 , line+13);
                for(int j=0; j<10; j++){
                //    System.out.println(patern.time[i]);
                    sheet.setValueAt(metrologyMath.time(patern.time[i], j), 0 , line+17+j);
                    sheet.setValueAt(patern.data_t[i][j], 1 , line+17+j);
                    sheet.setValueAt(device.data_t[i][j], 3 , line+17+j);
                    if(Rh){
                        sheet.setValueAt(patern.data_Rh[i][j], 2 , line+17+j);
                        if(device.average_Rh[i]==-1)
                            sheet.setValueAt("-", 4 , line+17+j);
                        else
                            sheet.setValueAt(device.data_Rh[i][j], 4 , line+17+j);
                    }
                }
                
                double[] unc_t= new double[8];
                unc_t[0]=device.standard_t[i];
                unc_t[1]=Double.parseDouble(type.device.resolution_t)/Math.sqrt(3);
                unc_t[2]=patern.standard_t[i];
                unc_t[3]=0.01/Math.sqrt(3);
                unc_t[4]=data_probe[i].uncertainty_t/2;
                unc_t[5]=data_probe[i].drift_t/Math.sqrt(3);
                unc_t[6]=data[i].t1/Math.sqrt(3);
                unc_t[7]=data[i].t2/2;
 
                sheet.setValueAt(device.average_t[i], 7 , line+5);
                sheet.setValueAt(patern.average_t[i], 7 , line+7);
                sheet.setValueAt(data_probe[i].correction_t, 7 , line+9);
                sheet.setValueAt(type.device.resolution_t, 9 , line+6);
                sheet.setValueAt(data_probe[i].uncertainty_t, 9, line+9);
                sheet.setValueAt(data_probe[i].drift_t, 9, line+10);
                sheet.setValueAt(data[i].t1, 9, line+11);
                sheet.setValueAt(data[i].t2, 9, line+12);
                for(int j=0; j<unc_t.length; j++){
                    sheet.setValueAt(unc_t[j], 13, line+5+j);
                }
                double unc =metrologyMath.uncertainty(unc_t);
                double round = metrologyMath.find_round(2*unc, Double.parseDouble(type.device.resolution_t));
                if(Rh){
                    if(round<0.1)
                        round=0.1;
                }
                double pt=metrologyMath.round_d(patern.average_t[i]+data_probe[i].correction_t,round);
                double div =metrologyMath.round_d(device.average_t[i],round);
                val.probe_t= metrologyMath.round(pt,round).replace(".", ",");
                val.device_t = metrologyMath.round(div,round).replace(".", ",");
                val.error_t = metrologyMath.round(div-pt,round).replace(".", ",");
                val.uncertainty_t = metrologyMath.round(2*unc,round).replace(".", ",");
                
                if(Rh){
                    sheet.setValueAt(val.probe_t, 5, line+14);
                    sheet.setValueAt(val.device_t, 7, line+14);
                    sheet.setValueAt(val.error_t, 9, line+14);
                    sheet.setValueAt(val.uncertainty_t, 13, line+14);
                    double[] unc_Rh= new double[8];
                    unc_Rh[0]=device.standard_Rh[i];
                    unc_Rh[1]=Double.parseDouble(type.device.resolution_Rh)/Math.sqrt(3);
                    unc_Rh[2]=patern.standard_Rh[i];
                    unc_Rh[3]=0.01/Math.sqrt(3);
                    unc_Rh[4]=data_probe[i].uncertainty_Rh/2;
                    unc_Rh[5]=data_probe[i].drift_Rh/Math.sqrt(3);
                    unc_Rh[6]=data[i].Rh1/Math.sqrt(3);
                    unc_Rh[7]=data[i].Rh2/2;
                    
                    line+=13;
                    sheet.setValueAt(device.average_Rh[i], 7 , line+5);
                    sheet.setValueAt(patern.average_Rh[i], 7 , line+7);
                    sheet.setValueAt(data_probe[i].correction_Rh, 7 , line+9);
                    sheet.setValueAt(type.device.resolution_Rh, 9 , line+6);
                    sheet.setValueAt(data_probe[i].uncertainty_Rh, 9, line+9);
                    sheet.setValueAt(data_probe[i].drift_Rh, 9, line+10);
                    sheet.setValueAt(data[i].Rh1, 9, line+11);
                    sheet.setValueAt(data[i].Rh2, 9, line+12);
                    for(int j=0; j<unc_Rh.length; j++){
                        sheet.setValueAt(unc_Rh[j], 13, line+5+j);
                    }
                    if(device.average_Rh[i]==-1){
                        val.probe_Rh= "-";
                        val.device_Rh = "-";
                        val.error_Rh = "-";
                        val.uncertainty_Rh = "-";
                    }else{
                    unc =metrologyMath.uncertainty(unc_Rh);
                    round = metrologyMath.find_round(2*unc, Double.parseDouble(type.device.resolution_Rh));
                    pt=metrologyMath.round_d(patern.average_Rh[i]+data_probe[i].correction_Rh,round);
                    div =metrologyMath.round_d(device.average_Rh[i],round);
                        val.probe_Rh= metrologyMath.round(pt,round).replace(".", ",");
                        val.device_Rh = metrologyMath.round(div,round).replace(".", ",");
                        val.error_Rh = metrologyMath.round(div-pt,round).replace(".", ",");
                        val.uncertainty_Rh = metrologyMath.round(2*unc,round).replace(".", ",");
                    }
                    sheet.setValueAt(val.probe_Rh, 5, line+14);
                    sheet.setValueAt(val.device_Rh, 7, line+14);
                    sheet.setValueAt(val.error_Rh, 9, line+14);
                    sheet.setValueAt(val.uncertainty_Rh, 13, line+14);
                }else{
                    sheet.setValueAt(val.probe_t, 5, line+17);
                    sheet.setValueAt(val.device_t, 7, line+17);
                    sheet.setValueAt(val.error_t, 9, line+17);
                    sheet.setValueAt(val.uncertainty_t, 13, line+17);
                }
                cdata.add(val);
            }
            String name = note_path+type.num+"_"+type.device.model + ".ods";
            sheet.getSpreadSheet().saveAs(new File(name));
            generate_cal(cdata,type);
            done.add(type.num);
        } catch (IOException e) {}
    }
    
    private void find_data(){
        if(patern.average_Rh!=null){
            Rh = true;
            note = new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\z_Rh.ods");
            cal = new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\sw_Rh.ods");
        }else{
            Rh = false;
            note = new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\z_t.ods");
            cal = new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\sw_t.ods");
        }
    }
    
    void put_patern(dev patern){
        this.patern= patern;
    }
    void put_environment(String[] environment){
        this.environment = environment;
    }
    
    void put_device(ArrayList<dev> devices){
        this.devices = devices;
    }
    
    void put_chamber(chamber_data[] data){
        this.data = data;
    }
    
    void put_paths(String note_path, String cal_path){
        this.note_path = note_path;
        this.cal_path = cal_path;
    }
    
    void put_data_probe(data_probe[] data_probe){
        this.data_probe=data_probe;
    }
    
    void run(ArrayList <certificate> data){
        int n=data.size();
        find_data();
        for(int i=0; i<n; i++){
            if(devices.size()==0) break;
            String name = data.get(i).device_serial;
       //    System.out.println("szukam: "+name);
            for(int j=0; j<devices.size(); j++){
       //         System.out.println("mam "+devices.get(j).name);
                if(devices.get(j).name.equals(name)){
                    generate_doc(devices.get(j), data.get(i));
                    devices.remove(j);
                    break;
                }
            }
        }
    }
 
    public ArrayList<String> get_done() {
        return done;
    }
}