package com.fernandoalencar.algamoneyapi.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "municipio")
public class Municipio {

    @Id
    private Long codigo_ibge;

    private String nome;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean capital;
    private String siafi_id;
    private Long ddd;
    private String fuso_horario;

    @ManyToOne
    @JoinColumn(name = "codigo_uf")
    private Estado estado;

    public Long getCodigo_ibge() {
        return codigo_ibge;
    }

    public void setCodigo_ibge(Long codigo_ibge) {
        this.codigo_ibge = codigo_ibge;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Boolean getCapital() {
        return capital;
    }

    public void setCapital(Boolean capital) {
        this.capital = capital;
    }

    public String getSiafi_id() {
        return siafi_id;
    }

    public void setSiafi_id(String siafi_id) {
        this.siafi_id = siafi_id;
    }

    public Long getDdd() {
        return ddd;
    }

    public void setDdd(Long ddd) {
        this.ddd = ddd;
    }

    public String getFuso_horario() {
        return fuso_horario;
    }

    public void setFuso_horario(String fuso_horario) {
        this.fuso_horario = fuso_horario;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo_ibge == null) ? 0 : codigo_ibge.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Municipio other = (Municipio) obj;
        if (codigo_ibge == null) {
            if (other.codigo_ibge != null)
                return false;
        } else if (!codigo_ibge.equals(other.codigo_ibge))
            return false;
        return true;
    }
}
