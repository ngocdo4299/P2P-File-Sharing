import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

import org.json.simple.JSONObject;

public class Peer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter username and port for this peer: (ex: Calar 101)");
		String[] setupValues = bufferedReader.readLine().split(" ");
		ServerThread serverThread = new ServerThread(setupValues[1]);
		serverThread.start();
		new Peer().updateListentoPeers(bufferedReader, setupValues[0], serverThread);
	}
	public void updateListentoPeers(BufferedReader bufferedReader, String username, ServerThread serverThread) throws Exception{
		System.out.println("Enter 'localhost:PORT' of  peers to receive message from");
		System.out.println("ex: localhost:202 - s to skip this step)");
		String input = bufferedReader.readLine();
		String[] inputValues = input.split(" "); 
		String[] address = null;
		if(!input.equals("s")) for (int i = 0; i < inputValues.length; i++) {
			address = inputValues[i].split(":");
			Socket socket = null;
			try {
				socket = new Socket(address[0], Integer.valueOf(address[1]));
				new PeerThread(socket,username).start();
			} catch(Exception e) {
				if(socket != null) socket.close();
				else System.out.println("invalid input!");
			}
		}
		PeerCommunication(bufferedReader, username, serverThread);
	}
	public void PeerCommunication(BufferedReader bufferedReader, String username, ServerThread serverThread) {
		try {
			System.out.println("Connected! e to exit, c to add more PORT, d to send file");
			boolean flag = true;
			while( flag ) {
				String message = bufferedReader.readLine();
				if(message.equals("e")) {
					flag = false;
					break;
				} else if (message.equals("c") || message.equals("y") ) {
					updateListentoPeers(bufferedReader, username, serverThread);
				}
				else if (message.equals("d")) {
					File folder = new File("./FileSharing");
					PeerSendFile(bufferedReader,serverThread, username, folder);
//					serverThread.sendFile("Hello.txt");
				}
				else {
					JSONObject obj = new JSONObject();
				      obj.put("username", username);
				      obj.put("message", message);
				      obj.put("code", null);
					serverThread.sendMessage(obj.toString());
				}
			}
			System.exit(0);
		} catch (Exception e) { }
	}
	public void PeerSendFile(BufferedReader bufferedReader,ServerThread serverThread, String username, File folder) throws IOException {
		String[] files = folder.list();

		for (String file : files)
		{
			System.out.println(file);
		}
		System.out.println("Which file would you like to send? From 1 to ");
		String input = bufferedReader.readLine();
		File file = new File("./FileSharing/"+input);
		if(file.exists()) {
			System.out.println(file.getPath());
			String str= "A file is sending !";
			JSONObject obj = new JSONObject();
	        obj.put("username", username);
	        obj.put("message", str);
	        obj.put("code", "FSend");
			obj.put("fileName", file.getName() );
			serverThread.sendMessage(obj.toString());
			serverThread.sendFile(file.getName());
		}else {
			System.out.print("Can't not find: " + input);
		}
		PeerCommunication(bufferedReader, username, serverThread);
	}
}
