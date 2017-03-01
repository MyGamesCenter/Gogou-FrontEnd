package info.gogou.gogou.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lxu on 2016-05-19.
 */
public class EmailUtil {

    public final static boolean verifyEmail(String email){
        if(email == null || email.isEmpty()){
            return false;
        }
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean flag = matcher.matches();
        if(flag){
            return true;
        }else{
            return false;
        }
    }
}
