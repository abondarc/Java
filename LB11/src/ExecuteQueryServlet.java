import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/execute-query")
public class ExecuteQueryServlet extends HttpServlet {
    private DBManager dbManager;
    private QueryExecutor queryExecutor;
    private DataInserter dataInserter;

    @Override
    public void init() {
        dbManager = new DBManager();
        queryExecutor = new QueryExecutor(dbManager);
        dataInserter = new DataInserter((Connection) dbManager);

        // Инициализация таблиц в базе данных
        dbManager.createTables();
        dbManager.createEmailsTable();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");

        if ("findUserWithShortestEmails".equals(action)) {
            // Обработка запроса поиска пользователя с самыми короткими письмами
            String result = queryExecutor.findUserWithShortestEmails();
            req.setAttribute("result", result);
        } else if ("getUsersWithSpecificSubject".equals(action)) {
            // Обработка запроса поиска пользователей с заданной темой письма
            String subject = req.getParameter("subject");
            String result = queryExecutor.getUsersWithSpecificSubject(subject);
            req.setAttribute("result", result);

            // Использование нового HTML-шаблона для отображения формы поиска писем
            RequestDispatcher dispatcher = req.getRequestDispatcher("/search_emails_by_subject.html");
            dispatcher.forward(req, resp);
            return;
        } else if ("insertEmail".equals(action)) {
            // Обработка запроса на добавление письма
            int senderId = Integer.parseInt(req.getParameter("senderId"));
            int receiverId = Integer.parseInt(req.getParameter("receiverId"));
            String subject = req.getParameter("subject");
            String text = req.getParameter("text");
            dataInserter.insertEmail(senderId, receiverId, subject, text);
            req.setAttribute("result", "Email successfully inserted");
        } else if ("getUsersWithoutSpecificSubject".equals(action)) {
            // Обработка запроса поиска пользователей без заданной темы письма
            String subject = req.getParameter("subject");
            String result = queryExecutor.getUsersWithoutSpecificSubject(subject);
            req.setAttribute("result", result);

            // Использование нового HTML-шаблона для отображения формы поиска пользователей
            RequestDispatcher dispatcher = req.getRequestDispatcher("/search_users_without_subject.html");
            dispatcher.forward(req, resp);
            return;
        } else if ("sendEmailToAllRecipients".equals(action)) {
            // Обработка запроса на рассылку письма всем адресатам
            int senderId = Integer.parseInt(req.getParameter("senderId"));
            String subject = req.getParameter("subject");
            String text = req.getParameter("text");
            queryExecutor.sendEmailToAllRecipients(senderId, subject, text);
            req.setAttribute("result", "Email successfully sent to all recipients");

            // Использование нового HTML-шаблона для отображения формы рассылки писем
            RequestDispatcher dispatcher = req.getRequestDispatcher("/send_email_to_all_recipients.html");
            dispatcher.forward(req, resp);
            return;
        } else if ("getUsersAndEmailCounts".equals(action)) {
            // Обработка запроса получения информации о пользователях и количестве писем
            String result = queryExecutor.getUsersAndEmailCounts();
            req.setAttribute("result", result);

            // Использование нового HTML-шаблона для отображения статистики
            RequestDispatcher dispatcher = req.getRequestDispatcher("/display_statistics.html");
            dispatcher.forward(req, resp);
            return;
        } else if ("displayUserEmails".equals(action)) {
            // Обработка запроса отображения информации о письмах пользователя
            int userId = Integer.parseInt(req.getParameter("userId"));
            String result = queryExecutor.getUserEmails(userId);
            req.setAttribute("result", result);

            // Использование нового HTML-шаблона для отображения информации о письмах
            RequestDispatcher dispatcher = req.getRequestDispatcher("/display_emails.html");
            dispatcher.forward(req, resp);
            return;
        } else if ("getEmailsByDate".equals(action)) {
            // Обработка запроса поиска писем по дате
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");
            String result = queryExecutor.getEmailsByDate(startDate, endDate);
            req.setAttribute("result", result);

            // Использование нового HTML-шаблона для отображения формы поиска писем по дате
            RequestDispatcher dispatcher = req.getRequestDispatcher("/search_emails_by_date.html");
            dispatcher.forward(req, resp);
            return;
        } else if ("getEmailsByKeywords".equals(action)) {
            // Обработка запроса поиска писем по ключевым словам
            String keywords = req.getParameter("keywords");
            String result = queryExecutor.getEmailsByKeywords(keywords);
            req.setAttribute("result", result);

            // Использование нового HTML-шаблона для отображения формы поиска писем по ключевым словам
            RequestDispatcher dispatcher = req.getRequestDispatcher("/search_emails_by_keywords.html");
            dispatcher.forward(req, resp);
            return;
        } else if ("getEmailsBySenderReceiver".equals(action)) {
            // Обработка запроса поиска писем по отправителю и получателю
            int senderId = Integer.parseInt(req.getParameter("senderId"));
            int receiverId = Integer.parseInt(req.getParameter("receiverId"));
            String result = queryExecutor.getEmailsBySenderReceiver(senderId, receiverId);
            req.setAttribute("result", result);

            // Использование нового HTML-шаблона для отображения формы поиска писем по отправителю и получателю
            RequestDispatcher dispatcher = req.getRequestDispatcher("/search_emails_by_sender_receiver.html");
            dispatcher.forward(req, resp);
            return;
        }

        // Отображение результата в виде HTML-документа
        RequestDispatcher dispatcher = req.getRequestDispatcher("/result_page.html");
        dispatcher.forward(req, resp);
    }
}
