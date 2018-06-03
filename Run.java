package XiangQi;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Run {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Game f=new Game();
				f.setVisible(true);				
			}			
		});
	}
}
class Game extends JFrame{
	private static final long serialVersionUID = 1L;
	Broad broad=new Broad();
	public Game(){
		setSize(800,900);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(broad);
	}
}
