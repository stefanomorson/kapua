/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.account.service.proxy;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.account.service.proxy.rest.AccountServiceRestProxy;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.service.account.Account;
import org.junit.Test;

public class AccountServiceProsyTest {

    @Test
    public void testFind() {

        try {
            AccountServiceRestProxy proxy = new AccountServiceRestProxy();
            Account account;
            account = proxy.find(KapuaEid.parseCompactId("AQ"), KapuaEid.parseCompactId("AQ"));
            System.out.println(account);
        } catch (KapuaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testAuthGet() {

        Response response = null;
        try {
            Client client = ClientBuilder.newClient();
            String path = String.format("/authorization/_test");

            response = null;
            WebTarget target = client.target(new URL("http", "localhost", 8183, path).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL3d3dy5lY2xpcHNlLm9yZy9rYXB1YSIsImlhdCI6MTUwMDgxMjMzNCwiZXhwIjoxNTAwODE0MTM0LCJzdWIiOiJBUSIsInNJZCI6IkFRIn0.poOmaiKBDaRMoMrzLtUItYXzqBecs7IECQg3UVKKCoO_SicY61ugHikeO3Wws6YfS_KDDB1K_oPfI5SK4D3PguaGTSF_JOAOektp1efgSQKW5fQPfM9Cx-oxw-yucwclfzkn5U551my0FHDvpoTEXH_XMSpU3yMZjxXjeVTwz8wpMRsLL1GZ_AVupxcF6tisArNGuf8H-_PEz5gc9HVsn9g8cJk5Uf0nEbHeYnoFdgy9BdUfHgTQE2JoIvnj9bR33gi9oWXdLC7CS_E9VW61Gb6aN0FnuGg_V9PlrHfN7AcLoCSxJ4C2DkchJCkqyRdth525_JJ5ep6v-TW21elveg")
                    .get();
            return;
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            response.close();
        }

    }

    @Test
    public void testAuth() {

        Response response = null;
        try {
            Client client = ClientBuilder.newClient();
            String path = String.format("/authorization/_checkPermission");

            response = null;
            WebTarget target = client.target(new URL("http", "localhost", 8183, path).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL3d3dy5lY2xpcHNlLm9yZy9rYXB1YSIsImlhdCI6MTUwMDgxNjcwMSwiZXhwIjoxNTAwODE4NTAxLCJzdWIiOiJBUSIsInNJZCI6IkFRIn0.BmqmKJUTpArEa5v9-c_w_I5dMEEb4SknaBUr-lOZ_62Z6wCP0cS90CY0nZIBuukgX2T8YdGwRtOhIvVyxaGJVsfIEZ5z9gDlOhXC1w8ujSkhLWb7MVLweuCNMrQjZEo3Hvsgzf2i6ysw2FUx4dyryjevSTqPve0HgE01JBc8_Z3uE7_eL_fsuxb8ZEbDz4hviQ9caho8g8uIJrwZXdWmd2zP6Jp3PzIvaug2SoqxTrSLzN6lj5TEa5_sMM3wlI8gQXd1_A-hAE0RUdIB7tNx2hjaoAT-FdNxYchRDT7ghDGX4FHR-7mhNcIK-gQTJ4h2kRh7k2Rufs1rlzPHWfJK2g")
                    .post(Entity.entity("{}", MediaType.APPLICATION_JSON));
            return;
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            response.close();
        }

    }
}
