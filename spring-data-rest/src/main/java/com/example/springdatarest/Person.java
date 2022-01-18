package com.example.springdatarest;

import javax.persistence.*;

@Entity
@Table(name = "person")
public class Person {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId(){
        return id;
    }

    public void setId( long id ){
        this.id = id;
    }

    @Column
    private String name;

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    @Column
    private int age;

    public int getAge(){
        return age;
    }

    public void setAge( int age ){
        this.age = age;
    }
}
