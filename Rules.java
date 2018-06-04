package XiangQi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 表述游戏规则部分
 * 界面与规则处理分离,不然Broad就显得冗长
 * @author wuwang
 */

public class Rules {
	private static Rules obj=new Rules();
	static Piece[][] allPiece;
	Stack<Step> history=new Stack<>();//记录历史Step  
	Stack<Piece> pieceHistory=new Stack<>();//记录被覆盖的棋子
	private Rules() {
	}
	public static Rules getRulesObj(Piece[][] arg0) {
		allPiece=arg0;
		return obj;
	}
	/**
	 * 移动坐标(oldx,oldy)的棋子(newx,newy)
	 * 如果位置不合法不予理会
	 * @param oldx
	 * @param oldy
	 * @param newx
	 * @param newy
	 */
	public void move(int oldx,int oldy,int newx,int newy) {
		//out of boundary
		if(!(isNotOutOFBoundaryLine(oldx)&&isNotOutOFBoundaryLine(newx)))
			return ;
		if(!(isNotOutOFBoundaryColumn(oldy)&&isNotOutOFBoundaryColumn(newy)))
			return ;			
		if(allPiece[oldx][oldy]==null) 
			return ;
		//move棋子自身移动到自身  无意义
		if(oldx==newx&&oldy==newy)
			return ;
		//move到己方棋子 无意义处理
		if(allPiece[newx][newy]!=null&&allPiece[oldx][oldy].color==allPiece[newx][newy].color) 
			return ;
		
		ArrayList<Pair> list=pieceNext(oldx,oldy);
		if(list!=null&&find(list,newx,newy))
			moveTo(oldx, oldy, newx, newy);
	}
		

	//没有执行repaint!
	void moveTo(int oldx,int oldy,int newx,int newy) {	
		if(allPiece[newx][newy]!=null) {//记录每一次移动
			history.push(new Step(new Pair(oldx,oldy),new Pair(newx,newy),true));
			pieceHistory.push(allPiece[newx][newy]);
		}
		else 
			history.push(new Step(new Pair(oldx,oldy),new Pair(newx,newy)));
		
		swap(oldx, oldy, newx, newy);
	}
	int getStepsNum() {
		return history.size();
	}
	//类似C++的move语句  交换资源控制权而已  
	void swap(int oldx,int oldy,int newx,int newy) {
		System.out.println("x:"+oldx+" y:"+oldy+"\t move"+" x"+newx+" y"+newy);
		allPiece[newx][newy]=allPiece[oldx][oldy];
		allPiece[oldx][oldy]=null;
	}
	/**
	 * 
	 * @param num 后退num步棋
	 */
	public void retreat(int num) {
		while(num--!=0) 
			retreat();
	}
	//只后退一步棋
	void retreat() {
		if(!history.isEmpty()) {
			Step step=history.pop();
			swap(step.getSecond().x,step.getSecond().y,step.getFirst().x,step.getFirst().y);
			allPiece[step.getSecond().x][step.getSecond().y]=null;
			if(step.isCovered) {
				System.out.println("run");
				assert(!pieceHistory.isEmpty()):"pieceHistory data exception";
				allPiece[step.getSecond().x][step.getSecond().y]=pieceHistory.pop();
			}		
		}
		
	}
	/**
	 * 讲对局棋谱写到本地
	 * @param path
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	void readChess(String path) throws FileNotFoundException, ClassNotFoundException, IOException {	
		ObjectInputStream in=new ObjectInputStream(new FileInputStream(path));		
		retreat(history.size());
		Object obj=null;
		while((obj=in.readObject())!=null) {
			Step step=(Step)obj;
			System.out.printf("%d %d %d %d \n",step.first.x,step.first.y,step.second.x,step.second.y);
			move(step.first.x,step.first.y,step.second.x,step.second.y);
		}
		in.close();
	}
	/**
	 * 从指定path读取棋谱到当前棋局
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void writeChess(String path) throws FileNotFoundException, IOException {
		ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(path,true));
		for(int i=0,len=history.size();i<len;++i) {
			out.writeObject(history.get(i));
		}
		out.writeObject(null);//写一个null 作为结束的flag
		out.flush();
		out.close();
	}
	/**
	 * 判定胜负
	 * 从pieceHistory已被覆盖的棋子中寻找
	 * @return  0是双方未发生输赢 1是黑胜 2是红胜
	 */
	int isWon() {
		for(int i=0,len=pieceHistory.size();i<len;++i)
		{
			if(pieceHistory.get(i).attr.equals("jiang")&&pieceHistory.get(i).color==true)
				return 1;
			else if(pieceHistory.get(i).attr.equals("jiang")&&pieceHistory.get(i).color==false)
				return 2;
		}
		return 0;
	}
	/**
	 * 打印list中的所有steps
	 * @param list
	 */
	void printNextSteps(ArrayList<Pair> list) {
		for(Pair p:list) {
			System.out.println("x:"+p.x+"\ty"+p.y);
		}
	}
	/**
	 * 在当前棋盘上寻找第一个出现的指定的棋子 
	 * @param attr 棋子
	 * @param color 颜色
	 * @return
	 */
	Pair find(String attr,boolean color) {
		for(int i=0;i<10;++i) {
			for(int j=0;j<9;++j) {
				if(allPiece[i][j]!=null
						&&allPiece[i][j].attr.equals(attr)
						&&allPiece[i][j].color==color) {
					return new Pair(i,j);
				}
			}
		}
		return null;
	}
	boolean find(ArrayList<Pair> list,int x,int y) {
		if(list==null)
			return false;
		for(Pair p:list) {
			if(p.equals(x,y))
				return true;
		}
		return false;
	} 
	/**
	 * 提供矩阵x,y位置的Piece,预测其下一步可以走的位置
	 * @param x 
	 * @param y
	 * @return
	 */
	ArrayList<Pair>  pieceNext(int x,int y){
		switch(allPiece[x][y].attr) {
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
			return paoNext(x,y);
		case "zu":
			return zuNext(x,y);
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
	void addJuNextC(ArrayList<Pair> next,int x,int y,int t) {		
		for(;isNotOutOFBoundaryColumn(t);) {			
			if(allPiece[x][t]==null)
				next.add(new Pair(x,t));
			else if(allPiece[x][t].color==allPiece[x][y].color)//同色
				break;
			else {
				next.add(new Pair(x,t));
				break;
			}
			if(t>y)		++t;
			else		--t;
		}	
	}
	void addJuNextL(ArrayList<Pair> next,int x,int y,int t) {
		for(;isNotOutOFBoundaryLine(t);) {
			if(allPiece[t][y]==null) 
				next.add(new Pair(t,y));
			else if(allPiece[t][y].color==allPiece[x][y].color) 
				break;
			else {
				next.add(new Pair(t,y));
				break;
			}
			if(t>x)		++t;
			else		--t;
		}	
	}
	ArrayList<Pair> juNext(int x,int y) {
		ArrayList<Pair> next=new ArrayList<>();
		//列
		addJuNextC(next,x,y,y+1);
		addJuNextC(next,x,y,y-1);
		//行
		addJuNextL(next,x,y,x+1);
		addJuNextL(next,x,y,x-1);
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
	/**
	 * 象的范围
	 * @param x
	 * @param y
	 * @param color
	 * @return
	 */
	boolean xiangBoundaryOutOf(int x,int y,boolean color) {
		//out of boundary
		if(isNotOutOFBoundaryLine(x)&&isNotOutOFBoundaryColumn(y)) {
			if(color) 
				return x>4&&x<10;
			else 
				return x<5;
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
			if(xiangBoundaryOutOf(nextx,nexty,color)&&
					allPiece[x+flagX[i]][y+flagY[i]]==null) {
				next.add(new Pair(nextx,nexty));
			}
			
		}	
		return next;
	}
	/**
	 * 士和将范围
	 * @param x
	 * @param y
	 * @param color
	 * @return
	 */
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
		//对将
		Pair redJ=find("jiang",true),blackJ=find("jiang",false);
		if(redJ.y==blackJ.y) {
			for(int i=blackJ.x+1;i<redJ.x;++i) {
				if(allPiece[i][redJ.y]!=null)
					return next;
			}
			if(color)
				next.add(blackJ);
			else 
				next.add(redJ);
		}
		return next;
	}
	void addPaoNextStepsL(ArrayList<Pair> next,int t,int a,boolean plus,boolean color) {
		for(;isNotOutOFBoundaryLine(t);) {
			if(allPiece[t][a]==null) 
				next.add(new Pair(t,a));	
			else
			{//遇到棋子 往棋子上方继续寻找可以吃子的位置
				for(t=plus?t+1:t-1;isNotOutOFBoundaryLine(t);t=plus?t+1:t-1) {
					if(allPiece[t][a]!=null&&allPiece[t][a].color!=color) {
						next.add(new Pair(t,a));
						break;
					}					
				}			
			}
			t=plus?t+1:t-1;
		}
	}
	void addPaoNextStepsC(ArrayList<Pair> next,int t,int a,boolean plus,boolean color) {
		for(;isNotOutOFBoundaryColumn(t);) {
			if(allPiece[a][t]==null) {
				next.add(new Pair(a,t));	
			}
			else
			{//遇到棋子 往棋子上方继续寻找可以吃子的位置
				for(t=plus?t+1:t-1;isNotOutOFBoundaryColumn(t);t=plus?t+1:t-1) {
					if(allPiece[a][t]!=null&&allPiece[a][t].color!=color) {
						next.add(new Pair(a,t));
						break;
					}					
				}
			}
			t=plus?t+1:t-1;
		}
	}	
	ArrayList<Pair> paoNext(int x,int y) {
		ArrayList<Pair> next=new ArrayList<>();
		boolean color=allPiece[x][y].color;
		addPaoNextStepsL(next,x-1,y,false,color);
		addPaoNextStepsL(next,x+1,y,true,color);
		addPaoNextStepsC(next,y-1,x,false,color);
		addPaoNextStepsC(next,y+1,x,true,color);
		return next;		
	}
	void addZuNextSteps(ArrayList<Pair> next,int x,int y) {
		if(isNotOutOFBoundaryColumn(y+1))
			next.add(new Pair(x,y+1));
		if(isNotOutOFBoundaryColumn(y-1))
			next.add(new Pair(x,y-1));
	}
	ArrayList<Pair> zuNext(int x,int y) {
		ArrayList<Pair> next=new ArrayList<>();
		if(allPiece[x][y].color) {
			if(x<5)//红过河 
				addZuNextSteps(next,x,y);
			if(isNotOutOFBoundaryLine(x-1)) 
				next.add(new Pair(x-1,y));
		}
		else {
			if(x>4) //黑过河
				addZuNextSteps(next,x,y);
			if(isNotOutOFBoundaryLine(x+1)) 
				next.add(new Pair(x+1,y));				
		}		
		return next;		
	}
}
