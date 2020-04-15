import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class PeerThread extends Thread{
	private BufferedReader bufferedReader;
	private FileOutputStream fos;
    private BufferedOutputStream bos;
    private InputStream is; 
    private String status = "chat";
	public PeerThread(Socket socket) throws IOException {
		this.is = socket.getInputStream();
		bufferedReader = new BufferedReader(new InputStreamReader(is));
	}
	public void run() {
		boolean flag = true;
		while(flag) {
			try {
				while(this.status.equals("chat")) {
					String input = bufferedReader.readLine();
					if(input.contains("username")) {
						JSONParser parser = new JSONParser();
						JSONObject json = (JSONObject) parser.parse(input);
						System.out.println("[ "+json.get("username")+" ] : "+ json.get("message"));
						if(json.get("code") != null) {
							this.status = json.get("code").toString();
						}
					}
				}
				while(this.status.equals("FSend")) {
					
					this.fos = new FileOutputStream("./FileReceive/Hello.txt");
					bos = new BufferedOutputStream(fos);
					int bytesRead;
				    int current = 0;
					while(flag) {
							try {
								byte [] mybytearray  = new byte [6022386];
							    bytesRead = is.read(mybytearray,0,mybytearray.length);
							    current = bytesRead;
							    do {
							         bytesRead =
							            is.read(mybytearray, current, (mybytearray.length-current));
							         if(bytesRead >= 0) current += bytesRead;
							      } while(bytesRead > -1);

							      bos.write(mybytearray, 0 , current);
							      bos.flush();
							      System.out.println("File ./FileSharing/search2.png downloaded (" + current + " bytes read)");
							}
						    finally {
						      if (fos != null) fos.close();
						      if (bos != null) bos.close();
						    }
							System.out.println("File downloaded");
					}
				}
			} catch(Exception e) {
				flag = false;
				interrupt();
			}
		}
	}
}
