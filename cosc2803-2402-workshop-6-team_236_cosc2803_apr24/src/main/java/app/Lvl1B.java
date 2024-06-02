package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Lvl1B implements Handler{
    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Mission Statement</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";
        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
            <div class='topnav'>
            <a href='/'>Home</a>
            
                <a href='movies.html'>List All Movies</a>
                <a href='moviestype.html'>Get Movies by Type</a>
                <a href='directorMovie.html'>Directors</a>
                <a href='mission.html'>Mission Statement</a>
            </div>
            </select>
        """;

        html = html + """
            <div class='topnav'>
            <a href='/'>Home</a>
                <a href='movies.html'>List All Movies</a>
                <a href='moviestype.html'>Get Movies by Type</a>
                <a href='directorMovie.html'>Directors</a>
                <a href='mission.html'>Mission Statement</a>
            </div>
            </select>
        """;

        html = html + "<form action='/moviestype.html' method='post'>";
        html = html + "   <div class='form-group'>";
        //html = html + "      <label for='movietype_drop'>Food Loss</label>";
        html = html + "      <select id='movietype_drop' name='movietype_drop'>";
        html = html + "         <option>Food Loss</option>";
        html = html + "         <option>Food Loss by Country</option>";
        html = html + "         <option>Food Loss by Commodity</option>";
        html = html + "      </select>";
        html = html + "   </div>";

        html = html + "</form>";

        // Add header content block
        html = html + """
            <div class='header'>
                <h1>
                    Mission Statement
                </h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the movies list
        html = html + "<p>The purpose of our website is to address the social challenge of investigating food loss and waste through including a table which features unbiased information and statistics to inform users on the subject. A user can interact with the table in many ways to gather relevant data for themselves. The table consists of various statistics and data within a year range of many countries and regions. Additional data many be found below such as the maximum single year loss percentage of any commodity.</p>" +
                        "<p>The site can be used as a regular information website. However, the are multiple features users can manipulate for their needs. For example, users are able to filter and search for specific information in the bar above the table in addition to being able to change the year range of data shown. Users can also scroll through the table using the scroll bar to access any data that may not fit in the given space.</p>";


        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'>
                <p>COSC2803 Module 0 - Week 06</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }
}
