package net.pdiaz.nfunction.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by pablo.diaz on 4/1/16.
 */
public class Generator {
  private File outputFolder;

  public Generator(File outputFolder) {
    this.outputFolder = outputFolder;

    outputFolder.mkdirs();
  }

  public Writer createClass(String classBaseName) throws IOException {
    File file = new File(outputFolder, classBaseName + ".java");

    return new FileWriter(file);
  }
}
