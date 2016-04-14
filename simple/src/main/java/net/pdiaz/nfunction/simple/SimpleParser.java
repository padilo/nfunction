package net.pdiaz.nfunction.simple;

import net.pdiaz.nfunction.base.FunctionInfo;
import net.pdiaz.nfunction.base.Generator;
import net.pdiaz.nfunction.base.Parser;

import java.io.File;
import java.io.IOException;

/**
 * Created by pablo.diaz on 14/4/16.
 */
public class SimpleParser implements Parser{
  private final File file;
  private final String encoding;

  public SimpleParser(File file, String encoding) {
    this.file = file;
    this.encoding = encoding;
  }

  @Override
  public void parse(String classBaseName, Generator generator, FunctionInfo functionInfo) throws IOException {
    System.out.println("file = " + file);
    System.out.println("classBaseName = " + classBaseName);
  }
}
