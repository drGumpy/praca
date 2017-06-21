package Zapis;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
 
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;


//in progerss
class IrValue {
    double emissivity;
    int distance;
    double[] paternValue;
}

//information about calibrated devices
class device{
    String model;
    String type;
    String producent;
    String resolution_t;
    String resolution_Rh;
    String[] channel;
    public String toString(){
        return type+"\t"+model+"\t"+producent;
    }
}

//information about probes
class probe{
    String model="-";
    String type="-";
    String producent="-";
    public String toString(){
        return model+"\t"+type+"\t"+producent;
    }
}
 
class client{
    String name;
    String address;
    String postal_code;
    String place;
    public String toString(){
        return address+", "+postal_code+" "+place;
    }
}

//information required to certificate
class certificate{
    String num;
    client user= new client();
    client declarant= new client();
    device device = new device();
    String device_serial;
    probe probe= new probe();
    String[] probe_serial =null;
    String probe_ser;
    String calibration_date;
    String calibration_code;
    IrValue pyrometer;
    
    public String toString(){
        String s= "numer świadectwa: "+num+"\n";
        s+="Zgłaszający:\n";
        s+=declarant+"\n";
        s+="Użytkownik:\n";
        s+=user+"\n";
        s+="Urządzenie:\n";
        s+=device+"\t"+device_serial+"\n";
        if(!probe_serial.equals("")){
            s+="Sonda:\n";
            s+=probe+"\t"+probe_serial+"\n";
        }
        s+="kod wzorcowania:\t"+calibration_code+"\n";
        s+="data wzorcowania:\t"+calibration_date;
        return s;
    }
}

public class certificateData {
    
    static types typ = new types();
    private static File file= new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium kopia.ods");
    
    //certificates to do
    private static ArrayList<certificate> data = new ArrayList<certificate>();
        
    //clients data base
    private static HashMap<String, client> clients_data =new HashMap<String, client>();
    
    //probes data base
    private static HashMap<String, probe> probes_data =new HashMap<String, probe>();
    
    //devices data base
    private static HashMap<String, device> devices_data =new HashMap<String, device>();
    
    //Finding not issued certificates  - no calibration date
    private static void order() throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Zlecenia");
        int d=1;
        //first row without calibration date
        while(sheet.getValueAt(2,d)!="") d++;
        while(sheet.getValueAt(5,d)!=""){
            if(sheet.getValueAt(2,d)==""){
            	certificate order = new certificate();
            	order.num = sheet.getValueAt(1,d).toString();
            	order.declarant.name= sheet.getValueAt(3,d).toString();
            	clients_data.put(order.declarant.name, order.declarant);
            	order.user.name= sheet.getValueAt(4,d).toString();
            	clients_data.put(order.user.name, order.user);
            	order.device.model= sheet.getValueAt(5,d).toString();
            	order.device_serial= sheet.getValueAt(6,d).toString();
            	order.probe.model= sheet.getValueAt(7,d).toString();
            	String probe =sheet.getValueAt(8,d).toString();
            	order.probe_ser=probe;
            	if(probe.equals(",")){
            		String[] arr = {""};
            		order.probe_serial = arr;
            	}else{
            		probe=probe.replaceAll("\\s+", "");
            		order.probe_serial= probe.split(",");
            	}
            	order.calibration_code=sheet.getValueAt(9,d).toString();
            	order.calibration_date=sheet.getValueAt(10,d).toString();
            	devices_data.put(order.device.model, order.device);
            	probes_data.put(order.probe.model, order.probe);
            	data.add(order);
            }
            d++;
        }
    }
    
    //find information about clients, who ordered calibration
    private static void find_client_data() throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Klienci");
        int i=0;
        String name;
        while(sheet.getValueAt(0,i)!=""){
            name = sheet.getValueAt(0,i).toString();
            if(clients_data.containsKey(name)){
                client andrzej = new client();
                andrzej.name=name;
                andrzej.address= sheet.getValueAt(1,i).toString();
                andrzej.postal_code= sheet.getValueAt(2,i).toString();
                andrzej.place= sheet.getValueAt(3,i).toString();
                clients_data.put(andrzej.name, andrzej);
            }
            i++;
        }        
    }
    
    //find information about calibrated devices
    private static void find_device_data() throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Urządzenia");
        int i=0;
        String model;
        while(sheet.getValueAt(0,i)!=""){
            model = sheet.getValueAt(0,i).toString();
            if(devices_data.containsKey(model)){
                device nunczaku = new device();
                nunczaku.model= model;
                nunczaku.type= sheet.getValueAt(1,i).toString();
                nunczaku.producent= sheet.getValueAt(2,i).toString();
                nunczaku.resolution_t= sheet.getValueAt(4,i).toString();
                nunczaku.resolution_Rh= sheet.getValueAt(7,i).toString();
                String chanel = sheet.getValueAt(11,i).toString();
                if(chanel.equals(",")){
                    String[] arr = {""};
                    nunczaku.channel = arr;
                }
                else
                    nunczaku.channel = chanel.split(",");
                devices_data.put(nunczaku.model, nunczaku);
            }
            i++;
        }
    }
    
    //find information about used probes
    private static void find_probe_data() throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Sondy");
        int i=0;
        String model;
        while(sheet.getValueAt(0,i)!=""){
            model = sheet.getValueAt(0,i).toString();
            if(probes_data.containsKey(model)){
                probe nunczaku = new probe();
                nunczaku.model=model;
                nunczaku.type= sheet.getValueAt(1,i).toString();
                nunczaku.producent= sheet.getValueAt(2,i).toString();
                probes_data.put(nunczaku.model, nunczaku);
            }
            i++;
        }
    }
    
    //put data to one certificate class
    private static void uzupełnij(){
        certificate c= new certificate();
        for (int i=0; i<data.size(); i++){
            c=data.get(i);
            c.declarant = clients_data.get(data.get(i).declarant.name);
            c.user = clients_data.get(data.get(i).user.name);
            c.device = devices_data.get(data.get(i).device.model);
            if(probes_data.containsKey(data.get(i).probe.model)){
                c.probe = probes_data.get(data.get(i).probe.model);
            }
            else
                c.probe=new probe();
            data.set(i, c);
        }
    }
    
    // in progerss
    static ArrayList<certificate> get_data(boolean Ir, int point){
    //    verification.certificate(data, Ir, point);
        return data;
    }
    
    //return Lists with information about calibration
    static ArrayList<certificate> get_data(){
        //    verification.certificate(data, Ir, point);
            return data;
        }
    
    //set file with information about calibration
    static void set_file(File _file){
        file=_file;
    }
    
    //print data about calibration, unused 
    static void print(){
        for(int i=0; i<data.size(); i++){
            System.out.println("nr"+data.get(i).num+"\nzgłaszający:");
            System.out.println(data.get(i).declarant +"\nużytkownik:");
            System.out.println(data.get(i).user +"\nurządzenie:");
            System.out.println(data.get(i).device + "\tnr. "+data.get(i).device_serial+"\nsonda:");
            System.out.println(data.get(i).probe + "\tnr. "+data.get(i).probe_serial[0]+"\nkod:");
            System.out.println(data.get(i).calibration_code +"\ndata:");
            System.out.println(data.get(i).calibration_date +"\n");
        }
    }
    
    //run fining data
    static void run() throws IOException{
        order();
        find_client_data();
        find_device_data();
        find_probe_data();
        uzupełnij();
    }
    
    public static void main(String[] args) throws IOException{
        order();
        find_client_data();
        find_device_data();
        find_probe_data();
        uzupełnij();
        print();
        
        System.out.println("dzień dobry");
    }
}