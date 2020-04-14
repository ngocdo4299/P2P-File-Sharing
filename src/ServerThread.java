import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread {
	private ServerSocket serverSocket;
	private Set< ServerThreadThreads > serverThreadThreads = new HashSet< ServerThreadThreads >();
	public ServerThread(String portNumb) throws IOException {
		serverSocket = new ServerSocket(Integer.valueOf(portNumb));
	}
	public void run() {
		try {
			while(true) {
				ServerThreadThreads serverThreadThread = new ServerThreadThreads(serverSocket.accept(),this);
				serverThreadThreads.add(serverThreadThread);
				serverThreadThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void sendMessage(String message) {
		try {
			serverThreadThreads.forEach( t -> t.getPrintWriter().println(message));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public Set<ServerThreadThreads> getServerThreadThreads(){
		return serverThreadThreads;
	}
}
