package cs340.getpet.persistence;

public class SearchResponse {
    public final Animal[] results;

    SearchResponse(Animal[] results) {
        this.results = results;
    }
}
