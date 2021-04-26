package cs340.getpet.http;

import cs340.getpet.http.rest.Endpoint;
import cs340.getpet.http.rest.RequestBody;
import cs340.getpet.http.rest.Response;
import cs340.getpet.http.rest.ResponseBody;
import cs340.getpet.http.rest.RestException;
import cs340.getpet.http.rest.RestHttpHandler;
import cs340.getpet.http.rest.ValidationException;
import cs340.getpet.http.rest.Validator;
import cs340.getpet.http.rest.ResponseBody.EmptyResponse;
import cs340.getpet.persistence.Animal;
import cs340.getpet.persistence.Cage;
import cs340.getpet.persistence.Persistence;
import cs340.getpet.persistence.SearchQuery;
import cs340.getpet.persistence.Persistence.PersistenceException;

public class PersistenceHttpHandler extends RestHttpHandler {
    private final Persistence persistence;

    public PersistenceHttpHandler(Persistence persistence) {
        super(
            "/persistence",
            new Endpoint.Builder()
                .path("/cages")
                .get(CagesGetResponse.class, (req) -> {
                    try {
                        return Response.withBody(200, new CagesGetResponse(persistence.getCages()));
                    } catch (PersistenceException e) {
                        throw new RestException(RestException.Code.INTERNAL, e);
                    }
                })
                .build(),
            new Endpoint.Builder()
                .path("/search")
                .post(AnimalSearchRequest.class, AnimalSearchResponse.class, (req) -> {
                    try {
                        return Response.withBody(200, new AnimalSearchResponse(persistence.search(req.body.query)));
                    } catch (PersistenceException e) {
                        throw new RestException(RestException.Code.INTERNAL, e);
                    }
                })
                .build(),
            new Endpoint.Builder()
                .path("/animal/new")
                .post(AnimalNewRequest.class, EmptyResponse.class, (req) -> {
                    try {
                        persistence.newAnimal(req.body.animal);
                        return Response.empty(200);
                    } catch (PersistenceException e) {
                        throw new RestException(RestException.Code.INTERNAL, e);
                    }
                })
                .build(),
            new Endpoint.Builder()
                .path("/animal/{intakeNumber}")
                .get(AnimalGetResponse.class, (req) -> {
                    int intakeNumber = req.pathVariables.getInt("intakeNumber");
                    try {
                        Animal animal = persistence.getAnimal(intakeNumber);
                        if (animal != null)
                            return Response.withBody(200, new AnimalGetResponse(animal));
                        else
                            throw new RestException(RestException.Code.NOT_FOUND);
                    } catch (PersistenceException e) {
                        throw new RestException(RestException.Code.INTERNAL, e);
                    }
                })
                .put(AnimalPutRequest.class, ResponseBody.EmptyResponse.class, (req) -> {
                    int intakeNumber = req.pathVariables.getInt("intakeNumber");
                    try {
                        persistence.updateAnimal(intakeNumber, req.body.animal);
                        return Response.empty(200);
                    } catch (PersistenceException e) {
                        throw new RestException(RestException.Code.INTERNAL, e);
                    }
                })
                .delete(RequestBody.EmptyRequest.class, ResponseBody.EmptyResponse.class, (req) -> {
                    int intakeNumber = req.pathVariables.getInt("intakeNumber");
                    try {
                        if (!persistence.deleteAnimal(intakeNumber))
                            throw new RestException(RestException.Code.NOT_FOUND);
                        else
                            return Response.empty(200);
                    } catch (PersistenceException e) {
                        throw new RestException(RestException.Code.INTERNAL, e);
                    }
                })
                .build()
        );
        
        this.persistence = persistence;
    }
}

final class AnimalSearchRequest implements RequestBody {
    public final SearchQuery query;

    AnimalSearchRequest(SearchQuery query) {
        this.query = query;
    }

    @Override
    public void validate() throws ValidationException {
        Validator.assertNonNull(query, "Query must not be null");
        Validator.assertMatches(query.breed, "^[a-zA-Z ]{0,50}$", "Breed must be alphabetic and 50 characters or less.");
        Validator.assertFalse(query.cageNumber < 0, "Cage number cannot be negative");
    }
}

final class AnimalNewRequest implements RequestBody {
    public final Animal animal;

    AnimalNewRequest(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void validate() throws ValidationException {
        Validator.assertNonNull(animal, "Animal must not be null");
        Validator.assertNull(animal.intakeNumber, "Animal must not contain an intake number; those are auto-assigned");
        Validator.assertFalse(animal.cageNumber < 0, "Cage number cannot be negative");
        Validator.assertNonNull(animal.species, "Animal must have species");
        Validator.assertNonNull(animal.breed, "Animal must have breed");
        Validator.assertMatches(animal.breed, "^[a-zA-Z ]{1,50}$", "Animal breed must be alphabetic and <= 50 characters");
        Validator.assertNonNull(animal.size, "Animal must have size");
        Validator.assertNonNull(animal.colors, "Animal must have one or more color");
        Validator.assertFalse(animal.colors.length == 0, "Animal must have one or more color");
        Validator.assertNonNull(animal.gender, "Animal must have gender");
        Validator.assertTrue(animal.weight >= 1.0 && animal.weight <= 250.0, "Animal weight must be between 1 and 250 lbs.");
        Validator.assertMatches(animal.name, "^[a-zA-Z ]{1,50}$", "Animal name must be alphabetic and <= 50 characters");
    }
}

final class AnimalPutRequest implements RequestBody {
    public final Animal animal;

    AnimalPutRequest(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void validate() throws ValidationException {
        Validator.assertNonNull(animal, "Animal must not be null");
        Validator.assertNull(animal.intakeNumber, "Animal must not contain an intake number; those are auto-assigned");
        Validator.assertFalse(animal.cageNumber < 0, "Cage number cannot be negative");
        Validator.assertNonNull(animal.species, "Animal must have species");
        Validator.assertNonNull(animal.breed, "Animal must have breed");
        Validator.assertMatches(animal.breed, "^[a-zA-Z ]{1,50}$", "Animal breed must be alphabetic and <= 50 characters");
        Validator.assertNonNull(animal.size, "Animal must have size");
        Validator.assertNonNull(animal.colors, "Animal must have one or more color");
        Validator.assertFalse(animal.colors.length == 0, "Animal must have one or more color");
        Validator.assertNonNull(animal.gender, "Animal must have gender");
        Validator.assertTrue(animal.weight >= 1.0 && animal.weight <= 250.0, "Animal weight must be between 1 and 250 lbs.");
        Validator.assertMatches(animal.name, "^[a-zA-Z ]{1,50}$", "Animal name must be alphabetic and <= 50 characters");
    }
}

final class CagesGetResponse implements ResponseBody {
    public final Cage[] cages;

    CagesGetResponse(Cage[] cages) {
        this.cages = cages;
    }
}

final class AnimalSearchResponse implements ResponseBody {
    public final Animal[] results;

    AnimalSearchResponse(Animal[] results) {
        this.results = results;
    }
}

final class AnimalGetResponse implements ResponseBody {
    public final Animal animal;

    AnimalGetResponse(Animal animal) {
        this.animal = animal;
    }
}