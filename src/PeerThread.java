import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class PeerThread extends Thread{
	private BufferedReader bufferedReader;
	public PeerThread(Socket socket) throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Receive from port: " + socket.getPort());
	}
	public void run() {
		boolean flag = true;
		while(flag) {
			try {
				String input = bufferedReader.readLine();
				if(input.contains("username")) {
					JSONParser parser = new JSONParser();
					JSONObject json = (JSONObject) parser.parse(input);
					System.out.println("[ "+json.get("username")+" ] : "+ json.get("message"));
				}
			} catch(Exception e) {
				flag = false;
				interrupt();
			}
		}
	}
}
