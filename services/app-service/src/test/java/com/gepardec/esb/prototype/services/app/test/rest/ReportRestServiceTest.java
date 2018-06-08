package com.gepardec.esb.prototype.services.app.test.rest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.security.RunAs;
import java.net.URL;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@RunWith(Arquillian.class)
public class ReportRestServiceTest {

    @ArquillianResource
    private URL deploymentUrl;

    @Test
    @RunAsClient
    public void test(){

    }

    @Deployment
    public static Archive createDeployment() {
        Archive archive = ShrinkWrap.create(WebArchive.class, "service-app-test.war")
                                    .addPackages(true, "com.gepardec.esb.prototype.services.app")
                                    .addAsLibraries(
                                            Maven.resolver()
                                                 .loadPomFromFile("pom.xml")
                                                 .importCompileAndRuntimeDependencies()
                                                 .resolve()
                                                 .withTransitivity()
                                                 .asFile()
                                    )
                                    .addAsResource("META-INF/beans.xml")
                                    .addAsResource("project-stages.yml");

        // If we want to see what has been packaged !!!!
        //        archive.as(ZipExporter.class).exportTo(new File(System.getenv("HOME") + "/" + archive.getName()), true);

        return archive;
    }
}
