import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadThreads extends Thread{
	private ServerThread serverThread;
	private Socket socket;
	private PrintWriter printWriter;
	public ServerThreadThreads(Socket socket, ServerThread serverThread) {
		System.out.println("AcceptConnection: " + socket + serverThread + this.printWriter);
		this.serverThread = serverThread;
		this.socket = socket;
	}
	public void run() {
		try {
			System.out.println("Executing  thread : " + this.getName()) ;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.printWriter = new PrintWriter(socket.getOutputStream(),true);
			System.out.println("still work!");
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
}
