import java.awt.*;
import java.awt.event.*;

public class Graph {
	private final int width = 640;
	private final int height = 480;
	private final int lineWidth = 2;
	private Canvas canvas;
	private final Color backgroundColor = Color.GRAY; // WHITE
	private final Color ecgColor = Color.BLACK;
	private final Color poIRColor = Color.YELLOW;
	private final Color poRedColor = Color.RED;
	private int[] data_ecg = new int[width/lineWidth];
	private int[] data_po_IR = new int[width/lineWidth];
	private int[] data_po_Red = new int[width/lineWidth];
	private int x = 0;
	
	public Graph() {
		Frame frame = new Frame("Graph");
		frame.setSize(width, height);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		canvas = new Canvas() {
			public void paint(Graphics g) {
				// System.out.println("paint ");
				// System.out.println("start");
				g.setColor(ecgColor);
				for(int i=0;i<data_ecg.length;i++){
					g.fillRect(i*lineWidth, height - data_ecg[i], lineWidth, lineWidth);
				}
				g.setColor(poIRColor);
				for(int i=0;i<data_po_IR.length;i++){
					g.fillRect(i*lineWidth, height - data_po_IR[i], lineWidth, lineWidth);
				}
				g.setColor(poRedColor);
				for(int i=0;i<data_po_Red.length;i++){
					g.fillRect(i*lineWidth, height - data_po_Red[i], lineWidth, lineWidth);
				}
				//--
				//x++;
				//data[x%data.length] = (int)(50*Math.sin(x/10.0)) + height/2;
				// System.out.println("end");
			}
			public void update(Graphics g) {
				// System.out.println("update ");
				g.setColor(backgroundColor);
				g.fillRect(x*lineWidth, 0, lineWidth, height); // clear previous
				g.setColor(ecgColor);
				g.fillRect(x*lineWidth, height - data_ecg[x], lineWidth, lineWidth);
				g.setColor(poIRColor);
				g.fillRect(x*lineWidth, height - data_po_IR[x], lineWidth, lineWidth);
				g.setColor(poRedColor);
				g.fillRect(x*lineWidth, height - data_po_Red[x], lineWidth, lineWidth);
			}
		};
		canvas.setBackground(backgroundColor);
		canvas.setSize(width, height);
		frame.add(canvas);
		frame.setVisible(true);
	}
	
	public void newData(int ecg, int po_IR, int po_Red, long elapsedMillis){
		// data[ (int)(elapsedMillis % data.length) ] = ecg + height/2;
		x++;
		x %= data_ecg.length;
		data_ecg[x] = ecg - 290;
		// 2^18 = 262144
		data_po_IR[x] = (po_IR-130000) / 100 + height/2;
		data_po_Red[x] = (po_Red-130000) / 100 + height/2;
		canvas.repaint();
	}
	
	public static void main(String args[]) {
		new Graph();
	}
}
