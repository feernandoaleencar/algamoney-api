package com.fernandoalencar.algamoneyapi.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    private Long codigo_uf;

    private String uf;
    private String nome;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String regiao;

    public Long getCodigo_uf() {
        return codigo_uf;
    }

    public void setCodigo_uf(Long codigo_uf) {
        this.codigo_uf = codigo_uf;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
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

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo_uf == null) ? 0 : codigo_uf.hashCode());
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
        Estado other = (Estado) obj;
        if (codigo_uf == null) {
            if (other.codigo_uf != null)
                return false;
        } else if (!codigo_uf.equals(other.codigo_uf))
            return false;
        return true;
    }
}
