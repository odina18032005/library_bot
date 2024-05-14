package uz.pdp.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bot.states.child.Janr;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private String currency;
    private String photoId;
    private String name;
    private String discription;
    private String bookId;
    private String janr;
}
