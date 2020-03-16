
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("123456");
        mysqlDataSource.setDatabaseName("tangshi");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setCharacterEncoding("UTF8");
        dataSource = mysqlDataSource;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf8");
        PrintWriter out = resp.getWriter();
        out.println("<h1>唐诗三百首</h1>");
        out.println("<ol>");
        List<String> titles = getTilesDB();
        for(String title:titles){
            out.println("<li>"+title+"</li>");
        }
        out.println("</ol>");
    }

    private List<String> getTilesDB() {
        List<String> titles = new ArrayList<>();
        try(Connection con = dataSource.getConnection()){
            String sql = "select title from t_tangshi order by id";
            try(PreparedStatement stat = con.prepareStatement(sql)){
                try(ResultSet res = stat.executeQuery()){
                    while(res.next()){
                        String title = res.getString("title");
                        titles.add(title);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return titles;
    }
}
