package com.serefcan.sagligaadimadim.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class AdimsayarModel {

    @Getter @Setter private long tarih;
    @Getter @Setter private int adimsayisi;
    @Getter @Setter private int songun;


}