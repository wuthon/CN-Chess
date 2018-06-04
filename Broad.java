package XiangQi;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
/*
 * Carefully!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
/**
 * 继承JPanel 棋盘图形部分
 * @author wuwang
 */
public class Broad extends JPanel{
	private static final long serialVersionUID = 1L;
	final static int WIDTH=800,HEIGHT=900;//第一个棋子的xy坐标 后面的位置根据interval推出来		
	final static int beginX=20,beginY=25,chessSize=80,interval=85;
	
	Image broadBackGround=getImage("/home/wuwang/Chess/SHEET.GIF");
	Image pieceBorder=getImage("/home/wuwang/Chess/DELICATE/OOS.GIF");
	
	Ellipse2D[][] location=new Ellipse2D[10][9];//标记光标移动的有效位置
	Piece[][] allPiece=new Piece[10][9];//所有棋子 image是未选中的样式 SImage是选中样式
	int curX=-1,curY=-1;	
	Rules rules;
	
	public Broad() {		
		setSize(broadBackGround.getWidth(this),broadBackGround.getHeight(this));
		new Thread(()->initPic("/home/wuwang/Chess/POLISH")).start();
		new Thread(()->initEllipse()).start();
		rules=Rules.getRulesObj(allPiece);
		addListen();
	}
	void initEllipse() {
		for(int i=0;i<10;++i) {
			for(int j=0;j<9;++j) 
				location[i][j]=new Ellipse2D.Double
				(beginX+j*interval,beginY+i*interval,chessSize,chessSize);
		}
	}
	void initPic(String path) {//初始化一些需要的资源 如image.. 调用线程优化
		//init 	Image
		for(int i=0;i<9;i+=2) {//卒
			allPiece[6][i]=new Piece(getImage(path+"/RP.GIF"),
					getImage(path+"/RPS.GIF"),"zu",true);
			allPiece[3][i]=new Piece(getImage(path+"/BP.GIF"),
					getImage(path+"/BPS.GIF"),"zu",false);		
		}
		for(int i=1;i<9;i+=6) {//炮
			allPiece[7][i]=new Piece(getImage(path+"/RC.GIF"),
					getImage(path+"/RCS.GIF"),"pao",true);
			allPiece[2][i]=new Piece(getImage(path+"/BC.GIF"),
					getImage(path+"/BCS.GIF"),"pao",false);		
		}
		//建立map 保存name-image
		TreeMap<String,Image> redN=new TreeMap<>(),redS=new TreeMap<>(),
							  blackN=new TreeMap<>(),blackS=new TreeMap<>();	
		//根目录下四个子目录 子目录1,2,3,4保存不同类型的Image
		File file=new File(path+"/Chess");		
		File[] dir=file.listFiles();
		for(int i=0;i<4;++i) {
			File[] files=dir[i].listFiles();
			String name=dir[i].getName();
			for(int j=0;j<files.length;++j) {
				int p=files[j].getName().lastIndexOf('.');//文件名截取名字为attr
				switch(name){
				case "1":
					redS.put(files[j].getName().substring(0, p),getImage(files[j].getPath()));
					break;
				case "2":				
					blackN.put(files[j].getName().substring(0, p),getImage(files[j].getPath()));
					break;
				case "3":
					blackS.put(files[j].getName().substring(0, p),getImage(files[j].getPath()));
					break;
				case "4":
					redN.put(files[j].getName().substring(0, p),getImage(files[j].getPath()));
					break;					
				}			
			}
		}					
		String[] attrs=new String[] {
			"ju","ma","xiang","shi","jiang"
		};
		for(int i=0;i<5;++i) {
			allPiece[0][i]=new Piece(blackN.get(attrs[i]),blackS.get(attrs[i]),attrs[i],false);
			allPiece[9][i]=new Piece(redN.get(attrs[i]),redS.get(attrs[i]),attrs[i],true);		
		}
		for(int i=0;i<4;++i) {
			allPiece[0][8-i]=allPiece[0][i];
			allPiece[9][8-i]=allPiece[9][i]	;			
		}
	}
	public void setBackground(String str) {
		broadBackGround=getImage(str);
		repaint();
	}
	public void setPiece(String path) {
		initPic(path);
		repaint();
	}
	public void newGame() {
		//allPiece=new Piece[10][9];
		rules.retreat(rules.getStepsNum());
		curX=-1;
		curY=-1;
		repaint();
	}
	public void retreat() {
		rules.retreat(1);
		repaint();
	}
	@Override
	public void paint(Graphics g) {
		//super.paint(g);
		g.drawImage(broadBackGround, 0, 0, WIDTH, HEIGHT, this);
		drawAllPiece(g);
	}
	/**
	 * 画所有Piece
	 * @param g
	 */
	void drawAllPiece(Graphics g) {
		for(int i=0;i<10;++i) {
			for(int j=0;j<9;++j) {
				if(allPiece[i][j]!=null) {
					if(curX==i&&curY==j) {
						g.drawImage(allPiece[i][j].SImage, beginX+j*interval, beginY+i*interval,chessSize,chessSize,this);
					}
					else {
						g.drawImage(allPiece[i][j].image, beginX+j*interval, beginY+i*interval,chessSize,chessSize,this);
					}
				}
			}
		}
	}
	Image getImage(String path) {
		return Toolkit.getDefaultToolkit().getImage(path);
	}
	/**
	 * 监听
	 */
	void addListen() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{								
				if(e.getButton()==MouseEvent.BUTTON1) {
					Pair p=contain(e.getPoint());
					if(p==null)
						return ;
					if(curX!=-1&&curY!=-1) {
						if(allPiece[p.x][p.y]!=null&&allPiece[p.x][p.y].color==allPiece[curX][curY].color) {
							curX=p.x;
							curY=p.y;						
						}
						else {
							rules.move(curX, curY, p.x, p.y);
							repaint();
							curX=-1;
							curY=-1;
							int result=rules.isWon();
							if(result==2)
								JOptionPane.showMessageDialog(Broad.this,
										"<html><b><font size=11>红方胜</font></b></html>", "game over", JOptionPane.YES_OPTION);
							else if(result==1)
								JOptionPane.showMessageDialog(Broad.this,
										"<html><b><font size=11>黑方胜</font></b></html>", "game over", JOptionPane.YES_OPTION);
							return ;
						}
					}							
					if(allPiece[p.x][p.y]!=null) {
						curX=p.x;
						curY=p.y;
					}
					else {
						curX=-1;
						curY=-1;
					}
				}	
				else if(e.getButton()==MouseEvent.BUTTON3){//撤销选择
					curX=-1;
					curY=-1;
				}
				repaint();
			}
	});		
	this.addMouseMotionListener(new MouseMotionListener() {			
		@Override
		public void mouseDragged(MouseEvent e) {	
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			Pair p=contain(e.getPoint());
			if(p!=null&&allPiece[p.x][p.y]!=null) {
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else {
				setCursor(Cursor.getDefaultCursor()); 	
			}
		}				
	});	}
	/**
	 * 判断幕像素点是否在棋盘合法落子位置
	 * @param p 屏幕x,y坐标 
	 * @return  棋盘矩阵x,y下标
	 */
	Pair contain(Point p) {
		for(int i=0;i<10;++i) {
			for(int j=0;j<9;++j) {
				if(location[i][j].contains(p.getX(), p.getY())) {
					return new Pair(i, j);
				}
			}
		}		
		return null;
	}
	
}
