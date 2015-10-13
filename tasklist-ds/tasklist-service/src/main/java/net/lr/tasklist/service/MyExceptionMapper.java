package net.lr.tasklist.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class MyExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.serverError().entity(exception.getMessage()).build();
    }

}
