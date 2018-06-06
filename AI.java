package XiangQi;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
/**
 * 很弱的人机
 * @author wuwang
 */
public class AI {
/**当前棋盘*/
	Piece[][] broad;
/**Rules来获得下一步棋可以走合法的位置*/
	Rules kit;
/**评估表*/
	Map<String,Integer> list=new TreeMap<>();
/**前三部固定走法*/
	ArrayList<Step> steps=new ArrayList<>();
/**假设移动来估计ai下一步的不利情况*/
	Step step;
	Piece cover;
	
	int index=0;
	public AI(Piece[][] arg0,Rules arg1) {
		broad=arg0;
		kit=arg1;
		init();
	}
	void init() {
		list.put("shi", 6);
		list.put("xiang", 6);
		list.put("ma", 13);
		list.put("ju", 52);
		list.put("pao", 22);
		list.put("zu", 2);
		list.put("jiang",1000);
		steps.add(new Step(2,7,2,6));
		steps.add(new Step(0,1,2,2));
		steps.add(new Step(3,2,4,2));
	}
/**
 * 移动一步 并记录 用于对立估计
 * @param oldx
 * @param oldy
 * @param newx
 * @param newy
 */
	void move(int oldx,int oldy,int newx,int newy) {
		step=new Step(oldx,oldy,newx,newy);
		if(broad[newx][newy]!=null)
			cover=broad[newx][newy];
		broad[newx][newy]=broad[oldx][oldy];
		broad[oldx][oldy]=null;
	}
/**
 * 还原移动的一步
 */
	void back() {
		broad[step.first.x][step.first.y]=broad[step.second.x][step.second.y];
		if(cover!=null)
			broad[step.second.x][step.second.y]=cover;
		else 
			broad[step.second.x][step.second.y]=null;
		cover=null;
	}
/**
 * 两层搜索来确定ai最优走法
 * @return
 */
	public Get best() 
	{
		ArrayList<Get> all=new ArrayList<>();
		for(int i=0;i<10;++i)
		{
			for(int j=0;j<9;++j)
			{
				if(broad[i][j]!=null&&broad[i][j].color==false)
				{
					ArrayList<Pair> next=kit.pieceNext(i, j);
					for(Pair p:next)
					{
						int score;
						Step step=new Step(i,j,p.x,p.y);
						if(broad[p.x][p.y]!=null)
							score=list.get(broad[p.x][p.y].attr);
						else 
							score=0;
						move(i,j,p.x,p.y);
						Get red=curBest(true);
						back();
						all.add(new Get(score-red.score,step));
					}				
				}
				
			}
		}
		for(Get g:all) {
			System.out.println("score:"+g.score+"\tmove:"+String.format(
					"%d %d %d %d",g.step.first.x,g.step.first.y,g.step.second.x,g.step.second.y));
		}
		return findMax(all);		
	}
/**
 * 对于某颜色当前一步最优走法
 * @param color
 * @return
 */
	public Get curBest(boolean color) 
	{
		ArrayList<Get> all=new ArrayList<>();
		for(int i=0;i<10;++i)
		{
			for(int j=0;j<9;++j)
			{
				if(broad[i][j]!=null&&broad[i][j].color==color)
				{
					ArrayList<Pair> next=kit.pieceNext(i, j);
					for(Pair p:next)
					{
						int score;
						Step step=new Step(i,j,p.x,p.y);
						if(broad[p.x][p.y]!=null)
							score=list.get(broad[p.x][p.y].attr);
						else 
							score=0;
						all.add(new Get(score,step));
					}				
				}
				
			}
		}
		return findMax(all);		
	}
	Get findMax(ArrayList<Get> arr) {
		int max=arr.get(0).score;//Integer.MIN_VALUE
		Get get=arr.get(0);
		for(Get g:arr) {
			if(broad[g.step.first.x][g.step.first.y]!=null
					&&broad[g.step.second.x][g.step.second.y]!=null
					&&broad[g.step.first.x][g.step.first.y].color==broad[g.step.second.x][g.step.second.y].color)
				continue;
			if(g.score>=max) {
				max=g.score;
				get=g;
			}
		}
		return get;
	}
	Step getNext() {
		if(index<steps.size())
			return steps.get(index++);
		else 
			return best().step;
	}
	
}
class Get{
	int score;
	Step step;
	public Get(int arg0,Step arg1) {
		score=arg0;
		step=arg1;
	}
}
