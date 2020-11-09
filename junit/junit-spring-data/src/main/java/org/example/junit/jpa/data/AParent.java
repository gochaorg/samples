package org.example.junit.jpa.data;

import javax.persistence.*;

@Entity(name = "aparent")
public class AParent {
    //region id : Long
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId(){
        return id;
    }

    public void setId( Long id ){
        this.id = id;
    }
    //endregion

    //region name : String
    @Column(name = "name")
    private String name;

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }
    //endregion
}
