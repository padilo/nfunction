package net.pdiaz.nfunction;

import net.pdiaz.nfunction.base.FunctionInfo;
import net.pdiaz.nfunction.base.Generator;
import net.pdiaz.nfunction.base.Parser;
import net.pdiaz.nfunction.velocity.VelocityParser;

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
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

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
        addGeneratedAsSource();

        String sourceFolderStr = project.getBuild().getSourceDirectory();
        String encoding = project.getProperties().getProperty("project.build.sourceEncoding");

        scanAndGenerateFunctions(sourceFolderStr, encoding);

        log.info("End");
    }

    private void scanAndGenerateFunctions(String sourceFolderStr, String encoding) throws MojoExecutionException {
        if(StringUtils.isBlank(encoding)) {
            throw new MojoExecutionException("encoding must be set via project property: project.build.sourceEncoding");
        }

        try {
            int sourceFolderChars = sourceFolderStr.length() + 1;
            File sourceFolder = new File(sourceFolderStr);
            RegexFileFilter fileFilter = new RegexFileFilter("^.*" + extension + "$");
            Collection<File> files = FileUtils.listFiles(sourceFolder, fileFilter, DirectoryFileFilter.DIRECTORY);

            for (File file : files) {
                log.info("Found " + file.getAbsolutePath());
                String packagePath = file.getParentFile().getAbsolutePath().substring(sourceFolderChars);

                generateFunctions(packagePath, new File(project.getBasedir(), sourceOutputDir), file, encoding);
            }

        } catch (IOException e) {
            throw new MojoExecutionException("Error reading folder", e);
        }
    }

    private String extractClassBaseName(File file) {
        String filename = file.getName();

        int index = filename.lastIndexOf(extension);

        if(index == -1) {
            throw new RuntimeException("Unable to extract the extension from: " + filename);
        }

        return filename.substring(0, index-1);
    }

    private void addGeneratedAsSource() {
        project.addCompileSourceRoot(sourceOutputDir);
    }

    private void generateFunctions(String packagePath, File outputFolder, File file, String encoding) throws IOException {
        String classBaseName = extractClassBaseName(file);
        Generator generator = new Generator(new File(outputFolder, packagePath));
        Parser parser = new VelocityParser(file, encoding);

        for(FunctionInfo functionInfo: FunctionInfo.iterable(num, genericName)) {
            parser.parse(classBaseName, generator, functionInfo);
        }

        log.info("Generated " + classBaseName + "[1.." + num + "].java");

    }
}
