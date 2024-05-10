package uz.pdp.backend.service;

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
        return;
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
//    public Book getResult(Long id){
//        Book archive = get(id);
//        Date date = archive.getDate();
//        String currency = archive.getCurrency();
//        if (date!=null&&Objects.nonNull(currency)&&!currency.isEmpty()){
//            try {
//                CurrencyInfo[] currencyByNameAndDate = currencyService.getCurrencyByNameAndDate(currency, date);
//                CurrencyInfo currencyInfo = currencyByNameAndDate[0];
//                String rate = currencyInfo.getRate();
//                archive.setRate(rate);
//                save(archive);
//                return archive;
//            } catch (Exception e) {
//                return null;
//            }
//        }else {
//            return null;
//        }
//    }
}
