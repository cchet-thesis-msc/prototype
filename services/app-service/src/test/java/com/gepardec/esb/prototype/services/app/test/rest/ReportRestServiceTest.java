package com.gepardec.esb.prototype.services.app.test.rest;

import com.gepardec.esb.prototype.services.app.configuration.KeycloakConfiguration;
import com.gepardec.esb.prototype.services.app.rest.api.ReportRestService;
import com.gepardec.esb.prototype.services.app.rest.model.ReportModelDto;
import com.gepardec.esb.prototype.services.app.test.mock.NoopKeycloakConfiguration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.net.URL;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@RunWith(Arquillian.class)
public class ReportRestServiceTest {

    @ArquillianResource
    private URL deploymentUrl;

    private ReportRestService reportRestClient;

    @Before
    public void before() {
        reportRestClient = ProxyBuilder.builder(ReportRestService.class, ClientBuilder.newClient().target(deploymentUrl + "/rest-api"))
                                       .defaultConsumes(MediaType.APPLICATION_JSON)
                                       .build();
    }

    // -- Then --
    @Test(expected = BadRequestException.class)
    @RunAsClient
    public void test_generate_invalid_id() {
        // -- Given --
        final Long id = -1L;

        // -- When --
        reportRestClient.generate(id);
    }

    @Test
    @RunAsClient
    public void test_generate_valid_id() {
        // -- Given --
        final Long id = 1L;

        // -- When --
        final ReportModelDto actual = reportRestClient.generate(id);

        // -- Then --
        Assert.assertNotNull(actual);
    }

    @Test
    @RunAsClient
    public void test_testRetry() {
        // -- Given / When --
        final ReportModelDto actual = reportRestClient.testRetry();

        // -- Then --
        Assert.assertNotNull(actual);
    }

    @Deployment
    public static Archive createDeployment() {
        Archive archive = ShrinkWrap.create(WebArchive.class, "service-app-test.war")
                                    .addPackages(true, "com.gepardec.esb.prototype.services.app")
                                    .deleteClass(KeycloakConfiguration.class)
                                    .addClass(NoopKeycloakConfiguration.class)
                                    .addAsLibraries(
                                            Maven.resolver()
                                                 .loadPomFromFile("pom.xml")
                                                 .importCompileAndRuntimeDependencies()
                                                 .resolve()
                                                 .withTransitivity()
                                                 .asFile()
                                    )
                                    .addAsResource("META-INF/beans.xml")
                                    .addAsResource("project-stages.yml")
                                    .addAsResource("swarm.swagger.conf");

        // If we want to see what has been packaged !!!!
        //        archive.as(ZipExporter.class).exportTo(new File(System.getenv("HOME") + "/" + archive.getName()), true);

        return archive;
    }
}
