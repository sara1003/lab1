import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import javax.swing.JOptionPane;

public class DG {

	private class vnode{
		String data;   //111111
		enode first;
		Integer num;
		int w;
	}
	private class enode{
		String data;
		enode next;
		boolean visit;
		int w;
	}
	private vnode[] vlist;

	public DG(String[] str)
	{
		ArrayList<String> set=new ArrayList<String>();
		for(int i=0;i<str.length;i++)
		{
			if(!set.contains(str[i]))
			{
				set.add(str[i]);
			}
		}
		Object sa[]=set.toArray();
		vlist=new vnode[set.size()];
	    for(int i=0;i<set.size();i++)
	    {
	    	//System.out.println(set[i]);
	    	vlist[i]=new vnode();
	    	vlist[i].data=(String)sa[i];
	    	vlist[i].first=null;
	    	vlist[i].num=0;
	    }
	    for(int k=1;k<str.length;k++)
	    {
	    	int j=index(str[k-1]);
	    	enode node1=vlist[j].first;
	    	if(node1!=null)
	    	{
	    	while(node1!=null)
	    	{
	    		if(node1.data.equals(str[k]))
	    		{
	    			node1.w++;
	    			break;
	    		}
	    		node1=node1.next;
	    	}
	    	}
	    	else
	    	{
	    	enode node=new enode();
	    	node.data=str[k];
	    	node.visit=false;
	    	node.w=1;
	    	node.next=vlist[j].first;
	    	vlist[j].first=node;
	    	vlist[j].num++;
	    	}

	    }
}

	public int index(String st)
	{
		//System.out.println(vlist.length);
		for(int i=0;i<vlist.length;i++)
		{
			if((vlist[i].data).equals(st))
			{
				//System.out.println(i);
				return i;
			}
			if(i==(vlist.length-1))
			{
				return -1;
			}
		}

		return -1;
	}

	public String[] queryBridgeWords(String word1,String word2)
	{
		int m=index(word1);
		int n=index(word2);
		//System.out.println(m);
		ArrayList<String> str=new ArrayList<String>();
		if((m==-1)&&(n!=-1))
			System.out.println("No "+word1+" in the graph!");
		else if((m!=-1)&&(n==-1))
			System.out.println("No "+word2+" in the graph!");
		else if((m==-1)&&(n==-1))
			System.out.println("No "+word1+" and "+word2+" in the graph!");
		else
		{

			int i=0;
			enode e=vlist[m].first;
			while(e!=null)
			{
					enode e2=vlist[index(e.data)].first;
					while(e2!=null)
					{
						if(e2.data.equals(word2))
						{
							str.add(e.data);
						    break;
						}

						e2=e2.next;
					}

				e=e.next;
			}

		}
		if(str.size()==0)
			System.out.println("No bridge word from "+word1+" to "+word2);
		else
		{
			System.out.println("The bridge words from "+word1+" to "+word2+" is "+str);
		}
		String []newstr=new String[str.size()];
		Object []sa=str.toArray();
		for(int i=0;i<str.size();i++)
		{
			newstr[i]=(String)sa[i];

		}
		//Object [] newstr = (String[])str.toArray() ;
	    return newstr;
	}

	public ArrayList<String> randWalk() {

		Random r=new Random();
		int n1=r.nextInt(vlist.length);
		ArrayList<String> s=new ArrayList<String>();
		s.add(vlist[n1].data);
		while(true)
		{
			if(vlist[n1].num==0)
				return s;
			int n2=r.nextInt(vlist[n1].num);
			enode node=vlist[n1].first;
			for(int k=0;k<n2;k++)
			{
				node=node.next;
			}
			s.add(node.data);
			if(node.visit==true)
				return s;
			node.visit=true;
			n1=index(node.data);
		}
	}

	String [] generateNewText(String []inputText)
	{

		ArrayList <String> newtext= new ArrayList<String>();
		Random r=new Random();
		for (int i=0;i<inputText.length-1;i++)
		{
			newtext.add(inputText[i]);
			String l[]=queryBridgeWords(inputText[i],inputText[i+1]);
			if(l.length==0)
				continue;
			else
			{
				newtext.add(l[r.nextInt(l.length)]);
			}
		}
		newtext.add(inputText[inputText.length-1]);
		String []newtxt=new String[newtext.size()];
		Object []sa=newtext.toArray();

		for(int i=0;i<newtext.size();i++)
		{
			newtxt[i]=(String)sa[i];
		}
		return newtxt;
	}

	int [] Dijkstra(int v0, int n,int s0)
	{
		int []path;
		path=new int [n];
		int []visited;
		visited=new int [n];
		//Node dist[maxnum];
		//vnode []dist;
		//dist=new vnode [n];
		Queue<vnode> q=new PriorityQueue<>();
	    //初始化
	    final int INF=Integer.MAX_VALUE;
	    for(int i = 0; i < n; i++)
	    {
	        vlist[i].w = INF;
	        path[i] = -1;       //每个顶点都无父亲节点
	        visited[i] = 0;     //都未找到最短路
	    }
	    vlist[v0].w = 0;
	    q.add(vlist[v0]);
	    while(!q.isEmpty())
	    {
	        vnode cd = q.element();
	        q.poll();
	        int u=index(cd.data);
	        if(visited[u]==1)
	            continue;
	        visited[u] = 1;
	        enode p=vlist[u].first;
	        while(p!=null)
	        {
	            int tempv = index(p.data);
	            int tempw = p.w;
	            if(visited[tempv]==0 && (vlist[tempv].w > (vlist[u].w+tempw)))
	            {
	                vlist[tempv].w = vlist[u].w+tempw;
	                path[tempv] = u;
	                q.add(vlist[tempv]);
	            }
	            p = p.next;
	        }
	    }
	    System.out.println("最短路径长为："+vlist[s0].w);
	    return path;
	}
	String[] calcShortestPath(String word1, String word2)
	{
		ArrayList <String> p= new ArrayList<String>();
		int []Pa;
		int w1=index(word1);
		int w2=index(word2);
		int i=0;
		/*
		if(w1!=-1&&w2!=-1)
		{
			Pa=Dijkstra(w1, vlist.length,w2);
			while(w2!=-1)
			{
				p.add(vlist[w2].data);
				w2=Pa[w2];
			}
		}
		else if(w1!=-1&&word2.length()==0)
		{
			for(i=0;i<vlist.length;i++)
			{
				if(i==w1)
					continue;
				System.out.print(vlist[w1].data+"到"+vlist[i].data);
				w2=i;
				Pa=Dijkstra(w1, vlist.length,w2);
				while(w2!=-1)
				{
					p.add(vlist[w2].data);
					w2=Pa[w2];
				}
			}

		}
		*/
		if(w1==-1||w2==-1)
		{
			System.out.println("单词不存在");
		}
		else
		{
			Pa=Dijkstra(w1, vlist.length,w2);
			while(w2!=-1)
			{
				p.add(vlist[w2].data);
				w2=Pa[w2];
			}
		}
		String []path=new String[p.size()];
		Object []sa=p.toArray();
		for(i=0;i<p.size();i++)
		{
			path[i]=(String)sa[i];

		}
		return path;
	}

	static String[] readfromfile(String filepath)
	{
	     try
	     {
			FileReader fr=new FileReader(filepath);
			BufferedReader br=new BufferedReader(fr);
			String sf=null;
			String s2="";
	        while((sf=br.readLine())!=null){
	            s2=s2+sf+" ";
	        }
	        br.close();
            fr.close();

			Scanner sc=new Scanner(s2);
		    String str=sc.nextLine();
		    str=str.toLowerCase();
		    str=str.replaceAll("[^a-z]", " ");
		    str=str.replaceAll("\\s{1,}", " ");
		    String []sl=str.split(" ");
		    return sl;
	     }catch(Exception e)
		{
			e.printStackTrace();
		}
	     return null;
	}
	public static void main(String[] args) {
		   String s3[]=readfromfile("D:\\lab1\\java.txt");
		    DG dg=new DG(s3);
			/*String []s4= {"seek","to","explore","new","and","exciting"};
			String []s5=dg.generateNewText(s4);

			for(int i=0;i<s5.length;i++)
	        {
	        	System.out.println(s5[i]);
	        }
			ArrayList<String> s6=dg.randWalk();

	        System.out.println(s6);

	        String []s7=dg.calcShortestPath("to","explore");
	        for(int i=0;i<s7.length;i++)
	        {
	        	System.out.println(s7[s7.length-i-1]);
	        }*/



	            String a=JOptionPane.showInputDialog("choose function(1,2,3,4,5,6):");
	            while(a!="0")
	            {
	        	switch(a)
	        	{
	        	case "1":
	        	{/*
	        		GraphViz gViz=new GraphViz("C:\\Users\\dell\\Desktop", "D:\\Program Files\\graphviz2.38\\bin\\dot.exe");
	    	        gViz.start_graph();
	    	        for(int i=0;i<s3.length-1;i++)
	    	        {
	    	        	gViz.addln(s3[i]+"->"+s3[i+1]);
	    	        }
	    	        gViz.end_graph();
	    	        gViz.run();
	    	        break;*/
	        	}
	        	case "2":
	        		break;
	        	case "3":
	        		String w1=JOptionPane.showInputDialog("input word1:");
	        		String w2=JOptionPane.showInputDialog("input word2:");
	        		String s4[]=dg.queryBridgeWords(w1,w2);
	        		break;
	        	case "4":
	        		String s5[]=readfromfile("F:\\lab1\\newtext.txt");
	        		for(int i=0;i<s5.length;i++)
	        		{
	        			System.out.print(s5[i]+" ");
	        		}
	        		break;
	        	case "5":
	        		String wo1=JOptionPane.showInputDialog("input word1:");
	        		String wo2=JOptionPane.showInputDialog("input word2:");
	        		String []s7=dg.calcShortestPath(wo1,wo2);
	        		for(int i=s7.length-1;i>=0;i--)
	        		{
	        			System.out.print(s7[i]+" ");
	        		}
	        		break;
	        	case "6":
	        		ArrayList<String> s6=dg.randWalk();
    	            System.out.println(s6);
    	            System.out.println("i changed the lab");
	    	        break;

	        	default:
	        		break;
	        	}
	            a=JOptionPane.showInputDialog("choose function(1,2,3,4,5,6):");
	            }
	}

}
