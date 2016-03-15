package com.online.library.handler;

import com.online.library.dao.entity.Book;
import com.online.library.dao.entity.UserProfile;
import com.online.library.handler.util.PageGenerator;
import com.online.library.service.AccountService;
import com.online.library.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tim on 14.03.2016.
 */
public class BookServlet extends HttpServlet {
    private Map<String, Object> pageData = new HashMap<>();
    private BookService bookService;
    private AccountService accountService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*List<Book> bookList = bookService.getBookList();
        pageData.put("books", bookList);
        resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageData));*/
        // TODO: 15.03.2016

        Book bookById = bookService.getBookById(Integer.parseInt(req.getParameter("id")));
        pageData.put("book", bookById);

        resp.getWriter().println(PageGenerator.instance().getPage("html/bookInfo.html", pageData));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Add book
        UserProfile userBySessionId = accountService.getUserBySessionId(req.getRequestedSessionId());
        if (userBySessionId == null || userBySessionId.getLogin().equals("admin")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("You don't have permissions to add new books");
            return;
        }

        Book newBook = new Book();
        newBook.setTitle(req.getParameter("name"));
        newBook.setAuthor(req.getParameter("author"));
        newBook.setGenre(req.getParameter("genre"));
        bookService.addBook(newBook);

        resp.sendRedirect("/home");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Edit book
        UserProfile userBySessionId = accountService.getUserBySessionId(req.getRequestedSessionId());
        if (userBySessionId == null || userBySessionId.getLogin().equals("admin")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String author = req.getParameter("author");
        String genre = req.getParameter("genre");

        bookService.editBook(id, name, author, genre);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        bookService.deleteBook(id);

        resp.sendRedirect("book");
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
