package uz.pdp.backend.service;

import kotlin.collections.ArrayDeque;
import uz.pdp.backend.model.Book;
import uz.pdp.backend.repository.FileWriterAndLoader;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.bot.states.child.Janr;

import java.util.List;
import java.util.Objects;

public class BookService implements BaseService, PathConstants {

    private FileWriterAndLoader<Book> writerAndLoader;

    public BookService() {
        this.writerAndLoader = new FileWriterAndLoader<>(BOOKS_PATH);
    }

    public void save(Book book){
        List<Book> books = writerAndLoader.load(Book.class);
        for (int i = 0; i < books.size(); i++) {
            Book cur = books.get(i);
            if (Objects.equals(cur.getId(),book.getId())){
                books.set(i,book);
                writerAndLoader.write(books);
                return;
            }
        }
        books.add(book);
        writerAndLoader.write(books);
    }
    public List<Book> getBooks(String janr){
        List<Book> result = new ArrayDeque<>();
        List<Book> books = writerAndLoader.load(Book.class);
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.isAdded()){
                if (Objects.equals(book.getJanr(),janr)){
                    result.add(book);
                }
            }
        }
        return result;
    }
    public List<Book> getAll(){
        List<Book> books = writerAndLoader.load(Book.class);
        return books;
    }
}
