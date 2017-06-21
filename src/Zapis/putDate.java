package Zapis;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
 
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
 
public class putDate {
    static File file;
    
    static void put_file(File _file){
        file=_file;
    }
    
    static void date(ArrayList<String> done) throws IOException{
         final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Zlecenia");
         int d=0;
         if(done.size()>1)
             d=Integer.parseInt(done.get(0));
         else
             return;
         int n=0;
         Date date = new Date();
         while(n<done.size()){
             if(sheet.getValueAt(1, d).toString().equals(done.get(n))){
                 sheet.setValueAt(date, 2, d);
                 n++;
             }
             d++;
         }
         sheet.getSpreadSheet().saveAs(file);
    }
    
}