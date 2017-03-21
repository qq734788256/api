package wx.wyl.handle.Register;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.trim;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private static final String Token = "wangyunlongWXapi0001";

    /**
     * 微信开发者验证
     * @param request
     * @return
     */
    @RequestMapping(value = "/regist", method = RequestMethod.GET)
    public String regist(HttpServletRequest request){
        try{
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");

            String[] ArrTmp = { Token, timestamp, nonce };
            Arrays.sort(ArrTmp);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ArrTmp.length; i++) {
                sb.append(ArrTmp[i]);
            }
            String pwd = Encrypt(sb.toString());

            if(trim(pwd).equals(trim(signature))){
                return echostr;
            }else{
                return "false";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "false";
    }

    private String Encrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(bt);
            strDes = bytes2Hex(md.digest()); //to HexString
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid algorithm.");
            return null;
        }
        return strDes;
    }

    public String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
