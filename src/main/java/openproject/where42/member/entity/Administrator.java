package openproject.where42.member.entity;

import openproject.where42.group.Groups;
import openproject.where42.member.entity.enums.Planet;
import openproject.where42.member.entity.enums.MemberLevel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Administrator extends User {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name", nullable = false, unique = true)
    private String name;

    @NotNull
    private String passwd;

    @OneToMany(mappedBy = "owner")
    List<Groups> groups = new ArrayList<>();

    private String msg;

    @Enumerated
    private MemberLevel level = MemberLevel.administrator;

    @Enumerated
    private Locate locate;

    private String img;
}
