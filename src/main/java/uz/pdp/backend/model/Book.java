package uz.pdp.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bot.states.child.Janr;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class Book {
    private String id;
    private String photoId;
    private String name;
    private String author;
    private String discription;
    private String bookId;
    private String janr;
    private boolean isAdded;

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

}
