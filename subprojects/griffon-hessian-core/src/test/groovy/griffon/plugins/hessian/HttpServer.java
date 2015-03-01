/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.plugins.hessian;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.io.Closeable;
import java.io.IOException;

public class HttpServer implements Closeable {
    private final Server server;

    public static HttpServer of(String contextPath, Servlet servlet) {
        Server server = new Server(8080);
        ServletHolder sh = new ServletHolder(servlet);
        ServletContextHandler context = new ServletContextHandler(
            ServletContextHandler.SESSIONS);
        context.setContextPath(contextPath);
        context.addServlet(sh, "/*");
        server.setHandler(context);

        return new HttpServer(server);
    }

    private HttpServer(Server server) {
        this.server = server;
    }

    @Override
    public void close() throws IOException {
        try {
            server.stop();
        } catch (Exception err) {
            throw new IOException(err);
        }
    }

    public void start() throws IOException {
        try {
            server.start();
        } catch (Exception err) {
            throw new IOException(err);
        }
    }
}