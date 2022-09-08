package com.api.spaceprobecontrol.planet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "planet")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Positive
    @NotNull
    @Column(name = "x_axis", nullable = false)
    private int xAxis;
    @Positive
    @NotNull
    @Column(name = "y_axis", nullable = false)
    private int yAxis;

    /**
     *
     * @deprecated default constructor required by hibernate
    */
    @Deprecated
    public Planet() {
    }

    public Planet(int xAxis, int yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public Long getId() {
        return id;
    }

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", xAxis=" + xAxis +
                ", yAxis=" + yAxis +
                '}';
    }
}
