
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * 
 * @author jtt & gyx
 */
class GraphViz {
  /**
   * 
   * @author jtt & gyx
   */
  private transient String runPath = "";
  /**
   * 
   * @author jtt & gyx
   */
  private transient String dotPath = "";
  /**
   * 
   * @author jtt & gyx
   */
  private transient String runOrder = "";
  /**
   * 
   * @author jtt & gyx
   */
  private final static transient String DOTCODEFILE = "dotcode.txt";
  /**
   * 
   * @author jtt & gyx
   */
  private final static transient String RESULTGIF = "dotGif";
  /**
   * 
   * @author jtt & gyx
   */
  private final transient StringBuilder graph = new StringBuilder(20);
  
  /**
   * 
   * @author jtt & gyx
   */
  private final transient Runtime runtime = Runtime.getRuntime();

  /**
   * 
   * @author jtt & gyx
   */
  private final static Logger LOG = Logger.getLogger("LAB1"); 

  /**
   * 
   * @author jtt & gyx
   */
  public void run() {
    final File file = new File(runPath);
    file.mkdirs();
    writeGraphToFile(graph.toString(), runPath);
    creatOrder();
    try {
      runtime.exec(runOrder);
    } catch (IOException e) {
      LOG.fine(e.toString());
    }
  }

  /**
   * 
   * @author jtt & gyx
   */

  public void creatOrder() {
    runOrder += dotPath + " ";
    runOrder += runPath;
    runOrder += "\\" + DOTCODEFILE + " ";
    runOrder += "-T gif ";
    runOrder += "-o ";
    runOrder += runPath;
    runOrder += "\\" + RESULTGIF + ".gif";
  }

  /**
   * 
   * @author jtt & gyx
   */

  public final void writeGraphToFile(final String dotcode, final String filename) {
    try {
      final File file = new File(filename + "\\" + DOTCODEFILE);
      if (!file.exists()) {
        file.createNewFile();
      }
      final FileOutputStream fos = new FileOutputStream(file);
      fos.write(dotcode.getBytes());
      fos.close();
    } catch (IOException ioe) {
      LOG.fine(ioe.toString());
    }
  }

  /**
   * 
   * @author jtt & gyx
   */
  public GraphViz(final String runPath, final String dotPath) {
    this.runPath = runPath;
    this.dotPath = dotPath;
  }

  /**
   * 
   * @author jtt & gyx
   */
  public final void add(final String line) {
    graph.append(String.valueOf('\t'));
    graph.append(line);
  }

  /**
   * 
   * @author jtt & gyx
   */
  public void addln(final String line) {
    graph.append("\t" + line + "\n");
  }

  /**
   * 
   * @author jtt & gyx
   */
  public void addln() {
    graph.append('\n');
  }

  /**
   * 
   * @author jtt & gyx
   */
  public void startGraph() {
    graph.append("digraph G {\n");
  }

  /**
   * 
   * @author jtt & gyx
   */
  public void endGraph() {
    graph.append(String.valueOf('}'));
  }
}
