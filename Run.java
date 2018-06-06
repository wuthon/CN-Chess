package XiangQi;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Run {

	public static void main(String[] args) {
		EventQueue.invokeLater(()->new Game().setVisible(true));	
	}
	
}
/**
 * 
 * @author wuwang
 */
class Game extends JFrame{
	private static final long serialVersionUID = 1L;
	//path

	String path="/home/wuwang/Chess";
	String[] piece=new String[2];
	ArrayList<String> allBackground=new ArrayList<>();
	
	ImageIcon selected=new ImageIcon("/home/wuwang/图片/Material/10.jpg");
	Broad broad=new Broad();
	JMenuBar menuBar=new JMenuBar();
	int ChessIndex=1,PicIndex=1;
	
	Random rand=new Random();
	
	public Game(){
		setSize(795,920);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(menuBar);
		new Thread(()->loadingAllPic()).start();
		addSkinMenu();
		addGameMenu();
		addFileMenu();
		add(broad);
		
	}
	/**
	 * 加载所有资源路径
	 */
	void loadingAllPic() {
		File file=new File(path);
		File[] files=file.listFiles();
		for(File f:files) {
			if(f.getName().endsWith(".GIF")||f.getName().endsWith(".jpg")||f.getName().endsWith(".jpeg")) 
				allBackground.add(f.getPath());
		}
		piece[0]="/home/wuwang/Chess/DELICATE/";
		piece[1]="/home/wuwang/Chess/POLISH/";
	}
	int pieceNextIndex() {//就两种棋子style可以使用 所以还是不随机了..
		if(ChessIndex==0)
			return ChessIndex=1;
		else 
			return ChessIndex=0;
	}
	int bgNextIndex() {
		return rand.nextInt(allBackground.size());
	}
	String fontOf(String str) {
		return "<html><b><font size=5 color=gray>"+str+"</font></b></html>";
	}
	
	/**
	 * 菜单栏 三个menu 切换style 重开与悔棋 保存棋谱和读取棋谱
	 */
	
	void addGameMenu() {
		JMenu menu=new JMenu(fontOf("Game"));
		addItem(fontOf("New"),menu,(e)->{
			broad.newGame();
		});
		addItem(fontOf("Retreat"),menu,(e)->{
			broad.retreat();
		});
		JMenuItem item=new JMenuItem(fontOf("Ai"));
		item.setIcon(selected);
		item.addActionListener((e)->{
			if(broad.useAi)
				item.setIcon(null);
			else
				item.setIcon(selected);
			broad.useAi=!broad.useAi;
		});
		menu.add(item);
		menuBar.add(menu);
	}
	void addSkinMenu() {
		JMenu menu=new JMenu(fontOf("Skin"));
		addItem(fontOf("Piece"),menu,(e)->{
			broad.setPiece(piece[pieceNextIndex()]);
		}); 
		addItem(fontOf("Background"),menu,(e)->{
			broad.setBackground(allBackground.get(bgNextIndex()));
		}); 
		menuBar.add(menu);
	}
	void addFileMenu() {
		JMenu menu=new JMenu(fontOf("File"));
		addItem(fontOf("Save"),menu,(e)->{
			try {
				String path=fileChooser("Save");
				if(path.endsWith(".chess")) 
					broad.rules.writeChess(path);
				else 
					broad.rules.writeChess(path+".chess");				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}); 
		addItem(fontOf("Open"),menu,(e)->{
			try {
				String path=fileChooser("Open");
				assert(path.endsWith(".chess")):"error file format!";
				broad.rules.readChess(path);
				broad.repaint();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		}); 
		menuBar.add(menu);
	}
	void addItem(String name,JMenu menu,ActionListener action) {
		JMenuItem item=new JMenuItem(name);
		item.addActionListener(action);
		menu.add(item);
	}
	String fileChooser(String title) {
		JFileChooser chooser=new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle(title);
		chooser.setPreferredSize(new Dimension(600, 450));
		if(chooser.showOpenDialog(this)==JFileChooser.OPEN_DIALOG)
			return chooser.getSelectedFile().getPath();
		else 
			return null;
	}
}
