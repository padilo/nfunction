package net.pdiaz.nfunction;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by pablo on 9/20/15.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresProject = true)
public class GenerateNFunctionsMojo extends AbstractMojo {
    private Log log = getLog();

    @Parameter(defaultValue = "${project}", required = true)
    private MavenProject project;

    @Parameter(defaultValue = "java.vm")
    private String extension;

    @Parameter(defaultValue = "10")
    private Integer num;

    @Parameter(defaultValue = "target/generated-sources")
    private String sourceOutputDir;

    @Parameter(defaultValue = "T")
    private String genericName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        addSourceRoot();

        String sourceFolderStr = project.getBuild().getSourceDirectory();
        File sourceFolder = new File(sourceFolderStr);
        int sourceFolderChars = sourceFolderStr.length() + 1;
        String encoding = project.getProperties().getProperty("project.build.sourceEncoding");

        if(StringUtils.isBlank(encoding)) {
            throw new RuntimeException("encoding must be set via project property: project.build.sourceEncoding");
        }

        try {
            RegexFileFilter filefilter = new RegexFileFilter("^.*" + extension + "$");
            Collection<File> files = FileUtils.listFiles(sourceFolder, filefilter, DirectoryFileFilter.DIRECTORY);

            for (File file : files) {
                String classBaseName = extractClassBaseName(file);

                log.info("Found " + file.getAbsolutePath());
                String packagePath = file.getParentFile().getAbsolutePath().substring(sourceFolderChars);

                generateFunctions(classBaseName, packagePath, new File(project.getBasedir(), sourceOutputDir), file, encoding);
            }

        } catch (IOException e) {
            throw new MojoExecutionException("Error reading folder", e);
        }

        log.info("End");
    }

    private String extractClassBaseName(File file) {
        String filename = file.getName();

        int index = filename.lastIndexOf(extension);

        if(index == -1) {
            throw new RuntimeException("Unable to extract the extension from: " + filename);
        }

        return filename.substring(0, index-1);
    }

    private void addSourceRoot() {
        project.addCompileSourceRoot(sourceOutputDir);
    }

    private void generateFunctions(String classBaseName, String packagePath, File outputFolder, File file, String encoding) throws IOException {
        File outputDir = new File(outputFolder, packagePath);
        String outputBase = outputDir + File.separator + classBaseName;

        outputDir.mkdirs();

        Properties props = new Properties();

        props.put("file.resource.loader.path", file.getParent());
        props.put("input.encoding", encoding);
        props.put("output.encoding",encoding);

        VelocityEngine engine = new VelocityEngine(props);
        engine.init();

        Template t = engine.getTemplate(file.getName());

        String generics = "";

        for(int i=1; i<=num; i++) {
            VelocityContext context = new VelocityContext();

            if(generics.equals("")){
                generics = genericName + i;
            } else {
                generics += ", " + genericName + i;
            }

            FunctionInfo functionInfo = new FunctionInfo(i, num, genericName + i, generics);

            context.put("context", functionInfo);

            try(Writer writer = new FileWriter(new File(outputBase + i + ".java"))) {
                t.merge(context, writer);
            }
        }
        log.info("Generated " + classBaseName + "[1.." + num + "].java");

    }
}
