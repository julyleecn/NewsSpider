package lab409.utils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuawai on 03/11/2017.
 */
public class StringUtil {

    /*
    yyyy-mm-dd格式
     */

    public static int[] parseDateStrToInt(String date){
        String[] ymd = date.split("-");
        int[] result = new int[3];
        for(int i=0; i<3; i++){
            result[i] = Integer.parseInt(ymd[i]);
        }
        return result;
    }

    public static Long parseDateStr(String date){

        int[] result = parseDateStrToInt(date);
        Calendar start = Calendar.getInstance();
        start.set(result[0], result[1]-1, result[2]);
        Long time = start.getTimeInMillis();
        return time;
    }

    public static int matchNumberFromStr(String s){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        try {
            return Integer.parseInt(m.replaceAll("").trim());
        }catch (Exception e){
            return 0;
        }

    }


    public static void main(String[] args) {


        Long l = StringUtil.parseDateStr("2011-11-12");
        System.out.println(l);

        System.out.println(StringUtil.matchNumberFromStr("afdadf132"));

    }

    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    public static boolean isUTF8(String key){
        try {
            key.getBytes("utf-8");
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }
}
