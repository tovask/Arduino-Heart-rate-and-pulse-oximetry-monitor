import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * compile & run: javac *.java && java -cp hsqldb-2.3.6.jar:. PC
 * 
 * build into a single jar: 
 * 
 * https://docs.oracle.com/javase/tutorial/networking/datagrams/broadcasting.html
 *
 */
public class PC {
	
	private Database db = null;
	private Graph graph = null;
	
	private InetAddress getArduinoAddress(){
		
		int port = 2390;
		byte[] msg = "abc123".getBytes();
		
		try {
			InetAddress group = InetAddress.getByName("224.0.0.123");
			DatagramSocket socket = new DatagramSocket();
			
			socket.setSoTimeout(1000); // don't wait infinitely for reply
			
			while (true) {
				
				System.out.println("Sending packet to " + group);
				DatagramPacket request = new DatagramPacket(msg, msg.length, group, port);
				socket.send(request);
				System.out.println("UDP packet sent to " + request.getSocketAddress() + " from " + socket.getLocalSocketAddress());
				
				byte[] buffer = new byte[512];
				DatagramPacket response = new DatagramPacket(buffer, buffer.length);
				try {
					System.out.println("Waiting for reply...");
					socket.receive(response);
					
					String quote = new String(response.getData(), 0, response.getLength());
					
					System.out.println("Got reply from " + response.getAddress() + ":" + response.getPort() + " :");
					System.out.println(quote);
					System.out.println();
					
					InetAddress arduinoIP = InetAddress.getByName(quote.split("IP: ")[1]);
					System.out.println("Found IP: " + arduinoIP);
					return arduinoIP;
				} catch (SocketTimeoutException e) {
					System.out.println("Timeout reached!!! " + e);
					// s.close();
				}
				
				// Thread.sleep(1000);
			}
			
		} catch (SocketTimeoutException ex) {
			System.out.println("Timeout error: " + ex.getMessage());
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Client error: " + ex.getMessage());
			ex.printStackTrace();
		// } catch (InterruptedException ex) {
		// 	ex.printStackTrace();
		}
		return null;
	}
	
	private void receiveData(InetAddress arduinoIP) {
		Socket clientSocket = null;
		DataOutputStream outToServer = null;
		BufferedReader inFromServer = null;
		System.out.println("Connecting to " + arduinoIP);
		try {
			// clientSocket = new Socket(arduinoIP, 23);
			clientSocket = new Socket();
			int timeout = 5 * 1000;
			// This limits the time allowed to establish a connection in the case
			// that the connection is refused or server doesn't exist.
			clientSocket.connect(new InetSocketAddress(arduinoIP, 23), timeout);
			System.out.println("\nConnected to Arduino");
			// This stops the request from dragging on after connection succeeds.
			clientSocket.setSoTimeout(timeout);
			
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (true) {
				outToServer.write((int)65);
				
				String line = inFromServer.readLine();
				System.out.println("Received from Arduino: " + line);
				
				// Create a Pattern object, create matcher object.
				Matcher m = Pattern.compile("ecg: ([-]?\\d+), po_IR: ([-]?\\d+), po_Red: ([-]?\\d+), elapsedMillis: (\\d+)").matcher(line);
				if (m.find()) {
					int ecg = Integer.parseInt(m.group(1));
					int po_IR = Integer.parseInt(m.group(2));
					int po_Red = Integer.parseInt(m.group(3));
					long elapsedMillis = Long.parseLong(m.group(4));
					System.out.println("Parsed data: ecg: " + ecg + ", po_IR: " + po_IR + ", po_Red: " + po_Red + ", elapsedMillis: " + elapsedMillis);
					db.store( ecg, po_IR, po_Red, elapsedMillis);
					graph.newData( ecg, po_IR, po_Red, elapsedMillis);
				}else {
					System.out.println("Invalid data format!");
				}
			}
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}
		try{
			outToServer.close();
			inFromServer.close();
			clientSocket.close();
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
	}
	
	public void run() {
		try {
			db = new Database();
		} catch(Exception e) {
			System.out.println(e);
			System.out.println("Exception on database init: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		graph = new Graph();
		while (true) {
			InetAddress arduinoIP = getArduinoAddress();
			receiveData(arduinoIP);
		}
	}
	
	public static void main(String[] args) {
		new PC().run();
	}
}
