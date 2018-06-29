/*package SSH;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.jcraft.jsch.Channel;
 
public class Shell{
    private static final String USER="root";
    private static final String PASSWORD="123456";
    private static final String HOST="192.168.29.129";
    private static final int DEFAULT_SSH_PORT=22;
 
   
    public static void main(String[] args){
 
        try{
            JSch jsch=new JSch();
            Session session = jsch.getSession(USER,HOST,DEFAULT_SSH_PORT);
            session.setPassword(PASSWORD);
 
            UserInfo userInfo = new UserInfo() {
                @Override
                public String getPassphrase() {
                    System.out.println("getPassphrase");
                    return null;
                }
                @Override
                public String getPassword() {
                    System.out.println("getPassword");
                    return null;
                }
                @Override
                public boolean promptPassword(String s) {
                    System.out.println("promptPassword:"+s);
                    return false;
                }
                @Override
                public boolean promptPassphrase(String s) {
                    System.out.println("promptPassphrase:"+s);
                    return false;
                }
                @Override
                public boolean promptYesNo(String s) {
                    System.out.println("promptYesNo:"+s);
                    return true;//notice here!
                }
                @Override
                public void showMessage(String s) {
                    System.out.println("showMessage:"+s);
                }
            };
 
            session.setUserInfo(userInfo);
 
            session.connect(30000);   // making a connection with timeout.
 
            Channel channel=session.openChannel("shell");
 
         
            channel.setInputStream(System.in);
    
 
            channel.setOutputStream(System.out);
 
            channel.connect(3*1000);;
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static void main(String[] args) {
		System.out.println(111);
	}
}*/