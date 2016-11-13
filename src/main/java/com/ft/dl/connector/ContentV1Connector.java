package com.ft.dl.connector;

import static com.ft.api.util.transactionid.TransactionIdUtils.TRANSACTION_ID_HEADER;

import com.ft.ingestertemplate.ingestion.ApiConnector;
import com.ft.jerseyhttpwrapper.config.EndpointConfiguration;
import com.sun.jersey.api.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class ContentV1Connector {
    private final Client client;
    private final EndpointConfiguration endpointConfiguration;
    private final ApiConnector connector;

    public ContentV1Connector(Client client, EndpointConfiguration endpointConfiguration, ApiConnector connector) {
        this.client = client;
        this.endpointConfiguration = endpointConfiguration;
        this.connector = connector;
    }

    // content/items/v1/{id}
    public String getContent(String id, String transactionId) {
        return connector.get(id, transactionId,
                client.resource(generateUri("content/items/v1/{id}", id))
                        .header(TRANSACTION_ID_HEADER, transactionId));
    }

    private URI generateUri(String path, String... parameters) {
        return connector.generateUri(
                "http",
                endpointConfiguration.getHost(),
                endpointConfiguration.getPort(),
                endpointConfiguration.getPath() + path,
                parameters);
    }
}
