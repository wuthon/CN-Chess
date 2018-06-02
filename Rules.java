package XiangQi;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * 表述游戏规则部分
 * 界面与规则处理分离,不然Broad就显得冗长
 * @author wuwang
 *
 */
/*问题来了 悔棋怎么写？
 */

public class Rules {
	private static Rules obj=new Rules();
	Ellipse2D[][] allLocation;
	static Piece[][] allPiece;
	private Rules() {
	}
	public static Rules getRulesObj(Piece[][] arg0) {
		if(allPiece==null)
			allPiece=arg0;
		return obj;
	}
	//x=-1 y=-1但不repaint 移动后
	public void move(int oldx,int oldy,int newx,int newy) {
		//out of boundary
		if(oldx<0||oldy<0||oldx>9||oldy>8)
			return ;
		if(newx<0||newy<0||newx>9||newy>8)
			return ;
		//null pointer
		if(allPiece[oldx][oldy]==null) {
			return ;
		}
		//move棋子自身移动到自身  无意义
		if(oldx==newx&&oldy==newy)
			return ;
		//move到己方棋子 无意义处理
		if(allPiece[oldx][oldy].color==allPiece[newx][newy].color) {
			return ;
		}
		if(find(pieceNext(oldx,oldy),newx,newy))
			moveTo(newy, newy, newy, newy);
			
	}
	//类似C++的move语句  交换资源控制权而已  
	//没有执行repaint!
	void moveTo(int oldx,int oldy,int newx,int newy) {
		allPiece[newx][newy]=allPiece[oldx][oldy];
		allPiece[oldx][oldy]=null;
	}
	Pair contain(Point p) 
	{
		for(int i=0;i<10;++i) 
		{
			for(int j=0;j<9;++j) 
			{
				if(allLocation[i][j].contains(p.getX(), p.getY())) 
					return new Pair(i, j);
			}
		}		
		return null;
	}
	/**
	 * 提供矩阵x,y位置的Piece,预测其下一步可以走的位置
	 * @param x 
	 * @param y
	 * @return
	 */
	boolean find(ArrayList<Pair> list,int x,int y) {
		for(Pair p:list) {
			if(p.x==x&&p.y==y)
				return true;
		}
		return false;
	}
	ArrayList<Pair>  pieceNext(int x,int y){
		String attr=allPiece[x][y].attr;
		switch(attr) {
		case "ju":	
			return juNext(x,y);
		case "ma":
			break;
		case "xiang":
			break;
		case "shi":
			break;
		case "jiang":
			break;
		case "pao":
			break;
		case "bing":
			break;
		}		
		return null;
	}
	/**
	 * 
	 * @param 判断p列是否越界
	 * @return
	 */
	boolean isNotOutOFBoundaryColumns(int p) {
		if(p<0||p>8)
			return false;
		return true;
	}
	/**
	 * 
	 * @param p 判断p行是否越界
	 * @return
	 */
	boolean isNotOutOFBoundaryLine(int p) {
		if(p<0||p>9)
			return false;
		return true;
	}
	ArrayList<Pair> juNext(int x,int y) {
		//纵
		ArrayList<Pair> next=new ArrayList<>();
		for(int t=y+1;isNotOutOFBoundaryColumns(t);++t) {
			//有子 --》子的颜色 相同？break：add
			//无子 --》合法位置
			if(allPiece[x][t]==null)
				next.add(new Pair(x,t));
			else if(allPiece[x][t].color==allPiece[x][y].color) 
				break;
			else {
				next.add(new Pair(x,t));
				break;
			}
		}
		for(int t=y-1;isNotOutOFBoundaryColumns(t);--t) {
			//有子 --》子的颜色 相同？break：add
			//无子 --》合法位置
			if(allPiece[x][t]==null)
				next.add(new Pair(x,t));
			else if(allPiece[x][t].color==allPiece[x][y].color) 
				break;
			else {
				next.add(new Pair(x,t));
				break;
			}
		}
		//横
		for(int t=x-1;isNotOutOFBoundaryLine(t);--t) {
			//有子 --》子的颜色 相同？break：add
			//无子 --》合法位置
			if(allPiece[t][y]==null)
				next.add(new Pair(t,y));
			else if(allPiece[t][y].color==allPiece[t][y].color) 
				break;
			else {
				next.add(new Pair(t,y));
				break;
			}
		}
		for(int t=x+1;isNotOutOFBoundaryLine(t);++t) {
			//有子 --》子的颜色 相同？break：add
			//无子 --》合法位置
			if(allPiece[t][y]==null)
				next.add(new Pair(t,y));
			else if(allPiece[t][y].color==allPiece[t][y].color) 
				break;
			else {
				next.add(new Pair(t,y));
				break;
			}
		}
		return next;
	}
	Pair[] maNext(int x,int y) {
		
		
		
		
		return null;
	}
	Pair[] xiangNext(int x,int y) {
		return null;
		
	}
	Pair[] shiNext(int x,int y) {
		return null;
		
	}
	Pair[] jiangNext(int x,int y) {
		return null;
		
	}
	Pair[] paoNext(int x,int y) {
		return null;
		
	}
	Pair[] zuNext(int x,int y) {
		return null;
		
	}
}
