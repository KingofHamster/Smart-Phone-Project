package cs.hku.hkutreehole.Utils;

public class Utils {
    public static boolean checkHKUEmailAddress(String emailAddress){
        String reg = "\\w+@connect.hku.hk";
        return emailAddress.matches(reg);
    }
}
