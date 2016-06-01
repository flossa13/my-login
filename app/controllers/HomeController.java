package controllers;

import play.mvc.*;
import scala.xml.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import play.data.Form;
import play.data.FormFactory;

import play.db.*;
import views.html.*;

import models.Thanks_Card_Table;
import models.Division_Table;
import models.Employee_Table;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	@Inject
	private FormFactory formFactory;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result top() {
        return ok(top.render());
    }

    public Result cards() {
    	//感謝カードの新着一覧を表示
    	Connection connection =DB.getConnection();
    	ArrayList<String> today = new ArrayList<String>();//時間

    	ArrayList<String> send = new ArrayList<String>();//感謝した人

    	ArrayList<String> help = new ArrayList<String>();//件名
    	ArrayList<String> sent = new ArrayList<String>();//内容
    	ArrayList<String> receive = new ArrayList<String>();//????

    	ArrayList<String> read = new ArrayList<String>();//既読判別
    	ArrayList<String> cardid = new ArrayList<String>();//一つ一つのカードID
    	ArrayList<String> receivename = new ArrayList<String>();//感謝された人
    	ArrayList<String> readid = new ArrayList<String>();//?????


     	String sql = "SELECT thanks_card_table.card_id,thanks_card_table.today,thanks_card_table.receive_user_id, "
    			+ "employee_table.employee_namen,thanks_card_table.help_content,thanks_card_table.sent_content, "
    			+ "thanks_card_table.read_card "
    			+ "FROM thanks_card_table INNER JOIN employee_table "
    			+ "ON thanks_card_table.send_user_id = employee_table.employee_id "
    			+ "ORDER BY card_id DESC";

    	String sql2 = "SELECT employee_table.employee_namen "
    			+ "FROM thanks_card_table INNER JOIN employee_table "
    			+ "ON employee_table.employee_id = thanks_card_table.receive_user_id ORDER BY today DESC";


    	 try {
    		PreparedStatement Ssqlex = connection.prepareStatement(sql);
    		ResultSet rsex = Ssqlex.executeQuery();
    		PreparedStatement Ssqlex2 = connection.prepareStatement(sql2);
    		ResultSet rsex2 = Ssqlex2.executeQuery();

    		while(rsex.next()){
    			today.add(rsex.getString("today"));
    			send.add(rsex.getString("employee_namen"));
    			help.add(rsex.getString("help_content"));
    			sent.add(rsex.getString("sent_content"));
    			read.add(rsex.getString("read_card"));
    			cardid.add(rsex.getString("card_id"));
    		}
    		while(rsex2.next()){
    			receive.add(rsex.getString("employee_namen"));
    		}


    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	 int count = cardid.size();
        return ok(cards.render(today,send,help,sent,read,count,cardid,receive));
    }

    public Result thankscard() {
    	//レコード指定で感謝カードを詳細に表示する
    	//感謝カードの新着一覧を表示
       	Form<Thanks_Card_Table> thanks = formFactory.form(Thanks_Card_Table.class).bindFromRequest();
    	Form<Employee_Table> employee = formFactory.form(Employee_Table.class).bindFromRequest();
    	Form<Division_Table> division = formFactory.form(Division_Table.class).bindFromRequest();
    	ArrayList<String> sendList = new ArrayList<>();
    	int id = -1;
    	String send = "", receive = "",help = "",sent = "";

    	try {

    		Thanks_Card_Table newTask = thanks.get();
    		Employee_Table newTask2 = employee.get();
			id = newTask.card_id;
			send = newTask2.employee_namen;
			help = newTask.help_content;
			sent = newTask.sent_content;
		 } catch (Exception e) {
				e.printStackTrace();
			}
    	return ok(thankscard2.render(id,send,help,sent));
    }

    public Result link1() {
        return ok(link1.render());
    }

    public Result link2() {
        return ok(link2.render());
    }

}
