package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import br.com.codenation.Model.Jogador;
import br.com.codenation.Model.Time;
import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

	public static final String TIME_NAO_ENCONTRADO = "Time não encontrado.";
	public static final String JOGADOR_NAO_ENCONTRADO = "Jogador não encontrado.";

	List<Time> times = new ArrayList<>();
	List<Jogador> jogadores = new ArrayList<>();

	@Desafio("incluirTime")
	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
		if (buscaJogadorPorId(id).isPresent()) {
			throw new IdentificadorUtilizadoException("Id de time em uso.");
		}

		times.add(new Time(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario));
	}

	@Desafio("incluirJogador")
	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
		if (buscaJogadorPorId(id).isPresent()) {
			throw new IdentificadorUtilizadoException("Id de jogador em uso.");
		}

		Optional<Time> time = buscaTimePorId(id);

		if (!time.isPresent()) {
			throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
		}

		jogadores.add(new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario));
		time.get().adicionaJogador(id);
	}

	@Desafio("definirCapitao")
	public void definirCapitao(Long idJogador) {
		Optional<Jogador> jogador = buscaJogadorPorId(idJogador);

		if (!jogador.isPresent()) {
			throw new JogadorNaoEncontradoException("Id de jogador inválida.");
		}

		Optional<Time> time = buscaTimePorId(jogador.get().getIdTime());
		time.ifPresent(time1 -> time1.setCapitao(jogador.get()));
	}

	@Desafio("buscarCapitaoDoTime")
	public Long buscarCapitaoDoTime(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (!time.isPresent()) {
			throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
		}

		if (time.get().getCapitao() == null) {
			throw new CapitaoNaoInformadoException("Capitão não informado para o time.");
		}

		return time.get().getCapitao().getId();
	}

	@Desafio("buscarNomeJogador")
	public String buscarNomeJogador(Long idJogador) {
		Optional<Jogador> jogador = buscaJogadorPorId(idJogador);

		if (jogador.isPresent()) {
			return jogador.get().getNome();
		}

		throw new JogadorNaoEncontradoException(JOGADOR_NAO_ENCONTRADO);
	}

	@Desafio("buscarNomeTime")
	public String buscarNomeTime(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (time.isPresent()) {
			return time.get().getNome();
		}

		throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
	}

	@Desafio("buscarJogadoresDoTime")
	public List<Long> buscarJogadoresDoTime(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (!time.isPresent()) {
			throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
		}

		List<Long> idJogadores = time.get().getJogadores();

		Collections.sort(idJogadores);

		return idJogadores;

	}

	@Desafio("buscarMelhorJogadorDoTime")
	public Long buscarMelhorJogadorDoTime(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (!time.isPresent()) {
		    throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
        }

        Jogador melhorJogador = null;

		for(Long idJogador : time.get().getJogadores()) {
		    Optional<Jogador> cmpJogador = buscaJogadorPorId(idJogador);
		    if (!cmpJogador.isPresent()) {
		        throw new JogadorNaoEncontradoException(JOGADOR_NAO_ENCONTRADO);
            }

            if (melhorJogador != null) {
                if (melhorJogador.getNivelHabilidade() < cmpJogador.get().getNivelHabilidade()) {
                    melhorJogador = cmpJogador.get();
                }
            } else {
                melhorJogador = cmpJogador.get();
            }
        }

        return melhorJogador.getId();
	}

	@Desafio("buscarJogadorMaisVelho")
	public Long buscarJogadorMaisVelho(Long idTime) {
        Optional<Time> time = buscaTimePorId(idTime);

        if (!time.isPresent()) {
            throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
        }

        Jogador melhorJogador = null;

        for(Long idJogador : time.get().getJogadores()) {
            Optional<Jogador> cmpJogador = buscaJogadorPorId(idJogador);
            if (!cmpJogador.isPresent()) {
                throw new JogadorNaoEncontradoException(JOGADOR_NAO_ENCONTRADO);
            }

            if (melhorJogador != null) {
                if ((melhorJogador.getDataNascimento().isBefore(cmpJogador.get().getDataNascimento())) ||
                    (melhorJogador.getDataNascimento().isEqual(cmpJogador.get().getDataNascimento()) && melhorJogador.getId() > cmpJogador.get().getId())
                ) {
                    melhorJogador = cmpJogador.get();
            } else {
                melhorJogador = cmpJogador.get();
            }
        }
	}

	@Desafio("buscarTimes")
	public List<Long> buscarTimes() {
		throw new UnsupportedOperationException();
	}

	@Desafio("buscarJogadorMaiorSalario")
	public Long buscarJogadorMaiorSalario(Long idTime) {
		throw new UnsupportedOperationException();
	}

	@Desafio("buscarSalarioDoJogador")
	public BigDecimal buscarSalarioDoJogador(Long idJogador) {
		throw new UnsupportedOperationException();
	}

	@Desafio("buscarTopJogadores")
	public List<Long> buscarTopJogadores(Integer top) {
		throw new UnsupportedOperationException();
	}

	@Desafio("buscarCorCamisaTimeDeFora")
	public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {
		throw new UnsupportedOperationException();
	}

	Optional<Time> buscaTimePorId(Long id) {
		return this.times.stream().filter(t -> t.getId().equals(id)).findFirst();
	}

	Optional<Jogador> buscaJogadorPorId(Long id) {
		return this.jogadores.stream().filter(j -> j.getId().equals(id)).findFirst();
	}
}
