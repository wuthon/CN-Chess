package XiangQi;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Run {

	public static void main(String[] args) {
		EventQueue.invokeLater(()->new Game().setVisible(true));		
	}
	
}
class Game extends JFrame{
	private static final long serialVersionUID = 1L;
	String path="/home/wuwang/Chess";
	Broad broad=new Broad();
	JMenuBar menuBar=new JMenuBar();
	int ChessIndex=1,PicIndex=1;
	ArrayList<String> allBackground=new ArrayList<>();
	String[] piece=new String[2];
	public Game(){
		setSize(800,920);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(menuBar);
		new Thread(()->loadingAllPic()).start();
		addSkinMenu();
		addGameMenu();
		add(broad);
		
	}
	void loadingAllPic() {
		File file=new File(path);
		File[] files=file.listFiles();
		for(File f:files) {
			if(f.getName().endsWith(".GIF")) {
				System.out.println(f.getPath());
				allBackground.add(f.getPath());
			}
		}
		piece[0]="/home/wuwang/Chess/DELICATE/";
		piece[1]="/home/wuwang/Chess/POLISH/";
	}
	int pieceNextIndex() {
		if(ChessIndex==0)
			return ChessIndex=1;
		else 
			return ChessIndex=0;
	}
	int bgNextIndex() {
		if(PicIndex==allBackground.size()) {
			return PicIndex=0;
		}
		return ++PicIndex;
	}
	String fontOf(String str) {
		return "<html><b><font size=5 color=gray>"+str+"</font></b></html>";
	}
	void addGameMenu() {
		JMenu menu=new JMenu(fontOf("Game"));
		addItem(fontOf("New"),menu,(e)->{
			new Thread(()->broad.newGame()).start();
		});
		addItem(fontOf("Retreat"),menu,(e)->{
			broad.retreat();
		});
		menuBar.add(menu);
	}
	void addSkinMenu() {
		JMenu menu=new JMenu(fontOf("Skin"));
		addItem(fontOf("Piece"),menu,(e)->{
			new Thread(()->broad.setPiece(piece[pieceNextIndex()])).start();
		}); 
		addItem(fontOf("Background"),menu,(e)->{
			new Thread(()->broad.setBackground(allBackground.get(bgNextIndex()))).start();
		}); 
		menuBar.add(menu);
	}
	void addItem(String name,JMenu menu,ActionListener action) {
		JMenuItem item=new JMenuItem(name);
		item.addActionListener(action);
		menu.add(item);
	}
}
