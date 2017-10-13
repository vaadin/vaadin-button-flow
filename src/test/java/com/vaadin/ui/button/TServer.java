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

public class TServer {

    private String webAppPath = "webapp";

    public TServer(String webAppPath) {
        super();
        this.webAppPath = webAppPath;
    }

    public TServer() {
        super();
    }

    public Server startServer() throws Exception {
        return startServer(getPort());
    }

    public Server startServer(int port) throws Exception {

        Server server = new Server();

        final ServerConnector connector = new ServerConnector(server);

        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });

        WebAppContext context = new WebAppContext();
        VaadinServlet vaadinServlet = createServlet();

        ServletHolder servletHolder = new ServletHolder(vaadinServlet);

        File file = new File(webAppPath);
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

        context.addServlet(servletHolder, "/");
        configure(context, server);
        server.setHandler(context);
        server.start();
        return server;
    }

    protected VaadinServlet createServlet() {
        return new DemoServlet();
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    protected int getPort() {
        return 9998;
    }

    /**
     * Hook for additional configuration.
     *
     * @param context
     *            the context
     * @param server
     *            the server
     */
    protected void configure(WebAppContext context, Server server) {

    }

}
