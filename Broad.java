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

import javax.swing.JPanel;
/*
 * Carefully!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class Broad extends JPanel{
	private static final long serialVersionUID = 1L;
	Image broadBackGround=getImage("/home/wuwang/Chess/SHEET.GIF");
	Image pieceBorder=getImage("/home/wuwang/Chess/DELICATE/OOS.GIF");
	
	Ellipse2D[][] location=new Ellipse2D[10][9];//用于判断光标移动是否是有效位置
	Piece[][] allPiece=new Piece[10][9];//所有棋子 image是未选中的样式 SImage是选中样式
	int curX=-1,curY=-1;	
	//第一个棋子的xy坐标 后面的位置根据interval推出来
	final static int beginX=20,beginY=25,chessSize=80,interval=85;
	
	Rules rules;
	public Broad() {		
		this.setSize(broadBackGround.getWidth(this),broadBackGround.getHeight(this));
		initPic();
		rules=Rules.getRulesObj(allPiece);
		addListen();
	}
	
	
	void initPic() {//初始化所有需要的图片
		for(int i=0;i<9;i+=2) {//卒
			allPiece[6][i]=new Piece(getImage("/home/wuwang/Chess/DELICATE/RP.GIF"),
					getImage("/home/wuwang/Chess/DELICATE/RPS.GIF"),"zu",true);
			allPiece[3][i]=new Piece(getImage("/home/wuwang/Chess/DELICATE/BP.GIF"),
					getImage("/home/wuwang/Chess/DELICATE/BPS.GIF"),"zu",false);		
		}
		for(int i=1;i<9;i+=6) {//炮
			allPiece[7][i]=new Piece(getImage("/home/wuwang/Chess/DELICATE/RC.GIF"),
					getImage("/home/wuwang/Chess/DELICATE/RCS.GIF"),"pao",true);
			allPiece[2][i]=new Piece(getImage("/home/wuwang/Chess/DELICATE/BC.GIF"),
					getImage("/home/wuwang/Chess/DELICATE/BCS.GIF"),"pao",false);		
		}
		//建立map 保存name-image
		TreeMap<String,Image> redN=new TreeMap<>(),redS=new TreeMap<>(),
							  blackN=new TreeMap<>(),blackS=new TreeMap<>();	
		//根目录下四个子目录 子目录1,2,3,4保存不同类型的Image
		File file=new File("/home/wuwang/Chess/DELICATE/Chess");		
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

	@Override
	public void paint(Graphics g) {
		super.paint(g);//	boolean isSelected=false;
		g.drawImage(broadBackGround, 0, 0, 800, 900, this);
		for(int i=0;i<10;++i) {
			for(int j=0;j<9;++j) 
				location[i][j]=new Ellipse2D.Double(beginX+j*interval,beginY+i*interval,chessSize,chessSize);
		}
		drawPiece(g);
	}
	/**
	 * 画所有Piece
	 * @param g
	 */
	void drawPiece(Graphics g) {
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
				System.out.println("Clicked");
				
				
				if(e.getButton()==MouseEvent.BUTTON1) {
					System.out.println("Clicked");
					Pair p=contain(e.getPoint());				
					if(curX!=-1&&curY!=-1) {
						rules.move(curX, curY, p.x, p.y);
						return ;
						//move(curX,curY,p.x,p.y);
						//return ;
					}
					
					if(p!=null&&allPiece[p.x][p.y]!=null) {
						curX=p.x;
						curY=p.y;
						repaint();
					}
					else {
						curX=-1;
						curY=-1;
						repaint();
					}
				}	
				else if(e.getButton()==MouseEvent.BUTTON3){
					curX=-1;
					curY=-1;
					repaint();
				}
			}
	});		
	this.addMouseMotionListener(new MouseMotionListener() {			
		@Override
		public void mouseDragged(MouseEvent e) {	
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			Pair p=contain(e.getPoint());
			if(p!=null) {
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else {
				setCursor(Cursor.getDefaultCursor()); 	
			}
			}				
		});					
	}
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
