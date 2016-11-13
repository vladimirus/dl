package com.ft.dl;

import com.ft.api.jaxrs.errors.RuntimeExceptionMapper;
import com.ft.api.util.buildinfo.BuildInfoResource;
import com.ft.api.util.buildinfo.VersionResource;
import com.ft.apiingester.configuration.ContentApiConfiguration;
import com.ft.apiingester.connector.ContentConnector;
import com.ft.dl.configuration.DlConfiguration;
import com.ft.dl.connector.ContentV1Connector;
import com.ft.dl.converter.ContentConverter;
import com.ft.dl.service.*;
import com.ft.ingestertemplate.ingestion.ApiConnector;
import com.sun.jersey.api.client.Client;
import com.xeiam.dropwizard.sundial.SundialBundle;
import com.xeiam.dropwizard.sundial.SundialConfiguration;
import com.xeiam.dropwizard.sundial.tasks.StartJobTask;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.nio.file.Paths;

@SuppressWarnings("unchecked")
public class DlApp extends Application<DlConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DlApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<DlConfiguration> bootstrap) {
        bootstrap.addBundle(new SundialBundle() {
            @Override
            public SundialConfiguration getSundialConfiguration(Configuration configuration) {
                return ((DlConfiguration) configuration).getSundialConfiguration();
            }
        });
    }

    @Override
    public void run(DlConfiguration configuration, Environment environment) throws Exception {

        ContentConnector contentConnector = contentConnector(configuration.getContentApiConfiguration(), environment);
        ContentV1Connector contentV1Connector = contentV1Connector(configuration.getContentApiConfiguration(), environment);

        ContentConverter contentConverter = new ContentConverter(environment.getObjectMapper());
        ArticleDownloader downloader = new DefaultDownloader(contentConnector, contentConverter);
        FileManager fileManager = new FileManager(Paths.get("/Volumes/Untitled/content"));
        ContentManager contentManager = new DefaultContentManager(contentConverter, fileManager);

        environment.getApplicationContext().setAttribute("downloader", downloader);
        environment.getApplicationContext().setAttribute("contentManager", contentManager);

        environment.jersey().register(new BuildInfoResource());
        environment.jersey().register(new VersionResource());
        environment.jersey().register(new RuntimeExceptionMapper());
        environment.admin().addTask(new StartJobTask());
    }

    private ContentV1Connector contentV1Connector(ContentApiConfiguration contentApiConfiguration, Environment environment) {
        return null; // TODO: 11/11/2016
    }

    private ContentConnector contentConnector(ContentApiConfiguration configuration, Environment environment) {
        Client client = Client.create();

        client.setConnectTimeout(configuration.getClientConfiguration().getConnectTimeout());
        client.setReadTimeout(configuration.getClientConfiguration().getReadTimeout());

        ContentConnector contentConnector = new ContentConnector(client, configuration.getEndpointConfiguration(),
                new ApiConnector(environment.metrics()), configuration.getApiKey());

        contentConnector.setLastCheckedId(configuration.getKnownId());
        return contentConnector;
    }
}
