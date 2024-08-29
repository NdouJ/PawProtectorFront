package hr.algebra.pawprotectorfront.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReview {
    private Integer idUserReview;
    private Integer grade;
    private Integer userId;
    private Integer breederId;
    private String review;
}
