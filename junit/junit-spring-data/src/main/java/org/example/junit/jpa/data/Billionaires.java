package org.example.junit.jpa.data;


import javax.persistence.*;
import java.util.Date;

@Entity(name = "billionaires")
public class Billionaires {
    //region id : Long
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Возвращает идентификатор записи
     * @return идентификатор записи
     */
    public Long getId() {
        return id;
    }

    /**
     * Указывает идентификатор записи
     * @param id идентификатор записи
     */
    public void setId(Long id) {
        this.id = id;
    }
    //endregion
    //region firstName : String
    @Column(name = "first_name")
    private String firstName;

    /**
     * Возвращает имя персоны
     * @return имя персоны
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Указывает имя персоны
     * @param firstName имя персоны
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    //endregion
    //region lastName : String
    @Column(name = "last_name")
    private String lastName;

    /**
     * Возвращает фамилию персоны
     * @return фамилия
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Укаызвает фамилию персоны
     * @param lastName фамилия
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    //endregion
    //region career : String
    @Column(name = "career")
    private String career;

    /**
     * Возвращает описание карьеры
     * @return описание карьеры
     */
    public String getCareer() {
        return career;
    }

    /**
     * Указыавет описание карьеры
     * @param career описание карьеры
     */
    public void setCareer(String career) {
        this.career = career;
    }
    //endregion
    //region intNum : Integer
    @Column(name = "int_num", nullable = true)
    private Integer intNum;

    /**
     * Возвращает некое тестовое поле
     * @return некое тестовое поле
     */
    public Integer getIntNum() { return intNum;}

    /**
     * Указывает некое тестовое поле
     * @param intNum некое тестовое поле
     */
    public void setIntNum(Integer intNum) {
        this.intNum = intNum;
    }
    //endregion

    //region date : java.util.Date - column: date_a TemporalType.DATE
    @Temporal(TemporalType.DATE)
    @Column(name = "date_a", nullable = true)
    private Date date;

    /**
     * Возвращает некое тестовое поле
     * @return некое тестовое поле
     */
    public Date getDate() { return date; }

    /**
     * Указывает некое тестовое поле
     * @param date некое тестовое поле
     */
    public void setDate(Date date) { this.date = date; }
    //endregion
}
