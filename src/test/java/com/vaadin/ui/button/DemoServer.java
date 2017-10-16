/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.ui.button;

import java.io.File;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

import com.vaadin.server.VaadinServlet;

/**
 * Server class for running component demos and executing integration tests.
 * <p>
 * The main method in this class opens a web server to http://localhost:9998/ to
 * serve all your test UIs annotated with <code>@Route</code> for development
 * and integration testing. This class can be extended for additional
 * configuration.
 * 
 * @author Vaadin Ltd
 */
public class DemoServer {

    public static void main(String... args) throws Exception {
        new DemoServer().startServer();
    }

    // contains the client engine copied by maven-dependency-plugin
    private static final String WEB_APP_PATH = "target/dependency/META-INF/resources";

    /**
     * Starts a web server to the port defined by {@link #getPort()}. It serves
     * the test UIs annotated with <code>@Route</code>.
     * 
     * @return the server object
     */
    public Server startServer() throws Exception {

        Server server = new Server();

        final ServerConnector connector = new ServerConnector(server);
        connector.setPort(getPort());
        server.setConnectors(new Connector[] { connector });

        WebAppContext context = new WebAppContext();

        VaadinServlet vaadinServlet = createServlet();
        ServletHolder servletHolder = new ServletHolder(vaadinServlet);
        context.addServlet(servletHolder, "/");

        File file = new File(WEB_APP_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        context.setWar(file.getPath());
        context.setContextPath("/");

        ClassList classlist = ClassList.setServerDefault(server);
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration");

        // Enable annotation scanning for uitest classes even though they are
        // not inside webroot
        context.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN,
                ".*/target/test-classes/");

        configure(context, server);
        server.setHandler(context);
        server.start();
        return server;
    }

    /**
     * Creates and returns the {@link VaadinServlet} to be served by this
     * server.
     * 
     * @return the servlet to be served by this server
     */
    protected VaadinServlet createServlet() {
        return new DemoServlet();
    }

    /**
     * Gets the port number to which this server will be connected.
     * 
     * @return the port number to which this server will be connected.
     */
    protected int getPort() {
        return 9998;
    }

    /**
     * Hook for additional configuration to perform before starting the server.
     *
     * @param context
     *            the context
     * @param server
     *            the server
     */
    protected void configure(WebAppContext context, Server server) {

    }

}
