package app;
import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DirectorMovies implements Handler {

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/directorMovie.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Movies by Director</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
            <div class='topnav'>
                <a href='/'>Homepage</a>
                <a href='movies.html'>List All Movies</a>
                <a href='moviestype.html'>Get Movies by Type</a>
                <a href='directorMovie.html'>Directors</a>
            </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
                <h1>
                    List Movies by Director
                </h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        /* Add HTML for the web form
         * We are giving two ways here
         *  - one for a text box
         *  - one for a drop down
         * 
         * Whitespace is used to help us understand the HTML!
         * 
         * IMPORTANT! the action speicifes the URL for POST!
         */
        html = html + "<form action='/directorMovie.html' method='post'>";
        
        html = html + "   <div class='form-group'>";
        html = html + "      <label for='director_drop'>Select the Director (Dropdown):</label>";
        html = html + "      <select id='director_drop' name='director_drop'>";
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<Movie> director = jdbc.getDirector();
        for (Movie movie : director) {
            html = html + "         <option>" + movie.director + "</option>";
        }
        html = html + "      </select>";
        html = html + "   </div>";

        html = html + "   <div class='form-group'>";
        html = html + "      <label for='director_textbox'>Select the Director (Textbox)</label>";
        html = html + "      <input class='form-control' id='director_textbox' name='director_textbox'>";
        html = html + "   </div>";

        html = html + "   <button type='submit' class='btn btn-primary'>Get all of the movies!</button>";

        html = html + "</form>";

        /* Get the Form Data
         *  from the drop down list
         * Need to be Careful!!
         *  If the form is not filled in, then the form will return null!
        */
        String director_drop = context.formParam("director_drop");
        // String movietype_drop = context.queryParam("director_drop");
        if (director_drop == null) {
            // If NULL, nothing to show, therefore we make some "no results" HTML
            html = html + "<h2><i>No Results to show for dropbox</i></h2>";
        } else {
            // If NOT NULL, then lookup the movie by type!
            html = html + outputMovies(director_drop);
        }

        String director_textbox = context.formParam("director_textbox");
        if (director_textbox == null || director_textbox == "") {
            // If NULL, nothing to show, therefore we make some "no results" HTML
            html = html + "<h2><i>No Results to show for textbox</i></h2>";
        } else {
            // If NOT NULL, then lookup the movie by type!
            html = html + outputMovies(director_textbox);
        }

        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'>
                <p>COSC2803 Module 0 - Week 06</p>
            </div>
        """;

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public String outputMovies(String director) {
        String html = "";
        html = html + "<h2>" + director + " Movies</h2>";

        // Look up movies from JDBC
        ArrayList<String> movieTitles = getMovieName(director);
        ArrayList<String> movieStars = getMovieStars(director);
        
        // Add HTML for the movies list
        int i;
        html = html + "<table>";
        html = html + "<th>Movie Title</th>" + "<th>Movie Stars</th>";
        for (i = 0; i < movieTitles.size(); ++i) {
            html = html + "<tr>" + "<td>" + movieTitles.get(i) + "</td>" + "<td>" + movieStars.get(i) + "</td>" + "</tr>";
        }
        html = html + "</table>";

        // 🚨 This block of code is for Pre-Req students.
        // Altneratively we can use JDBCConnection to add HTML for the movies list
        // Uncomment the code to use the JDBCConnection Objects example(s)
        // JDBCConnection jdbc = new JDBCConnection();
        // ArrayList<Movie> movies = jdbc.getMoviesByType(director);
        // html = html + "<h2>" + 
        //               director +
        //               " Movies with Years (from JDBCConnection)</h2>" +
        //               "<ul>";
        // for (Movie movie : movies) {
        //     html = html + "<li>" + movie.name + " was made in " + movie.year + "</li>";
        // }
        // html = html + "</ul>";

        return html;
    }

    /**
     * Get all the movies in the database by a given type.
     * Note this takes a string of the type as an argument!
     * This has been implemented for you as an example.
     * HINT: you can use this to find all of the horror movies!
     */
    public ArrayList<String> getMovieName(String director) {
        ArrayList<String> movies = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT mvtitle FROM MOVIE m Natural JOIN DIRECTOR JOIN MOVSTAR ms ON m.mvnumb = ms.mvnumb JOIN STAR s ON ms.STARNUMB = s.starnumb Where Dirname = '" + director + "'";
            System.out.println(query);
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String title = results.getString("mvtitle");

                movies.add(title);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return movies;
    }

    public ArrayList<String> getMovieStars(String director) {
        ArrayList<String> movies = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT starname FROM MOVIE m Natural JOIN DIRECTOR JOIN MOVSTAR ms ON m.mvnumb = ms.mvnumb JOIN STAR s ON ms.STARNUMB = s.starnumb Where Dirname = '" + director + "'";
            System.out.println(query);
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String title = results.getString("starname");

                movies.add(title);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return movies;
    }

}
