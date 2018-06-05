package XiangQi;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AI {
/**当前棋盘*/
	Piece[][] broad;
/**Rules来获得下一步棋可以走合法的位置*/
	Rules kit;
/**AI使用棋子颜色*/
	boolean aiColor=false;
	Map<String,Integer> table=new TreeMap<>();
	ArrayList<Step> steps=new ArrayList<>(); 
	int index=0;
	int a,b,c,d;
	Piece cover;
	public AI(Piece[][] arg0,Rules arg1) {
		broad=arg0;
		kit=arg1;
		init();
	}
	public AI(Piece[][] arg0,Rules arg1,boolean arg2) {
		this(arg0,arg1);
		aiColor=arg2;
	}
	/**
	 * ai(x1,y1)移动到(x2,y2)对于ai不利的的评估
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	int get(int oldx,int oldy,int newx,int newy) {
		int get;
		if(broad[oldx][oldy]!=null&&broad[oldx][oldy].color!=aiColor) 
			get=table.get(broad[newx][newy].attr);
		else  get=0;
		kit.move(oldx, oldy, newx, newy);
		GenPair<Integer, Step> max=find(true);
		System.out.println("back");
		kit.retreat();
		return get-max.first;
	}
	void pMove(int x1,int y1,int x2,int y2) {
		a=x1;
		b=y1;
		c=x2;
		d=y2;
		if(broad[x2][y2]!=null) 
			cover=broad[x2][y2];
		swap(x1, x2, x2, y2);		
	}
	void swap(int oldx,int oldy,int newx,int newy) {
		broad[newx][newy]=broad[oldx][oldy];
		broad[oldx][oldy]=null;
	}
	void back() {
		broad[a][b]=broad[c][d];
		if(cover!=null)
			broad[c][d]=cover;
		else broad[c][d]=null;
		cover=null;
	}
	Step best2() {
		ArrayList<GenPair<Integer,Step>> arr=new ArrayList<>();
	
		for(int i=0;i<10;++i) {
			for(int j=0;j<9;++j) {
				if(broad[i][j]!=null&&broad[i][j].color==aiColor) {
					ArrayList<Pair> next=kit.pieceNext(i, j);
					for(Pair p:next) {
						int get=get(i,j,p.x,p.y);
						Step step=new Step(i,j,p.x,p.y);
						arr.add(new GenPair<Integer, Step>(get,step));						
					}					
				}				
			}
		}
		return getMax(arr).second;
	}
	/**
	 * 对于红或黑下一步的最有解
	 * @param color
	 * @return
	 */
	GenPair<Integer, Step> find(boolean color ) {
		ArrayList<GenPair<Integer,Step>> evaluateNext=new ArrayList<>();
		for(int i=0;i<10;++i) {
			for(int j=0;j<9;++j) {
				if(broad[i][j]!=null&&broad[i][j].color==color) {
					ArrayList<Pair> next=kit.pieceNext(i, j);
					for(Pair p:next) {
						Step step=new Step(i,j,p.x,p.y);
						if(broad[p.x][p.y]!=null&&broad[p.x][p.y].color!=color) {
							Integer get=table.get(broad[p.x][p.y].attr);
							evaluateNext.add(new GenPair<Integer, Step>(get,step));
						}
						else {
							evaluateNext.add(new GenPair<Integer, Step>(0,step));
						}							
					}
					
				}
			}
		}
			
		return getMax(evaluateNext);
	}
	GenPair<Integer,Step> getMax(ArrayList<GenPair<Integer,Step>> arr) {
		Integer max=Integer.MIN_VALUE;
		GenPair<Integer,Step> best=null;
		for(GenPair<Integer,Step> step:arr) {
			if(step.first>max) {
				max=step.first;
				best=step;
			}
		}
		return best;			
	}
	Step next(boolean r) {
		if(r) {
			if(++index<=steps.size()) 
				return steps.get(index-1);
			else
				return best2();
		}
		else
			return best2();
		
	}
	void init() {
		table.put("shi", 6);
		table.put("xiang", 6);
		table.put("ma", 13);
		table.put("ju", 52);
		table.put("pao", 22);
		table.put("zu", 2);
		table.put("jiang",1000);
		steps.add(new Step(2,7,2,6));
		steps.add(new Step(0,1,2,2));
		steps.add(new Step(3,2,4,2));
	}
}
