package org.recap.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Created by premkb on 16/8/17.
 *
 * This class is to generate sequence id for owning inst bib id, owning inst holding id, owning inst item id which is used for generate dummy record
 */

@Entity
@Table(name = "OWNING_INST_ID_SEQ", catalog = "")
public class OwningInstitutionIDSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    public Integer getId() {
        return id;
    }

}
