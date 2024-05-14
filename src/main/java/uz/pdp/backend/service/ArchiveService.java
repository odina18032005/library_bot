package uz.pdp.backend.service;

import kotlin.collections.ArrayDeque;
import uz.pdp.backend.model.Book;
import uz.pdp.backend.repository.FileWriterAndLoader;
import uz.pdp.backend.statics.PathConstants;
import uz.pdp.baen.BeanController;
import uz.pdp.bot.states.child.Janr;

import java.util.List;
import java.util.Objects;

public class ArchiveService implements BaseService, PathConstants {

    private FileWriterAndLoader<Book> writerAndLoader;

    public ArchiveService() {
        this.writerAndLoader = new FileWriterAndLoader<>(ARCHIVES_PATH);
    }

    public void save(Book book){
        List<Book> archives = writerAndLoader.load(Book.class);
        for (int i = 0; i < archives.size(); i++) {
            Book cur = archives.get(i);
            if (Objects.equals(cur.getBookId(),book.getBookId())){
                archives.set(i,book);
                writerAndLoader.write(archives);
                return;
            }
        }
        archives.add(book);
        writerAndLoader.write(archives);
    }
    public List<Book> getBooks(Janr janr){
        List<Book> result = new ArrayDeque<>();
        List<Book> archives = writerAndLoader.load(Book.class);
        for (int i = 0; i < archives.size(); i++) {
            Book book = archives.get(i);
            if (Objects.equals(book.getJanr(),janr)){
                result.add(book);
            }
        }
        return result;
    }
    public Book get(Janr janr){
        List<Book> archives = writerAndLoader.load(Book.class);
        for (int i = 0; i < archives.size(); i++) {
            Book book = archives.get(i);
            if (Objects.equals(book.getJanr(),janr)){
                return book;
            }
        }
        return null;
    }
}
