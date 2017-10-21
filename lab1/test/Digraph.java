package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * 
 * @author jtt & gyx
 */
public class Digraph {

  /**
   * 
   * @author jtt & gyx
   */
  private static class Vnode {
    /**
     * 
     * @author jtt & gyx
     */
    public transient String data;
    /**
     * 
     * @author jtt & gyx
     */
    public transient Enode first;
    /**
     * 
     * @author jtt & gyx
     */
    public transient Integer num;
    /**
    * 
    * @author jtt & gyx
    */
    public transient int weight;
    /**
     * 
     * @author jtt & gyx
     */
     public Vnode() {
       weight = 1;
     }
  }
  
  /**
   * 
   * @author jtt & gyx
   */
  private static class Enode {
    /**
     * 
     * @author jtt & gyx
     */
    public transient String data;
    /**
     * 
     * @author jtt & gyx
     */
    public transient Enode next;
    /**
     * 
     * @author jtt & gyx
     */
    public transient boolean visit;
    /**
     * 
     * @author jtt & gyx
     */
    public transient int weight;
    /**
    * 
    * @author jtt & gyx
    */
    public Enode() {
      weight = 0;
      visit = false;
    }
    
  }

  /**
   * 
   * @author jtt & gyx
   */
  final private transient Vnode[] vlist;
  
  /**  constructor
   * 
   * @param str.
   */
  public Digraph(final String... str) {
    final ArrayList<String> set = new ArrayList<String>();
    for (int i = 0;i < str.length;i++) {
      if (!set.contains(str[i])) {
        set.add(str[i]);
      }
    }
    final Object[] schange = set.toArray();
    vlist = new Vnode[set.size()];
    for (int i = 0;i < set.size();i++) {
      vlist[i] = new Vnode();
      vlist[i].data = (String)schange[i];
      vlist[i].first = null;
      vlist[i].num = 0;
    }
    for (int k = 1;k < str.length;k++) {
      final int loc = index(str[k - 1]);
      Enode node1 = vlist[loc].first;
      if (node1 == null) {
        final Enode node = new Enode();
        node.data = str[k];
        node.visit = false;
        node.weight = 1;
        node.next = vlist[loc].first;
        vlist[loc].first = node;
        vlist[loc].num++;
      } else {
        while (node1 != null) {
          if (node1.data.equals(str[k])) {
            node1.weight++;
            break;
          }
          node1 = node1.next;
        }
      }
    }
  }
  
  /**
   * 
   * @author jtt & gyx
   */
  public void show() {
    GraphViz gViz=new GraphViz("D:\\temp", "D:\\download\\graphviz-2.38\\release\\bin\\dot.exe");
    gViz.startGraph();
    for(int i = 0; i<vlist.length; i++) {
      String vert = vlist[i].data;
      Enode next = vlist[i].first;
      while(next!=null){
        gViz.addln(vert+"->"+next.data+" [ label = \""+next.weight+" \"];");
        next = next.next;
      }
    }
    gViz.endGraph();
    gViz.run();
  }
  
  /** find the index of the words in vertex list.
   * 
   * @param  st.
   * @return index of the word.
   */
  public final int index(final String str) {
    int locOfVertexx = -1;
    for (int i = 0;i < vlist.length;i++) {
      if (vlist[i].data.equals(str)) {
        locOfVertexx = i;
      }
      if (i == vlist.length - 1) {
        break;
      }
    }
    return locOfVertexx;
  }

  /** find bridge words between word1 and word2.
   * 
   * @param word1.
   * @param word2.
   * @return bridge words list.
   */
  public final String[] queryBridgeWords(final String word1,final String word2) {
    final int word1Loc = index(word1);
    final int word2Loc = index(word2);
    final ArrayList<String> str = new ArrayList<String>();
    if (word1Loc == -1 && word2Loc != -1) {
      System.out.println("No " + word1 + " in the graph!");
    } else if (word1Loc != -1 && word2Loc == -1) {
      System.out.println("No " + word2 + " in the graph!");
    } else if (word1Loc == -1 && word2Loc == -1) {
      System.out.println("No " + word1 + " and " + word2 + " in the graph!");
    } else {
      Enode eNode = vlist[word1Loc].first;
      while (eNode != null) {
        Enode eNode2 = vlist[index(eNode.data)].first;
        while (eNode2 != null) {
          if (eNode2.data.equals(word2)) {
            str.add(eNode.data);
            break;
          }
          eNode2 = eNode2.next;
        }
        eNode = eNode.next;
      }
    }
    if (str.isEmpty()) {
      System.out.println("No bridge word from " + word1 + " to " + word2);
    } else {
      System.out.println("The bridge words from " + word1 + " to " + word2 + " is " + str);
    }
    String[] newst = (String[])str.toArray(new String[str.size()]); 
    return newst;
  }

  /** calculate random path.
   * 
   * @return random path list
   */
  public ArrayList<String> randWalk() {
    final Random rand = new Random();
    int loc1 = rand.nextInt(vlist.length);
    final ArrayList<String> randomList = new ArrayList<String>();
    randomList.add(vlist[loc1].data);
    while (true) {
      if (vlist[loc1].num == 0) {
        break;
      }
      final int loc2 = rand.nextInt(vlist[loc1].num);
      Enode randNode = vlist[loc1].first;
      for (int k = 0;k < loc2;k++) {
        randNode = randNode.next;
      }
      randomList.add(randNode.data);
      if (randNode.visit) {
        break;
      }  
      randNode.visit = true;
      loc1 = index(randNode.data);
    }
    return randomList;
  }

  /**
   * 
   * @author jtt & gyx
   */
  public final String [] generateNewText(final String ...inputText) {
    final ArrayList<String> newtext = new ArrayList<String>();
    final Random rand = new Random();
    for (int i = 0;i < inputText.length - 1;i++) {
      newtext.add(inputText[i]);
      final String[] bridgeLst = queryBridgeWords(inputText[i],inputText[i + 1]);
      if (bridgeLst.length == 0) {
        continue;
      } else {
        newtext.add(bridgeLst[rand.nextInt(bridgeLst.length)]);
      }
    }
    newtext.add(inputText[inputText.length - 1]);
    String[] newtxtText = (String[])newtext.toArray(new String[newtext.size()]);
    return newtxtText;
  }
  
  /**
   * 
   * @author jtt & gyx
   */
  public final int [] dijkstra(final int sourceVertex, final int vertexNum,final int destVertex) {
    int []stPath;
    stPath = new int [vertexNum];
    int []isVisit;
    isVisit = new int [vertexNum];
    final Queue<Vnode> queue = new PriorityQueue<>();
    //初始化
    final int Inf = Integer.MAX_VALUE;
    for (int i  =  0; i < vertexNum; i++) {
      vlist[i].weight  =  Inf;
      stPath[i]  =  -1;     //每个顶点都无父亲节点
      isVisit[i]  =  0;   //都未找到最短路
    }
    vlist[sourceVertex].weight  =  0;
    queue.add(vlist[sourceVertex]);
    while (!queue.isEmpty()) {
      final Vnode vertexNode  =  queue.element();
      queue.remove();
      final int verLoc = index(vertexNode.data);
      if (isVisit[verLoc] == 1) {
        continue;
      }
      isVisit[verLoc]  =  1;
      Enode edgeNode = vlist[verLoc].first;
      while (edgeNode != null) {
        final int tempv  =  index(edgeNode.data);
        final int tempw  =  edgeNode.weight;
        if (isVisit[tempv] == 0 && vlist[tempv].weight > (vlist[verLoc].weight + tempw)) {
          vlist[tempv].weight  =  vlist[verLoc].weight + tempw;
          stPath[tempv] = verLoc;
          queue.add(vlist[tempv]);
        }
        edgeNode = edgeNode.next;
      }
    }
    System.out.println("最短路径长为：" + vlist[destVertex].weight);
    return stPath;
  }
  
  /**
   * 
   * @author jtt & gyx
   */
  public final String[] calcShortestPath(final String word1, final String word2) {
    final ArrayList<String> pList = new ArrayList<String>();
    int [] stPath;
    final int word1Loc = index(word1);
    int word2Loc = index(word2);
    int count = 0;
    if (word1Loc == -1 || word2Loc == -1) {
      System.out.println("单词不存在");
    } else {
      stPath = dijkstra(word1Loc, vlist.length,word2Loc);
      while (word2Loc != -1) {
        pList.add(vlist[word2Loc].data);
        word2Loc = stPath[word2Loc];
      }
    }
    String[] stPath2 = (String[])pList.toArray(new String[pList.size()]);
    return stPath2;
  }

  /**
   * 
   * @author jtt & gyx
   * @throws IOException 
   */
  public static String[] readfromfile(final String filepath) throws IOException {
    String[] wordsList;
    InputStreamReader bufReader = null;
    try {
        bufReader = new InputStreamReader(new FileInputStream(filepath),"UTF-8");
    } catch (UnsupportedEncodingException | FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    final StringBuilder str0 = new StringBuilder();
    int tempChar = -1;
    try {
        if (bufReader != null) {
            while ((tempChar = bufReader.read()) != -1) {
                if ((char) tempChar == '\n' || (char) tempChar == '\r'){
                    str0.append(String.valueOf(' '));
                }else {
                  str0.append((char) tempChar);
                }
                
              }
        }
        
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } finally {
        if (bufReader != null) {
            bufReader.close();
        }
    }
    
    
    
    final Scanner scan = new Scanner(str0.toString());
    String str = scan.nextLine();
    scan.close();
    str = str.toLowerCase(Locale.ENGLISH);
    str = str.replaceAll("[^a-z]", " ");
    str = str.replaceAll("\\s{1,}", " ");
    wordsList = str.split(" ");
    return wordsList;
  }

  /** main function.
   * 
   * @param args.
   * @throws IOException 
   */
  public static void main(final String[] args) throws IOException {
    final String[] text = readfromfile("D:\\java.txt");
    if (text.length == 0) {
      return;
    }
    final Digraph digrapg = new Digraph(text);
    String aChoise = JOptionPane.showInputDialog("choose function(1,2,3,4,5,6):");
    while (aChoise!=null &&!aChoise.equals("0")) {
      switch (aChoise) {
      case "1":
          digrapg.show();
          break;
        case "3":
          final String word1 = JOptionPane.showInputDialog("input word1:");
          final String word2 = JOptionPane.showInputDialog("input word2:");
          digrapg.queryBridgeWords(word1,word2);
          break;
        case "4":
          final String[] newText = readfromfile("D:\\newtext.txt");
          for (int i = 0;i < newText.length;i++) {
            System.out.print(newText[i] + " ");
          }
          break;
        case "5":
          final String wo1 = JOptionPane.showInputDialog("input word1:");
          final String wo2 = JOptionPane.showInputDialog("input word2:");
          final String [] shotestPath = digrapg.calcShortestPath(wo1,wo2);
          for (int i = shotestPath.length - 1;i >= 0;i--) {
            System.out.print(shotestPath[i] + " ");
          }
          break;
        case "6":
          final ArrayList<String> randList = digrapg.randWalk();
          System.out.println(randList);
          System.out.println("i changed the lab");
          break;

        default:
          break;
      }
      aChoise = JOptionPane.showInputDialog("choose function(1,2,3,4,5,6):");
    }
  }

}