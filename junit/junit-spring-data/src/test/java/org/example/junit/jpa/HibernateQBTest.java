package org.example.junit.jpa;

import org.example.junit.jpa.data.BillionairesRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class HibernateQBTest {
    @Autowired
    private BillionairesRepo repo;

    @Autowired
    private EntityManager em;

    @Test
    public void test01(){
        System.out.println("run test01");
        System.out.println("==================");
        System.out.println("repo "+repo);
        System.out.println("em "+em);

        //DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("rows");
        if( repo!=null ){
            repo.findAll().forEach( row ->{
                System.out.println(
                    "id="+row.getId()+
                        " f.name="+row.getFirstName()+
                        " l.name="+row.getLastName()+
                        " career="+row.getCareer()+
                        " date="+(row.getDate()!=null ? df.format( row.getDate() ) : null)
                );
            });
        }
    }
}
