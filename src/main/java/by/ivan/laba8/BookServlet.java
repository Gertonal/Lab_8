package by.ivan.laba8;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    private static final String FILE_PATH = "C:/Users/vanya/IdeaProjects/laba8/library.json";
    private List<Book> library;
    private Gson gson = new Gson();

    public void init() throws ServletException {
        try {
            library = readLibraryFromFile();
        } catch (IOException e) {
            library = new ArrayList<>();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(gson.toJson(library));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Book book = gson.fromJson(sb.toString(), Book.class);
        library.add(book);
        writeLibraryToFile(library);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(library));
    }

    private List<Book> readLibraryFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            return gson.fromJson(reader, new TypeToken<List<Book>>(){}.getType());
        }
    }

    private void writeLibraryToFile(List<Book> library) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(library, writer);
        }
    }
}