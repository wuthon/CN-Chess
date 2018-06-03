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
		if(allPiece[newx][newy]!=null&&allPiece[oldx][oldy].color==allPiece[newx][newy].color) {
			return ;
		}

		ArrayList<Pair> list=pieceNext(oldx,oldy);
		if(list==null) {
			return ;
		}
		for(Pair p:list) {
			System.out.println("x:"+p.x+"\ty"+p.y);
		}
		if(find(list,newx,newy)) {	
			moveTo(oldx, oldy, newx, newy);
		}
			
	}
	//类似C++的move语句  交换资源控制权而已  
	//没有执行repaint!
	void moveTo(int oldx,int oldy,int newx,int newy) {
		System.out.println("x:"+oldx+"y:"+oldy+"\tmove"+"x"+newx+"y"+newy);
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
		if(list==null)
			return false;
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
			return maNext(x,y);
		case "xiang":
			return xiangNext(x,y);
		case "shi":
			return shiNext(x,y);
		case "jiang":
			return jiangNext(x,y);
		case "pao":
			break;
		case "zu":
			break;
		}		
		return null;
	}
	/**
	 * 
	 * @param 判断p列是否越界
	 * @return
	 */
	boolean isNotOutOFBoundaryColumn(int p) {
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
	/**
	 * 根据坐标预测下一步可走的范围
	 * @param x 当前纵列x
	 * @param y 当前纵列y
	 * @param color 棋子红黑颜色不同范围
	 * @return
	 */
	ArrayList<Pair> juNext(int x,int y) {
		//有子 --》子的颜1色 相同？break：add
		//无子 --》合法位置
		//纵
		ArrayList<Pair> next=new ArrayList<>();
		for(int t=y+1;isNotOutOFBoundaryColumn(t);++t) {			
			if(allPiece[x][t]==null)
				next.add(new Pair(x,t));
			else if(allPiece[x][t].color==allPiece[x][y].color) 
				break;
			else {
				next.add(new Pair(x,t));
				break;
			}
		}
		for(int t=y-1;isNotOutOFBoundaryColumn(t);--t) {
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
			if(allPiece[t][y]==null)
				next.add(new Pair(t,y));
			else if(allPiece[t][y].color==allPiece[x][y].color) 
				break;
			else {
				next.add(new Pair(t,y));
				break;
			}
		}
		for(int t=x+1;isNotOutOFBoundaryLine(t);++t) {
			if(allPiece[t][y]==null) {
				next.add(new Pair(t,y));
				continue;
				}
			else if(allPiece[t][y].color==allPiece[x][y].color) 
				break;
			else {
				next.add(new Pair(t,y));
				break;
			}
		}
		return next;
	}
	ArrayList<Pair>  maNext(int x,int y) {	
		ArrayList<Pair> next=new ArrayList<>();
		boolean color=allPiece[x][y].color;
		int[] nextX=new int[] {
				-1,-2,-2,-1,1,2, 2, 1
		};
		int[] nextY=new int[] {
				-2,-1, 1, 2,2,1,-1,-2
		};
		for(int i=0;i<8;++i) {
			//out of boundary
			int nextx=x+nextX[i],nexty=y+nextY[i];
			if(isNotOutOFBoundaryLine(nextx)&&isNotOutOFBoundaryColumn(nexty))
			{
				if(allPiece[nextx][nexty]!=null&&color==allPiece[nextx][nexty].color)//equal color
					continue;
				//别马腿
				if(nextX[i]==2&&allPiece[x+1][y]==null) {
					next.add(new Pair(nextx,nexty));
				}
				else if(nextX[i]==-2&&allPiece[x-1][y]==null) {
					next.add(new Pair(nextx,nexty));
				}
				else if(nextY[i]==2&&allPiece[x][y+1]==null) {
					next.add(new Pair(nextx,nexty));
				}
				else if(nextY[i]==-2&&allPiece[x][y-1]==null) {
					next.add(new Pair(nextx,nexty));
				}							
			}	
		}
		return next;
	}
	//可以写成lambda表达式
	boolean xiangBoundaryOutOf(int x,int y,boolean color) {
		//out of boundary
		if(isNotOutOFBoundaryLine(x)&&isNotOutOFBoundaryColumn(y)) {
			if(color) 
				return x>4&&x<10;
			else 
				return y<5;
		}
		else return false;
	}
	ArrayList<Pair> xiangNext(int x,int y) {
		ArrayList<Pair> next=new ArrayList<>();
		boolean color=allPiece[x][y].color;
		int[] nextX=new int[] {
			-2,-2,2, 2
		};
		int[] nextY=new int[] {
			-2, 2,2,-2
		};
		int[] flagX=new int[4];
		int[] flagY=new int[4];
		for(int i=0;i<flagX.length;++i){
			flagX[i]=nextX[i]/2;
		}
		for(int i=0;i<flagY.length;++i){
			flagY[i]=nextY[i]/2;
		}
		for(int i=0;i<4;++i) {
			int nextx=x+nextX[i],nexty=y+nextY[i];
			if(xiangBoundaryOutOf(nextx,nexty,color)) {
				if(allPiece[x+flagX[i]][y+flagY[i]]==null) {
					next.add(new Pair(nextx,nexty));
				}
			}
			
		}	
		return next;
	}
	boolean ScopeOfShiOrJiang(int x,int y,boolean color) {
		//out of boundary
		if(isNotOutOFBoundaryLine(x)&&isNotOutOFBoundaryColumn(y)&&y>2&&y<6) {
			if(color) 
				return x>6;
			else 
				return x<3;	
		}
		else return false;			
	}
	ArrayList<Pair> shiNext(int x,int y) {
		ArrayList<Pair> next=new ArrayList<>();
		boolean color=allPiece[x][y].color;
		int[] nextX=new int[] {
			-1,-1,1, 1
		};
		int[] nextY=new int[] {
			-1, 1,1,-1
		};
		for(int i=0;i<4;++i) {
			int nextx=x+nextX[i],nexty=y+nextY[i];
			if(ScopeOfShiOrJiang(nextx,nexty,color)) {
				next.add(new Pair(nextx,nexty));
			}
		}
		return next;
	}
	ArrayList<Pair> jiangNext(int x,int y) {
		ArrayList<Pair> next=new ArrayList<>();
		boolean color=allPiece[x][y].color;
		int[] nextStep=new int[] {
			-1,1
		};
		for(int i=0;i<2;++i) {
			int nextx=x+nextStep[i];
			if(ScopeOfShiOrJiang(nextx,y,color)) {
				next.add(new Pair(nextx,y));
			}
		}
		for(int i=0;i<2;++i) {
			int nexty=y+nextStep[i];
			if(ScopeOfShiOrJiang(x,nexty,color)) {
				next.add(new Pair(x,nexty));
			}
		}
		return next;
	}
	ArrayList<Pair> paoNext(int x,int y) {
		ArrayList<Pair> next=new ArrayList<>();
		
		
		return next;		
	}
	ArrayList<Pair> zuNext(int x,int y) {
		ArrayList<Pair> next=new ArrayList<>();
		
		
		return next;
		
	}
}
