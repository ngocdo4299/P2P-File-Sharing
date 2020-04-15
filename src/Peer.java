import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.simple.JSONObject;

public class Peer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter username and port for this peer: ");
		String[] setupValues = bufferedReader.readLine().split(" ");
		ServerThread serverThread = new ServerThread(setupValues[1]);
		serverThread.start();
		new Peer().updateListentoPeers(bufferedReader, setupValues[0], serverThread);
	}	
	public void updateListentoPeers(BufferedReader bufferedReader, String username, ServerThread serverThread) throws Exception{
		System.out.println("Enter localhost:xxx");
		System.out.println(" peers to receive message from (s to skip) : ");
		String input = bufferedReader.readLine();
		String[] inputValues = input.split(" "); 
		if(!input.equals("s")) for (int i = 0; i < inputValues.length; i++) {
			String[] address = inputValues[i].split(":");
			Socket socket = null;
			try {
				socket = new Socket(address[0], Integer.valueOf(address[1]));
				new PeerThread(socket).start();
			} catch(Exception e) {
				if(socket != null) socket.close();
				else System.out.println("invalid input!");
			}
		}
		PeerCommunication(bufferedReader, username, serverThread);
	}
	public void PeerCommunication(BufferedReader bufferedReader, String username, ServerThread serverThread) {
		try {
			System.out.println("Connection created (e to exit, c to change)");
			boolean flag = true;
			while( flag ) {
				String message = bufferedReader.readLine();
				if(message.equals("e")) {
					flag = false;
					break;
				} else if (message.equals("c")) {
					updateListentoPeers(bufferedReader, username, serverThread);
				}
				else if (message.equals("d")) {
					updateListentoPeers(bufferedReader, username, serverThread);
				}
				else {
					JSONObject obj = new JSONObject();
				      obj.put("username", username);
				      obj.put("message", message);
					serverThread.sendMessage(obj.toString());
				}
			}
			System.exit(0);
		} catch (Exception e) { }
	}
}
