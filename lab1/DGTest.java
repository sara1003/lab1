import static org.junit.Assert.*;

import org.junit.Test;

public class DGTest {

  String []str={"to", "explore", "strange", "new", "worlds", "to", 
      "seek", "out", "new", "life", "and","new","civilizations"};
  public DG dg=new DG(str);
  @Test
  public void testCalcShortestPath6() {
    String str1= "";
    String str2=dg.calcShortestPath("to$","new");
    assertEquals(str1,str2);
  }
  @Test
  public void testCalcShortestPath5() {
    String str1= "";
    String str2=dg.calcShortestPath("not","worlds");
    assertEquals(str1,str2);
  }
  @Test
  public void testCalcShortestPath4() {
    String str1= "";
    String str2=dg.calcShortestPath("a","worlds");
    assertEquals(str1,str2);
  }
  
  
  @Test
  public void testCalcShortestPath3() {
    String str1= "";
    String str2=dg.calcShortestPath("","");
    assertEquals(str1,str2);
  }
  @Test
  public void testCalcShortestPath2() {
    String str1= "new,strange,explore,to";
    String str2=dg.calcShortestPath("to","new");
    assertEquals(str1,str2);
  }
  @Test
  public void testCalcShortestPath1() {
    String str1= "to,explore,to,strange,explore,to,new,strange,explore,to,worlds,new,strange,explore,to,seek,out,life,and,civilizations";
    String str2=dg.calcShortestPath("to","");
    assertEquals(str1,str2);
  }

}
