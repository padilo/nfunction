package net.pdiaz.nfunction.velocity;

import net.pdiaz.nfunction.base.FunctionInfo;
import net.pdiaz.nfunction.base.Generator;
import net.pdiaz.nfunction.base.Parser;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

/**
 * Created by pablo.diaz on 4/1/16.
 */
public class VelocityParser implements Parser {
  private Template template;

  public VelocityParser(File template, String encoding) {
    Properties props = new Properties();

    props.put("file.resource.loader.path", template.getParent());
    props.put("input.encoding", encoding);
    props.put("output.encoding",encoding);

    VelocityEngine engine = new VelocityEngine(props);
    engine.init();

    this.template = engine.getTemplate(template.getName());

  }

  public void parse(String classBaseName, Generator generator, FunctionInfo functionInfo) throws IOException {
    VelocityContext context = new VelocityContext();

    context.put("context", functionInfo);

    try(Writer writer = generator.createClass(classBaseName + functionInfo.getI())) {
      template.merge(context, writer);
    }
  }
}
