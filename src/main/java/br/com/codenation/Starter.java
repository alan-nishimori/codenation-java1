package br.com.codenation;

import br.com.codenation.Model.Jogador;
import br.com.codenation.Model.Time;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Starter {

    public static void main(String[] args) {
        DesafioMeuTimeApplication desafio = new DesafioMeuTimeApplication();

        desafio.incluirTime(3L, "Nome", LocalDate.of(1914, 8, 25), "verde", "branco");
        desafio.incluirTime(1L, "Nome", LocalDate.of(1914, 8, 25), "laranja", "branco");
        desafio.incluirJogador(1L, 1L, "Jogador 1", LocalDate.of(1995, 9, 20), 8, BigDecimal.valueOf(15000));
        desafio.incluirJogador(5L, 1L, "Jogador 2", LocalDate.of(1998, 12, 23), 10, BigDecimal.valueOf(20000));
        desafio.incluirJogador(3L, 1L, "Jogador 3", LocalDate.of(1997, 11, 22), 8, BigDecimal.valueOf(25000));
        desafio.incluirJogador(4L, 1L, "Jogador 4", LocalDate.of(1998, 12, 23), 9, BigDecimal.valueOf(30000));


        System.out.println(desafio.buscarTimes());
        System.out.println(desafio.buscarCorCamisaTimeDeFora(1L, 3L));
        System.out.println(desafio.buscarJogadoresDoTime(1L));
    }
}
