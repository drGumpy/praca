package Zapis;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
 
public class metrologyMath {
    
    static double standard_deviation(double[] data){
        double average=0;
        int elements=data.length;
        for(int i =0; i<elements; i++)
            average+=data[i];
        average/=elements;
        double s=0;
        for(int i =0; i<elements; i++)
            s+=Math.pow(data[i]-average, 2);
        s=s/(elements)/(elements-1);
        return Math.sqrt(s);
    }
    
    static double average(double[] data){
        double average=0;
        int elements=data.length;
        for(int i =0; i<elements; i++)
            average+=data[i];
        average/=elements;
        return average;
    }
    
    static double error(double[] data){
        double sol=0;
        int n=data.length;
        for(int i =0; i<n; i++)
            sol+=Math.pow(data[i], 2);
        return Math.sqrt(sol);
    }
    
    static String[] environment(double t_min, double t_max, double Rh_min, double Rh_max){
        String[] data= new String[2];
        data[0] = Double.toString(t_min)+" ÷ "+Double.toString(t_min);
        data[0] = Double.toString(t_min)+" ÷ "+Double.toString(t_min);
        return data;
    }
    static double calculate(double t, double Rh, double d1, double d2, double d3, double d4){
        double d, a, b;
        a=d1+(d2-d1)*t;
        b=d3+(d4-d3)*t;
        d=a+(b-a)*Rh;
        return d;
        }
    
    static double calculate(double t, double d1, double d2){
        double a;
        a=d1+(d2-d1)*t;
        return a;
        }
    
    static data_probe easy_calculate(double cor, data_probe d1, data_probe d2) {
        data_probe sol = new data_probe();
        sol.correction_Rh= d1.correction_Rh+cor*(d2.correction_Rh-d1.correction_Rh);
        sol.correction_t= d1.correction_t+cor*(d2.correction_t-d1.correction_t);
        sol.uncertainty_Rh=Math.max(d1.uncertainty_Rh, d2.uncertainty_Rh);
        sol.uncertainty_t=Math.max(d1.uncertainty_t, d2.uncertainty_t);
    /*    sol.drift_Rh = drift_Rh;
        sol.drift_t = drift_t;*/
        return sol;
    }
    static double uncertainty(double[] number){
        double sum= 0;
        for(int i=0; i<number.length; i++)
            sum+= Math.pow(number[i], 2.0);
        sum= Math.sqrt(sum);
        return sum;
    }
    static double find_round(double unc, double res){
        double a=Math.log10(unc),
                b=Math.log10(res);
        if(a>0 && b<0){
            if(a>1)
                return 0.1;
            else
                return Math.pow(10, ((int)a-1));
        }
        if((int)a-1>b){
            return Math.pow(10, ((int)a-2));
        }else{
            double sol;
            if(b<0){
                double i= Math.pow(10, ((int)b-1));
                sol= Math.round(Math.round(res/i))*i;
                return sol;
            }else{
                double i= Math.pow(10, ((int)b));
                sol= Math.round(Math.round(res/i)*i);
                return sol;
            }
        }
    }
    
    public static double round_(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
 
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    static double round_d(double num, double round){
        double s=Math.round(num/round);
        s=s*round;
        if(round==1){
            return (int)s;
        }
        double d= Math.log10(round);
        if(d%1==0)
            return round_(s,-1*(int)d);
        return round_(s,-1*(int)d+1);
    }
    
    static String round(double num, double round){
        if(round>=1){
            return Integer.toString((int)Math.round(num));
        }
        double d= Math.log10(round);
        int places;
        if(d%1!=0)
            places=-1*(int)d+1;
        else
            places=-1*(int)d;
        double s=Math.round(num/round);
        s=s*round;
        String format = "%."+places+"f";
        return String.format(format, s);
    }
    
    static String parseTime(String data){
        Date date;
        try {
            date = new SimpleDateFormat("'PT'HH'H'mm'M's'S'").parse(data);
            String newString = new SimpleDateFormat("HH:mm").format(date); // 9:00
            return newString;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return data;
        }
    }
    
    static String time (String time, int d){
        LocalTime t= LocalTime.parse(time);
        t=t.plusMinutes(d);
        return t.toString();
    }
    
    static boolean validate(String num){
        //System.out.println("numer "+num);
        try {
              double d =Double.parseDouble(num);
              if(d>100 || d <0)
                  return false;
              return true;
            } catch (NumberFormatException e) {
              return false;
            }
    }
}