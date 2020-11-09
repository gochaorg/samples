package org.example.junit.jpa.data;


import javax.persistence.*;
import java.util.Date;

@Entity(name = "billionaires")
public class Billionaires {
    //region id : Long
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    //endregion
    //region firstName : String
    @Column(name = "first_name")
    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    //endregion
    //region lastName : String
    @Column(name = "last_name")
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    //endregion
    //region career : String
    @Column(name = "career")
    private String career;

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }
    //endregion
    //region intNum : Integer
    @Column(name = "int_num", nullable = true)
    private Integer intNum;
    public Integer getIntNum() {
        return intNum;
    }
    public void setIntNum(Integer intNum) {
        this.intNum = intNum;
    }
    //endregion

    //region date : java.util.Date - column: date_a TemporalType.DATE
    @Temporal(TemporalType.DATE)
    @Column(name = "date_a", nullable = true)
    private Date date;

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    //endregion
}
