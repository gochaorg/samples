package org.example.junit;

import java.util.concurrent.atomic.AtomicInteger;

public class PersonRepoImpl implements PersonRepo {
    private final static AtomicInteger idSeq=new AtomicInteger(0);
    private final static String[] firstNames = new String[]{
      "Oliver", "Jack", "Harry", "Jacob", "Charlie", "Thomas",
            "George", "Oscar", "James", "William"
    };
    private final static String[] lastNames = new String[]{
      "Adams", "Allen", "Armstrong", "Atkinson", "Atkinson", "Bailey",
            "Ball", "Bell", "Brown", "Carter", "Davies", "Dixon"
    };

    @Override
    public Person find(int id){
        int i1 = Math.abs(id) % firstNames.length;
        int i2 = Math.abs(id) % lastNames.length;
        return new Person(id, firstNames[i1]+" "+lastNames[i2]);
    }
}
