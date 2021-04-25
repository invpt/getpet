package cs340.getpet.http;

import cs340.getpet.http.rest.Endpoint;
import cs340.getpet.http.rest.RequestBody;
import cs340.getpet.http.rest.Response;
import cs340.getpet.http.rest.ResponseBody;
import cs340.getpet.http.rest.RestException;
import cs340.getpet.http.rest.RestHttpHandler;
import cs340.getpet.http.rest.ValidationException;
import cs340.getpet.http.rest.ResponseBody.EmptyResponse;
import cs340.getpet.persistence.Animal;
import cs340.getpet.persistence.Cage;
import cs340.getpet.persistence.Persistence;
import cs340.getpet.persistence.SearchQuery;
import cs340.getpet.persistence.Color;
import cs340.getpet.persistence.Gender;
import cs340.getpet.persistence.Size;
import cs340.getpet.persistence.Species;
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
                        return Response.withBody(200, new AnimalSearchResponse(persistence.search(req.body)));
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
                        return Response.withBody(200, new AnimalGetResponse(persistence.getAnimal(intakeNumber)));
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
                        persistence.deleteAnimal(intakeNumber);
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

final class AnimalSearchRequest extends SearchQuery implements RequestBody {
    AnimalSearchRequest(Species species, Gender[] genders, String breed, Color[] colors, Size[] sizes) {
        super(species, genders, breed, colors, sizes);
    }

    @Override
    public void validate() throws ValidationException {
        // Validate that breed is <= 50 characters and is only alphabetic
		if (!breed.matches("^[a-zA-Z ]{0,50}$"))
			throw new ValidationException("Breed must be alphabetic and 50 characters or less.");
    }
}

final class AnimalNewRequest implements RequestBody {
    public final Animal animal;

    AnimalNewRequest(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void validate() throws ValidationException {
        // Validate string fields of the Animal, like name
        if (!animal.name.matches("^[a-zA-Z ]{1,50}$"))
            throw new ValidationException("Name must be alphabetic and 50 characters or less.");
        if (!animal.breed.matches("^[a-zA-Z ]{1,50}$"))
            throw new ValidationException("Breed must be alphabetic and 50 characters or less.");
        if (animal.weight > 250.0 || animal.weight < 1.0)
            throw new ValidationException("Weight must lie within the range of 1 and 250 lbs");
    }
}

final class AnimalPutRequest implements RequestBody {
    public final Animal animal;

    AnimalPutRequest(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void validate() throws ValidationException {
        // Validate string fields of the Animal, like name
        if (!animal.name.matches("^[a-zA-Z ]{1,50}$"))
            throw new ValidationException("Name must be alphabetic and 50 characters or less.");
        if (!animal.breed.matches("^[a-zA-Z ]{1,50}$"))
            throw new ValidationException("Breed must be alphabetic and 50 characters or less.");
        if (animal.weight > 250.0 || animal.weight < 1.0)
            throw new ValidationException("Weight must lie within the range of 1 and 250 lbs");
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