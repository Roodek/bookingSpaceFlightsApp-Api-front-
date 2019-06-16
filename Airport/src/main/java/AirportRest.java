
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/")
public class AirportRest  {

    @GET
    @Path("/gettourists")
    @Produces({ "application/json" })
    public Response listRes(){

        JSONArray tourists = new JSONArray();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {

            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.tourists");
            tourists = writeToJson(resultSet);

        }catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

        return Response
                .status(200)
                .header("Access-Control-Allow-Origin","*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity(tourists.toString()).build();
    }

    @GET
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTourist(@PathParam("id") Long id) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {

            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            statement.execute("delete from public.tourists where id="+id);
        }
        return Response.ok().status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity(id+" deleted")
                .build();
    }
    @GET
    @Path("/deleteflight/{id}/{flight}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFlight(@PathParam("id") Long id, @PathParam("flight") Long flight) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            System.out.println("id+ "+id+" flight: "+ flight);
            statement.execute("UPDATE public.tourists SET flights = array_remove(flights,"+ flight+") WHERE id="+id);
        }
        return Response.ok().status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity("flight "+flight+" from pass "+id+" deleted")
                .build();
    }

    @GET
    @Path("/addtourist/{name}/{surname}/{sex}/{country}/{notes}/{birthdate}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTourist(@PathParam("name") String name,
                               @PathParam("surname") String surname,
                               @PathParam("sex") String sex,
                               @PathParam("country") String country,
                               @PathParam("notes") String notes,
                               @PathParam("birthdate") String birthdate) throws SQLException{
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO public.tourists (name,surname,sex,country,notes,birthdate)" +
                                    " values(\'"+name+ "\',\'"+surname+"\',\'"+sex+"\',\'"+country+"\',\'"+notes+"\',\'"+birthdate+"\');");
        }

        return Response.ok().status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity("user "+name+surname+sex+country+notes+birthdate+" added")
                .build();
    }

    @GET
    @Path("/addflighttopassenger/{id}/{flight}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFlightToPassenger(@PathParam("id") Long id, @PathParam("flight") Long flight) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            System.out.println("id+ "+id+" flight: "+ flight);
            statement.execute("UPDATE public.tourists SET flights = array_append(flights,"+ flight+") WHERE id="+id);
        }
        return Response.ok().status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity("flight "+flight+" from pass "+id+" deleted")
                .build();
    }



    public JSONArray writeToJson(ResultSet resultSet) throws SQLException{
        JSONArray result = new JSONArray();
        while (resultSet.next()) {
            JSONObject record = new JSONObject() ;

            record.put("id",resultSet.getString("id"));
            record.put("name",resultSet.getString("name"));
            record.put("surname",resultSet.getString("surname"));
            record.put("sex",resultSet.getString("sex"));
            record.put("country",resultSet.getString("country"));
            record.put("notes",resultSet.getString("notes"));
            record.put("birthdate",resultSet.getString("birthdate"));
            if(resultSet.getString("flights")!=null) {
                record.put("flights", resultSet.getString("flights").replace('{', '[').replace('}', ']'));
            }else{
                record.put("flights","[]");
            }
            System.out.printf(record.toString());
            result.put(record);
        }
        return result;
    }

    /**********************************************************/
    /* flights */
    /**********************************************************/
    public JSONArray writeToJsonFlights(ResultSet resultSet) throws SQLException{
        JSONArray result = new JSONArray();
        while (resultSet.next()) {
            JSONObject record = new JSONObject() ;

            record.put("id",resultSet.getString("id"));
            record.put("departure_time",resultSet.getString("departure_time"));
            record.put("arival_time",resultSet.getString("arival_time"));
            record.put("places",resultSet.getString("places"));
            record.put("ticket_price",resultSet.getString("ticket_price"));
            if(resultSet.getString("tourists")!=null) {
                record.put("tourists", resultSet.getString("tourists").replace('{', '[').replace('}', ']'));
            }else{
                record.put("tourists","[]");
            }
            System.out.printf(record.toString());
            result.put(record);
        }
        return result;
    }
    @GET
    @Path("/getflights")
    @Produces({ "application/json" })
    public Response listFlights(){

        JSONArray flights = new JSONArray();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {

            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.flights");
            flights = writeToJsonFlights(resultSet);

        }catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

        return Response
                .status(200)
                .header("Access-Control-Allow-Origin","*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity(flights.toString()).build();
    }
    @GET
    @Path("/deleteflightfromschedule/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFlightFromSchedule(@PathParam("id") Long id) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {

            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            statement.execute("delete from public.flights where id="+id);
        }
        return Response.ok().status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity(id+" deleted")
                .build();
    }

    @GET
    @Path("/deletepassenger/{id}/{passenger}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePassengerFromFlight(@PathParam("id") Long id, @PathParam("passenger") Long passenger) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            System.out.println("id+ "+id+" flight: "+ passenger);
            statement.execute("UPDATE public.flights SET tourists = array_remove(tourists,"+ passenger+") WHERE id="+id);
        }
        return Response.ok().status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity("passenger "+passenger+" from pass "+id+" deleted")
                .build();
    }

    @GET
    @Path("/addflight/{departure_time}/{arival_time}/{places}/{ticket_price}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFlight(@PathParam("departure_time") String departure_time,
                               @PathParam("arival_time") String arival_time,
                               @PathParam("places") String places,
                               @PathParam("ticket_price") String ticket_price) throws SQLException{
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO public.flights (departure_time,arival_time,places,ticket_price)" +
                    " values(\'"+departure_time+ "\',\'"+arival_time+"\',\'"+places+"\',\'"+ticket_price+"\');");
        }

        return Response.ok().status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity("flight "+departure_time+arival_time+places+ticket_price+" added")
                .build();
    }

    @GET
    @Path("/addpassengertoflight/{id}/{passenger}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPassengerToFlight(@PathParam("id") Long id, @PathParam("passenger") Long passenger) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "admin")) {
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            System.out.println("id+ "+id+" passenger: "+ passenger);
            statement.execute("UPDATE public.flights SET tourists = array_append(tourists,"+ passenger+") WHERE id="+id);
        }
        return Response.ok().status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .entity("flight "+passenger+" from pass "+id+" added")
                .build();
    }

}
