package com.yoti.app.oauthdemojwt.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="random_city")
@Data
public class RandomCity {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name = "name")
    private String name;

}
