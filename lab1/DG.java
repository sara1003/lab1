import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Dg {

  private class Vnode {
    String data;
    Enode first;
    Integer num;
    int weight;
  }
  
  private class Enode {
    String data;
    Enode next;
    boolean visit;
    int weight;
  }
  
  private Vnode[] vlist;

  /**  constructor
   * 
   * @param str.
   */
  public Dg(String[] str) {
    ArrayList<String> set = new ArrayList<String>();
    for (int i = 0;i < str.length;i++) {
      if (!set.contains(str[i])) {
        set.add(str[i]);
      }
    }
    Object[] sa = set.toArray();
    vlist = new Vnode[set.size()];
    for (int i = 0;i < set.size();i++) {
      vlist[i] = new Vnode();
      vlist[i].data = (String)sa[i];
      vlist[i].first = null;
      vlist[i].num = 0;
    }
    for (int k = 1;k < str.length;k++) {
      int j = index(str[k - 1]);
      Enode node1 = vlist[j].first;
      if (node1 != null) {
        while (node1 != null) {
          if (node1.data.equals(str[k])) {
            node1.weight++;
            break;
          }
          node1 = node1.next;
        }
      } else {
        Enode node = new Enode();
        node.data = str[k];
        node.visit = false;
        node.weight = 1;
        node.next = vlist[j].first;
        vlist[j].first = node;
        vlist[j].num++;
      }
    }
  }
  
  /** find the index of the words in vertex list.
   * 
   * @param  st.
   * @return index of the word.
   */
  public int index(String st) {
    for (int i = 0;i < vlist.length;i++) {
      if ((vlist[i].data).equals(st)) {
        return i;
      }
      if (i == (vlist.length - 1)) {
        return -1;
      }
    }
    return -1;
  }

  /** find bridge words between word1 and word2.
   * 
   * @param word1.
   * @param word2.
   * @return bridge words list.
   */
  public String[] queryBridgeWords(String word1,String word2) {
    int m = index(word1);
    int n = index(word2);
    ArrayList<String> str = new ArrayList<String>();
    if ((m == -1) && (n != -1)) {
      System.out.println("No " + word1 + " in the graph!");
    } else if ((m != -1) && (n == -1)) {
      System.out.println("No " + word2 + " in the graph!");
    } else if ((m == -1) && (n == -1)) {
      System.out.println("No " + word1 + " and " + word2 + " in the graph!");
    } else {
      Enode e = vlist[m].first;
      while (e != null) {
        Enode e2 = vlist[index(e.data)].first;
        while (e2 != null) {
          if (e2.data.equals(word2)) {
            str.add(e.data);
            break;
          }
          e2 = e2.next;
        }
        e = e.next;
      }
    }
    if (str.size() == 0) {
      System.out.println("No bridge word from " + word1 + " to " + word2);
    } else {
      System.out.println("The bridge words from " + word1 + " to " + word2 + " is " + str);
    }
    String []newstr = new String[str.size()];
    Object []sa = str.toArray();
    for (int i = 0;i < str.size();i++) {
      newstr[i] = (String)sa[i];
    }
    return newstr;
  }

  /** calculate random path.
   * 
   * @return random path list
   */
  public ArrayList<String> randWalk() {
    Random r = new Random();
    int n1 = r.nextInt(vlist.length);
    ArrayList<String> s = new ArrayList<String>();
    s.add(vlist[n1].data);
    while (true) {
      if (vlist[n1].num == 0) {
        return s;
      }
      int n2 = r.nextInt(vlist[n1].num);
      Enode node = vlist[n1].first;
      for (int k = 0;k < n2;k++) {
        node = node.next;
      }
      s.add(node.data);
      if (node.visit == true) {
        return s;
      }  
      node.visit = true;
      n1 = index(node.data);
    }
  }

  String [] generateNewText(String []inputText) {
    ArrayList<String> newtext = new ArrayList<String>();
    Random r = new Random();
    for (int i = 0;i < inputText.length - 1;i++) {
      newtext.add(inputText[i]);
      String[] l = queryBridgeWords(inputText[i],inputText[i + 1]);
      if (l.length == 0) {
        continue;
      } else {
        newtext.add(l[r.nextInt(l.length)]);
      }
    }
    newtext.add(inputText[inputText.length - 1]);
    String []newtxt = new String[newtext.size()];
    Object []sa = newtext.toArray();

    for (int i = 0;i < newtext.size();i++) {
      newtxt[i] = (String)sa[i];
    }
    return newtxt;
  }

  int [] dijkstra(int v0, int n,int s0) {
    int []path;
    path = new int [n];
    int []visited;
    visited = new int [n];
    Queue<Vnode> q = new PriorityQueue<>();
    //初始化
    final int Inf = Integer.MAX_VALUE;
    for (int i  =  0; i < n; i++) {
      vlist[i].weight  =  Inf;
      path[i]  =  -1;     //每个顶点都无父亲节点
      visited[i]  =  0;   //都未找到最短路
    }
    vlist[v0].weight  =  0;
    q.add(vlist[v0]);
    while (!q.isEmpty()) {
      Vnode cd  =  q.element();
      q.poll();
      int u = index(cd.data);
      if (visited[u] == 1) {
        continue;
      }
      visited[u]  =  1;
      Enode p = vlist[u].first;
      while (p != null) {
        int tempv  =  index(p.data);
        int tempw  =  p.weight;
        if (visited[tempv] == 0 && (vlist[tempv].weight > (vlist[u].weight + tempw))) {
          vlist[tempv].weight  =  vlist[u].weight + tempw;
          path[tempv] = u;
          q.add(vlist[tempv]);
        }
        p = p.next;
      }
    }
    System.out.println("最短路径长为：" + vlist[s0].weight);
    return path;
  }
  
  String[] calcShortestPath(String word1, String word2) {
    ArrayList<String> p = new ArrayList<String>();
    int [] pa;
    int w1 = index(word1);
    int w2 = index(word2);
    int i = 0;
    if (w1 == -1 || w2 == -1) {
      System.out.println("单词不存在");
    } else {
      pa = dijkstra(w1, vlist.length,w2);
      while (w2 != -1) {
        p.add(vlist[w2].data);
        w2 = pa[w2];
      }
    }
    String []path = new String[p.size()];
    Object []sa = p.toArray();
    for (i = 0;i < p.size();i++) {
      path[i] = (String)sa[i];
    }
    return path;
  }

  static String[] readfromfile(String filepath) {
    try {
      FileReader fr = new FileReader(filepath);
      BufferedReader br = new BufferedReader(fr);
      String sf = null;
      String s2 = "";
      while ((sf = br.readLine()) != null) {
        s2 = s2 + sf + " ";
      }
      br.close();
      fr.close();

      Scanner sc = new Scanner(s2);
      String str = sc.nextLine();
      sc.close();
      str = str.toLowerCase();
      str = str.replaceAll("[^a-z]", " ");
      str = str.replaceAll("\\s{1,}", " ");
      String []sl = str.split(" ");
      return sl;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /** main function.
   * 
   * @param args.
   */
  public static void main(String[] args) {
    String[] s3 = readfromfile("D:\\lab1\\java.txt");
    Dg dg = new Dg(s3);
    String a = JOptionPane.showInputDialog("choose function(1,2,3,4,5,6):");
    while (a != "0") {
      switch (a) {
        case "3":
          String w1 = JOptionPane.showInputDialog("input word1:");
          String w2 = JOptionPane.showInputDialog("input word2:");
          dg.queryBridgeWords(w1,w2);
          break;
        case "4":
          String[] s5 = readfromfile("F:\\lab1\\newtext.txt");
          for (int i = 0;i < s5.length;i++) {
            System.out.print(s5[i] + " ");
          }
          break;
        case "5":
          String wo1 = JOptionPane.showInputDialog("input word1:");
          String wo2 = JOptionPane.showInputDialog("input word2:");
          String []s7 = dg.calcShortestPath(wo1,wo2);
          for (int i = s7.length - 1;i >= 0;i--) {
            System.out.print(s7[i] + " ");
          }
          break;
        case "6":
          ArrayList<String> s6 = dg.randWalk();
          System.out.println(s6);
          System.out.println("i changed the lab");
          break;

        default:
          break;
      }
      a = JOptionPane.showInputDialog("choose function(1,2,3,4,5,6):");
    }
  }

}