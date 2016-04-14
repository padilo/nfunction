package net.pdiaz.nfunction.base;

import java.io.IOException;

/**
 * Created by pablo.diaz on 4/1/16.
 */
public interface Parser {
  void parse(String classBaseName, Generator generator, FunctionInfo functionInfo) throws IOException;
}
