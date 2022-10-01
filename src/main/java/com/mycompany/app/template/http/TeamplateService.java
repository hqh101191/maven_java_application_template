package com.mycompany.app.template.http;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.mortbay.jetty.HttpStatus;

@Path("/template")
public class TeamplateService {

    static final Logger logger = Logger.getLogger(TeamplateService.class);
    @Context
    HttpServletRequest request;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doPostJson(String data) {
        return Response.status(HttpStatus.ORDINAL_200_OK).entity("template").build();
    }
}
