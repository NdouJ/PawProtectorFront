package hr.algebra.pawprotectorfront.models;

public class Dog {
    private int idDog;
    private String breedName;
    private int avgWeightFemale;
    private int avgWeightMale;
    private String description;
    private String image;

    public int getIdDog() {
        return idDog;
    }

    public void setIdDog(int idDog) {
        this.idDog = idDog;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public int getAvgWeightFemale() {
        return avgWeightFemale;
    }

    public void setAvgWeightFemale(int avgWeightFemale) {
        this.avgWeightFemale = avgWeightFemale;
    }

    public int getAvgWeightMale() {
        return avgWeightMale;
    }

    public void setAvgWeightMale(int avgWeightMale) {
        this.avgWeightMale = avgWeightMale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
