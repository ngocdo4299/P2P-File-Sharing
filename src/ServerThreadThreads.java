import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

public class ServerThreadThreads extends Thread{
	private ServerThread serverThread;
	private Socket socket;
	private PrintWriter printWriter;
	private String filePath = null;
	public ServerThreadThreads(Socket socket, ServerThread serverThread) {
		this.serverThread = serverThread;
		this.socket = socket;
	}
	public void run() {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			while(this.filePath == null) {
				this.printWriter = new PrintWriter(socket.getOutputStream(),true);
				
			}
			this.printWriter = new PrintWriter(socket.getOutputStream(),true);
				String str= "A file is sending !";
				JSONObject obj = new JSONObject();
		        obj.put("username", socket.getLocalPort());
		        obj.put("message", str);
		        obj.put("code", "FSend");
				serverThread.sendMessage(obj.toString());
				File file = new File(this.filePath);
				if(file.exists()) {
					try {
						System.out.println("Found file!");
						 byte [] mybytearray  = new byte [(int)file.length()];
						 fis = new FileInputStream(file);
						 bis = new BufferedInputStream(fis);
						 bis.read(mybytearray,0,mybytearray.length);
						 os = socket.getOutputStream();
				         System.out.println("Sending ./FileSharing/search.png" + "(" + mybytearray.length + " bytes)");
				         os.write(mybytearray,0,mybytearray.length);
				         os.flush();
				         System.out.println("Done.");
					}
					finally {
						if (bis != null) bis.close();
						if (os != null) os.close();
						if (fis != null) fis.close();
				        }
				}
				while(true) {
					serverThread.sendMessage(bufferedReader.readLine());
				}
			
		} catch(Exception e) {
			serverThread.getServerThreadThreads().remove(this);
		}
	}
	public PrintWriter getPrintWriter(){
		return printWriter;
	}
	public String getFilePath(String f) {
		this.filePath = f;
		return filePath;
	}
}
