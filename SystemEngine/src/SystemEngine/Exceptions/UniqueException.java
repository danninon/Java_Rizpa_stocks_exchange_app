package SystemEngine.Exceptions;

public class UniqueException extends IllegalArgumentException{
    public UniqueException(String typeWasDoubled, String ofWhatCompany){
        this.typeWasDoubled = typeWasDoubled;
        this.ofWhatCompany = ofWhatCompany;

    }
    public String getType(){return typeWasDoubled;}
    public String getDoubled(){return ofWhatCompany;}

    String typeWasDoubled;
    String ofWhatCompany;
}
