
package nu.te4.services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import nu.te4.support.User;


@Path("/")
public class LoginService {
        
    @POST
    @Path("login")
    public Response checkLogin(@Context HttpHeaders httpHeaders){
        if (!User.Authoricate(httpHeaders)) {
            System.out.println("aaaa");
            return Response.status(401).build();
        }
        System.out.println("bbbb");
        return Response.ok().build();
    }
    
    @POST
    @Path("user")
    public Response createUser(String body){
        if (!User.createUser(body)) {
            System.out.println("heh");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
                    System.out.println("asdasd");
        return Response.status(Response.Status.CREATED).build();
    }
}
    
